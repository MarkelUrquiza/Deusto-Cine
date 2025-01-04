package domain;

import java.util.HashMap;

public class Cliente{
	private String user;
	private String contrasenia;
	private String nombre; 
	private String apellidos;
	private String dni;
	private String correo; 
	private float salario;
	private HashMap<Entrada, Integer> carrito_de_compra;

	public Cliente(String user, String contrasenia, String nombre, String apellidos, String dni, String correo,HashMap<Entrada, Integer> carrito_de_compra, float salario) {
		this.user = user;
		this.contrasenia = contrasenia;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
		this.correo = correo;
		this.carrito_de_compra = carrito_de_compra;
		this.salario = salario;
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


	public float getSalario() {
		return salario;
	}


	public void setSalario(float salario) {
		this.salario = salario;
	}
	
}
