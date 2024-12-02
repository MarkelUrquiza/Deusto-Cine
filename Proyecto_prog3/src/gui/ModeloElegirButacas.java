package gui;

import java.util.Arrays;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import domain.Butaca;

public class ModeloElegirButacas extends DefaultTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Butaca> butacas;
	private List<String> titulos = Arrays.asList("","A","B","","C","D","E","","F","G");
	
	public ModeloElegirButacas(List<Butaca> butacas) {
		this.butacas = butacas;
	}
	

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}


	@Override
	public int getRowCount() {
		if (butacas == null) {
			return 0;
		}
		return butacas.size()/7;
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
	public Object getValueAt(int row, int column) {
		int fila = row;
		row = ((1+row)*7);
		switch (column) {
			case 0: return fila+1;
			case 1: return butacas.get(row-7);
			case 2: return butacas.get(row-6);
			case 3: return "";
			case 4: return butacas.get(row-5);
			case 5: return butacas.get(row-4);
			case 6: return butacas.get(row-3);
			case 7: return "";
			case 8: return butacas.get(row-2);
			case 9: return butacas.get(row-1);
		default:
			throw new IllegalArgumentException("Unexpected value: " + column);
		}
	}
	
	
	
	
}
