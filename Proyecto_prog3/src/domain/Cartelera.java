package domain;

import java.util.ArrayList;
import java.util.HashMap;

import db.BBDD;

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
	
	public ArrayList<Sala> cargarCartelera(BBDD bd) {
		ArrayList<Sala> cartel = new ArrayList<Sala>();
		for (int i = 0; i < 4; i++) {
			Sala sala = new Sala();
			
			sala.setHorarios(bd.getPeliculas(i+1));
			HashMap<String, ArrayList<Butaca>> butacas = new HashMap<>();
			for (String h: sala.getHorarios().keySet()) {
				butacas.put(h, (ArrayList<Butaca>) bd.getButacas(i+1,h));
				
			}
			sala.setButacas(butacas);
			
			//sala.cargarPelis();
			cartel.addLast(sala);
		}
		return cartel;
		
	}
	
	
}
