package gui;

import java.util.Arrays;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import domain.Pelicula;

public class ModeloPeliculas extends DefaultTableModel{
	private static final long serialVersionUID = 1L;
	private List<Pelicula> peliculas;
	private List<String> titulos = Arrays.asList("TITULO","DIRECTOR","GENERO","DURACION","HORARIOS");
	
	public ModeloPeliculas(List<Pelicula> peliculas) {
		super();
		this.peliculas = peliculas;
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
		// TODO Auto-generated method stub
		return super.isCellEditable(row, column);
	}

	@Override
	public Object getValueAt(int row, int column) {
		Pelicula p = peliculas.get(row);
		switch (column) {
		case 0: return p.getTitulo();
		case 1: return p.getDirector();
		case 2: p.getTipo();
		case 3: p.getDuracion();
		case 4: p.getHorarios();
		default: throw new IllegalArgumentException("Unexpected value: " + column);
		}
	}
	
	
}
