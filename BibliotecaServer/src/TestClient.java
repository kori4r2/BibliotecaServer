import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class TestClient{

	public class answerGetter extends Thread{
		public void run(){
			String answer = new String("");
			try{
				while(server.hasNextLine() && !endTest){
					lastLine = server.nextLine();
					System.out.println(lastLine);
					if(lastLine.equals("disconnect")){
						disconnect();
						return;
					}
					//System.out.println(lastLine);
					commandReceived = true;
					commandProcessed = false;
//					while(!commandProcessed){
//					}
				}
			}catch(Exception e){
			}
		}
	}


	private Socket client;
	public volatile boolean endTest;
	private PrintStream saida;
	private Scanner entrada;
	private Scanner server;
	public volatile String lastLine;
	private answerGetter read;
	private volatile boolean commandReceived;
	private volatile boolean commandProcessed;

	// Construtor
	public TestClient() throws Exception{
		endTest = false;
		//client = new Socket("192.168.182.91", 9669);
		client = new Socket("127.0.0.1", 9669);
		entrada = new Scanner(System.in);
		saida = new PrintStream(client.getOutputStream());
		server = new Scanner(client.getInputStream());
	}

	// Envia uma string para o servidor
	public void sendCommand(String s){
		saida.println(s);
	}
	
	public void getAnswers(){
		read = new answerGetter();
		read.start();
	}

	public void disconnect() throws IOException{
		saida.close();
		entrada.close();
		server.close();
		client.close();
	}

	public static void main(String[] args) throws Exception{
		TestClient tc = new TestClient();
		tc.getAnswers();
		String testCommand = "login";
		tc.sendCommand(testCommand);
		testCommand = "12345";
		tc.sendCommand(testCommand);
		testCommand = "senha";
		tc.sendCommand(testCommand);
		testCommand = "open";
		tc.sendCommand(testCommand);
		testCommand = "teste";
		tc.sendCommand(testCommand);
		testCommand = "disconnect";
		tc.sendCommand(testCommand);
//		String input = EntradaTeclado.leString();
//		while(!input.equals("sair")){
//			tc.sendCommand(input);
//		}
		while(tc.endTest){
		}
//		tc.disconnect();
	}
}
