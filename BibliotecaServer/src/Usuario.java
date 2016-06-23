import java.util.Vector;

public class Usuario implements Comparable<Usuario>{

	private int ID;
	private String senha;
	private Vector<Livro> uploads;
	private Vector<Livro> emprestimos;
	
	@Override
	public int compareTo(Usuario user1){
		return Integer.compare(this.getID(), user1.getID());
	}
	
	
	/*Construtor da classe Usuario recebe como parametros:
		- novoID = ID do usuario a ser criado
		- novaSenha = Senha do usuario a ser criado
	*/
	public Usuario(int novoID, String novaSenha){
		uploads = new Vector<Livro>(0, 1);
		emprestimos = new Vector<Livro>(0, 1);
		ID = novoID;
		senha = novaSenha;
	}
	
	//-------------Getters------------------//
	
	public int getID(){
		return ID;
	}

	//retorna o arraylist uploads
	public Vector<Livro> getUploads(){
		return uploads;
	}

	//retorna um livro especifico no arraylist de uploads
	public Livro getUpload(Livro up){
		int size = uploads.size();
		int i;
		for(i=0; i<size; i++){
			if(uploads.elementAt(i).compareTo(up) == 0){
				return uploads.elementAt(i);
			}
		}
		System.out.println("Book not found!");
		return null;
	}

	//retorna um livro especifico no arraylist de uploads
	public Livro getUpload(String title){
		int size = uploads.size();
		int i;
		for(i=0; i<size; i++){
			if(uploads.elementAt(i).getTitulo().compareTo(title) == 0){
				return uploads.elementAt(i);
			}
		}
		System.out.println("Book not found!");
		return null;
	}

	//retorna um livro especifico no arraylist de emprestimos
	public Livro getEmprestimo(Livro emp){
		int size = emprestimos.size();
		int i;
		for(i=0; i<size; i++){
			if(emprestimos.elementAt(i).compareTo(emp) == 0){
				return emprestimos.elementAt(i);
			}
		}
		System.out.println("Book not found!");
		return null;
	}

	//retorna um livro especifico no arraylist de emprestimos
	public Livro getEmprestimo(String title){
		int size = emprestimos.size();
		int i;
		for(i=0; i<size; i++){
			if(emprestimos.elementAt(i).getTitulo().compareTo(title) == 0){
				return emprestimos.elementAt(i);
			}
		}
		System.out.println("Book not found!");
		return null;
	}

	//retorna o arraylist emprestimos
	public Vector<Livro> getEmprestimos(){
		return emprestimos;
	}
	
	//------------Setters------------//
	
	public void setID(int novoID){
		ID = novoID;
	}
	
	//--------------------------------//
	//Funcao que recebe uma string e autentica com a Senha do usuario
	public boolean autenticar(String senha){
		return (senha.compareTo(senha) == 0);
	}
	//Funcao que imprime a lista de uploads do usuario
	public int printUploads(){
		if(uploads.isEmpty())return 0;
		int size = uploads.size();
		int i;
		for(i=0; i<size; i++){
			System.out.println(i+1 + "- " + uploads.elementAt(i).getTitulo());
		}
		return i;
	}
	//Funcao que imprime a lista de emprestimos do usuario
	public int printEmprestimos(){
		if(emprestimos.isEmpty())return 0;
		int size = emprestimos.size();
		int i;
		for(i=0; i<size; i++){
			System.out.println(i+1 + "- " + emprestimos.elementAt(i).getTitulo());
		}
		return i;
	}
	
	//-----------Insert/Remove--------------//
	//Insercao de um livro novo nos emprestimos
	public boolean insertEmprestimo(Livro novo){
		int size = emprestimos.size();
		int i;
		for(i=0; i<size; i++){
			try{
				if(novo.equals(emprestimos.elementAt(i))){
					System.out.println("You have already rented the book: " + novo.getTitulo());
					return false;
				}
			}catch(NullPointerException e){
				System.out.println("Invalid Book");
				return false;
			}
		}
		emprestimos.add(novo);
		emprestimos.sort(null);
		return true;
	}
	//insercao de um novo livro nos uploads
	public boolean insertUpload(Livro novo){
		int size = uploads.size();
		int i;
		for(i=0; i<size; i++){
			try{
				if(novo.equals(uploads.elementAt(i))){
					System.out.println("You have already uploaded the book: " + novo.getTitulo());
					return false;
				}
			}catch(NullPointerException e){
				System.out.println("Invalid Book");
				return false;
			}
		}
		uploads.add(novo);
		uploads.sort(null);
		return true;
	}
	//remocao de um livro da lista de emprestimos
	public boolean removeEmprestimo(Livro rmv){
		if(emprestimos.remove(rmv)==false){
			System.out.println("Book not found!");
			return false;
		}
		emprestimos.sort(null);
		return true;
	}
	//remocao de um livro da lista de uploads
	public boolean removeUpload(Livro rmv){
		if(uploads.remove(rmv)==false){
			System.out.println("Book not found!");
			return false;
		}
		uploads.sort(null);
		return true;
	}
	
