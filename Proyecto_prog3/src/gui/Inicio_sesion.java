package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import db.BBDD;
import domain.Cartelera;
import domain.Cliente;

public class Inicio_sesion extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private JLabel nombre, contrasenia;
    private JPanel pcentro, psur, poeste, peste, pcontra, pnombre;
    private JTextField nom;
    private JPasswordField con;
    private JButton btninicio, btnregistro;
    private JFrame vActual;
    
    public Inicio_sesion(BBDD bd) {
        vActual = this;
        setTitle("Inicio de Sesion");
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Inicializar paneles y establecer borde vacío para espaciado uniforme
        pcentro = new JPanel();
        pcentro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        psur = new JPanel();
        psur.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        poeste = new JPanel();
        poeste.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        peste = new JPanel();
        peste.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnombre = new JPanel(new GridLayout(2, 1));
        pcontra = new JPanel(new GridLayout(2, 1));

        // Configurar etiquetas y campos de texto
        nombre = new JLabel("Introduce tu nombre:");
        nombre.setHorizontalAlignment(JLabel.LEFT);
        nombre.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        contrasenia = new JLabel("Introduce tu contrasenia:");
        contrasenia.setHorizontalAlignment(JLabel.LEFT);
        contrasenia.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        nom = new JTextField(20);
        nom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        con = new JPasswordField(20);
        con.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        btnregistro = new JButton("Registrarme");
        btnregistro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Ventana_registro(vActual, bd);
            }
        });
        
        btninicio = new JButton("Iniciar sesion");
        btninicio.addActionListener((e) -> {
            if (con.getText().isEmpty() || nom.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Rellena todas las partes", "CUIDADO!!!", JOptionPane.WARNING_MESSAGE);
                nom.setText("");
                con.setText("");
            } else {
               
                if (bd.existeUsuarioyContrasenia(nom.getText(), con.getText())) {
                	Cliente c = bd.obtenerUsuario(nom.getText(), con.getText());

                	String h = bd.InicioSesion(c.getDni());
                	System.out.println(h);
                    bd.eliminarHorariosPasados(h);

                    
                	if (c.getCarrito_de_compra() == null ) {
                	    c.setCarrito_de_compra(bd.cargarCarrito(c.getDni()));
                	}
                    Cartelera cartelera = new Cartelera();
                    cartelera.setCartelera(cartelera.cargarCartelera(bd));
                    
                    dispose();
                    JProgressBar progressBar = new JProgressBar(0, 100);
                    progressBar.setValue(0);
                    progressBar.setStringPainted(true);

                    JPanel panel = new JPanel();
                    panel.setLayout(new BorderLayout());
                    panel.add(progressBar, BorderLayout.CENTER);

                    JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
                    JDialog dialog = optionPane.createDialog("Cargando...");
                    dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                    
                    new Thread(() -> {
                        int progress = 0;
                        try {
                            while (progress <= 100) {
                                progressBar.setValue(progress);
                                Thread.sleep(30);
                                progress += 1;
                            }
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        } finally {
                            dialog.dispose();
                        }
                    }).start();
                    
                    dialog.setVisible(true);
                	new Ventana_inicial(vActual,cartelera, c, bd);

                }
            }
        });
        InputMap inputMap = btninicio.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = btninicio.getActionMap();

        // Asociar una tecla (ej. "Enter") a una acción
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "Iniciar sesion");
        actionMap.put("Iniciar sesion", new AbstractAction() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void actionPerformed(ActionEvent e) {
            	if (con.getText().isEmpty() || nom.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Rellena todas las partes", "CUIDADO!!!", JOptionPane.WARNING_MESSAGE);
                    nom.setText("");
                    con.setText("");
                } else {
                   
                    if (bd.existeUsuarioyContrasenia(nom.getText(), con.getText())) {
                    	Cliente c = bd.obtenerUsuario(nom.getText(), con.getText());

                    	String h = bd.InicioSesion(c.getDni());
                    	System.out.println(h);
                        bd.eliminarHorariosPasados(h);

                        
                    	if (c.getCarrito_de_compra() == null ) {
                    	    c.setCarrito_de_compra(bd.cargarCarrito(c.getDni()));
                    	}
                        Cartelera cartelera = new Cartelera();
                        cartelera.setCartelera(cartelera.cargarCartelera(bd));
                        
                        dispose();
                        JProgressBar progressBar = new JProgressBar(0, 100);
                        progressBar.setValue(0);
                        progressBar.setStringPainted(true);

                        JPanel panel = new JPanel();
                        panel.setLayout(new BorderLayout());
                        panel.add(progressBar, BorderLayout.CENTER);

                        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
                        JDialog dialog = optionPane.createDialog("Cargando...");
                        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                        
                        new Thread(() -> {
                            int progress = 0;
                            try {
                                while (progress <= 100) {
                                    progressBar.setValue(progress);
                                    Thread.sleep(30);
                                    progress += 1;
                                }
                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt();
                            } finally {
                                dialog.dispose();
                            }
                        }).start();
                        
                        dialog.setVisible(true);
                    	new Ventana_inicial(vActual,cartelera, c, bd);

                    }
                }
            }
        });
        Font fuente = new Font(getName(), Font.BOLD, 16);
        nombre.setFont(fuente);
        contrasenia.setFont(fuente);
        btninicio.setFont(fuente);
        btnregistro.setFont(fuente);
        
        Font otra = new Font(getName(), Font.ITALIC, 13);
        nom.setFont(otra);
        con.setFont(otra);

        // Añadir paneles al marco de la ventana
        getContentPane().add(pcentro, BorderLayout.CENTER);
        getContentPane().add(psur, BorderLayout.SOUTH);
        getContentPane().add(poeste, BorderLayout.WEST);
        getContentPane().add(peste, BorderLayout.EAST);

        // Organizar componentes en el panel central
        pcentro.setLayout(new GridLayout(4, 1));
        pcentro.add(nombre);
        pcentro.add(pnombre);
        pcentro.add(contrasenia);
        pcentro.add(pcontra);
        
        pnombre.add(nom);
        pcontra.add(con);

        // Agregar botones en el panel inferior
        psur.add(btninicio);
        psur.add(btnregistro);
        
        // Configuración de la ventana
        ImageIcon imagen = new ImageIcon("resource/images/icono.png");
        setIconImage(imagen.getImage());
        setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);		
		
		// Añadir un WindowListener para capturar el cierre de la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres cerrar la ventana?", "Confirmar cierre", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                switch (result) {
				case JOptionPane.YES_OPTION: {
					System.exit(0);
				}
				default:
				}
            }
        });
    }
   
}
