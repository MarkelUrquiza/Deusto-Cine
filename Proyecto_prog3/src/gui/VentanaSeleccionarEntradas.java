package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VentanaSeleccionarEntradas extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTabbedPane pagina;
    private JLabel nombre, apellido1, apellido2, edadLabel;
    private JTextField nom, ap1, ap2;
    private JSlider edad;
    private Thread edadThread;
    private JFrame vNueva;  // Nueva ventana
    private JFrame vActual; // Ventana actual

    public VentanaSeleccionarEntradas(JFrame vI, int numeroPersonas) {
        vNueva = vI; // Asignar la ventana inicial
        vActual = this; // Asignar la ventana actual

        pagina = new JTabbedPane();

        for (int i = 1; i <= numeroPersonas; i++) {
            nombre = new JLabel("Nombre:");
            nombre.setHorizontalAlignment(SwingConstants.LEFT);
            apellido1 = new JLabel("Primer Apellido:");
            apellido1.setHorizontalAlignment(SwingConstants.LEFT);
            apellido2 = new JLabel("Segundo Apellido:");
            apellido2.setHorizontalAlignment(SwingConstants.LEFT);
            edadLabel = new JLabel("Edad: 0");
            edadLabel.setHorizontalAlignment(SwingConstants.LEFT);

            nom = new JTextField();
            ap1 = new JTextField();
            ap2 = new JTextField();
            nom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            ap1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            ap2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            edad = new JSlider(0, 100, 25);
            edad.setPaintTicks(true);
            edad.setPaintLabels(true);
            edad.setMajorTickSpacing(20);
            edad.setMinorTickSpacing(5);

            Runnable edadUpdater = () -> {
                while (true) {
                    edadLabel.setText("Edad: " + edad.getValue());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            edadThread = new Thread(edadUpdater);
            edadThread.start();

            JPanel panelCliente = new JPanel(new BorderLayout());
            JPanel datos = new JPanel(new GridLayout(8, 1, 10, 10));
            datos.setBorder(new EmptyBorder(10, 10, 10, 10));
            datos.add(nombre);
            datos.add(nom);
            datos.add(apellido1);
            datos.add(ap1);
            datos.add(apellido2);
            datos.add(ap2);
            datos.add(edadLabel);
            datos.add(edad);

            panelCliente.add(new JPanel(), BorderLayout.EAST);
            panelCliente.add(new JPanel(), BorderLayout.WEST);
            panelCliente.add(datos, BorderLayout.CENTER);

            pagina.addTab("Persona " + i, panelCliente);
        }

        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.addActionListener(e -> {
        	vNueva.setEnabled(true);
            vActual.dispose(); // Desactivar la ventana actual
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
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
