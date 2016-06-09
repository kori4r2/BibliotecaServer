import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;

// Essa classe vai ser do server. 
public class BookServer{
	private class InputReader extends Thread{
		BookServer server;
		public InputReader(BookServer s){
			server = s;
		}

		public void run(){
			String input = new String("end");
			do{
				/*
				try{
					input = EntradaTeclado.leString();
				}catch(IOException e){
				}
				*/
				System.out.println("lido: " + input);
			}while(!input.equals("end"));
		}
	}

	private volatile ServerSocket socket;
	private volatile Socket client;
	private volatile Vector<UserThread> runningThreads;
	private volatile boolean keepRunning;

	public BookServer(){
		keepRunning = true;
		runningThreads = new Vector<UserThread>(1, 1);
	}

	public void runServer() throws IOException{
		InputReader ir = new InputReader(this);
		ir.start();
		// Cria o servidor
		socket = new ServerSocket(9669);
		System.out.println("Porta 9669 aberta");

		// Sempre que um cliente se conectar, cria uma nova Thread para ele e armazena no vetor
		while(keepRunning){
			try{
				client = socket.accept();
			}catch(SocketException e){
				return;
			}
			UserThread t = new UserThread(client);
			t.start();
			runningThreads.add(t);
		}
	}

	public static void main(String[] args) throws IOException{
		// Cria um servidor e comeca a rodá-lo
		BookServer bs = new BookServer();
		try{
			bs.runServer();
		}catch(Exception e){
			System.out.println(e.getMessage());
			return;
		}
	}
}
