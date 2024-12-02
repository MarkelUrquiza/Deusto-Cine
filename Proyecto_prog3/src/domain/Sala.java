package domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import db.BBDD;

public class Sala {
	private static int cont = 1;
	private int id;
	private HashMap<String, ArrayList<Butaca>> butacas;
	private HashMap<String, Pelicula> horarios;
	
	public Sala() {
		super();
		this.id = cont;
		cont ++;
	}

	public Sala(HashMap<String, ArrayList<Butaca>> butacas, HashMap<String, Pelicula> horarios) {
		super();
		this.id = cont;
		cont ++;
		this.butacas = butacas;
		this.horarios = horarios;
	}

	public int getId() {
		return id;
	}

	public HashMap<String, ArrayList<Butaca>> getButacas() {
		return butacas;
	}

	public void setButacas(HashMap<String, ArrayList<Butaca>> butacas) {
		this.butacas = butacas;
	}

	public HashMap<String, Pelicula> getHorarios() {
		return horarios;
	}

	public void setHorarios(HashMap<String, Pelicula> horarios) {
		this.horarios = horarios;
	}
	
	public ArrayList<Butaca> crearButacas() {
		ArrayList<Butaca> asientos = new ArrayList<Butaca>();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 6; j++) {
				Butaca b = new Butaca(i, Columna.values()[j], false, this.id);
				if (i==8||i==9) {
					b.setVip(true);
				}
				asientos.add(b);
			}
			
		}
		return asientos;
	}
	
	public void cargarPelis(BBDD bd) {
		this.horarios= new HashMap<String, Pelicula>();
		try {
			File f = new File("resource/data/Pelis.txt");
			Scanner sc = new Scanner(f);
			while (sc.hasNextLine()) {
				String string = sc.nextLine();
				String [] campos = string.split(";");
				String titulo = campos[0];
				String dir = campos[1];
				Genero tipo = Genero.valueOf(campos[2]);
				float duracion = Float.parseFloat(campos[3]);
				String rutafoto = campos[4];
				boolean tresd = Boolean.parseBoolean(campos[5]);
				int id_sala = Integer.parseInt(campos[6]);
				
				String[] fechas = campos[7].split(",");
				if (id_sala==this.id) {
					int id_peli = bd.obtenerIdPeliculaPorTitulo(titulo);
					Pelicula peli = new Pelicula(id_peli ,id_sala,titulo, tipo, duracion, dir, rutafoto, tresd,null);
					for (int i = 0; i < fechas.length; i++) {
						if (!horarios.keySet().contains(fechas[i])) {
							this.horarios.put(fechas[i],peli);
						}		
					}
				}
				
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
