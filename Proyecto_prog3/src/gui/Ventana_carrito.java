package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import db.BBDD;
import domain.Cartelera;
import domain.Cliente;
import domain.Entrada;

public class Ventana_carrito extends JFrame{
	private static final long serialVersionUID = 1L;

	private JPanel pcentro,psur,pnorte,pfiltro,pizquierda, tablas, pizqarriba, pderecha;
	private JTextField filtrar;
	private JComboBox<String> combo;
	private JLabel titulo,filtro, salario;
	private JButton btncartelera,btnañadir,btncomprar, btnañadirsaldo, btncombi;
	private JTable tablacarrito, tablaentradas;
	private ModeloCarrito modelocarrito;
	private ModeloEntradas modeloentrada;
	private JScrollPane scrollTabla, scrollentradas;
	private JFrame vActual, vInicial;
	private int mouseEntradas = -1;
	
	public Ventana_carrito(Cliente c, JFrame vI, Cartelera cartelera, BBDD bd){
		vInicial = vI;
		vActual = this;
		pfiltro = new JPanel(new GridLayout(1,2));
		pcentro = new JPanel(new BorderLayout());
		pnorte = new JPanel(new BorderLayout());
		psur = new JPanel();
		pizquierda = new JPanel();
		tablas = new JPanel(new GridLayout(2,1));
		pizqarriba = new JPanel();
		pderecha = new JPanel();
		
		filtrar = new JTextField(20);
		
		filtro = new JLabel("Filtrar por: ");
        //IAG ChatGPT
        //Como formatear el salario
		salario = new JLabel("Saldo disponible: " + String.format("%.2f", c.getSalario())+ "€   ");
		salario.setOpaque(true);
		salario.setBackground(Color.LIGHT_GRAY);
		Font fuente2 = new Font(getName(),Font.PLAIN , 15);
		salario.setFont(fuente2);
		
		titulo = new JLabel("Carrito de la compra", JLabel.CENTER);
		titulo.setOpaque(true);
		titulo.setBackground(Color.LIGHT_GRAY);
		Font fuente = new Font(getName(),Font.BOLD , 30);
		titulo.setFont(fuente);
		
		String [] combotitles = {"NOMBRE DE PELICULA","SALA","FECHA","PRECIO"};
		combo = new JComboBox<String>(combotitles);
		combo.setSelectedItem(combotitles[0]);	
		
		btncomprar = new JButton("Comprar");
		btncomprar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				HashMap<Entrada, Integer> carrito = filtrarEntradasUnicas(c.getCarrito_de_compra());
				float precioTotal = 0;
				for(Entrada en: carrito.keySet()) {
					float precio = 0;
					for(Entrada entrada: bd.calcularPrecioTotal(en)) {
						precio += entrada.CalcularPrecio(entrada.getEdad());
					}
					precioTotal += precio; 
				}
				if (precioTotal > c.getSalario()) {
	                JOptionPane.showMessageDialog(null, "No tienes saldo suficiente", "SALDO INSUFICIENTE...", JOptionPane.WARNING_MESSAGE);
				} else {
					c.setSalario(c.getSalario()-precioTotal);
					bd.cambiarSaldo(c.getSalario(), c.getDni());
					bd.comprarCarrito3(c.getDni());
					c.setCarrito_de_compra(new HashMap<Entrada, Integer>());
					modelocarrito = new ModeloCarrito(c.getCarrito_de_compra(),bd);		
					tablacarrito = new JTable(modelocarrito);
					dispose();
					vInicial.setVisible(true);
				}

			}
		});
		btnañadirsaldo = new JButton("");
		btnañadirsaldo.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String result = JOptionPane.showInputDialog(null, "Introduce la cantidad que vas a depositar:", "AÑADIR SALDO", JOptionPane.INFORMATION_MESSAGE);
		        //IAG ChatGPT
		        //Para ver si ha cerrado el JOptionPane
		        if (result == null || result.trim().isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Has cerrado la pestaña o no has puesto ningun valor.", "Error", JOptionPane.ERROR_MESSAGE);
		            return;
		        }
		        try {
		            int cantidad = Integer.parseInt(result.trim());
		            vActual.setVisible(false);
		            c.setSalario(c.getSalario() + cantidad);
		            bd.cambiarSaldo(c.getSalario(), c.getDni());
		            new Tarjeta(vInicial, cantidad);
		        } catch (NumberFormatException ex) {
		            JOptionPane.showMessageDialog(null, "Por favor, introduce un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});

		ImageIcon img3 = new ImageIcon("resource/images/wallet.png");
    	Image imagenOriginalcolumn3 = img3.getImage();
		Image imagenRedimensionadacolumn3 = imagenOriginalcolumn3.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		ImageIcon imgredimensionadacolumn3 = new ImageIcon(imagenRedimensionadacolumn3);
		btnañadirsaldo.setIcon(imgredimensionadacolumn3);
		
		btnañadir = new JButton("");
		btnañadir.addActionListener((e)-> {
			new Aniadir_carrito(vI,cartelera,null,bd, c);
			vActual.setVisible(false);
		});
		ImageIcon img = new ImageIcon("resource/images/agregar.png");
    	Image imagenOriginalcolumn = img.getImage();
		Image imagenRedimensionadacolumn = imagenOriginalcolumn.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		ImageIcon imgredimensionadacolumn = new ImageIcon(imagenRedimensionadacolumn);
		btnañadir.setIcon(imgredimensionadacolumn);
		
		btncartelera = new JButton("");
		btncartelera.addActionListener((e)-> {
			vInicial.setVisible(true);
			vActual.setVisible(false);
		});
		ImageIcon img2 = new ImageIcon("resource/images/volverAtras.png");
    	Image imagenOriginalcolumn2 = img2.getImage();
		Image imagenRedimensionadacolumn2 = imagenOriginalcolumn2.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		ImageIcon imgredimensionadacolumn2 = new ImageIcon(imagenRedimensionadacolumn2);
		btncartelera.setIcon(imgredimensionadacolumn2);
		
		btncombi = new JButton("Combinaciones");
		btncombi.addActionListener((e)-> {
			List<Integer> edades = Arrays.asList(3, 10, 18, 65);
			String s = "";
			for(List<String> i: combinaciones(edades, c.getSalario())) {
				s += i + "\n";
			}
			
			JOptionPane.showConfirmDialog(null,"Estas son las combinaciones de edades que se pueden hacer segun tu saldo:\n"+ s,"Combinaciones de Edades",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
		});
		
		HashMap<Entrada, Integer> carrito = filtrarEntradasUnicas(c.getCarrito_de_compra());

		modelocarrito = new ModeloCarrito(carrito,bd);		
		tablacarrito = new JTable(modelocarrito);
		
		tablacarrito.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JLabel c = new JLabel(value.toString());
				c.setOpaque(true);
				
				if (row%2==0) {
					c.setBackground(Color.cyan);
				} else {
					c.setBackground(Color.white);	
				}
				if (isSelected) {
					c.setBackground(tablacarrito.getSelectionBackground());
					c.setForeground(tablacarrito.getSelectionForeground());
				}
				
		        //IAG ChatGPT
		        //Hacer que el filtro se coloree
				String filtroTexto = filtrar.getText().toLowerCase();
		        String columnaSeleccionada = combo.getSelectedItem().toString();

		        boolean resaltar = false;
		        if (filtroTexto != null && !filtroTexto.isEmpty()) {
		            switch (columnaSeleccionada) {
		                case "NOMBRE DE PELICULA":
		                    resaltar = column == 0 && value.toString().toLowerCase().contains(filtroTexto);
		                    break;
		                case "SALA":
		                    resaltar = column == 1 && value.toString().toLowerCase().contains(filtroTexto);
		                    break;
		                case "FECHA":
		                    resaltar = column == 2 && value.toString().toLowerCase().contains(filtroTexto);
		                    break;
		                case "PRECIO":
		                    resaltar = column == 3 && value.toString().toLowerCase().contains(filtroTexto);
		                    break;
		            }
		        }

		        if (resaltar) {
		            String texto = value.toString();
		            int inicio = texto.toLowerCase().indexOf(filtroTexto);
		            int fin = inicio + filtroTexto.length();
		            StringBuilder resaltadoHtml = new StringBuilder("<html>");
		            resaltadoHtml.append(texto, 0, inicio)
		                         .append("<span style='background-color:yellow;color:red;'>")
		                         .append(texto.substring(inicio, fin))
		                         .append("</span>")
		                         .append(texto.substring(fin))
		                         .append("</html>");
		            c.setText(resaltadoHtml.toString());
		        } else if (!isSelected) {
		            c.setText(value.toString());
		        }
				return c;
			}
		});;
		scrollTabla = new JScrollPane(tablacarrito);
		
		scrollTabla.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollTabla.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollTabla.setBorder(new TitledBorder("Entradas"));
		
		tablacarrito.setFillsViewportHeight(true);
		tablacarrito.getTableHeader().setReorderingAllowed(false);
		
		getContentPane().add(pcentro,BorderLayout.CENTER);
		getContentPane().add(pnorte,BorderLayout.NORTH);
		getContentPane().add(psur,BorderLayout.SOUTH);
		
		pnorte.add(titulo, BorderLayout.CENTER);
		pnorte.add(pizqarriba, BorderLayout.WEST);
		pnorte.add(pderecha,BorderLayout.EAST);
		
		pizquierda.add(filtro);
		pizquierda.add(filtrar);
		pizquierda.add(combo);
		
		pderecha.add(salario);
		pderecha.add(btnañadirsaldo);

		filtrar.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				if (combo.getSelectedItem() != null) {
					filtrar(c,bd);
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (combo.getSelectedItem() != null) {
					filtrar(c,bd);
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (combo.getSelectedItem() != null) {
					filtrar(c,bd);
				}
			}
		});
		
		tablaentradas = new JTable();
		tablaentradas.addMouseListener(new MouseAdapter() {
		
			@Override
			public void mouseExited(MouseEvent e) {
				mouseEntradas = -1;				
				tablaentradas.repaint();
			}
		});
		tablaentradas.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseEntradas = tablaentradas.rowAtPoint(e.getPoint());
				tablaentradas.repaint();	
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				
			}
		});
		tablaentradas.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JLabel r = new JLabel(value.toString(), CENTER);
				r.setOpaque(true);
				if (row == mouseEntradas) {
					r.setBackground(tablaentradas.getSelectionBackground());
					r.setForeground(tablaentradas.getSelectionForeground());
				} else {
					if (column == 4) {
						if (Float.parseFloat(value.toString()) == 0) {
							r.setBackground(Color.green);
							r.setForeground(Color.white);
						}else if (Float.parseFloat(value.toString()) ==  12) {
							r.setBackground(Color.blue);
							r.setForeground(Color.white);
						} else {
							r.setBackground(Color.gray);
							r.setForeground(Color.white);
						}
					}else {
						r.setBackground(Color.white);
						r.setForeground(Color.black);
					}
				}
				
				return r;
			}
		});
		
		tablacarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablacarrito.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		    @Override
		    public void valueChanged(ListSelectionEvent e) {
		        //IAG ChatGPT
		        //Que cargue la tabla entradas cuando se selecciona una fila de la tabla carrito
		        if (!e.getValueIsAdjusting()) {
		            int row = tablacarrito.getSelectedRow();
		            if (row != -1) {
		                String titulo = (String) tablacarrito.getValueAt(row, 0);
		                String horario = (String) tablacarrito.getValueAt(row, 2);
		                int sala = (int) tablacarrito.getValueAt(row, 1);
		                
		                modeloentrada = new ModeloEntradas(filtrarEntradaParaTablaEntradas(titulo, horario, sala, c.getCarrito_de_compra()));
		                tablaentradas.setModel(modeloentrada);
		            } else {
		                tablaentradas.setModel(new ModeloEntradas(new HashMap<>()));
		            }
		        }
		    }
		});

		scrollentradas = new JScrollPane(tablaentradas);
		
		pfiltro.add(pizquierda);
		
		pizqarriba.add(btncartelera);
		pizqarriba.add(btnañadir);
		
		psur.add(btncomprar);
		psur.add(btncombi);
		
		tablas.add(scrollTabla);
		tablas.add(scrollentradas);
		
		pcentro.add(pfiltro,BorderLayout.NORTH);
		pcentro.add(tablas,BorderLayout.CENTER);
		
		ImageIcon imagen = new ImageIcon("resource/images/icono.png");
		setIconImage(imagen.getImage());
		setTitle("DeustoCine");
		setSize(1000,600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}
	
	public void filtrar(Cliente c, BBDD bd) {
	    HashMap<Entrada, Integer> carrito = filtrarEntradasUnicas(c.getCarrito_de_compra());
	    tablaentradas.setModel(new ModeloEntradas(new HashMap<>()));

	    if (carrito != null) {
	        if (combo.getSelectedItem().toString() != null) {
	            HashMap<Entrada, Integer> carritoFiltrado = new HashMap<>();
	            switch (combo.getSelectedItem().toString()) {
	                case "NOMBRE DE PELICULA":
	                    for (Entrada entrada : carrito.keySet()) {
	                        if (entrada.getTitulo_peli().contains(filtrar.getText())) {
	                            carritoFiltrado.put(entrada, carrito.get(entrada));
	                        }
	                    }
	                    break;
	                case "SALA":
	                    for (Entrada entrada : carrito.keySet()) {
	                        if (Integer.toString(entrada.getSala()).contains(filtrar.getText())) {
	                            carritoFiltrado.put(entrada, carrito.get(entrada));
	                        }
	                    }
	                    break;
	                case "PRECIO":
	                    for (Entrada entrada : carrito.keySet()) {
	                        if (Float.toString(entrada.getPrecio()).contains(filtrar.getText())) {
	                            carritoFiltrado.put(entrada, carrito.get(entrada));
	                        }
	                    }
	                    break;
	                default:
	                    for (Entrada entrada : carrito.keySet()) {
	                        if (entrada.getHorario().contains(filtrar.getText())) {
	                            carritoFiltrado.put(entrada, carrito.get(entrada));
	                        }
	                    }
	            }

	            modelocarrito = new ModeloCarrito(carritoFiltrado, bd);
	            tablacarrito.setModel(modelocarrito);
	            tablacarrito.clearSelection();
	        }
	    }
	}

    //IAG ChatGPT
    //Filtrar las entradas que no tengan misma sala, misma peli y mismo horario
	public HashMap<Entrada, Integer> filtrarEntradasUnicas(HashMap<Entrada, Integer> carrito) {
	    HashMap<String, Entrada> uniqueEntriesMap = new HashMap<>();
	    HashMap<Entrada, Integer> filteredCarrito = new HashMap<>();

	    for (Entrada e : carrito.keySet()) {
	        Integer cantidad = carrito.get(e);

	        String claveUnica = e.getSala() + "_" + e.getHorario() + "_" + e.getHorario();

	        if (!uniqueEntriesMap.containsKey(claveUnica)) {
	            uniqueEntriesMap.put(claveUnica, e);
	            filteredCarrito.put(e, cantidad);
	        }
	    }

	    return filteredCarrito;
	}
	
	public HashMap<Entrada, Integer> filtrarEntradaParaTablaEntradas(String titulo, String horario, int sala, HashMap<Entrada, Integer> carrito){
	    HashMap<Entrada, Integer> carritofiltrado = new HashMap<>();
		
	    for(Entrada e: carrito.keySet()) {
	    	if (e.getHorario().equals(horario) && e.getTitulo_peli().equals(titulo) && e.getSala() == sala) {
				carritofiltrado.put(e, carrito.get(e));
			}
	    }
	    
		return carritofiltrado;
	}
	
	 public static List<List<String>> combinaciones(List<Integer> edades, float saldo) {
	        Set<List<String>> resultado = new HashSet<>();
	        combinacionesR(edades, new ArrayList<>(), resultado, saldo, 0, 0);

	        List<List<String>> listaResultado = new ArrayList<>(resultado);
	        listaResultado.removeIf(List::isEmpty); // Eliminamos combinaciones vacías.

	        return listaResultado;
	    }

	    public static void combinacionesR(List<Integer> edades, List<Integer> aux, Set<List<String>> result, float saldo, float suma, int start) {
	        if (suma > saldo) {
	            return;
	        }

	        if (suma <= saldo) {
	            List<String> copia = new ArrayList<>();
	            for (int edad : aux) {
	                copia.add(edades(edad));
	            }
	            Collections.sort(copia);
	            result.add(copia);
	        }

	        for (int i = start; i < edades.size(); i++) {
	            float precio = calcularPrecio(edades.get(i));
	            if (precio < 0) continue;

	            aux.add(edades.get(i));
	            suma += precio;

	            combinacionesR(edades, aux, result, saldo, suma, i + 1);

	            suma -= precio;
	            aux.remove(aux.size() - 1);
	        }
	    }

	    public static float calcularPrecio(int edad) {
	        if (edad < 3) {
	            return 0;
	        } else if (edad < 10) {
	            return 0;
	        } else if (edad < 18) {
	            return 8;
	        } else if (edad < 65) {
	            return 12;
	        } else {
	            return 8;
	        }
	    }

	    public static String edades(int edad) {
	        if (edad < 3) {
	            return "0-2";
	        } else if (edad < 10) {
	            return "3-9"; 
	        } else if (edad < 18) {
	            return "10-17";
	        } else if (edad < 65) {
	            return "18-64";
	        } else {
	            return "65-...";
	        }
	    }
}
