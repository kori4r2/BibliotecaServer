import java.util.ArrayList;

public class Usuario implements Comparable<Usuario>{

	private int ID;
	private String Senha;
	private ArrayList<Livro> Uploads;
	private ArrayList<Livro> Emprestimos;
	
	@Override
	public int compareTo(Usuario user1){
		return Integer.compare(this.getID(), user1.getID());
		
	}
	
	
	/*Construtor da classe Usuario recebe como parametros:
		- novoID = ID do usuário a ser criado
		- novaSenha = Senha do usuário a ser criado
	*/
	public Usuario(int novoID, String novaSenha){
		Uploads = new ArrayList<Livro>();
		Emprestimos = new ArrayList<Livro>();
		ID = novoID;
		Senha = novaSenha;
	}
	//Funcao que recebe como parâmetros dois livros e os compara pelo nome
	private boolean comparaLivro(Livro book1, Livro book2){
		if(book1.getTitulo().compareTo(book2.getTitulo())==0)return true;
		return false;
	}
	
	//-------------Getters------------------//
	
	public int getID(){
		return ID;
	}
	
	//retorna o arraylist uploads
	public ArrayList<Livro> getUploads(){
		return Uploads;
	}
	//retorna um lviro especifico no arraylist de uploads
	public Livro getUpload(Livro up){
		int size = Uploads.size();
		int i;
		for(i=0; i<size; i++){
			if(comparaLivro(Uploads.get(i), up)==true){
				return Uploads.get(i);
			}
		}
		System.out.println("Book not found!");
		return null;
	}
	//retorna um livro especifico no arraylist de emprestimos
	public Livro getEmprestimo(Livro emp){
		int size = Emprestimos.size();
		int i;
		for(i=0; i<size; i++){
			if(comparaLivro(Emprestimos.get(i), emp)==true){
				return Emprestimos.get(i);
			}
		}
		System.out.println("Book not found!");
		return null;
	}
	//retorna o arraylist emprestimos
	public ArrayList<Livro> getEmprestimos(){
		return Emprestimos;
	}
	
	//------------Setters------------//
	
	public void setID(int novoID){
		ID = novoID;
	}
	
	public void setSenha(String novaSenha){
		Senha = novaSenha;
	}
	
	//--------------------------------//
	//Funcao que recebe uma string e autentica com a Senha do usuario
	public boolean Autenticar(String senha){
		if(senha.compareTo(Senha)==0)return true;
		return false;
	}
	//Funcao que imprime a lista de Uploads do usuário
	public int printUploads(){
		if(Uploads.isEmpty())return 0;
		int size = Uploads.size();
		int i;
		for(i=0; i<size; i++){
			System.out.println(i+1 + "- " + Uploads.get(i).getTitulo());
		}
		return i;
	}
	//Funcao que imprime a lista de Emprestimos do usuario
	public int printEmprestimos(){
		if(Emprestimos.isEmpty())return 0;
		int size = Emprestimos.size();
		int i;
		for(i=0; i<size; i++){
			System.out.println(i+1 + "- " + Emprestimos.get(i).getTitulo());
		}
		return i;
	}
	
	//-----------Insert/Remove--------------//
	//Inserção de um livro novo nos emprestimos
	public boolean insertEmprestimo(Livro novo)throws NullPointerException{
		int size = Emprestimos.size();
		int i;
		for(i=0; i<size; i++){
			try{
				if(comparaLivro(novo, Emprestimos.get(i)) == true){
					System.out.println("You have already arrended the book: " +
													novo.getTitulo());
					return false;
				}
			}catch(NullPointerException e){
				System.out.println("Invalid Book");
			}
		}
		Emprestimos.add(novo);
		return true;
	}
	//inserção de um novo livro nos uploads
	public boolean insertUpload(Livro novo)throws NullPointerException{
		int size = Uploads.size();
		int i;
		for(i=0; i<size; i++){
			try{
				if(comparaLivro(novo, Uploads.get(i))==true){
					System.out.println("You have already uploaded the book: " +
														novo.getTitulo());
					return false;
				}
			}catch(NullPointerException e){
				System.out.println("Invalid Book");
				return false;
			}
		}
		Uploads.add(novo);
		return true;
	}
	//remocao de um livro da lista de emprestimos
	public boolean removeEmprestimo(Livro rmv){
		if(Emprestimos.remove(rmv)==false){
			System.out.println("Book not found!");
			return false;
		}
		return true;
	}
	//remocao de um livro da lista de uploads
	public boolean removeUpload(Livro rmv){
		if(Uploads.remove(rmv)==false){
			System.out.println("Book not found!");
			return false;
		}
		return true;
	}
	
	//------------------ Search Uploads --------------------------//
	
		public ArrayList<String> searchTitleUploads(String title){
			ArrayList<String> ans = new ArrayList<String>();
			int size = Uploads.size();
			int i;
			for(i=0; i<size; i++){
				if(Uploads.get(i).contemTitulo(title)){
					ans.add(Uploads.get(i).getTitulo());
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
			int size = Uploads.size();
			int i;
			for(i=0; i<size; i++){
				if(Uploads.get(i).contemCategoria(category)){
					ans.add(Uploads.get(i).getCategoria());
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
			int size = Uploads.size();
			int i;
			for(i=0; i<size; i++){
				if(Uploads.get(i).contemEditora(editora)){
					ans.add(Uploads.get(i).getEditora());
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
			int size = Uploads.size();
			int i;
			for(i=0; i<size; i++){
				if(Uploads.get(i).contemAutores(autor)){
					ans.add(Uploads.get(i).getAutores());
				}
			}
			if(ans.isEmpty()==true){
				System.out.println("No results found");;
				return null;
			}
			return ans;
		}
		
		
		//------------------ Search Emprestimos --------------------------//
		
			public ArrayList<String> searchTitleEmprestimos(String title){
				ArrayList<String> ans = new ArrayList<String>();
				int size = Emprestimos.size();
				int i;
				for(i=0; i<size; i++){
					if(Emprestimos.get(i).contemTitulo(title)){
						ans.add(Emprestimos.get(i).getTitulo());
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
				int size = Emprestimos.size();
				int i;
				for(i=0; i<size; i++){
					if(Emprestimos.get(i).contemCategoria(category)){
						ans.add(Emprestimos.get(i).getCategoria());
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
				int size = Emprestimos.size();
				int i;
				for(i=0; i<size; i++){
					if(Emprestimos.get(i).contemEditora(editora)){
						ans.add(Emprestimos.get(i).getEditora());
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
				int size = Emprestimos.size();
				int i;
				for(i=0; i<size; i++){
					if(Emprestimos.get(i).contemAutores(autor)){
						ans.add(Emprestimos.get(i).getAutores());
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
