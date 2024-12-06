package domain;

import java.util.HashMap;

public class Cliente{
	private String user;
	private String contrasenia;
	private String nombre; 
	private String apellidos;
	private String dni;
	private String correo; 
	private HashMap<Entrada, Integer> carrito_de_compra;

	public Cliente(String user, String contrasenia, String nombre, String apellidos, String dni, String correo,HashMap<Entrada, Integer> carrito_de_compra) {
		this.user = user;
		this.contrasenia = contrasenia;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
		this.correo = correo;
		this.carrito_de_compra = carrito_de_compra;
	}
	
	
	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getContrasenia() {
		return contrasenia;
	}


	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getApellidos() {
		return apellidos;
	}


	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}


	public String getDni() {
		return dni;
	}


	public void setDni(String dni) {
		this.dni = dni;
	}


	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public HashMap<Entrada, Integer> getCarrito_de_compra() {
		return carrito_de_compra;
	}

	public void setCarrito_de_compra(HashMap<Entrada, Integer> carrito_de_compra) {
		this.carrito_de_compra = carrito_de_compra;
	}
	/*public HashMap<Entrada, Integer> cargarEntradas() {
		HashMap<Entrada, Integer> entradas = new HashMap<Entrada, Integer>();
		File f = new File("resource/data/Entrada.txt");
		try {
			Scanner sc = new Scanner(f);
			while (sc.hasNextLine()) {
				String string = sc.nextLine();
				String[] campos = string.split(";");
				
				int id_sala = Integer.parseInt(campos[0]);
				int id_peli = Integer.parseInt(campos[1]);
				String titulo = campos[2];
				int fila = Integer.parseInt(campos[3]);
				int column = Integer.parseInt(campos[4]);
				boolean vip = Boolean.valueOf(campos[5]);
				String hora = campos[6];
				
				Butaca butaca = new Butaca(fila, Columna.values()[column], vip,  id_sala);
				Entrada entrada = new Entrada(butaca, id_peli, titulo, id_sala,10,hora);

				
				entradas.put(entrada, 1);
			}
			sc.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return entradas;
	}*/
}
