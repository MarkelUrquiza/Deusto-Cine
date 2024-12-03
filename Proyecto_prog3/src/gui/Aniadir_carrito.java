package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import db.BBDD;
import domain.Cartelera;
import domain.Cliente;
import domain.Entrada;
import domain.Pelicula;
import domain.Sala;

public class Aniadir_carrito extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel pcentro,pnorte,poeste,psur,pderecha,pcombo,pvacio,pbtnaniadir,pvacio2;
	private JLabel titulo,foto;
	private JButton btncancelar,btnaniadir; 
	private JList<String> listapelis;
	private JComboBox<String> horarios;
	private JTextArea info;
	private JFrame vActual,vInicial;

	public Aniadir_carrito(JFrame vI,Cartelera cartelera,String valor, BBDD bd, Cliente c){
		vActual = this;
		vInicial = vI;
		
		pcentro = new JPanel(new GridLayout(1,2));
		poeste = new JPanel();
		pnorte = new JPanel();
		psur = new JPanel();
		pderecha = new JPanel(new GridLayout(4,1));
		pcombo = new JPanel(new GridLayout(2,1));
		pvacio = new JPanel();
		pbtnaniadir = new JPanel(new GridLayout(2,1));
		pvacio2 = new JPanel();
		
		horarios = new JComboBox<String>();
		
		foto = new JLabel();
		
		titulo = new JLabel("Comprar entradas");
		
		info = new JTextArea();
		info.setEditable(false);
		
		String [] oLista = cargarLista(cartelera);
		
		listapelis = new JList<String>(oLista);
		listapelis.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				}
			
			@Override
			public void focusGained(FocusEvent e) {
				if (valor!=null) {
					listapelis.setSelectedValue(valor, true);
					cargarPanelCentro(valor,cartelera);
				}
				else {
					listapelis.setSelectedIndex(0);
					cargarPanelCentro(listapelis.getSelectedValue(), cartelera);
				}
			}
		});
		
		listapelis.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				String value = listapelis.getSelectedValue();
				if (value!=null) {
					cargarPanelCentro(value,cartelera);
				}
				if (!listapelis.isSelectedIndex(listapelis.getSelectedIndex())) {
					listapelis.setSelectedIndex(0);
					cargarPanelCentro(value, cartelera);
				}
			}
		});
		
		btnaniadir = new JButton("Añadir al carrito");
		btnaniadir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String value = listapelis.getSelectedValue();
				Pelicula p = bd.obtenerPeliculaportitulo(value);
				if (comprobarLimiteEntradas(c, p.getId_sala(), p.getTitulo())) {
					Object resultado = JOptionPane.showInputDialog(null, "Cuantas entradas quieres aniadir?", "Numero de entradas", 
							JOptionPane.QUESTION_MESSAGE, null, new Object[] {1,2,3,4}, 1);
					if (resultado != null && comprobarLimiteEntradas2(c, p.getId_sala(), p.getTitulo(), (int) resultado) == true) {
						new VentanaSeleccionarEntradas(vI,vActual,Integer.parseInt(resultado.toString()),bd,p.getId_sala(),p.getTitulo(),c,horarios.getSelectedItem().toString(),cartelera);
						vActual.setEnabled(false);
					} else {
							JOptionPane.showConfirmDialog(null, "No puedes comprar mas de 4 entradas con esta cuenta, consulta tu carrito y veras cuantas puedes reservar", 
									"No mas entradas", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
						}
				} else {
					JOptionPane.showConfirmDialog(null, "No puedes comprar mas de 4 entradas con esta cuenta, consulta tu carrito y veras cuantas puedes reservar", 
							"No mas entradas", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
				}
								
			}
		});
		btncancelar = new JButton("Cancelar");
		btncancelar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (vInicial.isVisible()) {
					vInicial.setEnabled(true);
					vActual.setVisible(false);
				}
				else {
					vInicial.setVisible(true);
					vActual.setVisible(false);
				}
			}
		});
		
		getContentPane().add(pcentro,BorderLayout.CENTER);
		getContentPane().add(poeste,BorderLayout.WEST);
		getContentPane().add(pnorte,BorderLayout.NORTH);
		getContentPane().add(psur,BorderLayout.SOUTH);
		
		pnorte.add(titulo);
		psur.add(btncancelar);
		poeste.add(listapelis);
		
		pbtnaniadir.add(pvacio2);
		pbtnaniadir.add(btnaniadir);
		
		pcentro.add(foto);
		pcentro.add(pderecha);
		pderecha.add(info);
		pderecha.add(pcombo);
		pderecha.add(pvacio);
		pderecha.add(pbtnaniadir);
		
		ImageIcon imagen = new ImageIcon("resource/images/icono.png");
		setIconImage(imagen.getImage());
		setTitle("DeustoCine");
		setSize(1000,600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}

	public String[] cargarLista(Cartelera cartelera) {
		String palabra = "";
		ArrayList<Pelicula> pelis = new ArrayList<Pelicula>();
		for (Sala sala : cartelera.getCartelera()) {
			for (String string : sala.getHorarios().keySet()) {
				if (!pelis.contains(sala.getHorarios().get(string))) {
					pelis.add(sala.getHorarios().get(string));
					palabra = palabra+sala.getHorarios().get(string).getTitulo()+";";
				}
				
			}
		}
		String[] l = palabra.split(";");
		return l;
	}
	public void cargarPanelCentro(String valor,Cartelera cartelera) {
		ArrayList<Pelicula> pelis = new ArrayList<Pelicula>();
		for (Sala sala : cartelera.getCartelera()) {
			for (String string : sala.getHorarios().keySet()) {
				if (!pelis.contains(sala.getHorarios().get(string))) {
					pelis.add(sala.getHorarios().get(string));
				}
				
			}
		}
		int pos=0;
		boolean enc=false;
		while (!enc&&pos<pelis.size()) {
			if (pelis.get(pos).getTitulo().equals(valor)) {		
				
				Pelicula peli = pelis.get(pos);
				ImageIcon img = new ImageIcon(peli.getRutafoto());
				Image imagenOriginal = img.getImage();
				Image imagenRedimensionada = imagenOriginal.getScaledInstance(foto.getWidth(), foto.getHeight(), Image.SCALE_SMOOTH);
				ImageIcon imgredimensionada = new ImageIcon(imagenRedimensionada);
				foto.setIcon(imgredimensionada);
				
				info.setText("Titulo: "+peli.getTitulo()+"\nDirigida por: "+peli.getDirector()+"\nGenero: "+
						peli.getTipo().toString()+"\nDuracion: "+peli.getDuracion()+"h");
				
				horarios.removeAllItems();
				
				String horas = "";
				for (Sala sala : cartelera.getCartelera()) {
					for (String string : sala.getHorarios().keySet()) {
						if (sala.getHorarios().get(string).equals(peli) && !horas.contains(string)) {
							horas += string+";"; 
						}
					}
				}
				String[] h = horas.split(";");
				for (int i = 0; i < h.length; i++) {
					horarios.addItem(h[i]);
				}
				pcombo.add(horarios, BorderLayout.SOUTH);
				enc = true;
			}else pos++;
		}
		
	}
	public boolean comprobarLimiteEntradas(Cliente c,int id_sala, String titulo) {
	    int contador = 0;

	    for (Entrada e : c.getCarrito_de_compra().keySet()) {

	        if (e.getSala()  == id_sala && e.getTitulo_peli().equals(titulo) && e.getHorario().equals(horarios.getSelectedItem().toString())) {
	            
	            contador = c.getCarrito_de_compra().get(e);
	        }
	    }

	    return (contador) < 5;
	}
	public boolean comprobarLimiteEntradas2(Cliente c,int id_sala, String titulo, int numerodeentradasameter) {
	    int contador = 0;

	    for (Entrada e : c.getCarrito_de_compra().keySet()) {

	        if (e.getSala()  == id_sala && e.getTitulo_peli().equals(titulo) && e.getHorario().equals(horarios.getSelectedItem().toString())) {
	            
	            contador = c.getCarrito_de_compra().get(e);
	        }
	    }

	    return (contador + numerodeentradasameter) < 5;
	}
	
}
