package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import db.BBDD;
import domain.Cartelera;

public class Inicio_sesion extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private JLabel nombre, contrasenia;
    private JPanel pcentro, psur, poeste, peste, pcontra, pnombre;
    private JTextField nom;
    private JPasswordField con;
    private JButton btninicio, btncierre, btnregistro;
    private JFrame vActual;
    
    public Inicio_sesion(Cartelera cartelera, BBDD bd) {
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

        // Configurar botones y acciones
        btncierre = new JButton("Cerrar sesion");
        btncierre.addActionListener((e) -> System.exit(0));
        
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
                    dispose();
                	JOptionPane.showConfirmDialog(null, "Bienvenido a nuestro cine "+nom.getText(), "Bienvenido", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                	try {
						Thread.sleep(2000);
	                    new Ventana_inicial(cartelera);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

                }
            }
        });

        // Configurar fuentes y bordes
        Font fuente = new Font(getName(), Font.BOLD, 16);
        nombre.setFont(fuente);
        contrasenia.setFont(fuente);
        btncierre.setFont(fuente);
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
        psur.add(btncierre);

        // Configuración de la ventana
        ImageIcon imagen = new ImageIcon("resource/images/icono.png");
        setIconImage(imagen.getImage());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }
   
}
