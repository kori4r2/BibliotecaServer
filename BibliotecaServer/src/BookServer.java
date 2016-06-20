import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;

// Essa classe vai ser do server.
public class BookServer{
	/*
	private class InputReader extends Thread{
		BookServer server;
		public InputReader(BookServer s){
			server = s;
		}

		public void run(){
			String input = new String("end");
			do{
				try{
					input = EntradaTeclado.leString();
				}catch(IOException e){
				}
				System.out.println("lido: " + input);
			}while(!input.equals("end"));
		}
	}
	*/

	private volatile ServerSocket socket;
	private volatile Socket client;
	private volatile Vector<UserThread> runningThreads;
	private volatile boolean keepRunning;
	private volatile Vector<Usuario> userList;
	private volatile Vector<Livro> bookList;
	private volatile boolean sorted;

	public BookServer(){
		keepRunning = true;
		runningThreads = new Vector<UserThread>(1, 5);
		userList = new Vector<Usuario>(1, 5);
		bookList = new Vector<Livro>(1, 5);
		Livro teste = new Livro("PDFtest.pdf", "teste");
		bookList.add(teste);
		sorted = true;
	}

	// Busca binaria pelo usuario com o ID desejado
	private Usuario searchUser(int ID){
		if(!sorted)
			userList.sort(null);
		int start = 0, end = (userList.size() - 1), middle, result;
		Usuario u;
		while(start <= end){
			middle = ((start + end) / 2);
			u = userList.elementAt(middle);
			result = (u.getID() - ID);
			if(result == 0)
				return u;
			else if(result < 0)
				start = middle + 1;
			else
				end = middle - 1;
		}
		return null;
	}

	public Usuario authenticate(int ID, String password){
		Usuario u = searchUser(ID);
		if(u != null){
			if(u.autenticar(password))
				return u;
		}
		return null;
	}

	public Livro searchBook(String title){
		for(Livro l : bookList){
			if(l.getTitulo().equals(title)){
				return l;
			}
		}
		return null;
	}

	public void runServer() throws IOException{
		//InputReader ir = new InputReader(this);
		//ir.start();
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
			UserThread t = new UserThread(client, this);
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
