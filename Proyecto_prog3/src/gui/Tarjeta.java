package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tarjeta extends JFrame {
    private static final long serialVersionUID = 1L;

    private JFrame vActual, vInicial;
    private JPanel leftPanel, rightPanel, cardPanel;
    private JLabel titleLabel, amountLabel, emailLabel, cardInfoLabel;
    private JTextField emailField, cardField, expiryField, cvcField;
    private JButton acceptButton;

    public Tarjeta(JFrame vI) {
        vInicial = vI;
        vActual = this;

        // Configuración de la ventana principal
        this.setTitle("Tarjeta");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 400);
        this.setLayout(new BorderLayout());

        // Inicializar componentes
        initComponents();

        // Agregar los paneles al frame
        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.CENTER);
        this.add(acceptButton, BorderLayout.SOUTH);

        // Hacer la ventana visible
        this.setVisible(true);
    }

    private void initComponents() {
        // Panel izquierdo - Información del monto
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        titleLabel = new JLabel("Pagar a Pruebas de cursos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        amountLabel = new JLabel("120,00 €");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 22));
        amountLabel.setForeground(Color.BLACK);

        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(10)); // Espacio entre líneas
        leftPanel.add(amountLabel);

        // Panel derecho - Información de pago
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(5, 1, 10, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        emailLabel = new JLabel("Correo electrónico:");
        emailField = new JTextField();

        cardInfoLabel = new JLabel("Información de la tarjeta:");
        cardField = new JTextField("1234 1234 1234 1234");
        expiryField = new JTextField("MM / AA");
        cvcField = new JTextField("CVC");

        cardPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        cardPanel.add(cardField);
        cardPanel.add(expiryField);
        cardPanel.add(cvcField);

        rightPanel.add(emailLabel);
        rightPanel.add(emailField);
        rightPanel.add(cardInfoLabel);
        rightPanel.add(cardPanel);

        // Botón de aceptar
        acceptButton = new JButton("Aceptar");
        acceptButton.setFont(new Font("Arial", Font.BOLD, 14));
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validar campos y mostrar mensaje
                String email = emailField.getText();
                String cardNumber = cardField.getText();
                String expiryDate = expiryField.getText();
                String cvc = cvcField.getText();

                if (email.isEmpty() || cardNumber.isEmpty() || expiryDate.isEmpty() || cvc.isEmpty()) {
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
    }
}
