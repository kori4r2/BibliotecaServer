import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;

// Essa classe vai ser do server.
public class BookServer{
	private ServerSocket socket;
	private Socket client;
	private Vector<UserThread> runningThreads;
	private volatile boolean keepRunning;
	private volatile Vector<Usuario> userList;
	private volatile Vector<Livro> bookList;
	private volatile boolean sorted;

	public BookServer(){
		keepRunning = true;
		runningThreads = new Vector<UserThread>(1, 5);
		userList = new Vector<Usuario>(1, 5);
		bookList = new Vector<Livro>(1, 5);
		sorted = true;
	}

	// Busca binaria pelo usuario com o ID desejado
	private Usuario searchUser(int ID){
		if(userList.size() == 0)
			return null;
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

	public boolean addNewUser(Usuario newUser){
		if(searchUser(newUser.getID()) == null){
			userList.add(newUser);
			userList.sort(null);
			sorted = true;
			return true;
		}else
			return false;
	}

	public Usuario authenticate(int ID, String password){
		Usuario u = searchUser(ID);
		if(u != null){
			if(u.autenticar(password))
				return u;
		}
		return null;
	}

	public void addNewBook(Livro newBook){
		bookList.add(newBook);
		bookList.sort(null);
	}

	public Livro searchBookTitle(String title){
		int start = 0, end = (bookList.size() - 1), middle, result;
		Livro l;
		while(start <= end){
			middle = ((start + end) / 2);
			l = bookList.elementAt(middle);
			result = (l.getTitulo().compareTo(title));
			if(result == 0)
				return l;
			else if(result < 0)
				start = middle + 1;
			else
				end = middle - 1;
		}
		return null;
	}

	public Livro searchBookPDF(String filename){
		for(Livro l : bookList){
			if(l.getPdf().equals(filename)){
				return l;
			}
		}
		return null;
	}

	public String[] getBookList(){
		String[] list = new String[bookList.size()];
		for(int i = 0; i < bookList.size(); i++){
			list[i] = bookList.elementAt(i).getTitulo() + " => " + bookList.elementAt(i).getAcervo();
		}
		return list;
	}

	public void runServer() throws IOException{
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
