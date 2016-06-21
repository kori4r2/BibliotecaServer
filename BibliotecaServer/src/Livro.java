public class Livro {
	
	private String titulo;
	private String autores;
	private int edicao;
	private String editora;
	private String categoria;
	private String pdfName;
	private int acervo;
	//Construtor que seta o nome do pdf e titulo do livro
	public Livro(String pdfname, String title){
		pdfName = pdfname;
		titulo = title;
		acervo = 1;
	}
	
	// --------- Getters --------------------------//
	public int getAcervo(){
		return acervo;
	}
	
	public String getTitulo(){
		if(titulo == null)return "Book has no title";
		return titulo;
	}
	
	public String getAutores(){
		if(autores == null)return "Autores nao especificados";
		return autores;
	}
	
	public String getEditora(){
		if(editora == null)return "Editora nao especificada";
		return editora;
	}
	
	public String getCategoria(){
		if(categoria == null)return "Categoria nao especificada";
		return categoria;
	}
	
	public int getEdicao(){
		return edicao;
	}
	
	public String getPdf(){
		return pdfName;
	}
	
	//------------Setters------------------------//
	
	public void setTitulo(String title){
		titulo = title;
	}
	
	public void setAutores(String a){
		autores = a;
	}
	
	public void setEditora(String e){
		editora = e;
	}
	
	public void setCategoria(String c){
		categoria = c;
	}
	
	public void setEdicao(int edition){
		edicao = edition;
	}
	
	public void setPdfName(String pdf){
		pdfName = pdf;
	}

	public boolean equals(Livro l){
		return titulo.equals(l.getTitulo());
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	//----------Add/Rmv-------------//
	public int addAcervo(int quant){
		if(quant>0 && acervo>=0){
			acervo += quant;
			return acervo;
		}
		else{
			System.out.println("Nao foi possivel adicionar");
			return 0;
		}
	}
	
	public int rmvAcervo(int quant){
		if(quant>0 && acervo>=quant){
			acervo -= quant;
			return acervo;
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
