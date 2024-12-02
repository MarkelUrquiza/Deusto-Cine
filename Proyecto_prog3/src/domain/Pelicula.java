package domain;

public class Pelicula {
	private static int cont = 1;
	private int id;
	private String titulo;
	private String director;
	private Genero tipo;
	private float duracion;
	private String rutafoto;
	private boolean tresd;	
	private String horarios;
	private int id_sala;

	public Pelicula(int id_sala,String titulo, Genero tipo, float duracion, String director, String rutafoto, boolean tresd, String horarios) {
		super();
		this.id = cont;
		this.id_sala = id_sala;
		this.titulo = titulo;
		this.tipo = tipo;
		this.duracion = duracion;
		this.director = director;
		this.rutafoto = rutafoto;
		this.tresd = tresd;
		this.horarios = horarios;
		cont++;
	}
	public Pelicula(int id,int id_sala,String titulo, Genero tipo, float duracion, String director, String rutafoto, boolean tresd, String horarios) {
		super();
		this.id = id;
		this.id_sala = id_sala;
		this.titulo = titulo;
		this.tipo = tipo;
		this.duracion = duracion;
		this.director = director;
		this.rutafoto = rutafoto;
		this.tresd = tresd;
		this.horarios = horarios;
	}

	public boolean isTresd() {
		return tresd;
	}

	public void setTresd(boolean tresd) {
		this.tresd = tresd;
	}

	public String getRutafoto() {
		return rutafoto;
	}

	public void setRutafoto(String rutafoto) {
		this.rutafoto = rutafoto;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public static int getCont() {
		return cont;
	}

	public static void setCont(int cont) {
		Pelicula.cont = cont;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Genero getTipo() {
		return tipo;
	}

	public void setTipo(Genero tipo) {
		this.tipo = tipo;
	}

	public float getDuracion() {
		return duracion;
	}

	public void setDuracion(float duracion) {
		this.duracion = duracion;
	}

	public String getHorarios() {
		return horarios;
	}

	public void setHorarios(String horarios) {
		this.horarios = horarios;
	}

	public int getId_sala() {
		return id_sala;
	}

	public void setId_sala(int id_sala) {
		this.id_sala = id_sala;
	}

	@Override
	public String toString() {
		return "Pelicula [id=" + id + ", titulo=" + titulo + ", director=" + director + ", tipo=" + tipo + ", duracion="
				+ duracion + ", rutafoto=" + rutafoto + ", tresd=" + tresd + ", horarios=" + horarios + ", id_sala="
				+ id_sala + "]";
	}

}
