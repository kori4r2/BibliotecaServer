import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

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
			Integer.parseInt(id);
		}catch(NumberFormatException e){
			return null;
		}

		return server.authenticate(userID, password);
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
					saida.println("CommandEnd");
				}else if(userInput.equals("open")){
					System.out.println("searching for book...");
					String title = entrada.nextLine();
					Livro result = server.searchBook(title);
					if(result != null){
						File file = new File("../PDFBooks/" + result.getPdf());
						if(file != null)
							saida.println("sucesso");
						else
							saida.println("erro2");
					}else
						saida.println("erro1");
					saida.println("CommandEnd");
				}else if(userInput.equals("disconnect")){
					saida.println("disconnect");
				}
				System.out.println("Waiting for new command...");
			}
		}catch(IllegalStateException e){
			System.out.println("exception caught");
		}
		// Indica o fim da conexao
//		System.out.println("Fim da conexao com " + client.getInetAddress().getHostAddress());
	}
}