	//------------------ Search uploads --------------------------//
	
	public Vector<String> searchTitleUploads(String title){
		Vector<String> ans = new Vector<String>(0, 1);
		int size = uploads.size();
		int i;
		for(i=0; i<size; i++){
			if(uploads.elementAt(i).contemTitulo(title)){
				ans.add(uploads.elementAt(i).getTitulo());
			}
		}
		if(ans.isEmpty()==true){
			System.out.println("No results found");;
			return null;
		}
		return ans;
	}
	
	public Vector<String> searchCategoriaUploads(String category){
		Vector<String> ans = new Vector<String>();
		int size = uploads.size();
		int i;
		for(i=0; i<size; i++){
			if(uploads.elementAt(i).contemCategoria(category)){
				ans.add(uploads.elementAt(i).getCategoria());
			}
		}
		if(ans.isEmpty()==true){
			System.out.println("No results found");;
			return null;
		}
		return ans;
	}

	public Vector<String> searchEditoraUploads(String editora){
		Vector<String> ans = new Vector<String>();
		int size = uploads.size();
		int i;
		for(i=0; i<size; i++){
			if(uploads.elementAt(i).contemEditora(editora)){
				ans.add(uploads.elementAt(i).getEditora());
			}
		}
		if(ans.isEmpty()==true){
			System.out.println("No results found");;
			return null;
		}
		return ans;
	}
	
	public Vector<String> searchAutoresUploads(String autor){
		Vector<String> ans = new Vector<String>();
		int size = uploads.size();
		int i;
		for(i=0; i<size; i++){
			if(uploads.elementAt(i).contemAutores(autor)){
				ans.add(uploads.elementAt(i).getAutores());
			}
		}
		if(ans.isEmpty()==true){
			System.out.println("No results found");;
			return null;
		}
		return ans;
	}
		
		
	//------------------ Search emprestimos --------------------------//
		
	public Vector<String> searchTitleEmprestimos(String title){
		Vector<String> ans = new Vector<String>(0, 1);
		int size = emprestimos.size();
		int i;
		for(i=0; i<size; i++){
			if(emprestimos.elementAt(i).contemTitulo(title)){
				ans.add(emprestimos.elementAt(i).getTitulo());
			}
		}
		if(ans.isEmpty()==true){
			System.out.println("No results found");;
			return null;
		}
		return ans;
	}
	
	public Vector<String> searchCategoriaEmprestimos(String category){
		Vector<String> ans = new Vector<String>(0, 1);
		int size = emprestimos.size();
		int i;
		for(i=0; i<size; i++){
			if(emprestimos.elementAt(i).contemCategoria(category)){
				ans.add(emprestimos.elementAt(i).getCategoria());
			}
		}
		if(ans.isEmpty()==true){
			System.out.println("No results found");;
			return null;
		}
		return ans;
	}

	public Vector<String> searchEditoraEmprestimos(String editora){
		Vector<String> ans = new Vector<String>(0, 1);
		int size = emprestimos.size();
		int i;
		for(i=0; i<size; i++){
			if(emprestimos.elementAt(i).contemEditora(editora)){
				ans.add(emprestimos.elementAt(i).getEditora());
			}
		}
		if(ans.isEmpty()==true){
			System.out.println("No results found");;
			return null;
		}
		return ans;
	}
	
	public Vector<String> searchAutoresEmprestimos(String autor){
		Vector<String> ans = new Vector<String>(0, 1);
		int size = emprestimos.size();
		int i;
		for(i=0; i<size; i++){
			if(emprestimos.elementAt(i).contemAutores(autor)){
				ans.add(emprestimos.elementAt(i).getAutores());
			}
		}
		if(ans.isEmpty()==true){
			System.out.println("No results found");;
			return null;
		}
		return ans;
	}
						
			
	public static void main(String[] args) {
		
	}

}
