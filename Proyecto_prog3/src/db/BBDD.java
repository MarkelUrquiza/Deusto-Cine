package db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import domain.Butaca;
import domain.Cliente;
import domain.Columna;
import domain.Entrada;
import domain.Genero;
import domain.Pelicula;

public class BBDD {	
	private final String PROPERTIES_FILE = "resource/conf/fichero.properties";
	private final String LOG_FOLDER = "resource/log";
	
	private Properties properties;
	private static String driverName;
	private static String databaseFile;
	private static String connectionString;
	
	private static Logger logger = Logger.getLogger(BBDD.class.getName());

	public BBDD() {
		try (FileInputStream fis = new FileInputStream("resource/conf/logger.properties")) {
			LogManager.getLogManager().readConfiguration(fis);
			
			properties = new Properties();
			properties.load(new FileReader(PROPERTIES_FILE));
			
			driverName = properties.getProperty("driver");
			databaseFile = properties.getProperty("file");
			connectionString = properties.getProperty("connection");
			
			File dir = new File(LOG_FOLDER);
			
			if (!dir.exists()) {
				dir.mkdirs();
			}
			dir = new File(databaseFile.substring(0, databaseFile.lastIndexOf("/")));
			
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			Class.forName(driverName);
		} catch (Exception ex) {
			logger.warning(String.format("Error al cargar el driver de BBDD: %s", ex.getMessage()));
		}
	}

