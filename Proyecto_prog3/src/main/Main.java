package main;

import db.BBDD;
import domain.Cartelera;
import domain.Sala;
import gui.Inicio_sesion;

public class Main {

	public static void main(String[] args) {

		BBDD bd = new BBDD();
		bd.crearBBDD();
		bd.insertardatosporDefecto();
		bd.inicilizarButacasContxt();
		
        Cartelera cartelera = new Cartelera();
        cartelera.setCartelera(cartelera.cargarCartelera(bd));
        
		new Inicio_sesion(cartelera,bd);
		//new VentanaSeleccionarEntradas(1);

	}

}
