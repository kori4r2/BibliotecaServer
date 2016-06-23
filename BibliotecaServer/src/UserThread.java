import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
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

	// Obtem as informacoes do cliente e do servidor
	public UserThread(Socket c, BookServer s){
		client = c;
		server = s;
	}

	private Usuario checkUserLogin(String id, String password){
		int userID = -1;
		try{
			userID = Integer.parseInt(id);
		}catch(NumberFormatException e){
			return null;
		}

		return server.authenticate(userID, password);
	}

	private void sendPDFPage(PDFPage page) throws Exception{
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

		// Prepara o envio para o usuario
		OutputStream outStream = (OutputStream)saida;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        ImageIO.write(bImg, "png", byteArrayOutputStream);
		// Salca a imagem em um arquivo para comparacao
		ImageIO.write(bImg, "png", new File("../testImage/sentTest.png"));

		// Espera o usuario estar pronto para receber
		String response = entrada.nextLine();
		if(response.equals("error")){
			throw new Exception("erro no envio da imagem (user side)");
		}

		// Envia o tamanho da imagem e depois os bytes em si
	        byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
	        outStream.write(size);
	        outStream.write(byteArrayOutputStream.toByteArray());
		saida.println("success");
	}

	private void readPDF(File file) throws Exception{
		saida.println("reading");

		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		PDFFile currentPDF = new PDFFile(buf);

		int currentPage = 1;
		int nPages = currentPDF.getNumPages();
		// draw the first page to an image
		PDFPage page = currentPDF.getPage(currentPage);
		sendPDFPage(page);
		// Avisa o numero de pagina atual
		saida.println("" + currentPage);
		// Avisa o numero maximo de paginas
		saida.println("" + nPages);
		String command;
		do{
			command = entrada.nextLine();
			switch(command){
				case "next":
					currentPage++;
					break;
				case "previous":
					currentPage--;
					break;
				case "choice":
					String pageString = entrada.nextLine();
					currentPage = Integer.parseInt(pageString);
					break;
				case "close":
					currentPage = 1;
					break;
			}
			if(!command.equals("close")){
				saida.println("sending");
				currentPage = (currentPage < 1) ? 1 : (currentPage > nPages) ? nPages : currentPage;
				page = currentPDF.getPage(currentPage);
				sendPDFPage(page);
				saida.println("" + currentPage);
			}
		}while(!command.equals("close"));
	}

	private void newUpload(String filename) throws Exception{
		File file = new File("../PDFBooks/" + filename);
		// Se o pdf ja existe na pasta, evita o upload
		if(file.exists() && !file.isDirectory()){
			return;
		}

		// Avisa o usuario que o upload sera feito
		saida.println("upload");
		InputStream inStream = client.getInputStream();
		FileOutputStream fOut = new FileOutputStream("../PDFBooks/" + filename);
		// Espera a resposta do usuario para confirmar o upload
		String response = entrada.nextLine();
		if(response.equals("error")){
			throw new Exception("invalid file");
		}

		// Avisa que pode iniciar o envio
		saida.println("ready");
		// Recebe o tamanho do arquivo em bytes
		byte[] sizeAr = new byte[4];
		inStream.read(sizeAr);
		int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
		// Recebe o arquivo como um array de bytes
		byte[] myByteArray = new byte[size];
		int bytesRead = 0;
		// Garante que recebeu o arquivo inteiro
		while(bytesRead < size){
			bytesRead += inStream.read(myByteArray, bytesRead, size-bytesRead);
		}
		response = entrada.nextLine();
		if(response.equals("error")){
			throw new Exception("upload failed");
		}
		// Escreve no novo arquivo criado
		fOut.write(myByteArray, 0, myByteArray.length);
		fOut.close();
	}

	private void removeUpload(Livro book){
		// Se o livro existe no acerto sem estar emprestado, apenas remove 1
		if(book.getAcervo() > 0){
			book.rmvAcervo(1);
			currentUser.removeUpload(book);
			saida.println("success");
		}else{
			// Caso contrario e o livro esteja atualmente com o usuario, devolve o livro
			if(currentUser.getEmprestimo(book) != null){
				currentUser.removeEmprestimo(book);
				currentUser.removeUpload(book);
				book.addAcervo(1);
				saida.println("success");
			}else{
				saida.println("error");
			}
		}
	}

	private void sendFullList(){
		String[] list = server.getBookList();
		for(int i = 0; i < list.length; i++){
			saida.println(list[i]);
		}
	}

	private void sendUploadList(){
		Vector<Livro> list = currentUser.getUploads();
		for(int i = 0; i < list.size(); i++){
			saida.println(list.elementAt(i).getTitulo());
		}
	}

	private void sendLoanList(){
		Vector<Livro> list = currentUser.getEmprestimos();
		for(int i = 0; i < list.size(); i++){
			saida.println(list.elementAt(i).getTitulo());
		}
	}

	public void run() throws NumberFormatException{
		String[] input;
		System.out.println("Nova conexao com o cliente" + client.getInetAddress().getHostAddress());
		// Obtem a entrada e a saida do cliente
		try{
			saida = new PrintStream(client.getOutputStream());
			entrada = new Scanner(client.getInputStream());
		}catch(IOException e){
			System.out.println("Excecao detectada na thread de id " + this.getId());
			System.out.println("             : " + e.getMessage());
			return;
		}
		// Avisa o cliente que a conexao foi feita
		saida.println("Conexao bem-sucedida");

		try{
			String userInput;
			// Enquanto existir a conexao recebe o input do cliente
			while(entrada.hasNextLine()){
				userInput = entrada.nextLine();
				System.out.println("Received input: " + userInput);
				boolean success;
				String title, filename, idString, password;
				Livro aux, result;
				int id;
				// Avalia o input para decidir o que deve ser feito
				switch(userInput){
					// Cria um novo usuario
					case "newUser":
						success = true;
						idString = entrada.nextLine();
						password = entrada.nextLine();
						id = -1;
						// Avalia se o parametro passado foi valido
						try{
							id = Integer.parseInt(idString);
						}catch(NumberFormatException e){
							success = false;
							saida.println("errorID");
						}
						// Caso seja, tenta criar novo usuario (campo id deve ser unico)
						if(success){
							// Avisa o usuario do resultado
							Usuario newUser = new Usuario(id, password);
							if(server.addNewUser(newUser)){
								currentUser = newUser;
								saida.println("success");
							}else
								saida.println("error");
						}
						break;
					// Se for enviado o comando de login, recebe as informacoes de usuario e senha
					// e tenta realizar o login
					case "login":
						idString = entrada.nextLine();
						password = entrada.nextLine();
						currentUser = checkUserLogin(idString, password);
						// Envia uma resposta para o cliente com o resultado da operacao
						if(currentUser != null)
							saida.println("success");
						else
							saida.println("error");
						break;
					// Se receber a mensagem de logout apaga a referencia de usuario atual
					case "logout":
						currentUser = null;
						saida.println("success");
						break;
					// Se receber a mensage para abrir o livro
					case "open":
						// Busca o livro com o titulo passado na lista de emprestimos do usuario
						System.out.println("searching for book...");
						title = entrada.nextLine();
						result = currentUser.getEmprestimo(title);
						if(result != null){
							// Caso tenha encontrado, busca o arquivo pdf na pasta correspondente
							File file = new File("../PDFBooks/" + result.getPdf());
							if(file != null){
								// Ao encontrar o arquivo, comeca a fazer a leitura
								success = true;
								try{
									readPDF(file);
								}catch(Exception e){
									// Caso de algo errado, avisa o usuario
									success = false;
									saida.println("error");
									System.out.println(e.getMessage());
								}
								// Se for encerrado com sucesso, avisa o usuario
								if(success)
									saida.println("success");
							}else
								saida.println("errorFile");
						}else
							saida.println("errorLoan");
						break;
					// Se for recebida a mensagem de upload, recebe o titulo e o nome de arquivo
					case "upload":
						System.out.println("Receiving new upload...");
						title = entrada.nextLine();
						filename = entrada.nextLine();
						success = true;

						// Verifica se os dados passados sao validos
						result = server.searchBookTitle(title);
						// Os campos de titulo e nome de arquivo devem ser unicos
						if(result == null){
							result = server.searchBookPDF(filename);
							if(result != null){
								success = false;
								saida.println("errorTitle");
								saida.println(result.getTitulo());
							}
						}else{
							Livro checkResult = server.searchBookPDF(filename);
							if(checkResult == null){
								success = false;
								saida.println("errorFilename");
								saida.println(result.getPdf());
							}
						}

						// Caso sejam, avalia o que deve ser feito
						if(success){
							// Se o livro ja existe
							if(result != null){
								// Tenta fazer o upload
								if(currentUser.insertUpload(result)){
									// Caso consiga, avisa o usuario
									result.addAcervo(1);
									saida.println("success");
								// Caso seja um upload repetido, avisa o usuario
								}else
									saida.println("error");
							// Se o livro nao existe no acervo
							}else{
								success = true;
								try{
									// Tenta realizar o upload
									newUpload(filename);
								}catch(Exception e){
									// Caso de erro, avisa o usuario
									success = false;
									saida.println("error");
								}
								// Caso o upload seja bem sucedido
								if(success){
									// Adiciona o livro ao acervo e avisa o usuario
									Livro newBook = new Livro(filename, title);
									currentUser.insertUpload(newBook);
									server.addNewBook(newBook);
									saida.println("success");
								}
							}
							System.out.println("Upload ended");
						}else
							System.out.println("Upload error");
						break;
					// Se for recebida a mensagem de remover upload
					case "rmvUpload":
						// Recebe o titulo e busca o livro n lista de uploads do usuario atual
						title = entrada.nextLine();
						result = currentUser.getUpload(title);
						if(result != null){
							// Caso encontre, realiza a remocao
							removeUpload(result);
						}else
							// Caso contrario, avisa
							saida.println("error");
						break;
					// Se for pedido, envia a lista completa de livros do acervo
					case "fullList":
						sendFullList();
						saida.println("finished");
						break;
					// Se for pedido novo emprestimo, recebe o nome do livro
					case "newLoan":
						// Busca o livro no acervo
						title = entrada.nextLine();
						result = server.searchBookTitle(title);
						if(result != null){
							// Checa a disponibilidade do livro
							if(result.getAcervo() > 0){
								// Tenta fazer o emprestimo e avisa o usuario do resultado
								if(currentUser.insertEmprestimo(result)){
									result.rmvAcervo(1);
									saida.println("success");
								}else
									saida.println("errorRepeat");
							}else
								saida.println("errorNotAvailable");
						}else
							saida.println("errorNotFound");
						break;
					//TO DO: Devolucao de emprestimo
					//Se for pedido, envia a lista de uploads realizados pelo usuario atual
					case "uploadList":
						sendUploadList();
						saida.println("finished");
						break;
					// Se for pedido, envia a lista de emprestimos realizados pelo usuario atual
					case "loanList":
						sendLoanList();
						saida.println("finished");
						break;
					// Se for enviada mensagem de desconectar, envia a mensagem para que o usuario o faca,
					// encerrando a execucao da thread
					case "disconnect":
						saida.println("disconnect");
						break;
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