    public void crearBBDD() {
        if (properties.get("createBBDD").equals("true")) {
            String sql1 = "CREATE TABLE IF NOT EXISTS Cliente (\n"
                    + " dni TEXT PRIMARY KEY,\n"
                    + " username TEXT NOT NULL,\n"
                    + " contrasenia TEXT NOT NULL,\n"
                    + " nombre TEXT NOT NULL,\n"
                    + " apellidos TEXT NOT NULL,\n"
                    + " correo TEXT NOT NULL,\n"
                    + " salario TEXT NOT NULL\n);";

            String sql2 = "CREATE TABLE IF NOT EXISTS Pelicula (\n"
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + " id_sala INTEGER NOT NULL,\n"
                    + " titulo TEXT UNIQUE NOT NULL,\n"
                    + " director TEXT NOT NULL,\n"
                    + " tipo TEXT NOT NULL,\n"
                    + " duracion FLOAT NOT NULL,\n"
                    + " rutafoto TEXT NOT NULL,\n"
                    + " horarios TEXT NOT NULL,\n"
                    + " tresd INTEGER NOT NULL\n);";

            String sql3 = "CREATE TABLE IF NOT EXISTS Entrada (\n"
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + " id_butaca INTEGER NOT NULL,\n"
                    + " id_carrito INTEGER NOT NULL,\n"
                    + " id_peli INTEGER NOT NULL,\n"
                    + " id_horario INTEGER NOT NULL,\n"
                    + " edad INTEGER NOT NULL,\n"
                    + " nombre TEXT NOT NULL,\n"
                    + " apellido TEXT NOT NULL,\n"
                    + " apellido2 TEXT NOT NULL,\n"
                    + " FOREIGN KEY(id_horario) REFERENCES Horarios(id) ON DELETE CASCADE,\n"
                    + " FOREIGN KEY(id_butaca) REFERENCES Butaca(id) ON DELETE CASCADE,\n"
                    + " FOREIGN KEY(id_carrito) REFERENCES Carrito(id) ON DELETE CASCADE,\n"
                    + " FOREIGN KEY(id_peli) REFERENCES Pelicula(id) ON DELETE CASCADE\n);";

            String sql4 = "CREATE TABLE IF NOT EXISTS Carrito (\n"
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + " cliente_dni TEXT,\n"
                    + " FOREIGN KEY(cliente_dni) REFERENCES Cliente(dni) ON DELETE CASCADE\n);";
            
            String sql5 = "CREATE TABLE IF NOT EXISTS Butaca (\n"
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + " fila INTEGER NOT NULL,\n"
                    + " columna INTEGER NOT NULL,\n"
                    + " vip INTEGER NOT NULL,\n"
                    + " id_sala INTEGER NOT NULL\n);";
            
            String sql6 = "CREATE TABLE IF NOT EXISTS Butaca_Horario (\n"
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + " id_butaca INTEGER NOT NULL,\n"
                    + " id_horario INTEGER NOT NULL,\n"
                    + " FOREIGN KEY(id_horario) REFERENCES Horarios(id) ON DELETE CASCADE,\n"
                    + " FOREIGN KEY(id_butaca) REFERENCES Butaca(id) ON DELETE CASCADE);";
            
            String sql7 = "CREATE TABLE IF NOT EXISTS InicioSesion (\n"
            		+ " horario_inicio_sesion TEXT NOT NULL,\n"
            	    + " cliente_dni TEXT PRIMARY KEY,\n"
            	    + " FOREIGN KEY(cliente_dni) REFERENCES Cliente(dni) ON DELETE CASCADE);";
            
            String sql8 = "CREATE TABLE IF NOT EXISTS Horarios (\n"
		            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
		            + " horario STRING UNIQUE NOT NULL);";
            
            String sql9 = "CREATE TABLE IF NOT EXISTS EntradasCompradas (\n"
            		+ " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            		+ " id_butaca INTEGER NOT NULL,\n"
                    + " cliente_dni TEXT NOT NULL,\n"
            		+ " id_peli INTEGER NOT NULL,\n"
            		+ " id_horario INTEGER NOT NULL,\n"
                    + " edad INTEGER NOT NULL,\n"
                    + " nombre TEXT NOT NULL,\n"
                    + " apellido TEXT NOT NULL,\n"
            		+ " FOREIGN KEY(id_horario) REFERENCES Horarios(id) ON DELETE CASCADE,\n"
            		+ " FOREIGN KEY(id_butaca) REFERENCES Butaca(id) ON DELETE CASCADE,\n"
            		+ " FOREIGN KEY(cliente_dni) REFERENCES Cliente(dni) ON DELETE CASCADE,\n"
                    + " FOREIGN KEY(id_peli) REFERENCES Pelicula(id) ON DELETE CASCADE\n);";

            
            try (Connection con = DriverManager.getConnection(connectionString);
                 PreparedStatement pStmt1 = con.prepareStatement(sql1);
                 PreparedStatement pStmt2 = con.prepareStatement(sql2);
                 PreparedStatement pStmt3 = con.prepareStatement(sql3);
                 PreparedStatement pStmt4 = con.prepareStatement(sql4);
            	 PreparedStatement pStmt5 = con.prepareStatement(sql5);
               	 PreparedStatement pStmt6 = con.prepareStatement(sql6);
                 PreparedStatement pStmt7 = con.prepareStatement(sql7);
                 PreparedStatement pStmt8 = con.prepareStatement(sql8);
            	 PreparedStatement pStmt9 = con.prepareStatement(sql9)) {
            	
            	
                if (!pStmt1.execute()) logger.info("Tabla Cliente creada o ya existía.");
                if (!pStmt2.execute()) logger.info("Tabla Pelicula creada o ya existía.");
                if (!pStmt3.execute()) logger.info("Tabla Entrada creada o ya existía.");
                if (!pStmt4.execute()) logger.info("Tabla Carrito creada o ya existía.");
                if (!pStmt5.execute()) logger.info("Tabla Butaca creada o ya existía.");
                if (!pStmt6.execute()) logger.info("Tabla Butaca_Horario creada o ya existía.");
                if (!pStmt7.execute()) logger.info("Tabla InicioSesion creada o ya existía.");
                if (!pStmt8.execute()) logger.info("Tabla Horarios creada o ya existía.");
                if (!pStmt9.execute()) logger.info("Tabla EntradasCompradas creada o ya existía.");
                
            } catch (Exception e) {
                logger.warning(String.format("Error al crear las tablas: %s", e.getMessage()));
            }
        }
    }
    //IAG ChatGPT
    //Insertar los datos por defecto a la base de datos
    public void insertarDatosPorDefecto() {
        if (properties.get("createBBDD").equals("true")) {
            if (hayTablasConDatos()) {
                logger.info("Al menos una tabla ya tiene datos. No se insertarán datos por defecto.");
                return;
            }

            String insertCliente = "INSERT OR IGNORE INTO Cliente (dni, username, contrasenia, nombre, apellidos, correo, salario) VALUES\n"
                    + "('11111111A', 'user1', 'pass1', 'John', 'Doe', 'john.doe@example.com', 56),\n"
                    + "('22222222B', 'user2', 'pass2', 'Jane', 'Doe', 'jane.doe@example.com', 78),\n"
                    + "('33333333C', 'user3', 'pass3', 'Alice', 'Smith', 'alice.smith@example.com', 45),\n"
                    + "('44444444D', 'user4', 'pass4', 'Bob', 'Johnson', 'bob.johnson@example.com', 62),\n"
                    + "('55555555E', 'user5', 'pass5', 'Charlie', 'Brown', 'charlie.brown@example.com', 89),\n"
                    + "('66666666F', 'user6', 'pass6', 'Eve', 'Davis', 'eve.davis@example.com', 34),\n"
                    + "('77777777G', 'user7', 'pass7', 'Frank', 'Miller', 'frank.miller@example.com', 100),\n"
                    + "('88888888H', 'user8', 'pass8', 'Grace', 'Lee', 'grace.lee@example.com', 73),\n"
                    + "('99999999I', 'user9', 'pass9', 'Hank', 'Wilson', 'hank.wilson@example.com', 54),\n"
                    + "('00000000J', 'user10', 'pass10', 'Ivy', 'Moore', 'ivy.moore@example.com', 21);";

            String insertPelicula = "INSERT OR IGNORE INTO Pelicula (id_sala, titulo, director, tipo, duracion, rutafoto, horarios, tresd) VALUES\n"
                    + "(1, 'Inception', 'Christopher Nolan', 'ACCION', 2.5, 'resource/images/Inception.png', '1,2', 1),\n"
                    + "(2, 'The Godfather', 'Francis Ford Coppola', 'DRAMA', 2.9, 'resource/images/TheGodfather.png', '3,4', 0),\n"
                    + "(3, 'Dune', 'Denis Villeneuve', 'CIENCIA_FICCION', 2.8, 'resource/images/Dune.png', '5,6', 1),\n"
                    + "(4, 'Interstellar', 'Christopher Nolan', 'CIENCIA_FICCION', 3.0, 'resource/images/Interstellar.png', '7,8', 1),\n"
                    + "(1, 'Titanic', 'James Cameron', 'ROMANCE', 3.2, 'resource/images/Titanic.png', '9,10', 0),\n"
                    + "(2, 'Pulp Fiction', 'Quentin Tarantino', 'CRIMEN', 2.8, 'resource/images/PulpFiction.png', '11,12', 0),\n"
                    + "(3, 'Avengers: Endgame', 'Anthony Russo', 'ACCION', 3.1, 'resource/images/AvengersEndgame.png', '13,14', 1),\n"
                    + "(4, 'The Matrix', 'Lana Wachowski', 'CIENCIA_FICCION', 2.6, 'resource/images/TheMatrix.png', '15,16', 1);";

            String insertHorarios = "INSERT OR IGNORE INTO Horarios (horario) VALUES\n"
                    + "('2025-01-21 16:00'),\n"
                    + "('2025-01-21 18:30'),\n"
                    + "('2025-01-22 17:00'),\n"
                    + "('2025-01-22 20:30'),\n"
                    + "('2025-01-23 16:00'),\n"
                    + "('2025-01-23 19:30'),\n"
                    + "('2025-01-24 14:30'),\n"
                    + "('2025-01-24 19:00'),\n"
                    + "('2025-01-25 14:30'),\n"
                    + "('2025-01-25 19:00'),\n"
                    + "('2025-01-26 16:00'),\n"
                    + "('2025-01-26 20:00'),\n"
                    + "('2025-01-27 14:00'),\n"
                    + "('2025-01-28 21:00'),\n"
                    + "('2025-01-31 17:00'),\n"
                    + "('2025-01-31 20:00');";

            String insertCarrito = "INSERT OR IGNORE INTO Carrito (cliente_dni) VALUES\n"
                    + "('11111111A'),\n"
                    + "('22222222B'),\n"
                    + "('33333333C'),\n"
                    + "('44444444D');";

            String insertEntrada = "INSERT OR IGNORE INTO Entrada (id_butaca, id_carrito, id_peli, id_horario, edad, nombre, apellido, apellido2) VALUES\n"
                    + "(4, 1, 1, 1, 25, 'Carlos', 'González', 'Fernández'),\n"
                    + "(49, 1, 2, 3, 30, 'Ana', 'Martínez', 'Rodríguez'),\n"
                    + "(98, 2, 3, 5, 22, 'Luis', 'Pérez', 'Gómez'),\n"
                    + "(139, 2, 4, 7, 28, 'María', 'López', 'Martín'),\n"
                    + "(1, 2, 5, 10, 35, 'Javier', 'Sánchez', 'Ruiz'),\n"
                    + "(36, 3, 6, 11, 40, 'Isabel', 'García', 'Ortiz'),\n"
                    + "(92, 3, 7, 14, 27, 'Andrés', 'Hernández', 'Jiménez'),\n"
                    + "(134, 3, 8, 15, 33, 'Lucía', 'Ramírez', 'Alonso'),\n"
                    + "(135, 3, 8, 15, 29, 'Sofía', 'Torres', 'Moreno'),\n"
                    + "(1, 4, 1, 2, 24, 'Diego', 'Morales', 'Navarro');";

            String insertButacaHorario = "INSERT OR IGNORE INTO Butaca_Horario (id_butaca, id_horario) VALUES\n"
                    + "(4, 1),\n"
                    + "(49, 3),\n"
                    + "(98, 5),\n"
                    + "(139, 7),\n"
                    + "(1, 10),\n"
                    + "(36, 11),\n"
                    + "(92, 14),\n"
                    + "(134, 15),\n"
                    + "(135, 15),\n"
                    + "(1, 2);";

            try (Connection con = DriverManager.getConnection(connectionString);
                 PreparedStatement insertStmt1 = con.prepareStatement(insertCliente);
                 PreparedStatement insertStmt2 = con.prepareStatement(insertPelicula);
                 PreparedStatement insertStmt3 = con.prepareStatement(insertCarrito);
                 PreparedStatement insertStmt4 = con.prepareStatement(insertEntrada);
                 PreparedStatement insertStmt5 = con.prepareStatement(insertHorarios);
                 PreparedStatement insertStmt6 = con.prepareStatement(insertButacaHorario)) {

                insertStmt1.execute();
                insertStmt2.execute();
                insertStmt3.execute();
                insertStmt4.execute();
                insertStmt5.execute();
                insertStmt6.execute();

                logger.info("Se han insertado los datos correctamente.");
            } catch (Exception e) {
                logger.warning(String.format("Error al insertar los datos: %s", e.getMessage()));
            }
        }
    }
    //IAG ChatGPT
    //Controlar si alguna esta rellena, para no meter datos innecesarios
    private boolean hayTablasConDatos() {
        String[] tablas = {"Cliente", "Pelicula", "Horarios", "Carrito", "Entrada", "Butaca_Horario"};
        try (Connection con = DriverManager.getConnection(connectionString)) {
            for (String tabla : tablas) {
                String query = String.format("SELECT COUNT(*) AS total FROM %s", tabla);
                try (PreparedStatement stmt = con.prepareStatement(query);
                     ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getInt("total") > 0) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            logger.warning(String.format("Error al verificar datos en las tablas: %s", e.getMessage()));
        }
        return false;
    }


	public boolean existeUsuario(String username, String dni, String correo) {
		boolean existe = false;
		String sql = "SELECT * FROM Cliente WHERE username = ? or dni = ? or correo = ?";
	    try (Connection con = DriverManager.getConnection(connectionString);
				PreparedStatement ps = con.prepareStatement(sql)){

			ps.setString(1, username);
			ps.setString(2, dni);
			ps.setString(3, correo);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				existe = true;
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
		    logger.warning(String.format("Error al buscar usuario: %s", e.getMessage()));
		}
		return existe;
	}
	public void registrar(Cliente c) {
		String sql = "INSERT INTO Cliente (dni, username, contrasenia, nombre, apellidos, correo, salario) VALUES (?, ?, ?, ?, ?, ?, ?);";
		
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement Stmt = con.prepareStatement(sql)) {
	
				Stmt.setString(1, c.getDni());
				Stmt.setString(2, c.getUser());
				Stmt.setString(3, c.getContrasenia());
				Stmt.setString(4, c.getNombre());
				Stmt.setString(5, c.getApellidos());
				Stmt.setString(6, c.getCorreo());
				Stmt.setFloat(7, c.getSalario());
				
				
				if (!Stmt.execute()) {
					logger.info(String.format("Usuario con DNI: %s insertado en la BBDD", c.getDni()));
			    }
				Stmt.close();
		} catch (Exception ex) {
			logger.warning(String.format("Error al insertar nuevo usuario: %s", ex.getMessage()));
		}			
		
	}
	public boolean existeUsuarioyContrasenia(String username, String contrasenia) {
		boolean existe = false;
		String sql = "SELECT * FROM Cliente WHERE username = ? and contrasenia = ?";
	    try (Connection con = DriverManager.getConnection(connectionString);
				PreparedStatement ps = con.prepareStatement(sql)){

			ps.setString(1, username);
			ps.setString(2, contrasenia);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				existe = true;
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
		    logger.warning(String.format("Error al buscar el usuario y contraseña: %s", e.getMessage()));
		}
		return existe;
	}
	public Cliente obtenerUsuario(String user, String contrasenia) {
		Cliente c = null;
		String sql = "SELECT * FROM Cliente WHERE username = ? and contrasenia = ?";
		
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {			
			
			pStmt.setString(1, user);
			pStmt.setString(2, contrasenia);
			ResultSet rs = pStmt.executeQuery();			

			if (rs.next()) {
				c = new Cliente(rs.getString("username"), rs.getString("contrasenia"),
						rs.getString("nombre"), rs.getString("apellidos"), 
						rs.getString("dni"), rs.getString("correo"), null, rs.getFloat("salario"));
			}
			
			rs.close();
			pStmt.close();
			logger.info(String.format("Se ha encontrado correctamente el usuario con DNI: %s", c.getDni()));			
		} catch (Exception e) {
			logger.warning(String.format("Error al encontrar el usuario con username: %s", user));						
		}		
		
		return c;
	}
	public HashMap<Entrada, Integer> cargarCarrito(String dni) {
	    HashMap<Entrada, Integer> carrito = new HashMap<>();
	    String sql = "SELECT * FROM Carrito WHERE cliente_dni = ?";

	    try (Connection con = DriverManager.getConnection(connectionString);
	         PreparedStatement pStmt = con.prepareStatement(sql)) {
	        pStmt.setString(1, dni);
	        ResultSet rs = pStmt.executeQuery();
	        
	        if (rs.next()) {
	            int idCarrito = rs.getInt("id");
	            List<Entrada> entradas = obtenerEntradaPorIDCarrito(idCarrito);

	            for (Entrada entrada : entradas) {
	                carrito.put(entrada, numeroEntradas(dni, entrada.getTitulo_peli(), entrada.getHorario()));
	            }
	        }
	        rs.close();
	        pStmt.close();
	    } catch (Exception e) {
	        logger.warning(String.format("Error al cargar el carrito del cliente con DNI: %s. Detalles: %s", dni, e.getMessage()));
	    }

	    return carrito;
	}

