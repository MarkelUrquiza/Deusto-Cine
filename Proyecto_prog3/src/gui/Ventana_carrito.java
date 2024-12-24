package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
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
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import db.BBDD;
import domain.Butaca;
import domain.Cartelera;
import domain.Cliente;
import domain.Entrada;

public class Ventana_carrito extends JFrame{
	private static final long serialVersionUID = 1L;

	private JPanel pcentro,psur,pnorte,pfiltro,pizquierda, tablas, pizqarriba;
	private JTextField filtrar;
	private JComboBox<String> combo;
	private JLabel titulo,filtro, salario;
	private JButton btncartelera,btnañadir,btncomprar;
	private JTable tablacarrito, tablaentradas;
	private ModeloCarrito modelocarrito;
	private ModeloEntradas modeloentrada;
	private JScrollPane scrollTabla, scrollentradas;
	private JFrame vActual, vInicial;
	private int mouseEntradas = -1;
	
	public Ventana_carrito(Cliente c, JFrame vI, Cartelera cartelera, BBDD bd){
		vInicial = vI;
		vActual = this;
		pfiltro = new JPanel(new GridLayout(1,2));
		pcentro = new JPanel(new BorderLayout());
		pnorte = new JPanel(new BorderLayout());
		psur = new JPanel();
		pizquierda = new JPanel();
		tablas = new JPanel(new GridLayout(2,1));
		pizqarriba = new JPanel();
		
		filtrar = new JTextField(20);
		
		filtro = new JLabel("Filtrar por: ");
		
		salario = new JLabel("Salario disponible: " + String.format("%.2f", c.getSalario())+ "€   ");
		salario.setOpaque(true);
		salario.setBackground(Color.LIGHT_GRAY);
		Font fuente2 = new Font(getName(),Font.PLAIN , 15);
		salario.setFont(fuente2);
		
		titulo = new JLabel("Carrito de la compra", JLabel.CENTER);
		titulo.setOpaque(true);
		titulo.setBackground(Color.LIGHT_GRAY);
		Font fuente = new Font(getName(),Font.BOLD , 30);
		titulo.setFont(fuente);
		
		String [] combotitles = {"NOMBRE DE PELICULA","SALA","FECHA","PRECIO"};
		combo = new JComboBox<String>(combotitles);
		combo.setSelectedItem(combotitles[0]);
		
		btncomprar = new JButton("Comprar");
		btncomprar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				bd.comprarCarrito3(c.getDni());
				c.setCarrito_de_compra(new HashMap<Entrada, Integer>());
				modelocarrito = new ModeloCarrito(c.getCarrito_de_compra(),bd);		
				tablacarrito = new JTable(modelocarrito);
				dispose();
				vInicial.setVisible(true);
			}
		});
		btnañadir = new JButton("");
		btnañadir.addActionListener((e)-> {
			new Aniadir_carrito(vI,cartelera,null,bd, c);
			vActual.setVisible(false);
		});
		ImageIcon img = new ImageIcon("resource/images/agregar.png");
    	Image imagenOriginalcolumn = img.getImage();
		Image imagenRedimensionadacolumn = imagenOriginalcolumn.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		ImageIcon imgredimensionadacolumn = new ImageIcon(imagenRedimensionadacolumn);
		btnañadir.setIcon(imgredimensionadacolumn);
		btncartelera = new JButton("");
		btncartelera.addActionListener((e)-> {
			vInicial.setVisible(true);
			vActual.setVisible(false);
		});
		ImageIcon img2 = new ImageIcon("resource/images/volverAtras.png");
    	Image imagenOriginalcolumn2 = img2.getImage();
		Image imagenRedimensionadacolumn2 = imagenOriginalcolumn2.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		ImageIcon imgredimensionadacolumn2 = new ImageIcon(imagenRedimensionadacolumn2);
		btncartelera.setIcon(imgredimensionadacolumn2);
		
		HashMap<Entrada, Integer> carrito = filtrarEntradasUnicas(c.getCarrito_de_compra());

		modelocarrito = new ModeloCarrito(carrito,bd);		
		tablacarrito = new JTable(modelocarrito);
		
		tablacarrito.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JLabel c = new JLabel(value.toString());
				c.setOpaque(true);
				
				if (row%2==0) {
					c.setBackground(Color.cyan);
				} else {
					c.setBackground(Color.white);	
				}
				if (isSelected) {
					c.setBackground(tablacarrito.getSelectionBackground());
					c.setForeground(tablacarrito.getSelectionForeground());
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
		
		pnorte.add(titulo, BorderLayout.CENTER);
		pnorte.add(pizqarriba, BorderLayout.WEST);
		pnorte.add(salario,BorderLayout.EAST);
		
		pizquierda.add(filtro);
		pizquierda.add(filtrar);
		pizquierda.add(combo);

		filtrar.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				if (combo.getSelectedItem() != null) {
					filtrar(c,bd);
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (combo.getSelectedItem() != null) {
					filtrar(c,bd);
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (combo.getSelectedItem() != null) {
					filtrar(c,bd);
				}
			}
		});
		
		tablaentradas = new JTable();
		tablaentradas.addMouseListener(new MouseAdapter() {
		
			@Override
			public void mouseExited(MouseEvent e) {
				mouseEntradas = -1;				
			}
		});
		tablaentradas.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseEntradas = tablaentradas.rowAtPoint(e.getPoint());
				tablaentradas.repaint();	
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				
			}
		});
		tablaentradas.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JLabel r = new JLabel(value.toString(), CENTER);
				r.setOpaque(true);
				if (row == mouseEntradas) {
					r.setBackground(tablaentradas.getSelectionBackground());
					r.setForeground(tablaentradas.getSelectionForeground());
				}
				if (column == 4) {
					if (Float.parseFloat(value.toString()) == 0) {
						r.setBackground(Color.green);
						r.setForeground(Color.white);
					}else if (Float.parseFloat(value.toString()) ==  12) {
						r.setBackground(Color.blue);
						r.setForeground(Color.white);
					} else {
						r.setBackground(Color.gray);
						r.setForeground(Color.white);
					}
				}else {
					r.setBackground(Color.white);
					r.setForeground(Color.black);
				}
				return r;
			}
		});
		
		tablacarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablacarrito.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int row = tablacarrito.getSelectedRow();
				String titulo = (String) tablacarrito.getValueAt(row, 0);
				String horario = (String) tablacarrito.getValueAt(row, 2);
				int sala = (int) tablacarrito.getValueAt(row, 1);
				modeloentrada = new ModeloEntradas(filtrarEntradaParaTablaEntradas(titulo, horario, sala, c.getCarrito_de_compra()));
				tablaentradas.setModel(modeloentrada);				
			}
		});
		scrollentradas = new JScrollPane(tablaentradas);
		
		pfiltro.add(pizquierda);
		
		pizqarriba.add(btncartelera);
		pizqarriba.add(btnañadir);
		
		psur.add(btncomprar);
		
		tablas.add(scrollTabla);
		tablas.add(scrollentradas);
		
		pcentro.add(pfiltro,BorderLayout.NORTH);
		pcentro.add(tablas,BorderLayout.CENTER);
		
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
	
	public void filtrar(Cliente c, BBDD bd) {
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
					modelocarrito = new ModeloCarrito(carritoNP,bd);
				}
				else if (combo.getSelectedItem().toString().equals("SALA")) {
					HashMap<Entrada, Integer> carritoS = new HashMap<Entrada, Integer>();
					for(Entrada entrada: carrito.keySet()) {
						if (Integer.toString(entrada.getSala()).contains(filtrar.getText())) {
							carritoS.put(entrada, carrito.get(entrada));
						}
					}
					modelocarrito = new ModeloCarrito(carritoS, bd);
				}
				else if (combo.getSelectedItem().toString().equals("PRECIO")) {
					HashMap<Entrada, Integer> carritoP = new HashMap<Entrada, Integer>();
					for(Entrada entrada: carrito.keySet()) {
						if (Float.toString(entrada.getPrecio()).contains(filtrar.getText())) {
							carritoP.put(entrada, carrito.get(entrada));
						}
					}
					modelocarrito = new ModeloCarrito(carritoP, bd);
				}
				else {
					HashMap<Entrada, Integer> carritoF = new HashMap<Entrada, Integer>();
					for(Entrada entrada: carrito.keySet()) {
						if (entrada.getHorario().contains(filtrar.getText())) {
							carritoF.put(entrada, carrito.get(entrada));
						}
					}
					modelocarrito = new ModeloCarrito(carritoF, bd);
				}
				tablacarrito.setModel(modelocarrito);
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
	
	public HashMap<Entrada, Integer> filtrarEntradaParaTablaEntradas(String titulo, String horario, int sala, HashMap<Entrada, Integer> carrito){
	    HashMap<Entrada, Integer> carritofiltrado = new HashMap<>();
		
	    for(Entrada e: carrito.keySet()) {
	    	if (e.getHorario().equals(horario) && e.getTitulo_peli().equals(titulo) && e.getSala() == sala) {
				carritofiltrado.put(e, carrito.get(e));
			}
	    }
	    
		return carritofiltrado;
	}

}
