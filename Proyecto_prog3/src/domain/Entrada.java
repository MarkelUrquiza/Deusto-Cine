package domain;;

public class Entrada {
	private static int cont = 1;
	private int id;
	private Butaca asiento;
	private int sala;
	private int id_peli;
	private String titulo_peli;
	private String horario;
	private float precio;
	private String nombre;
	private String apellido;
	private int edad;
	
	public Entrada(Butaca asiento, int id_peli, String titulo_peli, int sala, float precio, String horario) {
		super();
		this.id = cont;
		this.asiento = asiento;
		this.horario = horario;
		this.precio = precio;
		this.id_peli = id_peli;
		this.titulo_peli = titulo_peli;
		this.sala = sala;
		cont++;
	}
	
	public Entrada() {
		super();
		this.id=cont;
		cont++;
	}

	
	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public Butaca getAsiento() {
		return asiento;
	}

	public void setAsiento(Butaca asiento) {
		this.asiento = asiento;
	}

	public int getId() {
		return id;
	}

	public String getTitulo_peli() {
		return titulo_peli;
	}

	public void setTitulo_peli(String titulo_peli) {
		this.titulo_peli = titulo_peli;
	}

	public int getSala() {
		return sala;
	}

	public int getId_peli() {
		return id_peli;
	}


	@Override
	public String toString() {
		return "Entrada [id=" + id + ", asiento=" + asiento + ", sala=" + sala + ", id_peli=" + id_peli
				+ ", titulo_peli=" + titulo_peli + ", horario=" + horario + ", precio=" + precio + ", nombre=" + nombre
				+ ", apellido=" + apellido + ", edad=" + edad + "]";
	}
	
}
