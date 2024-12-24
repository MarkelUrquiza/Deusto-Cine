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
	private String apellido2;
	private int edad;
	
	public Entrada(Butaca asiento, int id_peli, String titulo_peli, int sala, float precio, String horario, String nombre, String apellido, int edad, String apellido2) {
		super();
		this.id = cont;
		this.asiento = asiento;
		this.horario = horario;
		this.precio =  CalcularPrecio(edad);
		this.id_peli = id_peli;
		this.titulo_peli = titulo_peli;
		this.sala = sala;
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
		this.apellido2 = apellido2;
		cont++;
	}
	public Entrada(int id,Butaca asiento, int id_peli, String titulo_peli, int sala, float precio, String horario, String nombre, String apellido, int edad, String apellido2) {
		super();
		this.id = id;
		this.asiento = asiento;
		this.horario = horario;
		this.precio =  CalcularPrecio(edad);
		this.id_peli = id_peli;
		this.titulo_peli = titulo_peli;
		this.sala = sala;
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
		this.apellido2 = apellido2;
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
	

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}
	
	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	@Override
	public String toString() {
		return "Entrada [id=" + id + ", asiento=" + asiento + ", sala=" + sala + ", id_peli=" + id_peli
				+ ", titulo_peli=" + titulo_peli + ", horario=" + horario + ", precio=" + precio + ", nombre=" + nombre
				+ ", apellido=" + apellido + ", edad=" + edad + "]";
	}
	
	public float CalcularPrecio(int edad) {
		if (edad < 3) {
			return -1;
		} else if (edad < 10) {
			return 0;
		} else if (edad < 18) {
			return 8;
		} else if (edad < 65) {
			return 12;
		} else {
			return 8;
		}
		
	}
}
