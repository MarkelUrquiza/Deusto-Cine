package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import db.BBDD;
import domain.Genero;
import domain.Pelicula;

public class VentanaAdmin extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTable tabla, tablaHorarios;
    private ModeloPeliculas modelo;
    private DefaultTableModel modeloHorarios;
    private JScrollPane scroll, scrollHorarios;
    private JButton btnVolver, btnEliminar;
    private int mouse = -1;
    private JFrame vInicial, vActual;
    private JPanel pCentral, psur;

    public VentanaAdmin(JFrame vI, BBDD bd) {
        vInicial = vI;
        vActual = this;
        
        psur =  new JPanel();
        
        ArrayList<Pelicula> pelis = (ArrayList<Pelicula>) bd.cogerPelis();
        
        btnVolver = new JButton("VOLVER");
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vActual.dispose();
                vInicial.setVisible(true);
            }
        });

        btnEliminar = new JButton("ELIMINAR");
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	modeloHorarios.setRowCount(0);
                int selectedRow = tabla.getSelectedRow();
                if (selectedRow != -1) {
                    Pelicula ps = pelis.get(selectedRow);
                    bd.eliminarPelicula(ps.getTitulo());
                    pelis.remove(ps);
                    modelo.fireTableDataChanged();
                    JOptionPane.showMessageDialog(null, "¡Película eliminada exitosamente!");
                } else {
                    JOptionPane.showMessageDialog(null, "Selecciona una película para eliminar.");
                }
            }
        });

        pCentral = new JPanel(new GridLayout(3, 1));

        modelo = new ModeloPeliculas(pelis, bd);
        tabla = new JTable(modelo);
        scroll = new JScrollPane(tabla);
        scroll.setBorder(new TitledBorder("Peliculas"));

        Vector<String> titulos = new Vector<String>(Arrays.asList("ID", "HORARIO"));
        modeloHorarios = new DefaultTableModel(new Vector<Vector<Object>>(), titulos);
        tablaHorarios = new JTable(modeloHorarios) {
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scrollHorarios = new JScrollPane(tablaHorarios);
        scrollHorarios.setBorder(new TitledBorder("Horarios"));

        tabla.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouse = tabla.rowAtPoint(e.getPoint());
                tabla.repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }
        });

        tabla.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	 mouse = -1;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
               
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });

        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown()) {
                    try {
                        JComboBox<Integer> salaComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4});
                        int salaOpcion = JOptionPane.showConfirmDialog(null, salaComboBox, "Selecciona el ID de la sala", JOptionPane.OK_CANCEL_OPTION);
                        if (salaOpcion != JOptionPane.OK_OPTION) {
                            JOptionPane.showMessageDialog(null, "Creación cancelada.");
                            return;
                        }
                        int idSala = (Integer) salaComboBox.getSelectedItem();

                        String titulo = JOptionPane.showInputDialog("Introduce el título de la película:");
                        if (bd.TituloExiste(titulo)) {
                            JOptionPane.showMessageDialog(null, "Creación cancelada. Ese titulo ya existe.");
                            return;
                        }
                        JComboBox<Genero> generoComboBox = new JComboBox<>(Genero.values());
                        generoComboBox.setSelectedIndex(0);
                        int generoOpcion = JOptionPane.showConfirmDialog(null, generoComboBox, "Selecciona el género", JOptionPane.OK_CANCEL_OPTION);
                        if (generoOpcion != JOptionPane.OK_OPTION) {
                            JOptionPane.showMessageDialog(null, "Creación cancelada.");
                            return;
                        }
                        Genero tipo = (Genero) generoComboBox.getSelectedItem();
                        int duracion = Integer.parseInt(JOptionPane.showInputDialog("Introduce la duración de la película en minutos:"));
                        float d = duracion / 60f;
                        String director = JOptionPane.showInputDialog("Introduce el nombre del director:");
                        String rutaFoto = JOptionPane.showInputDialog("Introduce el nombre del archivo de la foto (se guardará en resource/images/):");
                        if (rutaFoto == null || rutaFoto.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "La ruta de la foto no puede estar vacía.");
                            return;
                        }
                        rutaFoto = "resource/images/" + rutaFoto;
                        int tresDOption = JOptionPane.showConfirmDialog(null, "¿Es una película en 3D?", "Película 3D", JOptionPane.YES_NO_OPTION);
                        boolean tresD = (tresDOption == JOptionPane.YES_OPTION);
                        Set<String> horariosSet = new HashSet<>();
                        String horarios = "";

                        while (true) {
                            String inputHorarios = JOptionPane.showInputDialog("Introduce el ID de los horarios de la película:");
                            if (inputHorarios != null && !inputHorarios.trim().isEmpty()) {
                                if (horariosSet.contains(inputHorarios)) {
                                    JOptionPane.showMessageDialog(null, "El ID de horario ya ha sido ingresado. Por favor, introduce otro.");
                                    continue;
                                }
                                if (bd.BuscarHorarioPorId(Integer.parseInt(inputHorarios)) != null) {
                                    horariosSet.add(inputHorarios);
                                    if (!horarios.isEmpty()) {
                                        horarios += ",";
                                    }
                                    horarios += inputHorarios;
                                } else {
                                    JOptionPane.showMessageDialog(null, "Este id de horario no existe.");
                                    continue;
                                }

                                int o = JOptionPane.showConfirmDialog(null, "¿Deseas agregar otro horario?", "Agregar horario", JOptionPane.YES_NO_OPTION);
                                if (o == JOptionPane.NO_OPTION) {
                                    break;
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "El ID de los horarios no puede estar vacío.");
                            }
                        }
                        Pelicula nuevaPelicula = new Pelicula(idSala, titulo, tipo, d, director, rutaFoto, tresD, horarios);
                        pelis.add(nuevaPelicula);
                        modelo.fireTableDataChanged();
                        JOptionPane.showMessageDialog(null, "¡Película creada exitosamente!");
                        bd.meterPelicula(nuevaPelicula);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error al crear la película: " + ex.getMessage());
                    }
                }
            }
        };

        tabla.addKeyListener(keyListener);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (tabla.getSelectedRow() != -1) {
                    Pelicula p = pelis.get(tabla.getSelectedRow());
                    cargarHorarios(p, bd);
                }
            }
        });

        pCentral.add(scroll);
        pCentral.add(scrollHorarios);

        getContentPane().add(pCentral, BorderLayout.CENTER);
        getContentPane().add(psur, BorderLayout.SOUTH);
        
        psur.add(btnVolver);
        psur.add(btnEliminar);

        ImageIcon imagen = new ImageIcon("resource/images/icono.png");
        setIconImage(imagen.getImage());
        setTitle("DeustoCine");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    public void cargarHorarios(Pelicula p, BBDD bd) {
        modeloHorarios.setRowCount(0);
        String[] h = p.getHorarios().split(",");
        for (int i = 0; i < h.length; i++) {
            if (bd.BuscarHorarioPorId(Integer.parseInt(h[i])) == null) {
                modeloHorarios.addRow(new Object[]{h[i], "No existe"});
            } else {
                modeloHorarios.addRow(new Object[]{h[i], bd.BuscarHorarioPorId(Integer.parseInt(h[i]))});
            }
        }
    }
}