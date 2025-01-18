package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.function.Consumer;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import db.BBDD;
import domain.Butaca;
import domain.Cartelera;

public class Ventana_elegirbutaca extends JFrame {
    private static final long serialVersionUID = 1L;

    private JPanel pnorte, psur;
    private JTable tabla;
    private ModeloElegirButacas modelotabla;
    private JScrollPane scroll;
    private int fila, columna;
    private JLabel aviso;

    public Ventana_elegirbutaca(JFrame vI, BBDD bd, int id_sala, Consumer<Butaca> callback, String horario, Cartelera cartelera) {

        psur = new JPanel();
        pnorte = new JPanel();

        fila = -1;
        columna = -1;

        modelotabla = new ModeloElegirButacas(bd.getButacas(id_sala,horario));
        tabla = new JTable(modelotabla);

        tabla.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                fila = tabla.rowAtPoint(p);
                columna = tabla.columnAtPoint(p);
                tabla.repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {}
        });

        tabla.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Point p = e.getPoint();
                    int filaSeleccionada = tabla.rowAtPoint(p);
                    int columnaSeleccionada = tabla.columnAtPoint(p);

                    Object valorCelda = tabla.getValueAt(filaSeleccionada, columnaSeleccionada);
                    if (valorCelda instanceof Butaca) {
                        Butaca b = (Butaca) valorCelda;
                        if (!bd.existeButacaHorario(b.getId(), horario)) {
                            bd.reservarButaca(b, horario);
                            modelotabla = new ModeloElegirButacas(bd.getButacas(id_sala,horario));
                            tabla.setModel(modelotabla);
    		                try {
								Thread.sleep(1000);
						        //IAG ChatGPT
						        //Callback para que devuelva el resultado a ventana seleccionar entradas
	                            callback.accept(b);
	                            dispose();
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}

                        }else {
							JOptionPane.showConfirmDialog(null, "Esta butaca ya esta reservada, elige otra", "Butaca elegida", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
						}
		                
                    }
                }
            }
        });

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel j = new JLabel(value.toString());
                j.setOpaque(true);
                if (value instanceof Butaca) {
                    Butaca b = (Butaca) value;
                    j.setText("");
                    j.setHorizontalAlignment(JLabel.CENTER);
                    ImageIcon img;
                    if (!bd.existeButacaHorario(b.getId(), horario)) {
                        img = new ImageIcon("resource/images/Reservable.png");
                    } else {
                        img = new ImageIcon("resource/images/Reservado.png");
                    }
                    Image imagenOriginalcolumn = img.getImage();
                    Image imagenRedimensionadacolumn = imagenOriginalcolumn.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    ImageIcon imgredimensionadacolumn = new ImageIcon(imagenRedimensionadacolumn);
                    j.setIcon(imgredimensionadacolumn);
                } else {
                    j.setHorizontalAlignment(JLabel.CENTER);
                    j.setFont(new Font(getName(), Font.BOLD, 24));
                }
                if (column == 0) {
                    j.setBackground(Color.white);
                } else if (column != 0 && value.equals("")) {
                    j.setBackground(Color.gray);
                } else {
                    if (row == fila && column == columna) {
                        j.setBackground(Color.blue);
                    } else {
                        j.setBackground(Color.white);
                    }
                }
                return j;
            }
        });

        tabla.setRowHeight(50);
        tabla.getTableHeader().setReorderingAllowed(false);
        scroll = new JScrollPane(tabla);
        
        aviso = new JLabel("Haz doble click sobre el asiento para elegirlo");
        aviso.setFont(new Font("Arial", Font.BOLD, 18));
        
        pnorte.add(aviso);
        
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(psur, BorderLayout.SOUTH);
        getContentPane().add(pnorte, BorderLayout.NORTH);

        ImageIcon imagen = new ImageIcon("resource/images/icono.png");
        setIconImage(imagen.getImage());
        setTitle("DeustoCine");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }
}