	public List<Entrada> obtenerEntradaPorIDCarrito(int id_carrito) {
	    String sql = "SELECT * FROM Entrada WHERE id_carrito = ?";
	    Entrada e = null; 
	    List<Entrada> entradas = new ArrayList<>();
	    try (Connection con = DriverManager.getConnection(connectionString);
	         PreparedStatement pStmt = con.prepareStatement(sql)) {
	        
	        pStmt.setInt(1, id_carrito);
	        ResultSet rs = pStmt.executeQuery();
	        
	        while (rs.next()) {
	        	int id = rs.getInt("id");
	            int idButaca = rs.getInt("id_butaca");
	            int idPelicula = rs.getInt("id_peli");
	            String horario = BuscarHorarioPorId(rs.getInt("id_horario"));
	            int edad = rs.getInt("edad");
	            String nombre = rs.getString("nombre");
	            String apellido = rs.getString("apellido");
	            String apellido2 = rs.getString("apellido2");
	            
	            Butaca butaca = obtenerButacaporId(idButaca);
	            Pelicula pelicula = obtenerPeliculaporID(idPelicula);
	            
	            float precio;
        		if (edad < 3) {
        			precio = -1;
        		} else if (edad < 10) {
        			
        			precio = 0;
        		} else if (edad < 18) {
        			precio = 8;
        		} else if (edad < 65) {
        			precio = 12;
        		} else {
        			precio = 8;
        		}
	            
	            e = new Entrada(id,butaca, pelicula.getId(), pelicula.getTitulo(), pelicula.getId_sala(), precio, horario, nombre, apellido, edad, apellido2);
	            entradas.add(e);
	            
	        }
            logger.info(String.format("Se ha encontrado correctamente la entrada"));
	        rs.close();
	        pStmt.close();
	    } catch (Exception exception) {
	        logger.warning(String.format("Error al encontrar la entrada. Detalles: %s", exception.getMessage()));
	    }
	    
	    return entradas;
	}

	
	public Butaca obtenerButacaporId(int id) {
		
		String sql = "SELECT * FROM Butaca WHERE id = ?";
		Butaca b = null;
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {			
			
			pStmt.setInt(1, id);
			ResultSet rs = pStmt.executeQuery();			

			if (rs.next()) {
				boolean vip;
				if (rs.getInt("vip") == 1) {
					vip = true;
				} else vip = false;
				b = new Butaca(rs.getInt("id"),rs.getInt("fila"), Columna.valueOf(rs.getString("columna")) , vip , rs.getInt("id_sala"));
			}
			
			rs.close();
			pStmt.close();
			logger.info(String.format("Se ha encontrado correctamente la Butaca con id: %d", id));			
		} catch (Exception e) {
			logger.warning(String.format("Error al encontrar la Butaca con id: %d", id));						
		}		
		
		return b;
	}

	public Pelicula obtenerPeliculaporID(int id) {
	    Pelicula p = null;
	    String sql = "SELECT * FROM Pelicula WHERE id = ?";
	    
	    try (Connection con = DriverManager.getConnection(connectionString);
	         PreparedStatement pStmt = con.prepareStatement(sql)) {
	        
	        pStmt.setInt(1, id);
	        ResultSet rs = pStmt.executeQuery();

	        if (rs.next()) {
	            boolean tresd = rs.getInt("tresd") == 1;
	            Genero genero = null;
	            try {
	                genero = Genero.valueOf(rs.getString("tipo").toUpperCase());
	            } catch (IllegalArgumentException e) {
	                logger.warning(String.format("Valor desconocido para Genero: %s", rs.getString("tipo")));
	            }

	            p = new Pelicula(
	            	rs.getInt("id"),
	                rs.getInt("id_sala"),
	                rs.getString("titulo"),
	                genero,
	                rs.getFloat("duracion"),
	                rs.getString("director"),
	                rs.getString("rutafoto"),
	                tresd,
	                rs.getString("horarios")
	            );
	        } else {
	            logger.warning(String.format("No se encontró la película con id: %d", id));
	        }

	        rs.close();
	        pStmt.close();
	        logger.info(String.format("Se ha encontrado correctamente la Pelicula con id: %d", id));
	    } catch (Exception e) {
	        e.printStackTrace();  
	        logger.warning(String.format("Error al encontrar la Pelicula con id: %d", id));
	    }

	    return p;
	}


