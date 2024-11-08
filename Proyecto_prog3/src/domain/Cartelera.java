package domain;

import java.util.ArrayList;

public class Cartelera {
	private ArrayList<Sala> cartelera;

	public Cartelera() {
	}

	public ArrayList<Sala> getCartelera() {
		return cartelera;
	}

	public void setCartelera(ArrayList<Sala> cartelera) {
		this.cartelera = cartelera;
	}
	
	public ArrayList<Sala> cargarCartelera() {
		ArrayList<Sala> cartel = new ArrayList<Sala>();
		for (int i = 0; i < 4; i++) {
			Sala sala = new Sala();
			sala.setButacas(sala.crearButacas());
			sala.cargarPelis();
			cartel.add(sala);
		}
		return cartel;
		
	}
	
	
}
