
public class Livro {
	
	private String Titulo;
	private String Autores;
	private int Edicao;
	private String Editora;
	private String Categoria;
	private String pdfName;
	private int Acervo;
	//Construtor que seta o nome do pdf e titulo do livro
	public Livro(String pdfname, String title){
		pdfName = pdfname;
		Titulo = title;
	}
	
	// --------- Getters --------------------------//
	public int getAcervo(){
		return Acervo;
	}
	
	public String getTitulo(){
		if(Titulo == null)return "Book has no title";
		return Titulo;
	}
	
	public String getAutores(){
		if(Autores == null)return "Autores nao especificados";
		return Autores;
	}
	
	public String getEditora(){
		if(Editora == null)return "Editora nao especificada";
		return Editora;
	}
	
	public String getCategoria(){
		if(Categoria == null)return "Categoria nao especificada";
		return Categoria;
	}
	
	public int getEdicao(){
		return Edicao;
	}
	
	public String getPdf(){
		return pdfName;
	}
	
	//------------Setters------------------------//
	
	public void setTitulo(String title){
		Titulo = title;
	}
	
	public void setAutores(String autores){
		Autores = autores;
	}
	
	public void setEditora(String editora){
		Editora = editora;
	}
	
	public void setCategoria(String categoria){
		Categoria = categoria;
	}
	
	public void setEdicao(int edition){
		Edicao = edition;
	}
	
	public void setPdfName(String pdfname){
		pdfName = pdfname;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	//----------Add/Rmv-------------//
	public int addAcervo(int quant){
		if(quant>0 && Acervo>=0){
			Acervo += quant;
			return Acervo;
		}
		else{
			System.out.println("Nao foi possivel adicionar");
			return 0;
		}
	}
	
	public int rmvAcervo(int quant){
		if(quant>0 && Acervo>=quant){
			Acervo -= quant;
			return Acervo;
		}
		else{
			System.out.println("Nao foi possivel remover");
			return 0;
		}
	}
	
	//---------------Contem------------------//
	
	public boolean contemTitulo(String title){
		return this.getTitulo().contains(title);
	}
	
	public boolean contemAutores(String autores){
		return this.getAutores().contains(autores);
	}
	
	public boolean contemEditora(String editora){
		return this.getEditora().contains(editora);
	}
	
	public boolean contemCategoria(String category){
		return this.getCategoria().contains(category);
	}

}
