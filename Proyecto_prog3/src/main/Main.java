package main;

import db.BBDD;
import gui.Inicio_sesion;

public class Main {

	public static void main(String[] args) {

		BBDD bd = new BBDD();
		bd.crearBBDD();
		bd.insertardatosporDefecto();
		bd.inicilizarButacasContxt();
        
        //new Ventana_elegirbutaca(bd);
		new Inicio_sesion(bd);
		//new VentanaSeleccionarEntradas(1);

	}

}
