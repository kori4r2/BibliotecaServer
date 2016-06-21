import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;
import java.util.Vector;

import javax.imageio.ImageIO;

import com.sun.pdfview.*;

public class UserThread extends Thread{
	private Socket client;
	private BookServer server;
	// Entrada e saida do cliente
	private PrintStream saida;
	private Scanner entrada;
	private Usuario currentUser;
	private PDFFile currentPDF;

	// Obtem as informacoes do cliente e do servidor
	public UserThread(Socket c, BookServer s){
		client = c;
		server = s;
	}

	private Usuario checkUserLogin(String id, String password){
		int userID = -1;
		try{
			Integer.parseInt(id);
		}catch(NumberFormatException e){
			return null;
		}

		return server.authenticate(userID, password);
	}

	private void readPDF(File file) throws Exception{
		saida.println("reading");
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		currentPDF = new PDFFile(buf);
		// draw the first page to an image
		PDFPage page = currentPDF.getPage(1);
		int width = 800;
		int height = 600;
		// get the width and height for the doc at the default zoom -> NOT DEFAULT ZOOM
		Rectangle rect = new Rectangle((int)page.getPageBox().getX(), (int)page.getPageBox().getY(),
						(int)page.getWidth(), (int)page.getHeight());
		BufferedImage bImg = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_RGB);
		// generate the image
		Image img = page.getImage(rect.width, rect.height, rect, null, true, true);
		// Convert to buffered image
		Graphics2D g2d = bImg.createGraphics();
		g2d.drawImage(img, 0, 0, null);
		g2d.dispose();
		// Enviar para o usuario
		OutputStream outStream = (OutputStream)saida;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        ImageIO.write(bImg, "png", byteArrayOutputStream);
		ImageIO.write(bImg, "png", new File("../testImage/sentTest.png"));

	        byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
	        outStream.write(size);
	        outStream.write(byteArrayOutputStream.toByteArray());
	        outStream.flush();
	}

	private void newUpload(String filename) throws Exception{
		InputStream inStream = client.getInputStream();
		FileOutputStream fOut = new FileOutputStream("../PDFBooks/" + filename);

		byte[] sizeAr = new byte[4];
		inStream.read(sizeAr);
		int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
//		System.out.println("size read = " + size);
		byte[] myByteArray = new byte[size];
		int bytesRead = 0;
		while(bytesRead < size){
			bytesRead += inStream.read(myByteArray, bytesRead, size-bytesRead);
		}
//		System.out.println("Bytes actualy read = " + bytesRead);
		fOut.write(myByteArray, 0, myByteArray.length);
		fOut.close();
//		while(!entrada.nextLine().equals("uploaded")){
//		}
	}

	public void run() throws NumberFormatException{
		String[] input;
		System.out.println("Nova conexao com o cliente" + client.getInetAddress().getHostAddress());
		// Obtem a entrada e a saida do cliente
		try{
			saida = new PrintStream(client.getOutputStream());
			entrada = new Scanner(client.getInputStream());
		}catch(IOException e){
//			System.out.println("Excecao detectada na thread de id " + this.getId() + ": " + e.getMessage());
			return;
		}
		saida.println("Conexao bem-sucedida");

		try{
			String userInput;
			while(entrada.hasNextLine()){
				// Se for enviado o comando de login, recebe as informacoes de usuario e senha
				// e tenta realizar o login
				userInput = entrada.nextLine();
				System.out.println("Input recebido: " + userInput);
				if(userInput.equals("login")){
					String id = entrada.nextLine();
					String password = entrada.nextLine();
					currentUser = checkUserLogin(id, password);
					// Envia uma resposta para o cliente com o resultado da operacao
					if(currentUser != null)
						saida.println("sucesso");
					else
						saida.println("erro");
//					saida.println("CommandEnd");
				}else if(userInput.equals("open")){
					System.out.println("searching for book...");
					String title = entrada.nextLine();
					Livro result = server.searchBookTitle(title);
					if(result != null && result.getAcervo() > 0){
						File file = new File("../PDFBooks/" + result.getPdf());
						if(file != null){
							try{
								readPDF(file);
							}catch(Exception e){
								saida.println("deu ruim");
								System.out.println(e.getMessage());
							}
						}else
							saida.println("arquivo nao existe");
					}else
						saida.println("livro nao esta no acervo");
				}else if(userInput.equals("upload")){
					System.out.println("Receiving new upload...");
					String filename = entrada.nextLine();
					Livro result = server.searchBookPDF(filename);
					if(result != null){
						result.addAcervo(1);
						saida.println("exists");
						saida.println("added");
					}else{
						saida.println("upload");
						boolean success = true;
						try{
							newUpload(filename);
						}catch(Exception e){
							success = false;
							saida.println("error");
						}
						if(success)
							saida.println("added");
					}
				}else if(userInput.equals("disconnect")){
					saida.println("disconnect");
				}
				System.out.println("Waiting for new command...");
			}
		}catch(IllegalStateException e){
			System.out.println("exception caught");
		}
		// Indica o fim da conexao
		System.out.println("Fim da conexao com " + client.getInetAddress().getHostAddress());
	}
}
