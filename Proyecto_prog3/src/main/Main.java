package main;

import db.BBDD;
import domain.Cartelera;
import domain.Sala;
import gui.Inicio_sesion;
import gui.Ventana_elegirbutaca;

public class Main {

	public static void main(String[] args) {

		BBDD bd = new BBDD();
		bd.crearBBDD();
		bd.insertardatosporDefecto();
		bd.inicilizarButacasContxt();
		
        Cartelera cartelera = new Cartelera();
        cartelera.setCartelera(cartelera.cargarCartelera(bd));
        
        //new Ventana_elegirbutaca(bd);
		new Inicio_sesion(cartelera,bd);
		//new VentanaSeleccionarEntradas(1);

	}

}
