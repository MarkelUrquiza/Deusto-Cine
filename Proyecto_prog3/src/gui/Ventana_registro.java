package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import db.BBDD;
import domain.Cliente;

public class Ventana_registro extends JFrame {
    private static final long serialVersionUID = 1L;

    private JButton btnregistrar, btncancelar;
    private JPanel pcentro, psur;
    private JLabel user, contrasenia, confirmar, nombre, apellido, dni, correo;
    private JTextField u, con, conf, nom, ape, dn, cor;
    private JFrame vIncial, vActual;
    public Ventana_registro(JFrame vI, BBDD bd) {
        vIncial = vI;
        vActual = this;

        pcentro = new JPanel(new GridLayout(14, 1, 5, 5));
        pcentro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        psur = new JPanel();
        psur.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        nombre = new JLabel("Introduce tu nombre: ");
        nombre.setHorizontalAlignment(SwingConstants.LEFT);
        nombre.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        apellido = new JLabel("Introduce tu apellido: ");
        apellido.setHorizontalAlignment(SwingConstants.LEFT);
        apellido.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        correo = new JLabel("Introduce tu correo: ");
        correo.setHorizontalAlignment(SwingConstants.LEFT);
        correo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        dni = new JLabel("Introduce tu DNI: ");
        dni.setHorizontalAlignment(SwingConstants.LEFT);
        dni.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        user = new JLabel("Introduce nombre de usuario: ");
        user.setHorizontalAlignment(SwingConstants.LEFT);
        user.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        contrasenia = new JLabel("Introduce contrasenia: ");
        contrasenia.setHorizontalAlignment(SwingConstants.LEFT);
        contrasenia.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        confirmar = new JLabel("Confirma la contrasenia: ");
        confirmar.setHorizontalAlignment(SwingConstants.LEFT);
        confirmar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        nom = new JTextField();
        nom.setPreferredSize(new Dimension(200, 30));
        nom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        ape = new JTextField();
        ape.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        cor = new JTextField();
        cor.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        dn = new JTextField();
        dn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        u = new JTextField();
        u.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        con = new JTextField();
        con.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        conf = new JTextField();
        conf.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        Font fuente = new Font("Arial", Font.BOLD, 16);
        
        btnregistrar = new JButton("Registrar");
        btnregistrar.setFont(fuente);
        btnregistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = cor.getText().trim();
                String dniTexto = dn.getText().trim().toUpperCase();
                //IAG ChatGPT
                //Comprobar todos los datos
                if (!conf.getText().equals(con.getText())) {
                    JOptionPane.showConfirmDialog(null, "Contraseña y Confirmar contraseña no son iguales",
                            "Contraseñas mal!!!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    con.setText("");
                    conf.setText("");
                } else if (nom.getText().isEmpty() || ape.getText().isEmpty() || email.isEmpty() || dniTexto.isEmpty()
                        || u.getText().isEmpty() || con.getText().isEmpty() || conf.getText().isEmpty()) {
                    JOptionPane.showConfirmDialog(null, "Hay que rellenar todos los huecos",
                            "CUIDADO!!!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                } else if (!email.endsWith("@gmail.com")) {
                    JOptionPane.showConfirmDialog(null, "El correo debe terminar en @gmail.com",
                            "Correo inválido!!!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                } else if (!dniTexto.matches("\\d{8}[A-Za-z]")) {
                    JOptionPane.showConfirmDialog(null, "El DNI debe tener 8 números seguidos de una letra",
                            "DNI inválido!!!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    if (bd.existeUsuario(u.getText(), dniTexto, email)) {
                        u.setText(""); con.setText(""); nom.setText(""); ape.setText(""); dn.setText(""); cor.setText("");
                        JOptionPane.showConfirmDialog(null, "Este DNI, correo o nombre de usuario ya está en uso",
                                "Usuario existente!!!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        Cliente c = new Cliente(u.getText(), con.getText(), nom.getText(), ape.getText(), dniTexto, email, null, 0);
                        bd.registrar(c);
                        dispose();
                        vI.setVisible(true);
                    }
                }
            }
        });
        
        btncancelar = new JButton("Cancelar");
        btncancelar.setFont(fuente);
        btncancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vIncial.setVisible(true);
                vActual.setVisible(false);
            }
        });

        getContentPane().add(pcentro, BorderLayout.CENTER);
        getContentPane().add(psur, BorderLayout.SOUTH);

        pcentro.add(nombre);
        pcentro.add(nom);
        pcentro.add(apellido);
        pcentro.add(ape);
        pcentro.add(correo);
        pcentro.add(cor);
        pcentro.add(dni);
        pcentro.add(dn);
        pcentro.add(user);
        pcentro.add(u);
        pcentro.add(contrasenia);
        pcentro.add(con);
        pcentro.add(confirmar);
        pcentro.add(conf);
        

        psur.add(btncancelar);
        psur.add(btnregistrar);

        ImageIcon imagen = new ImageIcon("resource/images/icono.png");
        setIconImage(imagen.getImage());
        setTitle("DeustoCine");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }
}
