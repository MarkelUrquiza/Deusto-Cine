package main;

import db.BBDD;
import gui.Inicio_sesion;

public class Main {

	public static void main(String[] args) {

		BBDD bd = new BBDD();
		bd.crearBBDD();
		bd.inicilizarButacasContxt();
		bd.insertarDatosPorDefecto();
        
		new Inicio_sesion(bd);

	}

}
