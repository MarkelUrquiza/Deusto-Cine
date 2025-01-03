package gui;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.swing.table.DefaultTableModel;

import db.BBDD;
import domain.Pelicula;

public class ModeloPeliculas extends DefaultTableModel{
	private static final long serialVersionUID = 1L;
	private List<Pelicula> peliculas;
	private List<String> titulos = Arrays.asList("TITULO","DIRECTOR","GENERO","DURACION","HORARIOS");
	private BBDD bd;
	
	public ModeloPeliculas(List<Pelicula> peliculas, BBDD bd) {
		super();
		this.peliculas = peliculas;
		this.bd = bd;
	}

	@Override
	public int getRowCount() {
		if (peliculas == null) {
			return 0;
		}
		return peliculas.size();
	}

	@Override
	public int getColumnCount() {
		return titulos.size();
	}

	@Override
	public String getColumnName(int column) {
		return titulos.get(column);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		switch (column) {
		case 0,1,2,4: return false;
		case 3: return true;
		default: throw new IllegalArgumentException("Unexpected value: " + column);
		}
	}

	@Override
	public Object getValueAt(int row, int column) {
		Pelicula p = peliculas.get(row);
		switch (column) {
		case 0: return p.getTitulo();
		case 1: return p.getDirector();
		case 2: return p.getTipo();
		case 3: return String.format(Locale.US, "%.1f", p.getDuracion());
		case 4: return p.getHorarios();
		default: throw new IllegalArgumentException("Unexpected value: " + column);
		}
	}
	
	@Override
	public void setValueAt(Object aValue, int row, int column) {
	    Pelicula p = peliculas.get(row);
	    switch (column) {
	        case 3:
	            try {
	                p.setDuracion(Float.parseFloat(aValue.toString()));
	                bd.cambiarDuracionPeli(Float.parseFloat(aValue.toString()), p.getHorarios());
	            } catch (NumberFormatException e) {
	                System.err.println("Valor inválido para duración: " + aValue);
	            }
	            break;
	        default:
	            throw new IllegalArgumentException("Unexpected column index: " + column);
	    }
	    fireTableCellUpdated(row, column);
	}

	
}
