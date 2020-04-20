package zup.webservices.adm;

public class Operadora {
	
	private String nomeOperadora;
	private int codigoOperadora;
	private String categoriaOperadora;
	
	public Operadora(String nomeOperadora, int codigoOperadora, String categoriaOperadora) {
		super();
		this.nomeOperadora = nomeOperadora;
		this.codigoOperadora = codigoOperadora;
		this.categoriaOperadora = categoriaOperadora;
	}
	
	public String getNomeOperadora() {
		return nomeOperadora;
	}
	public void setNomeOperadora(String nomeOperadora) {
		this.nomeOperadora = nomeOperadora;
	}
	public int getCodigoOperadora() {
		return codigoOperadora;
	}
	public void setCodigoOperadora(int codigoOperadora) {
		this.codigoOperadora = codigoOperadora;
	}
	public String getCategoriaOperadora() {
		return categoriaOperadora;
	}
	public void setCategoriaOperadora(String categoriaOperadora) {
		this.categoriaOperadora = categoriaOperadora;
	}
	

}
