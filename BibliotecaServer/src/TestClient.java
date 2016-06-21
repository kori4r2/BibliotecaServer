import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class TestClient{

	public class answerGetter extends Thread{
		public void run(){
			String answer = new String("");
			try{
				while(entrada.hasNextLine()){
					lastLine = entrada.nextLine();
					System.out.println(lastLine);
					if(lastLine.equals("disconnect")){
						disconnect();
						return;
					}
					//System.out.println(lastLine);
					commandReceived = true;
					commandProcessed = false;
					while(!commandProcessed){
					}
				}
			}catch(Exception e){
			}
		}
	}


	private Socket socket;
	private PrintStream saida;
	private Scanner entrada;
	public volatile String lastLine;
	private answerGetter read;
	public volatile boolean commandReceived;
	public volatile boolean commandProcessed;

	// Construtor
	public TestClient() throws Exception{
		//socket = new Socket("192.168.182.91", 9669);
		socket = new Socket("127.0.0.1", 9669);
		saida = new PrintStream(socket.getOutputStream());
		entrada = new Scanner(socket.getInputStream());
		commandReceived = false;
	}

	// Envia uma string para o servidor
	public void sendCommand(String s){
		saida.println(s);
	}

	public void waitForResponse(){
		while(!commandReceived){
		}
	}

	public void responseProcessed(){
		commandProcessed = true;
		commandReceived = false;
	}

	public void sendFile(String filepath) throws Exception{
		File file = null;
		Path pdfpath = null;
		try{
			file = new File(filepath);
			pdfpath = Paths.get(filepath);
		}catch(Exception e){
			System.out.println("Nome de arquivo invalido (" + filepath + ")");
			return;
		}
		saida.println("upload");
		String[] aux = filepath.split("/");
		saida.println(aux[aux.length - 1]);

		while(!commandReceived){
		}
		commandReceived = false;
		commandProcessed = true;

		if(lastLine.equals("upload")){
			OutputStream outStream = (OutputStream)saida;

			if(file.length() > Integer.MAX_VALUE)
				System.out.println("file is too big");
			int fileSize = (int)file.length();
//			System.out.println("File size in bytes = " + fileSize);
			byte[] size = ByteBuffer.allocate(4).putInt(fileSize).array();

			byte[] byteArray = Files.readAllBytes(pdfpath);
//			System.out.println("array size = " + byteArray.length);

			outStream.write(size);
			outStream.write(byteArray, 0, fileSize);
			outStream.flush();
			saida.println("uploaded");
		}
	}


	public BufferedImage getImage() throws Exception{
		System.out.println("Receiving image...");
		InputStream inStream = socket.getInputStream();
		byte[] sizeAr = new byte[4];
		inStream.read(sizeAr);
		int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
		byte[] imageAr = new byte[size];
		int bytesRead = 0;
		while(bytesRead < size){
			bytesRead += inStream.read(imageAr, bytesRead, size-bytesRead);
		}
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
		System.out.println("Image Received");
		return image;
	}
	
	public void getAnswers(){
		read = new answerGetter();
		read.start();
	}

	public void disconnect() throws IOException{
		saida.close();
		entrada.close();
		socket.close();
	}

	public static void main(String[] args) throws Exception{
		TestClient tc = new TestClient();
		tc.getAnswers();
		while(!tc.commandReceived){
		}
		tc.commandReceived = false;
		tc.commandProcessed = true;

		String testCommand = "login";
		tc.sendCommand(testCommand);
		testCommand = "12345";
		tc.sendCommand(testCommand);
		testCommand = "senha";
		tc.sendCommand(testCommand);
		while(!tc.commandReceived){
		}
		tc.commandReceived = false;
		tc.commandProcessed = true;

		testCommand = "open";
		tc.sendCommand(testCommand);
		testCommand = "teste";
		tc.sendCommand(testCommand);
		while(!tc.commandReceived){
		}
		if(tc.lastLine.equals("reading")){
			// receber imagem
			BufferedImage image = tc.getImage();
			File outFile = new File("../testImage/test.png");
			ImageIO.write(image, "png", outFile);
			// escrever num arquivo pra testar
		}
		tc.commandReceived = false;
		tc.commandProcessed = true;

		tc.sendFile("../testPDF/Interface.pdf");
		while(!tc.commandReceived){
		}
		tc.commandReceived = false;
		tc.commandProcessed = true;

		testCommand = "disconnect";
		tc.sendCommand(testCommand);
//		String input = EntradaTeclado.leString();
//		while(!input.equals("sair")){
//			tc.sendCommand(input);
//		}
//		tc.disconnect();
	}
}
