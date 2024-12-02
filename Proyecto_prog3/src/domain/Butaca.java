package domain;

public class Butaca{
	private static int cont = 1;
	private int id;
	private int fila;
	private Columna columna;
	private boolean vip;
	private int id_sala;

	
	public Butaca(int fila, Columna columna, boolean vip, int id_sala) {
		super();
		this.id = cont;
		cont ++;
		this.fila = fila;
		this.columna = columna;
		this.vip = vip;
		this.id_sala = id_sala;
	}
	public Butaca(int fila, Columna columna, boolean vip, int id_sala, String paradiferenciar) {
		this.fila = fila;
		this.columna = columna;
		this.vip = vip;
		this.id_sala = id_sala;
	}
	public Butaca(int id,int fila, Columna columna, boolean vip, int id_sala) {
		super();
		this.id = id;
		this.fila = fila;
		this.columna = columna;
		this.vip = vip;
		this.id_sala = id_sala;
	}
	
	public int getId() {
		return id;
	}

	public int getId_sala() {
		return id_sala;
	}

	public int getFila() {
		return fila;
	}

	public void setFila(int fila) {
		this.fila = fila;
	}

	public Columna getColumna() {
		return columna;
	}



	public void setColumna(Columna columna) {
		this.columna = columna;
	}


	public boolean isVip() {
		return vip;
	}


	public void setVip(boolean vip) {
		this.vip = vip;
	}




	@Override
	public String toString() {
		return "Butaca [id=" + id + ", fila=" + fila + ", columna=" + columna + ", vip=" + vip 
				+ ", id_sala=" + id_sala + "]";
	}
	
	
}
