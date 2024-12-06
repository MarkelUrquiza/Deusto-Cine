package gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import domain.Entrada;

public class ModeloEntradas extends DefaultTableModel{

	private static final long serialVersionUID = 1L;
	private HashMap<Entrada, Integer> entradas;
	private List<String> titulos = Arrays.asList("NOMBRE","APELLIDO","EDAD","PRECIO","FILA","COLUMNA");
	
	public ModeloEntradas(HashMap<Entrada, Integer> entradas) {
		super();
		this.entradas = entradas;
	}
	@Override
	public int getRowCount() {
		if (entradas == null) {
			return 0;
		}
		return entradas.keySet().size();
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
		return false;
	}

	/*@Override
	public Object getValueAt(int row, int column) {
		ArrayList<Entrada> entradass = new ArrayList<Entrada>(entradas.keySet());
		Entrada e = entradass.get(row);
		switch (column) {
			case 0: return e.getNombre();
			case 1: return e.getApellido();
			case 2: return e.getEdad();
			case 3: return e.getPrecio();
			case 4: return e.getAsiento().getFila();
			case 5: return e.getAsiento().getColumna();
			default:
				throw new IllegalArgumentException("Unexpected value: " + column);
		}
	}*/
}
