import java.util.ArrayList;

public class Usuario implements Comparable<Usuario>{

	private int ID;
	private String senha;
	private ArrayList<Livro> uploads;
	private ArrayList<Livro> emprestimos;
	
	@Override
	public int compareTo(Usuario user1){
		return Integer.compare(this.getID(), user1.getID());
	}
	
	
	/*Construtor da classe Usuario recebe como parametros:
		- novoID = ID do usuario a ser criado
		- novaSenha = Senha do usuario a ser criado
	*/
	public Usuario(int novoID, String novaSenha){
		uploads = new ArrayList<Livro>();
		emprestimos = new ArrayList<Livro>();
		ID = novoID;
		senha = novaSenha;
	}
	
	//-------------Getters------------------//
	
	public int getID(){
		return ID;
	}

	//retorna o arraylist uploads
	public ArrayList<Livro> getUploads(){
		return uploads;
	}

	//retorna um livro especifico no arraylist de uploads
	public Livro getUpload(Livro up){
		int size = uploads.size();
		int i;
		for(i=0; i<size; i++){
			if(uploads.equals(up)){
				return uploads.get(i);
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
			if(emprestimos.get(i).equals(emp)){
				return emprestimos.get(i);
			}
		}
		System.out.println("Book not found!");
		return null;
	}
	//retorna o arraylist emprestimos
	public ArrayList<Livro> getEmprestimos(){
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
			System.out.println(i+1 + "- " + uploads.get(i).getTitulo());
		}
		return i;
	}
	//Funcao que imprime a lista de emprestimos do usuario
	public int printEmprestimos(){
		if(emprestimos.isEmpty())return 0;
		int size = emprestimos.size();
		int i;
		for(i=0; i<size; i++){
			System.out.println(i+1 + "- " + emprestimos.get(i).getTitulo());
		}
		return i;
	}
	
	//-----------Insert/Remove--------------//
	//Insercao de um livro novo nos emprestimos
	public boolean insertEmprestimo(Livro novo)throws NullPointerException{
		int size = emprestimos.size();
		int i;
		for(i=0; i<size; i++){
			try{
				if(novo.equals(emprestimos.get(i))){
					System.out.println("You have already arrended the book: " + novo.getTitulo());
					return false;
				}
			}catch(NullPointerException e){
				System.out.println("Invalid Book");
			}
		}
		emprestimos.add(novo);
		return true;
	}
	//insercao de um novo livro nos uploads
	public boolean insertUpload(Livro novo)throws NullPointerException{
		int size = uploads.size();
		int i;
		for(i=0; i<size; i++){
			try{
				if(novo.equals(uploads.get(i))){
					System.out.println("You have already uploaded the book: " + novo.getTitulo());
					return false;
				}
			}catch(NullPointerException e){
				System.out.println("Invalid Book");
				return false;
			}
		}
		uploads.add(novo);
		return true;
	}
	//remocao de um livro da lista de emprestimos
	public boolean removeEmprestimo(Livro rmv){
		if(emprestimos.remove(rmv)==false){
			System.out.println("Book not found!");
			return false;
		}
		return true;
	}
	//remocao de um livro da lista de uploads
	public boolean removeUpload(Livro rmv){
		if(uploads.remove(rmv)==false){
			System.out.println("Book not found!");
			return false;
		}
		return true;
	}
	
	//------------------ Search uploads --------------------------//
	
	public ArrayList<String> searchTitleUploads(String title){
		ArrayList<String> ans = new ArrayList<String>();
		int size = uploads.size();
		int i;
		for(i=0; i<size; i++){
			if(uploads.get(i).contemTitulo(title)){
				ans.add(uploads.get(i).getTitulo());
			}
		}
		if(ans.isEmpty()==true){
			System.out.println("No results found");;
			return null;
		}
		return ans;
	}
	
	public ArrayList<String> searchCategoriaUploads(String category){
		ArrayList<String> ans = new ArrayList<String>();
		int size = uploads.size();
		int i;
		for(i=0; i<size; i++){
			if(uploads.get(i).contemCategoria(category)){
				ans.add(uploads.get(i).getCategoria());
			}
		}
		if(ans.isEmpty()==true){
			System.out.println("No results found");;
			return null;
		}
		return ans;
	}

	public ArrayList<String> searchEditoraUploads(String editora){
		ArrayList<String> ans = new ArrayList<String>();
		int size = uploads.size();
		int i;
		for(i=0; i<size; i++){
			if(uploads.get(i).contemEditora(editora)){
				ans.add(uploads.get(i).getEditora());
			}
		}
		if(ans.isEmpty()==true){
			System.out.println("No results found");;
			return null;
		}
		return ans;
	}
	
	public ArrayList<String> searchAutoresUploads(String autor){
		ArrayList<String> ans = new ArrayList<String>();
		int size = uploads.size();
		int i;
		for(i=0; i<size; i++){
			if(uploads.get(i).contemAutores(autor)){
				ans.add(uploads.get(i).getAutores());
			}
		}
		if(ans.isEmpty()==true){
			System.out.println("No results found");;
			return null;
		}
		return ans;
	}
		
		
	//------------------ Search emprestimos --------------------------//
		
	public ArrayList<String> searchTitleEmprestimos(String title){
		ArrayList<String> ans = new ArrayList<String>();
		int size = emprestimos.size();
		int i;
		for(i=0; i<size; i++){
			if(emprestimos.get(i).contemTitulo(title)){
				ans.add(emprestimos.get(i).getTitulo());
			}
		}
		if(ans.isEmpty()==true){
			System.out.println("No results found");;
			return null;
		}
		return ans;
	}
	
	public ArrayList<String> searchCategoriaEmprestimos(String category){
		ArrayList<String> ans = new ArrayList<String>();
		int size = emprestimos.size();
		int i;
		for(i=0; i<size; i++){
			if(emprestimos.get(i).contemCategoria(category)){
				ans.add(emprestimos.get(i).getCategoria());
			}
		}
		if(ans.isEmpty()==true){
			System.out.println("No results found");;
			return null;
		}
		return ans;
	}

	public ArrayList<String> searchEditoraEmprestimos(String editora){
		ArrayList<String> ans = new ArrayList<String>();
		int size = emprestimos.size();
		int i;
		for(i=0; i<size; i++){
			if(emprestimos.get(i).contemEditora(editora)){
				ans.add(emprestimos.get(i).getEditora());
			}
		}
		if(ans.isEmpty()==true){
			System.out.println("No results found");;
			return null;
		}
		return ans;
	}
	
	public ArrayList<String> searchAutoresEmprestimos(String autor){
		ArrayList<String> ans = new ArrayList<String>();
		int size = emprestimos.size();
		int i;
		for(i=0; i<size; i++){
			if(emprestimos.get(i).contemAutores(autor)){
				ans.add(emprestimos.get(i).getAutores());
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
