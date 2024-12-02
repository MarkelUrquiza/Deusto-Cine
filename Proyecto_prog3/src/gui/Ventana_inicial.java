package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import db.BBDD;
import domain.Cartelera;
import domain.Cliente;
import domain.Pelicula;
import domain.Sala;

public class Ventana_inicial extends JFrame{
	private static final long serialVersionUID = 1L;

	private JPanel pcentro,psur,pnorte;
	private JLabel titulo;
	private JButton carrito, cerrarsesion;
	private JScrollPane scroll;
	private JFrame vActual;
	
	public Ventana_inicial(Cartelera cartelera, Cliente c, BBDD bd) {
		vActual = this;
		pcentro = new JPanel();
		psur = new JPanel();
		pnorte = new JPanel();
		
		carrito = new JButton("Ver carrito");
		cerrarsesion = new JButton("Cerrar Sesion");
		
		titulo = new JLabel("Cartelera");
		cargarPelis(cartelera,bd, c);
		
		Font fuente = new Font(getName(),Font.BOLD , 30);
		titulo.setFont(fuente);
		
		getContentPane().add(psur,BorderLayout.SOUTH);
		getContentPane().add(pnorte,BorderLayout.NORTH);

		pnorte.add(titulo);
		pnorte.setBackground(Color.LIGHT_GRAY);
		
		Font fuentebtn = new Font(getName(),Font.BOLD , 16);
		psur.add(carrito);
		carrito.setFont(fuentebtn);
		carrito.addActionListener(e -> {
			new Ventana_carrito(c,vActual,cartelera,bd);
			vActual.setVisible(false);
		});
		psur.add(cerrarsesion);
		cerrarsesion.setFont(fuentebtn);
		cerrarsesion.addActionListener(e -> {
			System.exit(0);
		});

		pcentro.setLayout(new GridLayout(0,4));
		scroll = new JScrollPane(pcentro);
		getContentPane().add(scroll,BorderLayout.CENTER);

		
		ImageIcon imagen = new ImageIcon("resource/images/icono.png");
		setIconImage(imagen.getImage());
		setTitle("DeustoCine");
		setSize(1200,600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
		
	}
	public void cargarPelis(Cartelera cartelera, BBDD bd, Cliente c) {
		Set<Pelicula> setpelis = new HashSet<Pelicula>();
		for (Sala sala : cartelera.getCartelera()) {
			for(String date : sala.getHorarios().keySet()) {
				setpelis.add(sala.getHorarios().get(date));
			}
		}
		ArrayList<Pelicula> pelis = new ArrayList<>(setpelis);
		for(Pelicula p: pelis) {
			JPanel peli = new JPanel(new BorderLayout());
			ImageIcon img = new ImageIcon(p.getRutafoto());
			Image imagenOriginal = img.getImage();
	        Image imagenRedimensionada = imagenOriginal.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
	        ImageIcon imgredimensionada = new ImageIcon(imagenRedimensionada);
			peli.add(new JLabel(imgredimensionada),BorderLayout.CENTER);
			String titulo = p.getTitulo();
			JLabel titulopeli = new JLabel(titulo);
			titulopeli.setHorizontalAlignment(SwingConstants.CENTER);
			peli.add(titulopeli,BorderLayout.SOUTH);
			
			peli.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					vActual.setEnabled(false);
					new Aniadir_carrito(vActual, cartelera, titulo, bd,c);
					
				}
			});
			pcentro.add(peli);
		}
		
	}
	

}