	public int obtenerIDButacaporIddeEntrada(int id) {
		String sql = "SELECT * FROM Entrada WHERE id = ?";
		int id_butaca = 0;
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {			
			pStmt.setInt(1, id);
			ResultSet rs = pStmt.executeQuery();
			
			id_butaca = rs.getInt("id_butaca");
			
			rs.close();
			pStmt.close();
			logger.info(String.format("Se ha encontrado correctamente la Butaca con id: %d", id_butaca));			
		} catch (Exception e) {
			logger.warning(String.format("Error al encontrar la Butaca con id: %d", id_butaca));						
		}		
		
		return id_butaca;
	}
	public void inicilizarButacasContxt() {
		if (properties.get("loadTXT").equals("true")) {
			borrarDatosDeButaca();			
			List<Butaca> butacas = loadTXTButacas();
			
			for (Butaca b: butacas) {
				meterButaca(b);
			}				
		}
	}
	public void borrarDatosDeButaca() {
		if (properties.get("cleanButacas").equals("true")) {	
			String sql1 = "DELETE FROM Butaca;";
			
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt1 = con.prepareStatement(sql1)) {
				
		        if (!pStmt1.execute()) {
		        	logger.info("Se han borrado las Butacas");
		        }
		        pStmt1.close();
			} catch (Exception ex) {
				logger.warning(String.format("Error al borrar las Butacas: %s", ex.getMessage()));
			}
		}
	}
	private List<Butaca> loadTXTButacas() {
	    List<Butaca> butacas = new ArrayList<>();
	    
	    try {
	        File file = new File(properties.getProperty("ficheroButacas"));
	        Scanner sc = new Scanner(file);
	        
	        while (sc.hasNextLine()) {
	            String line = sc.nextLine();
	            String[] campos = line.split(",");
	            
	            int fila = Integer.parseInt(campos[0]);
	            Columna columna = Columna.valueOf(campos[1]);
	            boolean vip = Boolean.parseBoolean(campos[2]);
	            int idSala = Integer.parseInt(campos[4]);
	            
	            Butaca butaca = new Butaca(fila, columna, vip, idSala);
	            butacas.add(butaca);
	        }
	        
	        sc.close();
	    } catch (Exception ex) {
	        logger.warning(String.format("Error leyendo Butacas.txt: %s", ex.getMessage()));
	    }
	    
	    return butacas;
	}
	public void meterButaca(Butaca b) {
		String sql = "INSERT INTO Butaca (id, fila, columna, vip, id_sala) VALUES (?, ?, ?, ?, ?);";
		
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement Stmt = con.prepareStatement(sql)) {
			int vip;
			if (b.isVip()) {
				vip = 1;
			} else vip = 0;
			
				Stmt.setInt(1, b.getId());
				Stmt.setInt(2, b.getFila());
				Stmt.setString(3, b.getColumna().toString());
				Stmt.setInt(4, vip);
				Stmt.setInt(5, b.getId_sala());
				
				
				if (!Stmt.execute()) {
					logger.info(String.format("Butaca con id: %d insertado en la BBDD", b.getId()));
			    }
			Stmt.close();
		} catch (Exception ex) {
			logger.warning(String.format("Error al insertar nueva butaca: %s", ex.getMessage()));
		}			
		
	}
	
	public List<Butaca> getButacas(int id_sala, String horario) {
		List<Butaca> butacas = new ArrayList<>();
		String sql = "SELECT * FROM Butaca WHERE id_sala = ?";
		
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {			
			pStmt.setInt(1, id_sala);
			
			ResultSet rs = pStmt.executeQuery();
			Butaca b;
			
			while (rs.next()) {
				boolean vip;
				if (rs.getInt("vip") == 1) {
					vip = true;
				} else vip = false;
				b = new Butaca(rs.getInt("id"),rs.getInt("fila"), Columna.valueOf(rs.getString("columna")) , vip , rs.getInt("id_sala"));
				butacas.add(b);
			}
			
			rs.close();
			pStmt.close();
			logger.info(String.format("Se ha obtenido la lista de butacas."));			
		} catch (Exception ex) {
			logger.warning(String.format("Error al obtener la lista de butacas: %s", ex.getMessage()));						
		}		
		
		return butacas;
	}
	public boolean existeButacaHorario(int id_butaca, String horario) {
		boolean existe = false;
		String sql = "SELECT * FROM Butaca_Horario WHERE id_butaca = ? and id_horario = ?";
	    try (Connection con = DriverManager.getConnection(connectionString);
				PreparedStatement ps = con.prepareStatement(sql)){

			ps.setInt(1, id_butaca);
			ps.setInt(2, BuscarHorario(horario));
			ResultSet rs = ps.executeQuery();
			if(rs.next()) { 
				existe = true;
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
		    logger.warning(String.format("Error al buscar el usuario y contraseña: %s", e.getMessage()));
		}
		return existe;
	}
	public HashMap<String, Pelicula> getPeliculas(int id_sala) {
		HashMap<String, Pelicula> horarios = new HashMap<>();
		String sql = "SELECT * FROM Pelicula WHERE id_sala = ?";
		
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {			
			pStmt.setInt(1, id_sala);
			
			ResultSet rs = pStmt.executeQuery();
			Pelicula p;
			
			while (rs.next()) {
				boolean tresd;
				if (rs.getInt("tresd") == 1) {
					tresd = true;
				} else {
					tresd = false;
				}
				int id_peli = obtenerIdPeliculaPorTitulo(rs.getString("titulo"));
				p = new Pelicula(id_peli,rs.getInt("id_sala"),rs.getString("titulo"), Genero.valueOf(rs.getString("tipo")), 
						rs.getFloat("duracion"), rs.getString("director"), 
						rs.getString("rutafoto"), tresd, rs.getString("horarios"));
				String[] campos = rs.getString("horarios").split(",");
	            for (String h : campos) { 
	                if (!horarios.containsKey(h)) {
	                    horarios.put(BuscarHorarioPorId(Integer.parseInt(h)), p);
	                }
	            }
				
			}
			
			rs.close();
			pStmt.close();
			logger.info(String.format("Se ha obtenido los horarios."));			
		} catch (Exception ex) {
			logger.warning(String.format("Error al obtener los horarios: %s", ex.getMessage()));						
		}		
		
		return horarios;
	}
	public void reservarButaca(Butaca b ,String horario) {
		String sql = "INSERT INTO Butaca_Horario (id_butaca,id_horario) VALUES (?,?)\n";
		
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement pStmt = con.prepareStatement(sql)) {

			pStmt.setInt(1, b.getId());
			pStmt.setInt(2, BuscarHorario(horario));

			if (pStmt.executeUpdate() != 1) {					
				logger.warning(String.format("No se ha podido actualizar la Butaca con id: %d", b.getId()));
			} else {					
				logger.info(String.format("Se ha actualizado la Butaca con id: %d", b.getId()));
			}			
			pStmt.close();
		} catch (Exception ex) {
			logger.warning(String.format("Error al actualizar la Butaca con id: %d", b.getId()));
		}				
	}
	public void CancelaReservaButaca(Butaca b,String horario) {
		String sql = "DELETE FROM Butaca_Horario WHERE id_butaca = ? and id_horario = ?;";
		
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement pStmt = con.prepareStatement(sql)) {

			pStmt.setInt(1, b.getId());
			pStmt.setInt(2, BuscarHorario(horario));
				
			if (pStmt.executeUpdate() != 1) {					
				logger.warning(String.format("No se ha podido actualizar la Butaca con id: %d", b.getId()));
			} else {					
				logger.info(String.format("Se ha actualizado la Butaca con id: %d", b.getId()));
			}			
			pStmt.close();
		} catch (Exception ex) {
			logger.warning(String.format("Error al actualizar la Butaca con id: %d", b.getId()));
		}				
	}
	public Pelicula obtenerPeliculaportitulo(String titulo) {
		Pelicula p = null;
		String sql = "SELECT * FROM Pelicula WHERE titulo = ?";
		
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {			
			
			pStmt.setString(1, titulo);
			ResultSet rs = pStmt.executeQuery();			

			if (rs.next()) {
				boolean tresd;
				if (rs.getInt("tresd") == 1) {
					tresd = true;
				} else {
					tresd = false;
				}
				int id_peli = obtenerIdPeliculaPorTitulo(titulo);
				p = new Pelicula(id_peli,rs.getInt("id_sala"),rs.getString("titulo"), Genero.valueOf(rs.getString("tipo")), 
						rs.getFloat("duracion"), rs.getString("director"), 
						rs.getString("rutafoto"), tresd, rs.getString("horarios"));
			}
			
			rs.close();
			pStmt.close();
			logger.info(String.format("Se ha encontrado correctamente la pelicula con titulo: %s", titulo));			
		} catch (Exception e) {
			logger.warning(String.format("Error al encontrar la pelicula con titulo: %s", titulo));						
		}		
		
		return p;
	}
	public void meterEntrada(int id_butaca, int id_peli, String horario, int id_carrito, int edad, String nombre, String apellido, String apellido2) {
		String sql = "INSERT INTO Entrada (id_butaca, id_carrito, id_peli, id_horario, edad, nombre, apellido, apellido2) VALUES (?,?,?,?,?,?,?,?)\n";
		
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement Stmt = con.prepareStatement(sql)) {
			Stmt.setInt(1, id_butaca);
			Stmt.setInt(2, id_carrito);
			Stmt.setInt(3, id_peli);
			Stmt.setInt(4, BuscarHorario(horario));
			Stmt.setInt(5, edad);
			Stmt.setString(6, nombre);
			Stmt.setString(7, apellido);
			Stmt.setString(8, apellido2);
			
			if (!Stmt.execute()) {
				logger.info(String.format("Entrada insertada en la BBDD"));
		    }
			Stmt.close();
		} catch (Exception ex) {
			logger.warning(String.format("Error al insertar nueva entrada: %s", ex.getMessage()));
		}			
		
	}
	public void meterCarrito(String dni) {
		String sql = "INSERT INTO Carrito (cliente_dni) VALUES (?)\n";
		
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement Stmt = con.prepareStatement(sql)) {
			Stmt.setString(1, dni);
			
			if (!Stmt.execute()) {
				logger.info(String.format("Carrito insertado en la BBDD"));
		    }
			Stmt.close();
		} catch (Exception ex) {
			logger.warning(String.format("Error al insertar nuevo Carrito: %s", ex.getMessage()));
		}			
		
	}
	public int obtenerIdcarritopordni(String dni) {
	    String sql = "SELECT id FROM Carrito WHERE cliente_dni = ?";
	    int id_carrito = 0;
	    try (Connection con = DriverManager.getConnection(connectionString);
	         PreparedStatement pStmt = con.prepareStatement(sql)) {
	        pStmt.setString(1, dni);
	        try (ResultSet rs = pStmt.executeQuery()) {
	            if (rs.next()) { 
	                id_carrito = rs.getInt("id");
	                logger.info(String.format("Se ha encontrado correctamente el Carrito con id: %d", id_carrito));
	            } else {
	                logger.warning(String.format("No se encontró un carrito asociado al dni: %s", dni));
	            }
	            rs.close();
	        }
	        pStmt.close();
	    } catch (Exception e) {
	        logger.warning(String.format("Error al encontrar el Carrito para el dni %s: %s", dni, e.getMessage()));
	    }
	    return id_carrito;
	}
	//IAG ChatGPT
	//Calcular el numero de entradas
	public int numeroEntradas(String dni, String titulo, String horario) {
	    int totalEntradas = 0;
	    try (Connection con = DriverManager.getConnection(connectionString)) {
	        int idCarrito = obtenerIdCarritoPorDni(dni);
	        if (idCarrito != 0) {
	            int idPelicula = obtenerIdPeliculaPorTitulo(titulo);
	            if (idPelicula != 0) {
	                totalEntradas = contarEntradas(idCarrito, idPelicula, horario);
	            } else {
	                logger.warning(String.format("No se encontró una película con el título: %s", titulo));
	            }
	        } else {
	            logger.warning(String.format("No se encontró un carrito para el cliente con DNI: %s", dni));
	        }
	    } catch (Exception e) {
	        logger.warning(String.format("Error al obtener el número de entradas: %s", e.getMessage()));
	    }
	    return totalEntradas;
	}
	public int obtenerIdCarritoPorDni(String dni) {
	    int idCarrito = 0;
	    String sql = "SELECT id FROM Carrito WHERE cliente_dni = ?";
	    try (Connection con = DriverManager.getConnection(connectionString);
	         PreparedStatement pStmt = con.prepareStatement(sql)) {
	        pStmt.setString(1, dni);
	        ResultSet rs = pStmt.executeQuery();
	        if (rs.next()) {
	            idCarrito = rs.getInt("id");
	        }
	        rs.close();
	        pStmt.close();
	    } catch (Exception e) {
	        logger.warning(String.format("Error al obtener el ID del carrito para el cliente con DNI: %s. Detalles: %s", dni, e.getMessage()));
	    }
	    return idCarrito;
	}
	public int obtenerIdPeliculaPorTitulo(String titulo) {
	    int idPelicula = 0;
	    String sql = "SELECT id FROM Pelicula WHERE titulo = ?";
	    try (Connection con = DriverManager.getConnection(connectionString);
	         PreparedStatement pStmt = con.prepareStatement(sql)) {
	        pStmt.setString(1, titulo);
	        ResultSet rs = pStmt.executeQuery();
	        if (rs.next()) {
	            idPelicula = rs.getInt("id");
	        }
	        rs.close();
	        pStmt.close();
	    } catch (Exception e) {
	        logger.warning(String.format("Error al obtener el ID de la película con título: %s. Detalles: %s", titulo, e.getMessage()));
	    }
	    return idPelicula;
	}
	//IAG ChatGPT
	//Contar entradas
	public int contarEntradas(int idCarrito, int idPelicula, String horario) {
	    int cantidad = 0;
	    String sql = "SELECT COUNT(*) AS total FROM Entrada WHERE id_carrito = ? AND id_peli = ? AND id_horario = ?";
	    try (Connection con = DriverManager.getConnection(connectionString);
	         PreparedStatement pStmt = con.prepareStatement(sql)) {
	        
	        int idHorario = BuscarHorario(horario);
	        
	        pStmt.setInt(1, idCarrito);
	        pStmt.setInt(2, idPelicula);
	        pStmt.setInt(3, idHorario);
	        
	        ResultSet rs = pStmt.executeQuery();
	        if (rs.next()) {
	            cantidad = rs.getInt("total");
	        }
	        rs.close();
	        pStmt.close();
	    } catch (Exception e) {
	        logger.warning(String.format("Error al contar entradas para carrito: %d, película: %d, horario: %s. Detalles: %s",
	                                     idCarrito, idPelicula, horario, e.getMessage()));
	    }
	    return cantidad;
	}
	//IAG ChatGPT
	//Comprar carrito version 1
	public void comprarCarrito(String dni) {
		if (properties.get("cleanButacas").equals("true")) {	
			String sql1 = "DELETE FROM Carrito WHERE cliente_dni = ?;";
			
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt1 = con.prepareStatement(sql1)) {
				pStmt1.setString(1, dni);
		        if (!pStmt1.execute()) {
		        	logger.info("Se han comprado las entradas");
		        }
		        pStmt1.close();
			} catch (Exception ex) {
				logger.warning(String.format("Error al comprar las Entradas: %s", ex.getMessage()));
			}
		}
	}
	//IAG ChatGPT
	//Comprar carrito version 2
	public void comprarCarrito2(String dni) {
	    String sql1 = "DELETE FROM Entrada WHERE id_carrito = (SELECT id FROM Carrito WHERE cliente_dni = ?);";
	    String sql2 = "DELETE FROM Carrito WHERE cliente_dni = ?;";
	    
	    try (Connection con = DriverManager.getConnection(connectionString)) {
	        con.setAutoCommit(false);

	        try (PreparedStatement pStmt1 = con.prepareStatement(sql1);
	             PreparedStatement pStmt2 = con.prepareStatement(sql2)) {

	            pStmt1.setString(1, dni);
	            int entradasEliminadas = pStmt1.executeUpdate();
	            logger.info(String.format("%d entradas eliminadas", entradasEliminadas));

	            pStmt2.setString(1, dni);
	            int carritosEliminados = pStmt2.executeUpdate();
	            logger.info(String.format("%d carritos eliminados", carritosEliminados));

	            con.commit();
	            pStmt1.close();
	            pStmt2.close();
	        } catch (Exception e) {
	            con.rollback();
	            throw e;
	        }
	    } catch (Exception ex) {
	        logger.warning(String.format("Error al comprar las Entradas: %s", ex.getMessage()));
	    }
	}
	//IAG ChatGPT
	//Comprar carrito version 3
	public void comprarCarrito3(String dni) {
	    String sql1 = "SELECT e.id_butaca, e.id_peli, e.id_horario, e.nombre, e.apellido, e.apellido2, e.edad " +
	                  "FROM Entrada e WHERE e.id_carrito = (SELECT id FROM Carrito WHERE cliente_dni = ?);";
	    String sqlInsertEntradasCompradas = "INSERT INTO EntradasCompradas (id_butaca, cliente_dni, id_peli, id_horario, edad, nombre, apellido) " +
	                                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
	    String sql2 = "DELETE FROM Butaca_Horario WHERE id_butaca IN (" +
	                  "SELECT id_butaca FROM Entrada WHERE id_carrito = (SELECT id FROM Carrito WHERE cliente_dni = ?));";
	    String sql3 = "DELETE FROM Entrada WHERE id_carrito = (SELECT id FROM Carrito WHERE cliente_dni = ?);";
	    String sql4 = "DELETE FROM Carrito WHERE cliente_dni = ?;";
	    
	    try (Connection con = DriverManager.getConnection(connectionString)) {
	        con.setAutoCommit(false);

	        try (PreparedStatement pStmt1 = con.prepareStatement(sql1);
	             PreparedStatement pStmtInsert = con.prepareStatement(sqlInsertEntradasCompradas);
	             PreparedStatement pStmt2 = con.prepareStatement(sql2);
	             PreparedStatement pStmt3 = con.prepareStatement(sql3);
	             PreparedStatement pStmt4 = con.prepareStatement(sql4)) {

	            pStmt1.setString(1, dni);
	            ResultSet rs = pStmt1.executeQuery();

	            while (rs.next()) {
	                pStmtInsert.setInt(1, rs.getInt("id_butaca"));
	                pStmtInsert.setString(2, dni);
	                pStmtInsert.setInt(3, rs.getInt("id_peli"));
	                pStmtInsert.setInt(4, rs.getInt("id_horario")); 
	                pStmtInsert.setInt(5, rs.getInt("edad"));
	                pStmtInsert.setString(6, rs.getString("nombre"));
	                pStmtInsert.setString(7, rs.getString("apellido"));
	                pStmtInsert.executeUpdate();
	            }

	            pStmt2.setString(1, dni);
	            int butacasHorarioEliminadas = pStmt2.executeUpdate();
	            logger.info(String.format("%d butacas_horario eliminadas", butacasHorarioEliminadas));

	            pStmt3.setString(1, dni);
	            int entradasEliminadas = pStmt3.executeUpdate();
	            logger.info(String.format("%d entradas eliminadas", entradasEliminadas));

	            pStmt4.setString(1, dni);
	            int carritosEliminados = pStmt4.executeUpdate();
	            logger.info(String.format("%d carritos eliminados", carritosEliminados));

	            con.commit();
	            pStmt1.close();
	            pStmt2.close();
	            pStmt3.close();
	            pStmt4.close();
	        } catch (Exception ex) {
	            con.rollback();
	            logger.warning(String.format("Error al comprar las Entradas: %s", ex.getMessage()));
	        }
	    } catch (Exception ex) {
	        logger.warning(String.format("Error en la conexión a la base de datos: %s", ex.getMessage()));
	    }
	}

	//IAG ChatGPT
	//Iniciar sesion
	public String InicioSesion(String dni) {
	    String selectSql = "SELECT horario_inicio_sesion FROM InicioSesion WHERE cliente_dni = ?";
	    String insertSql = "INSERT INTO InicioSesion (horario_inicio_sesion, cliente_dni) VALUES (?, ?)";
	    String updateSql = "UPDATE InicioSesion SET horario_inicio_sesion = ? WHERE cliente_dni = ?";
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	    String horarioActual = LocalDateTime.now().format(formatter);

	    try (Connection conn = DriverManager.getConnection(connectionString);
	         PreparedStatement selectStmt = conn.prepareStatement(selectSql);
	         PreparedStatement insertStmt = conn.prepareStatement(insertSql);
	         PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

	        selectStmt.setString(1, dni);
	        ResultSet rs = selectStmt.executeQuery();

	        if (rs.next()) {
	            updateStmt.setString(1, horarioActual);
	            updateStmt.setString(2, dni);
	            updateStmt.executeUpdate();
	            logger.info(String.format("Horario de inicio actualizado para cliente con DNI: %s", dni));
	        } else {
	            insertStmt.setString(1, horarioActual);
	            insertStmt.setString(2, dni);
	            insertStmt.executeUpdate();
	            logger.info(String.format("Sesión iniciada correctamente para cliente con DNI: %s", dni));
	        }
	        selectStmt.close();
	        insertStmt.close();
	        updateStmt.close();
	    } catch (Exception e) {
	        logger.warning(String.format("Error al insertar inicio de sesión: %s", e.getMessage()));
	    }
	    return horarioActual;
	}


	public String CerrarSesion(String dni) {
	    String sql = "DELETE FROM InicioSesion WHERE cliente_dni = ?";
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	    String horarioActual = LocalDateTime.now().format(formatter);

	    try (Connection conn = DriverManager.getConnection(connectionString);
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, dni);

	        pstmt.executeUpdate();
	        logger.info(String.format("Cierre de sesión exitoso para cliente DNI: %s a las %s", dni, horarioActual));
	    } catch (Exception e) {
	        logger.warning(String.format("Error al cerrar sesión: %s", e.getMessage()));
	    }
	    return horarioActual;
	}
	//IAG ChatGPT
	//Eliminar horarios pasados a la hora de iniciar sesion y todos los metodos que usa
	public void eliminarHorariosPasados(String horarioInicioSesion) {
	    List<String> horariosPasados = obtenerHorariosPasados(horarioInicioSesion);
	    if (!horariosPasados.isEmpty()) {
	        eliminarHorariosDeTablasRelacionadas(horariosPasados);
	        actualizarPeliculasYEliminarSiEsNecesario(horariosPasados);
	        eliminarHorariosDeTabla(horariosPasados);
	        logger.info("Horarios pasados y todos los datos relacionados han sido eliminados.");
	    } else {
	        logger.info("No hay horarios pasados para eliminar.");
	    }
	}

	private List<String> obtenerHorariosPasados(String horarioInicioSesion) {
	    List<String> horariosPasados = new ArrayList<>();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	    try {
	        LocalDateTime inicioSesion = LocalDateTime.parse(horarioInicioSesion, formatter);
	        String selectSql = "SELECT id FROM Horarios WHERE horario < ?";

	        try (Connection conn = DriverManager.getConnection(connectionString);
	             PreparedStatement pstmt = conn.prepareStatement(selectSql)) {

	            pstmt.setString(1, inicioSesion.format(formatter));
	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {
	                horariosPasados.add(String.valueOf(rs.getInt("id")));
	            }
	            rs.close();
	            pstmt.close();
	        }
	    } catch (Exception e) {
	        logger.warning(String.format("Error al obtener horarios pasados: %s", e.getMessage()));
	    }

	    return horariosPasados;
	}

	private void eliminarHorariosDeTablasRelacionadas(List<String> horariosPasados) {
	    eliminarEntradasPorHorarios(horariosPasados);
	    eliminarEntradasCompradasPorHorarios(horariosPasados);
	    eliminarButacasPorHorarios(horariosPasados);
	}

	private void eliminarEntradasPorHorarios(List<String> horariosPasados) {
	    String deleteSql = "DELETE FROM Entrada WHERE id_horario = ?";

	    try (Connection conn = DriverManager.getConnection(connectionString);
	         PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

	        for (String horarioId : horariosPasados) {
	            deleteStmt.setInt(1, Integer.parseInt(horarioId));
	            deleteStmt.executeUpdate();
	            logger.info(String.format("Entradas asociadas al horario con ID %s eliminadas.", horarioId));
	        }
	        deleteStmt.close();
	    } catch (Exception e) {
	        logger.warning(String.format("Error al eliminar entradas por horarios pasados: %s", e.getMessage()));
	    }
	}

	private void eliminarEntradasCompradasPorHorarios(List<String> horariosPasados) {
	    String deleteSql = "DELETE FROM EntradasCompradas WHERE id_horario = ?";

	    try (Connection conn = DriverManager.getConnection(connectionString);
	         PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

	        for (String horarioId : horariosPasados) {
	            deleteStmt.setInt(1, Integer.parseInt(horarioId));
	            deleteStmt.executeUpdate();
	            logger.info(String.format("Entradas compradas asociadas al horario con ID %s eliminadas.", horarioId));
	        }
	        deleteStmt.close();
	    } catch (Exception e) {
	        logger.warning(String.format("Error al eliminar entradas compradas por horarios pasados: %s", e.getMessage()));
	    }
	}

	private void eliminarButacasPorHorarios(List<String> horariosPasados) {
	    String deleteSql = "DELETE FROM Butaca_Horario WHERE id_horario = ?";

	    try (Connection conn = DriverManager.getConnection(connectionString);
	         PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

	        for (String horarioId : horariosPasados) {
	            deleteStmt.setInt(1, Integer.parseInt(horarioId));
	            deleteStmt.executeUpdate();
	            logger.info(String.format("Asociaciones de butacas con el horario ID %s eliminadas.", horarioId));
	        }
	        deleteStmt.close();
	    } catch (Exception e) {
	        logger.warning(String.format("Error al eliminar asociaciones de butacas por horarios pasados: %s", e.getMessage()));
	    }
	}

	private void actualizarPeliculasYEliminarSiEsNecesario(List<String> horariosPasados) {
	    String selectSql = "SELECT id, horarios FROM Pelicula";
	    String updateSql = "UPDATE Pelicula SET horarios = ? WHERE id = ?";
	    String deleteSql = "DELETE FROM Pelicula WHERE id = ?";

	    try (Connection conn = DriverManager.getConnection(connectionString);
	         PreparedStatement selectStmt = conn.prepareStatement(selectSql);
	         PreparedStatement updateStmt = conn.prepareStatement(updateSql);
	         PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

	        ResultSet rs = selectStmt.executeQuery();

	        while (rs.next()) {
	            int peliculaId = rs.getInt("id");
	            String horariosStr = rs.getString("horarios");

	            if (horariosStr == null || horariosStr.trim().isEmpty()) {
	                continue;
	            }

	            String[] horarios = horariosStr.split(",");
	            StringBuilder horariosActualizados = new StringBuilder();
	            boolean actualizado = false;

	            for (String h : horarios) {
	                h = h.trim();
	                if (!horariosPasados.contains(h)) {
	                    if (horariosActualizados.length() > 0) {
	                        horariosActualizados.append(",");
	                    }
	                    horariosActualizados.append(h);
	                } else {
	                    actualizado = true;
	                }
	            }

	            if (horariosActualizados.length() == 0) {
	                deleteStmt.setInt(1, peliculaId);
	                deleteStmt.executeUpdate();
	                logger.info(String.format("Película con ID %d eliminada debido a la falta de horarios.", peliculaId));
	            } else if (actualizado) {
	                updateStmt.setString(1, horariosActualizados.toString());
	                updateStmt.setInt(2, peliculaId);
	                updateStmt.executeUpdate();
	                logger.info(String.format("Película con ID %d actualizada con horarios: %s", peliculaId, horariosActualizados));
	            }
	        }
	        rs.close();
	        selectStmt.close();
	        updateStmt.close();
	        deleteStmt.close();
	    } catch (Exception e) {
	        logger.warning(String.format("Error al actualizar o eliminar películas: %s", e.getMessage()));
	    }
	}

	private void eliminarHorariosDeTabla(List<String> horariosPasados) {
	    String deleteSql = "DELETE FROM Horarios WHERE id = ?";

	    try (Connection conn = DriverManager.getConnection(connectionString);
	         PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

	        for (String horarioId : horariosPasados) {
	            deleteStmt.setInt(1, Integer.parseInt(horarioId));
	            deleteStmt.executeUpdate();
	            logger.info(String.format("Horario con ID %s eliminado de la tabla Horarios.", horarioId));
	        }
	        deleteStmt.close();
	    } catch (Exception e) {
	        logger.warning(String.format("Error al eliminar horarios pasados de la tabla Horarios: %s", e.getMessage()));
	    }
	}


    public String BuscarHorarioPorId(int id) {
    	String horario = null;
        String sql = "SELECT horario FROM Horarios WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(connectionString);
        		PreparedStatement pstmt = conn.prepareStatement(sql)){
        	
        	pstmt.setInt(1, id);
        	
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
                logger.info(String.format("Se ha encontrado el horario con id: %d , correctamente", id));
	            horario = rs.getString("horario");
			}
			rs.close();
			pstmt.close();

		} catch (SQLException e) {
            logger.warning(String.format("Error al devolver el horario", e.getMessage()));
		}
		return horario;
    }
    public int BuscarHorario(String horario) {
    	int id = 0;
        String sql = "SELECT id FROM Horarios WHERE horario = ?";
        try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, horario);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                logger.info(String.format("Se ha encontrado el horario con id: %d correctamente", rs.getInt("id")));
                id = rs.getInt("id");
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            logger.warning(String.format("Error al devolver el horario: %s", e.getMessage()));
        }
        return id;
    }

    public List<Entrada> calcularPrecioTotal(Entrada e) {
    	String sql = "SELECT * FROM Entrada WHERE id_horario = ? and id_peli = ? and id_carrito = ?";
    	List<Entrada> entradas = new ArrayList<Entrada>();
    	try (Connection conn = DriverManager.getConnection(connectionString);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

    		   pstmt.setInt(1, BuscarHorario(e.getHorario()));
               pstmt.setInt(2, e.getId_peli());
               pstmt.setInt(3, obtenerIdcarritoporEntrada(e));

               ResultSet rs = pstmt.executeQuery();
               while (rs.next()) {
            	int id = rs.getInt("id");
   	            int idButaca = rs.getInt("id_butaca");
   	            int idPelicula = rs.getInt("id_peli");
   	            String horario = BuscarHorarioPorId(rs.getInt("id_horario"));
   	            int edad = rs.getInt("edad");
   	            String nombre = rs.getString("nombre");
   	            String apellido = rs.getString("apellido");
   	            String apellido2 = rs.getString("apellido2");
   	            
   	            Butaca butaca = obtenerButacaporId(idButaca);
   	            Pelicula pelicula = obtenerPeliculaporID(idPelicula);
   	            
   	            float precio;
	           		if (edad < 3) {
	           			precio = -1;
	           		} else if (edad < 10) {
	           			precio = 0;
	           		} else if (edad < 18) {
	           			precio = 8;
	           		} else if (edad < 65) {
	           			precio = 12;
	           		} else {
	           			precio = 8;
	           		}
   	            
   	            e = new Entrada(id,butaca, pelicula.getId(), pelicula.getTitulo(), pelicula.getId_sala(), precio, horario, nombre, apellido, edad, apellido2);
   	            entradas.add(e);
               }
  	           rs.close();
  	           pstmt.close();
           } catch (SQLException ex) {
               logger.warning(String.format("Error al devolver el precio total: %s", ex.getMessage()));
           }
           return entradas;
    }
    public int obtenerIdcarritoporEntrada(Entrada entrada) {
	    String sql = "SELECT id_carrito FROM Entrada WHERE id = ?";
	    int id_carrito = 0;
	    try (Connection con = DriverManager.getConnection(connectionString);
	         PreparedStatement pStmt = con.prepareStatement(sql)) {
	        pStmt.setInt(1, entrada.getId());
	        try (ResultSet rs = pStmt.executeQuery()) {
	            if (rs.next()) { 
	                id_carrito = rs.getInt("id_carrito");
	                logger.info(String.format("Se ha encontrado correctamente el Carrito con id: %d", id_carrito));
	            } else {
	                logger.warning(String.format("No se encontró un carrito asociado a la entrada: %s", entrada.toString()));
	            }
	            rs.close();
	        }
	        pStmt.close();
	    } catch (Exception e) {
	        logger.warning(String.format("Error al encontrar el Carrito para la entrada %s: %s", entrada.toString(), e.getMessage()));
	    }
	    return id_carrito;
	}
    public int contarEntradasCompradas(Cliente c,String horario, String titulo) {
    	String sql = "SELECT * FROM EntradasCompradas WHERE id_horario = ? AND cliente_dni = ? AND id_peli = ?";
		int numeroentradas = 0;
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {
		    pStmt.setInt(1, BuscarHorario(horario));
		    pStmt.setString(2, c.getDni());
		    pStmt.setInt(3, obtenerIdPeliculaPorTitulo(titulo));
		    
		    try (ResultSet rs = pStmt.executeQuery()) {
		        while (rs.next()) { 
		            numeroentradas++;
		        }
		        rs.close();
		    }
		    pStmt.close();
		} catch (Exception e) {
		    logger.warning(String.format("Error al contar las entradas compradas: %s", e.getMessage()));
 	    }
 	    return numeroentradas;
    }
    public List<Pelicula> cogerPelis() {
    	String sql = "SELECT * FROM Pelicula";
    	List<Pelicula> pelis = new ArrayList<Pelicula>();
        try (Connection con = DriverManager.getConnection(connectionString);
             PreparedStatement pStmt = con.prepareStatement(sql)) {
        	ResultSet rs = pStmt.executeQuery();
        	while(rs.next()) {
        		boolean tresd = false;
        		if(rs.getInt("tresd")==1) tresd = true;
        		Pelicula p = new Pelicula(rs.getInt("id_sala"), rs.getString("titulo"), Genero.valueOf(rs.getString("tipo")),
        				rs.getFloat("duracion"), rs.getString("director"), rs.getString("rutafoto"), tresd, rs.getString("horarios"));
        		pelis.add(p);
        	}
        	rs.close();
        	pStmt.close();        	
        } catch (Exception e) {
            logger.warning(String.format("Error al devolver las peliculas: %s", e.getMessage()));
        }
        return pelis;
    }
    public void cambiarDuracionPeli(float duracion, String horarios) {
        String sql = "UPDATE Pelicula SET duracion = ? WHERE horarios = ?";
        try (Connection con = DriverManager.getConnection(connectionString);
             PreparedStatement pStmt = con.prepareStatement(sql)) {
            pStmt.setFloat(1, duracion);
            pStmt.setString(2, horarios);
            
            int r = pStmt.executeUpdate();
            if (r > 0) {
                logger.info("Duracion cambiada correctamente");
            } else {
                logger.info(String.format("No se encontro ninguna pelicula con horarios = %s", horarios));
            }
            pStmt.close();
        } catch (Exception e) {
            logger.warning(String.format("Error al cambiar la duracion: %s", e.getMessage()));
        }
    }
    public void meterPelicula(Pelicula p) {
        String sql = "INSERT OR IGNORE INTO Pelicula (id_sala, titulo, director, tipo, duracion, rutafoto, horarios, tresd) VALUES (?,?,?,?,?,?,?,?)";

		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement Stmt = con.prepareStatement(sql)) {
			boolean tresd = false;
			if (p.isTresd()) {
				tresd = true;
			}
			
			Stmt.setInt(1, p.getId_sala());
			Stmt.setString(2, p.getTitulo());
			Stmt.setString(3, p.getDirector());
			Stmt.setString(4, p.getTipo().toString());
			Stmt.setFloat(5, p.getDuracion());
			Stmt.setString(6, p.getRutafoto());
			Stmt.setString(7, p.getHorarios());
			Stmt.setBoolean(8, tresd);
				
			if (!Stmt.execute()) {
				logger.info(String.format("Pelicula con titulo: %s insertado en la BBDD", p.getTitulo()));
		    }
			Stmt.close();
		} catch (Exception ex) {
			logger.warning(String.format("Error al insertar nueva Pelicula: %s", ex.getMessage()));
		}			
		
	}
    public boolean TituloExiste(String titulo) {
        String sql = "SELECT * FROM Pelicula WHERE titulo = ?";
        boolean enc = false;
        try (Connection con = DriverManager.getConnection(connectionString);
             PreparedStatement pStmt = con.prepareStatement(sql)) {
            pStmt.setString(1, titulo);
            
            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
            	enc = true;
            }
            rs.close();
            pStmt.close();
        } catch (Exception e) {
            logger.warning(String.format("Error al cambiar la duracion: %s", e.getMessage()));
        }
        return enc;
    }
    public boolean eliminarPelicula(String titulo) {
        String Sql = "DELETE FROM Pelicula WHERE titulo = ?";

        try (Connection conn = DriverManager.getConnection(connectionString);
             PreparedStatement stmt = conn.prepareStatement(Sql)) {
            
            stmt.setString(1, titulo);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info(String.format("Película '%s' eliminada correctamente", titulo));
                stmt.close();
                return true;
            } else {
                logger.warning(String.format("No se encontró una película con el título '%s'", titulo));
                stmt.close();
                return false;
            }
        } catch (Exception e) {
            logger.warning(String.format("Error al eliminar película: %s", e.getMessage()));
            return false;
        }
    }
    public HashMap<Integer, String> cogerHorarios() {
    	String sql = "SELECT * FROM Horarios";
    	HashMap<Integer, String> horarios = new HashMap<>();
        try (Connection con = DriverManager.getConnection(connectionString);
             PreparedStatement pStmt = con.prepareStatement(sql)) {
        	ResultSet rs = pStmt.executeQuery();
        	while(rs.next()) {
        		if (!horarios.containsKey(rs.getInt("id"))) {
					horarios.put(rs.getInt("id"), rs.getString("horario"));
				}
        	}
        	rs.close();
        	pStmt.close();
        } catch (Exception e) {
            logger.warning(String.format("Error al devolver los horarios: %s", e.getMessage()));
        }
        return horarios;
    }
    public void meterHorario(String horario) {
        String sql = "INSERT OR IGNORE INTO Horarios (horario) VALUES (?)";

		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement Stmt = con.prepareStatement(sql)) {
			Stmt.setString(1, horario);
				
			if (!Stmt.execute()) {
				logger.info(String.format("Horario: %s insertado en la BBDD", horario));
		    }
			Stmt.close();
		} catch (Exception ex) {
			logger.warning(String.format("Error al insertar nuevo horario: %s", ex.getMessage()));
		}			
		
	}
    public boolean existeHorario(String horario) {
		boolean existe = false;
		String sql = "SELECT * FROM Horarios WHERE horario = ?";
	    try (Connection con = DriverManager.getConnection(connectionString);
				PreparedStatement ps = con.prepareStatement(sql)){

			ps.setString(1, horario);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) { 
				existe = true;
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
		    logger.warning(String.format("Error al buscar el usuario y contraseña: %s", e.getMessage()));
		}
		return existe;
	}
    public void cambiarSaldo(float saldoNuevo, String dni) {
        String sql = "UPDATE Cliente SET salario = ? WHERE dni = ?";
        try (Connection con = DriverManager.getConnection(connectionString);
             PreparedStatement pStmt = con.prepareStatement(sql)) {
            pStmt.setFloat(1, saldoNuevo);
            pStmt.setString(2, dni);
            
            int r = pStmt.executeUpdate();
            if (r > 0) {
                logger.info("Saldo cambiada correctamente");
            } else {
                logger.info(String.format("No se encontro ningun cliente con dni = %s", dni));
            }
            pStmt.close();
        } catch (Exception e) {
            logger.warning(String.format("Error al cambiar el saldo: %s", e.getMessage()));
        }
    }
    public void borrarBBDD() {
        String sql1 = "DROP TABLE IF EXISTS Cliente;";
        String sql2 = "DROP TABLE IF EXISTS Pelicula;";
        String sql3 = "DROP TABLE IF EXISTS Entrada;";
        String sql4 = "DROP TABLE IF EXISTS Carrito;";
        String sql5 = "DROP TABLE IF EXISTS Butaca;";
        String sql6 = "DROP TABLE IF EXISTS Butaca_Horario;";
        String sql7 = "DROP TABLE IF EXISTS InicioSesion;";
        String sql8 = "DROP TABLE IF EXISTS Horarios;";
        String sql9 = "DROP TABLE IF EXISTS EntradasCompradas;";

        try (Connection con = DriverManager.getConnection(connectionString);
             PreparedStatement pStmt1 = con.prepareStatement(sql1);
             PreparedStatement pStmt2 = con.prepareStatement(sql2);
             PreparedStatement pStmt3 = con.prepareStatement(sql3);
             PreparedStatement pStmt4 = con.prepareStatement(sql4);
             PreparedStatement pStmt5 = con.prepareStatement(sql5);
             PreparedStatement pStmt6 = con.prepareStatement(sql6);
             PreparedStatement pStmt7 = con.prepareStatement(sql7);
             PreparedStatement pStmt8 = con.prepareStatement(sql8);
             PreparedStatement pStmt9 = con.prepareStatement(sql9)) {

            if (!pStmt1.execute()) logger.info("Tabla Cliente eliminada o no existía.");
            if (!pStmt2.execute()) logger.info("Tabla Pelicula eliminada o no existía.");
            if (!pStmt3.execute()) logger.info("Tabla Entrada eliminada o no existía.");
            if (!pStmt4.execute()) logger.info("Tabla Carrito eliminada o no existía.");
            if (!pStmt5.execute()) logger.info("Tabla Butaca eliminada o no existía.");
            if (!pStmt6.execute()) logger.info("Tabla Butaca_Horario eliminada o no existía.");
            if (!pStmt7.execute()) logger.info("Tabla InicioSesion eliminada o no existía.");
            if (!pStmt8.execute()) logger.info("Tabla Horarios eliminada o no existía.");
            if (!pStmt9.execute()) logger.info("Tabla EntradasCompradas eliminada o no existía.");

        } catch (Exception e) {
            logger.warning(String.format("Error al eliminar las tablas: %s", e.getMessage()));
        }
        
    }

}
