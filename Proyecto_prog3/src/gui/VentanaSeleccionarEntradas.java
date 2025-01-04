package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import db.BBDD;
import domain.Butaca;
import domain.Cartelera;
import domain.Cliente;
import domain.Pelicula;

public class VentanaSeleccionarEntradas extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTabbedPane pagina;
    private JFrame vNueva;
    private JFrame vActual;
    private JFrame vAnterior;
    private ArrayList<Butaca> butacasSeleccionadas = new ArrayList<>();

    public VentanaSeleccionarEntradas(JFrame vi,JFrame vI, int numeroPersonas, BBDD bd, int id_sala, String titulo, Cliente c, String horario, Cartelera cartelera) {
        vNueva = vI;
        vAnterior = vi;
        vActual = this;

        pagina = new JTabbedPane();

        for (int i = 1; i <= numeroPersonas; i++) {
            JLabel nombre = new JLabel("Nombre:");
            nombre.setHorizontalAlignment(SwingConstants.LEFT);
            JLabel apellido1 = new JLabel("Primer Apellido:");
            apellido1.setHorizontalAlignment(SwingConstants.LEFT);
            JLabel apellido2 = new JLabel("Segundo Apellido:");
            apellido2.setHorizontalAlignment(SwingConstants.LEFT);
            JLabel edadLabel = new JLabel("Edad: 0");
            edadLabel.setHorizontalAlignment(SwingConstants.LEFT);

            JTextField nom = new JTextField();
            JTextField ap1 = new JTextField();
            JTextField ap2 = new JTextField();
            nom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            ap1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            ap2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JButton btnelegirbutaca = new JButton("Elegir Butaca");
            int personaIndex = i - 1;
            btnelegirbutaca.addActionListener(e -> {
                if (butacasSeleccionadas.size() > personaIndex && butacasSeleccionadas.get(personaIndex) != null) {
                    Butaca butacaPrevia = butacasSeleccionadas.get(personaIndex);
                    bd.CancelaReservaButaca(butacaPrevia, horario);
                }
		        //IAG ChatGPT
		        //Hacer que te devuelva la butaca seleccionada
                new Ventana_elegirbutaca(vActual, bd, id_sala, butacaSeleccionada -> {
                    if (butacasSeleccionadas.size() > personaIndex) {
                        butacasSeleccionadas.set(personaIndex, butacaSeleccionada);
                    } else {
                        butacasSeleccionadas.add(personaIndex,butacaSeleccionada);
                    }
                },horario, cartelera);
            });

            JSlider edad = new JSlider(0, 100, 0);
            edad.setPaintTicks(true);
            edad.setPaintLabels(true);
            edad.setMajorTickSpacing(20);
            edad.setMinorTickSpacing(5);
	        //IAG ChatGPT
	        //Para que cambie al instante sin hacer un hilo
            edad.addChangeListener(cl -> {
                edadLabel.setText("Edad: " + edad.getValue());
            });

            JPanel panelCliente = new JPanel(new BorderLayout());
            JPanel datos = new JPanel(new GridLayout(9, 1, 10, 10));
            datos.setBorder(new EmptyBorder(10, 10, 10, 10));
            datos.add(nombre);
            datos.add(nom);
            datos.add(apellido1);
            datos.add(ap1);
            datos.add(apellido2);
            datos.add(ap2);
            datos.add(edadLabel);
            datos.add(edad);
            datos.add(btnelegirbutaca);

            panelCliente.add(new JPanel(), BorderLayout.EAST);
            panelCliente.add(new JPanel(), BorderLayout.WEST);
            panelCliente.add(datos, BorderLayout.CENTER);

            pagina.addTab("Persona " + i, panelCliente);
        }

        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.addActionListener(e -> {
            boolean valido = true;
            boolean validaredad = true;
            boolean validarnombres = true;
            boolean validarbutacas =true;
            
            LinkedList<Object> ll = new LinkedList<Object>();
            ArrayList<String> al = new ArrayList<String>();
	        //IAG ChatGPT
	        //Coger la informacion de lo anterior
            for (int i = 0; i < pagina.getTabCount(); i++) {
                JPanel panelCliente = (JPanel) pagina.getComponentAt(i);
                JPanel datos = (JPanel) panelCliente.getComponent(2);

                JTextField nom = (JTextField) datos.getComponent(1);
                JTextField ap1 = (JTextField) datos.getComponent(3);
                JTextField ap2 = (JTextField) datos.getComponent(5);
                JSlider edad = (JSlider) datos.getComponent(7);
                
				String con = nom.getText() + ap1.getText() + ap2.getText();
				
                if (nom.getText().isEmpty() || ap1.getText().isEmpty() || ap2.getText().isEmpty()) {
                    valido = false;
                } else if (butacasSeleccionadas.size() != numeroPersonas){
                	validarbutacas = false;
                } else if (edad.getValue() < 3){
                	validaredad = false;
                } else if(al.contains(con)) {
                	validarnombres = false;
                } else {
					ll.addLast(edad.getValue());
					ll.addLast(nom.getText());
					ll.addLast(ap1.getText());
					ll.addLast(ap2.getText());
					al.add(con);
				}
            }

            if (valido && validaredad && validarnombres && validarbutacas) {
                Pelicula p = bd.obtenerPeliculaportitulo(titulo);
                if (c.getCarrito_de_compra().isEmpty()) {
                    bd.meterCarrito(c.getDni());
				}

                int id_peli = p.getId();
                for (Butaca butaca : butacasSeleccionadas) {
                    if (butaca != null) {
                    	int id_carrito = bd.obtenerIdcarritopordni(c.getDni());
                    	int edad = (int) ll.removeFirst();
                    	String nombre = (String) ll.removeFirst();
                    	String apellido = (String) ll.removeFirst();
                    	String apellido2 = (String) ll.removeFirst();
                    	bd.meterEntrada(butaca.getId(), id_peli, horario, id_carrito, edad, nombre, apellido, apellido2);
                        
                    }
                }
                c.setCarrito_de_compra(bd.cargarCarrito(c.getDni()));
                vAnterior.setEnabled(true);
                vAnterior.setVisible(true);
                vNueva.setVisible(false);
                vActual.dispose();
            } else if (!validarbutacas){
            	JOptionPane.showMessageDialog(null, "Tienes que elegir una butaca por persona", "Butacas incorrectas", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (!valido){
            	JOptionPane.showMessageDialog(null, "No puedes dejar parametros sin rellenar", "Texto vacio", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (!validarnombres) {
            	JOptionPane.showMessageDialog(null, "No puede haber dos entradas con nombre y apellidos totalmente iguales", "Duplicados", JOptionPane.WARNING_MESSAGE);
                return;
			} else {
            	JOptionPane.showMessageDialog(null, "La edad tiene que ser superior o igual a 3 años", "Edad incorrecta", JOptionPane.WARNING_MESSAGE);
                return;
			}
        });

        JPanel panelSur = new JPanel();
        panelSur.add(btnAceptar);

        add(pagina, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);

        ImageIcon imagen = new ImageIcon("resource/images/icono.png");
        setIconImage(imagen.getImage());
        setTitle("DeustoCine");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }
}
