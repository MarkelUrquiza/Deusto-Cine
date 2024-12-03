package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;

import db.BBDD;
import domain.Butaca;
import domain.Cartelera;
import domain.Cliente;
import domain.Entrada;

public class Ventana_carrito extends JFrame{
	private static final long serialVersionUID = 1L;

	private JPanel pcentro,psur,pnorte,pfiltro,pizquierda;
	private JTextField filtrar;
	private JComboBox<String> combo;
	private JLabel titulo,filtro;
	private JButton btncartelera,btnañadir,btncomprar;
	private JTable tablacarrito;
	private ModeloCarrito modelocarrito;
	private JScrollPane scrollTabla;
	private JFrame vActual, vInicial;
	public Ventana_carrito(Cliente c, JFrame vI, Cartelera cartelera, BBDD bd){
		vInicial = vI;
		vActual = this;
		pfiltro = new JPanel(new GridLayout(1,2));
		pcentro = new JPanel(new BorderLayout());
		pnorte = new JPanel();
		psur = new JPanel();
		pizquierda = new JPanel();
		
		filtrar = new JTextField(20);
		
		filtro = new JLabel("Filtrar por: ");
		titulo = new JLabel("Carrito de la compra");
		
		String [] combotitles = {"NOMBRE DE PELICULA","SALA","FECHA","PRECIO"};
		combo = new JComboBox<String>(combotitles);
		combo.setSelectedItem(combotitles[0]);
		
		btncomprar = new JButton("Comprar");
		btncomprar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				bd.comprarCarrito3(c.getDni());
				c.setCarrito_de_compra(new HashMap<Entrada, Integer>());
				modelocarrito = new ModeloCarrito(c.getCarrito_de_compra());		
				tablacarrito = new JTable(modelocarrito);
				dispose();
				vInicial.setVisible(true);
			}
		});
		btnañadir = new JButton("Añadir al carro");
		btnañadir.addActionListener((e)-> {
			new Aniadir_carrito(vI,cartelera,null,bd, c);
			vActual.setVisible(false);
		});
		btncartelera = new JButton("Ver cartelera");
		btncartelera.addActionListener((e)-> {
			vInicial.setVisible(true);
			vActual.setVisible(false);
		});
		HashMap<Entrada, Integer> carrito = filtrarEntradasUnicas(c.getCarrito_de_compra());

		modelocarrito = new ModeloCarrito(carrito);		
		tablacarrito = new JTable(modelocarrito);
		
		tablacarrito.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				
				if (row%2==0) {
					c.setBackground(Color.cyan);
				} else {
					c.setBackground(Color.white);	
				}
				return c;
			}
		});;
		scrollTabla = new JScrollPane(tablacarrito);
		
		scrollTabla.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollTabla.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollTabla.setBorder(new TitledBorder("Entradas"));
		
		//String [] titulos = {"PELICULA","SALA","FILA BUTACA","COLUMNA BUTACA","BUTACA VIP","FECHA","PRECIO","NUMERO DE ENTRADAS"};
		//modelocarrito.setColumnIdentifiers(titulos);
		
		//cargarTablaCarrito(c, bd);	
		tablacarrito.setFillsViewportHeight(true);
		tablacarrito.getTableHeader().setReorderingAllowed(false);
		
		getContentPane().add(pcentro,BorderLayout.CENTER);
		getContentPane().add(pnorte,BorderLayout.NORTH);
		getContentPane().add(psur,BorderLayout.SOUTH);
		
		pnorte.add(titulo);

		
		pizquierda.add(filtro);
		pizquierda.add(filtrar);
		pizquierda.add(combo);

		filtrar.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				if (combo.getSelectedItem() != null) {
					filtrar(c);
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (combo.getSelectedItem() != null) {
					filtrar(c);
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (combo.getSelectedItem() != null) {
					filtrar(c);
				}
			}
		});
		
		pfiltro.add(pizquierda);
		
		psur.add(btncartelera);
		psur.add(btnañadir);
		psur.add(btncomprar);
		
		pcentro.add(pfiltro,BorderLayout.NORTH);
		pcentro.add(scrollTabla,BorderLayout.CENTER);		
		
		ImageIcon imagen = new ImageIcon("resource/images/icono.png");
		setIconImage(imagen.getImage());
		setTitle("DeustoCine");
		setSize(1000,600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}
	
	public void cargarTablaCarrito(Cliente c, BBDD bd) {
		
		if (c.getCarrito_de_compra()!=null) {
			modelocarrito.setRowCount(0);
			for (Entrada entrada: c.getCarrito_de_compra().keySet()) {
				int id_butaca = bd.obtenerIDButacaporIddeEntrada(entrada.getId());
				Butaca butaca = bd.obtenerButacaporId(id_butaca);
				if (butaca.isVip()) {
					Object [] fila = {entrada.getTitulo_peli(),entrada.getSala(),butaca.getFila(),butaca.getColumna(),"SI",entrada.getHorario(),entrada.getPrecio(),c.getCarrito_de_compra().get(entrada)};
					modelocarrito.addRow(fila);
				} else {
					Object [] fila = {entrada.getTitulo_peli(),entrada.getSala(),butaca.getFila(),butaca.getColumna(),"NO",entrada.getHorario(),entrada.getPrecio(),c.getCarrito_de_compra().get(entrada)};
					modelocarrito.addRow(fila);
				}
				
			}	
		}
				
	}
	
	public void filtrar(Cliente c) {
		HashMap<Entrada, Integer> carrito = filtrarEntradasUnicas(c.getCarrito_de_compra());

		if (carrito!=null) {
			if (combo.getSelectedItem().toString()!=null) {
				if (combo.getSelectedItem().toString().equals("NOMBRE DE PELICULA")) {
					HashMap<Entrada, Integer> carritoNP = new HashMap<Entrada, Integer>();
					for(Entrada entrada: carrito.keySet()) {
						if (entrada.getTitulo_peli().contains(filtrar.getText())) {
							carritoNP.put(entrada, carrito.get(entrada));
						}
					}
					modelocarrito = new ModeloCarrito(carritoNP);		
					tablacarrito = new JTable(modelocarrito);
		            tablacarrito.revalidate();
		            tablacarrito.repaint();
				}
				else if (combo.getSelectedItem().toString().equals("SALA")) {
					HashMap<Entrada, Integer> carritoS = new HashMap<Entrada, Integer>();
					for(Entrada entrada: carrito.keySet()) {
						if (entrada.getSala() == Integer.parseInt(filtrar.getText())) {
							carritoS.put(entrada, carrito.get(entrada));
						}
					}
					modelocarrito = new ModeloCarrito(carritoS);		
					tablacarrito = new JTable(modelocarrito);
		            tablacarrito.revalidate();
		            tablacarrito.repaint();
				}
				else if (combo.getSelectedItem().toString().equals("PRECIO")) {
					HashMap<Entrada, Integer> carritoP = new HashMap<Entrada, Integer>();
					for(Entrada entrada: carrito.keySet()) {
						if (entrada.getPrecio() == Integer.parseInt(filtrar.getText())) {
							carritoP.put(entrada, carrito.get(entrada));
						}
					}
					modelocarrito = new ModeloCarrito(carritoP);		
					tablacarrito = new JTable(modelocarrito);
		            tablacarrito.revalidate();
		            tablacarrito.repaint();
				}
				else {
					HashMap<Entrada, Integer> carritoF = new HashMap<Entrada, Integer>();
					for(Entrada entrada: carrito.keySet()) {
						if (entrada.getHorario().equals(filtrar.getText())) {
							carritoF.put(entrada, carrito.get(entrada));
						}
					}
					modelocarrito = new ModeloCarrito(carritoF);		
					tablacarrito = new JTable(modelocarrito);
		            tablacarrito.revalidate();
		            tablacarrito.repaint();
				}
			}
		}
	}	
	public HashMap<Entrada, Integer> filtrarEntradasUnicas(HashMap<Entrada, Integer> carrito) {
	    HashMap<String, Entrada> uniqueEntriesMap = new HashMap<>();
	    HashMap<Entrada, Integer> filteredCarrito = new HashMap<>();

	    for (Entrada e : carrito.keySet()) {
	        Integer cantidad = carrito.get(e);

	        String claveUnica = e.getSala() + "_" + e.getHorario() + "_" + e.getHorario();

	        if (!uniqueEntriesMap.containsKey(claveUnica)) {
	            uniqueEntriesMap.put(claveUnica, e);
	            filteredCarrito.put(e, cantidad);
	        }
	    }

	    return filteredCarrito;
	}


}
