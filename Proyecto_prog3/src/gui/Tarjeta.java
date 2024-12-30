package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tarjeta extends JFrame {
    private static final long serialVersionUID = 1L;

    private JFrame vActual, vInicial;
    private JPanel pcentro, psur, poeste, peste, ptarjeta, pemail, pmonto;
    private JLabel titulo, cant, lemail, infotarjeta;
    private JTextField email, tarjeta, caducidad, cvc;
    private JButton btnaceptar;

    public Tarjeta(JFrame vI, int cantidad) {
        vInicial = vI;
        vActual = this;
        
        setTitle("Hacer Pago");
        setSize(400, 400);
        setLocationRelativeTo(null);
        ImageIcon imagen = new ImageIcon("resource/images/icono.png");
        setIconImage(imagen.getImage());

        // Inicializar paneles y establecer bordes vacíos para espaciado uniforme
        pcentro = new JPanel();
        pcentro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        psur = new JPanel();
        psur.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        poeste = new JPanel();
        poeste.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        peste = new JPanel();
        peste.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pmonto = new JPanel(new GridLayout(2, 1));
        pemail = new JPanel(new GridLayout(2, 1));
        ptarjeta = new JPanel(new GridLayout(2, 1));

        // Configurar etiquetas y campos de texto
        titulo = new JLabel("Dinero a depositar:");
        titulo.setHorizontalAlignment(JLabel.LEFT);
        titulo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        cant = new JLabel(String.valueOf(cantidad));
        cant.setHorizontalAlignment(JLabel.LEFT);
        cant.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        lemail = new JLabel("Correo electrónico:");
        lemail.setHorizontalAlignment(JLabel.LEFT);
        lemail.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        infotarjeta = new JLabel("Información de la tarjeta:");
        infotarjeta.setHorizontalAlignment(JLabel.LEFT);
        infotarjeta.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        email = new JTextField(20);
        email.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        tarjeta = new JTextField("1234 1234 1234 1234");
        tarjeta.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        caducidad = new JTextField("MM / AA");
        caducidad.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        cvc = new JTextField("CVC");
        cvc.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Panel para organizar los campos de tarjeta
        JPanel tarjetaPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        tarjetaPanel.add(tarjeta);
        tarjetaPanel.add(caducidad);
        tarjetaPanel.add(cvc);

        // Añadir componentes a los paneles correspondientes
        pmonto.add(titulo);
        pmonto.add(cant);
        pemail.add(lemail);
        pemail.add(email);
        ptarjeta.add(infotarjeta);
        ptarjeta.add(tarjetaPanel);

        // Configuración del botón Aceptar
        btnaceptar = new JButton("Aceptar");
        btnaceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validar campos y mostrar mensaje
                String em = email.getText();
                String tar = tarjeta.getText();
                String cadu = caducidad.getText();
                String Cvc = cvc.getText();

                if (em.isEmpty() || tar.isEmpty() || cadu.isEmpty() || Cvc.isEmpty()) {
                    JOptionPane.showMessageDialog(vActual, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(vActual, "Pago realizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    // Cerrar ventana actual y volver a la ventana inicial
                    vActual.dispose();
                    if (vInicial != null) {
                        vInicial.setVisible(true);
                    }
                }
            }
        });

        // Añadir paneles al marco de la ventana
        getContentPane().add(pcentro, BorderLayout.CENTER);
        getContentPane().add(psur, BorderLayout.SOUTH);
        getContentPane().add(poeste, BorderLayout.WEST);
        getContentPane().add(peste, BorderLayout.EAST);

        // Organizar componentes en el panel central
        pcentro.setLayout(new GridLayout(6, 1));
        pcentro.add(pmonto);
        pcentro.add(pemail);
        pcentro.add(ptarjeta);

        // Añadir botón Aceptar al panel sur
        psur.add(btnaceptar);

        // Configuración final de la ventana
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }
}
