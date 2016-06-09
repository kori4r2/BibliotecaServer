import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class UserThread extends Thread{
	// A thread vai ter uma variavel do tipo bozo criada e vai manipular as entradas e saidas do usuario
	private Socket client;
	// Entrada e saida do cliente
	private PrintStream saida;
	private Scanner entrada;
	private String clientId;
	private int score;
	private Vector<String> IDList;
	private Vector<Integer> scoreList;

	// Obtem as informacoes do cliente e do servidor
	public UserThread(Socket c){
		IDList = new Vector<String>(1, 1);
		scoreList = new Vector<Integer>(1, 1);
		client = c;
	}

	public void run() throws NumberFormatException{
		String[] input;
//		System.out.println("Nova conexao com o cliente" + client.getInetAddress().getHostAddress());
		// Obtem a entrada e a saida do cliente
		try{
			saida = new PrintStream(client.getOutputStream());
			entrada = new Scanner(client.getInputStream());
		}catch(IOException e){
//			System.out.println("Excecao detectada na thread de id " + this.getId() + ": " + e.getMessage());
			return;
		}

		try{
			while(entrada.hasNextLine()){
			}
		}catch(IllegalStateException e){
			System.out.println("exception caught");
		}
		// Indica o fim da conexao
//		System.out.println("Fim da conexao com " + client.getInetAddress().getHostAddress());
	}
}
