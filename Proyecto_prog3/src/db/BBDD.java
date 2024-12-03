package db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import domain.Butaca;
import domain.ButacaHorario;
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
			//Inicialización del Logger
			LogManager.getLogManager().readConfiguration(fis);
			
			//Lectura del fichero properties
			properties = new Properties();
			properties.load(new FileReader(PROPERTIES_FILE));
			
			driverName = properties.getProperty("driver");
			databaseFile = properties.getProperty("file");
			connectionString = properties.getProperty("connection");
			
			//Crear carpetas de log si no existe
			File dir = new File(LOG_FOLDER);
			
			if (!dir.exists()) {
				dir.mkdirs();
			}

			//Crear carpeta para la BBDD si no existe
			dir = new File(databaseFile.substring(0, databaseFile.lastIndexOf("/")));
			
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			//Cargar el diver SQLite
			Class.forName(driverName);
		} catch (Exception ex) {
			logger.warning(String.format("Error al cargar el driver de BBDD: %s", ex.getMessage()));
		}
	}

	/*public void initBD(String nombreBD)  {

		try {
			conexion = DriverManager.getConnection(connectionString);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void closeBD() {
		if (conexion != null) {
			try {
				conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}*/

    public void crearBBDD() {
        if (properties.get("createBBDD").equals("true")) {
            String sql1 = "CREATE TABLE IF NOT EXISTS Cliente (\n"
                    + " dni TEXT PRIMARY KEY,\n"
                    + " username TEXT NOT NULL,\n"
                    + " contrasenia TEXT NOT NULL,\n"
                    + " nombre TEXT NOT NULL,\n"
                    + " apellidos TEXT NOT NULL,\n"
                    + " correo TEXT NOT NULL\n);";

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
                    + " horario STRING NOT NULL,\n"
                    + " FOREIGN KEY(id_butaca) REFERENCES Butaca(id) ON DELETE CASCADE,\n"
                    + " FOREIGN KEY(id_carrito) REFERENCES Carrito(id) ON DELETE CASCADE,\n"
                    + " FOREIGN KEY(id_peli) REFERENCES Pelicula(id) ON DELETE CASCADE\n"
                    + " UNIQUE(id_butaca, id_carrito, id_peli, horario));";

            String sql4 = "CREATE TABLE IF NOT EXISTS Carrito (\n"
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + " cliente_dni TEXT,\n"
                    + " UNIQUE(cliente_dni),\n"
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
                    + " estado INTEGER NOT NULL,\n"
                    + " horario STRING NOT NULL,\n"
                    + " UNIQUE(id_butaca),\n"
                    + " FOREIGN KEY(id_butaca) REFERENCES Butaca(id) ON DELETE CASCADE);";
            
            try (Connection con = DriverManager.getConnection(connectionString);
                 PreparedStatement pStmt1 = con.prepareStatement(sql1);
                 PreparedStatement pStmt2 = con.prepareStatement(sql2);
                 PreparedStatement pStmt3 = con.prepareStatement(sql3);
                 PreparedStatement pStmt4 = con.prepareStatement(sql4);
            	 PreparedStatement pStmt5 = con.prepareStatement(sql5);
            	 PreparedStatement pStmt6 = con.prepareStatement(sql6)) {
            	
                if (!pStmt1.execute()) logger.info("Tabla Cliente creada o ya existía.");
                if (!pStmt2.execute()) logger.info("Tabla Pelicula creada o ya existía.");
                if (!pStmt3.execute()) logger.info("Tabla Entrada creada o ya existía.");
                if (!pStmt4.execute()) logger.info("Tabla Carrito creada o ya existía.");
                if (!pStmt5.execute()) logger.info("Tabla Butaca creada o ya existía.");
                if (!pStmt6.execute()) logger.info("Tabla ButacaHorario creada o ya existía.");

            } catch (Exception e) {
                logger.warning(String.format("Error al crear las tablas: %s", e.getMessage()));
            }
        }
    }

    public void insertardatosporDefecto() {
        if (properties.get("createBBDD").equals("true")) {
            String insertCliente = "INSERT OR IGNORE INTO Cliente (dni, username, contrasenia, nombre, apellidos, correo) VALUES\n"
                    + "('11111111A', 'user1', 'pass1', 'John', 'Doe', 'john.doe@example.com'),\n"
                    + "('22222222B', 'user2', 'pass2', 'Jane', 'Doe', 'jane.doe@example.com'),\n"
                    + "('33333333C', 'user3', 'pass3', 'Alice', 'Smith', 'alice.smith@example.com'),\n"
                    + "('44444444D', 'user4', 'pass4', 'Bob', 'Johnson', 'bob.johnson@example.com'),\n"
                    + "('55555555E', 'user5', 'pass5', 'Charlie', 'Brown', 'charlie.brown@example.com'),\n"
                    + "('66666666F', 'user6', 'pass6', 'Eve', 'Davis', 'eve.davis@example.com'),\n"
                    + "('77777777G', 'user7', 'pass7', 'Frank', 'Miller', 'frank.miller@example.com'),\n"
                    + "('88888888H', 'user8', 'pass8', 'Grace', 'Lee', 'grace.lee@example.com'),\n"
                    + "('99999999I', 'user9', 'pass9', 'Hank', 'Wilson', 'hank.wilson@example.com'),\n"
                    + "('00000000J', 'user10', 'pass10', 'Ivy', 'Moore', 'ivy.moore@example.com');";

            String insertPelicula = "INSERT OR IGNORE INTO Pelicula (id_sala, titulo, director, tipo, duracion, rutafoto, horarios, tresd) VALUES\n"
                    + "(1, 'Inception', 'Christopher Nolan', 'ACCION', 2.5, 'resource/images/Inception.png', '2024-10-11 16:00,2024-10-11 18:00', 1),\n"
                    + "(2, 'The Godfather', 'Francis Ford Coppola', 'DRAMA', 2.9, 'resource/images/TheGodfather.png', '2024-10-11 17:00,2024-10-11 19:00', 0),\n"
                    + "(3, 'Dune', 'Denis Villeneuve', 'CIENCIA_FICCION', 2.8, 'resource/images/Dune.png', '2024-10-12 20:00,2024-10-12 22:00', 1),\n"
                    + "(4, 'Interstellar', 'Christopher Nolan', 'CIENCIA_FICCION', 3.0, 'resource/images/Interstellar.png', '2024-10-13 14:00,2024-10-13 16:00', 1),\n"
                    + "(1, 'Titanic', 'James Cameron', 'ROMANCE', 3.2, 'resource/images/Titanic.png', '2024-10-14 18:00,2024-10-14 20:00', 0),\n"
                    + "(2, 'Pulp Fiction', 'Quentin Tarantino', 'CRIMEN', 2.8, 'resource/images/PulpFiction.png', '2024-10-15 16:00,2024-10-15 18:00', 0),\n"
                    + "(3, 'Avengers: Endgame', 'Anthony Russo', 'ACCION', 3.1, 'resource/images/AvengersEndgame.png', '2024-10-16 20:00,2024-10-16 22:30', 1),\n"
                    + "(4, 'The Matrix', 'Lana Wachowski', 'CIENCIA_FICCION', 2.6, 'resource/images/TheMatrix.png', '2024-10-17 16:00,2024-10-17 18:30', 1);";

            String insertCarrito = "INSERT OR IGNORE INTO Carrito (cliente_dni) VALUES\n"
                    + "('11111111A'),\n"
                    + "('22222222B'),\n"
                    + "('33333333C'),\n"
                    + "('44444444D');";

            String insertEntrada = "INSERT OR IGNORE INTO Entrada (id_butaca, id_carrito, id_peli, horario) VALUES\n"
                    + "(1, 1, 1, '2024-10-11 16:00'),\n"
                    + "(2, 1, 2, '2024-10-11 17:00'),\n"
                    + "(3, 2, 3, '2024-10-12 20:00'),\n"
                    + "(4, 2, 4, '2024-10-13 14:00'),\n"
                    + "(5, 2, 5, '2024-10-14 18:00'),\n"
                    + "(6, 3, 6, '2024-10-15 16:00'),\n"
                    + "(7, 3, 7, '2024-10-16 20:00'),\n"
                    + "(8, 3, 8, '2024-10-17 16:00'),\n"
                    + "(9, 4, 8, '2024-10-17 16:00'),\n"
                    + "(10, 4, 1, '2024-10-11 18:00');";
            
            String insertButacaHorario = "INSERT OR IGNORE INTO Butaca_Horario (id_butaca, estado, horario) VALUES\n"
            		+ "(1, 1, '2024-10-11 16:00'),\n"
            		+ "(2, 1, '2024-10-11 17:00'),\n"
            		+ "(3, 1, '2024-10-12 20:00'),\n"
            		+ "(4, 1, '2024-10-13 14:00'),\n"
            		+ "(5, 1, '2024-10-14 18:00'),\n"
            		+ "(6, 1, '2024-10-15 16:00'),\n"
            		+ "(7, 1, '2024-10-16 20:00'),\n"
            		+ "(8, 1, '2024-10-17 16:00'),\n"
            		+ "(9, 1, '2024-10-17 16:00'),\n"
            		+ "(10, 1, '2024-10-11 18:00');";

            try (Connection con = DriverManager.getConnection(connectionString);
                 PreparedStatement insertStmt1 = con.prepareStatement(insertCliente);
                 PreparedStatement insertStmt2 = con.prepareStatement(insertPelicula);
                 PreparedStatement insertStmt3 = con.prepareStatement(insertCarrito);
                 PreparedStatement insertStmt4 = con.prepareStatement(insertEntrada);
            	 PreparedStatement insertStmt5 = con.prepareStatement(insertButacaHorario);) {

                if (!insertStmt1.execute() && !insertStmt2.execute() && !insertStmt3.execute() && !insertStmt4.execute() && !insertStmt5.execute()) {
                    logger.info("Se han insertado los datos correctamente.");
                }
            } catch (Exception e) {
                logger.warning(String.format("Error al insertar los datos: %s", e.getMessage()));
            }
        }
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
			if(rs.next()) { //Si la SELECT SÍ ha devuelto información
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
		String sql = "INSERT INTO Cliente (dni, username, contrasenia, nombre, apellidos, correo) VALUES (?, ?, ?, ?, ?, ?);";
		
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement Stmt = con.prepareStatement(sql)) {
	
				Stmt.setString(1, c.getDni());
				Stmt.setString(2, c.getUser());
				Stmt.setString(3, c.getContrasenia());
				Stmt.setString(4, c.getNombre());
				Stmt.setString(5, c.getApellidos());
				Stmt.setString(6, c.getCorreo());
				
				
				if (!Stmt.execute()) {
					logger.info(String.format("Usuario con DNI: %s insertado en la BBDD", c.getDni()));
			    }
			
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
			if(rs.next()) { //Si la SELECT SÍ ha devuelto información
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
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {			
			
			pStmt.setString(1, user);
			pStmt.setString(2, contrasenia);
			ResultSet rs = pStmt.executeQuery();			

			if (rs.next()) {
				c = new Cliente(rs.getString("username"), rs.getString("contrasenia"),
						rs.getString("nombre"), rs.getString("apellidos"), 
						rs.getString("dni"), rs.getString("correo"), null);
			}
			
			rs.close();
			
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
	            int idButaca = rs.getInt("id_butaca");
	            int idPelicula = rs.getInt("id_peli");
	            String horario = rs.getString("horario");
	            
	            Butaca butaca = obtenerButacaporId(idButaca);
	            Pelicula pelicula = obtenerPeliculaporID(idPelicula);
	            
	            e = new Entrada(butaca, pelicula.getId(), pelicula.getTitulo(), pelicula.getId_sala(), 10, horario);
	            entradas.add(e);
	            
	        }
            logger.info(String.format("Se ha encontrado correctamente la entrada"));
	        rs.close();
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
			
			logger.info(String.format("Se ha obtenido la lista de butacas."));			
		} catch (Exception ex) {
			logger.warning(String.format("Error al obtener la lista de butacas: %s", ex.getMessage()));						
		}		
		
		return butacas;
	}
	public boolean existeButacaHorario(int id_butaca, String horario) {
		boolean existe = false;
		String sql = "SELECT * FROM Butaca_Horario WHERE id_butaca = ? and horario = ?";
	    try (Connection con = DriverManager.getConnection(connectionString);
				PreparedStatement ps = con.prepareStatement(sql)){

			ps.setInt(1, id_butaca);
			ps.setString(2, horario);
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
	                    horarios.put(h, p);
	                }
	            }
				
			}
			
			rs.close();
			
			logger.info(String.format("Se ha obtenido los horarios."));			
		} catch (Exception ex) {
			logger.warning(String.format("Error al obtener los horarios: %s", ex.getMessage()));						
		}		
		
		return horarios;
	}
	public void reservarButaca(Butaca b ,String horario) {
		String sql = "INSERT INTO Butaca_Horario (id_butaca,estado,horario) VALUES (?,?,?)\n";
		
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement pStmt = con.prepareStatement(sql)) {

			pStmt.setInt(1, b.getId());
			pStmt.setInt(2, 1);
			pStmt.setString(3, horario);

			if (pStmt.executeUpdate() != 1) {					
				logger.warning(String.format("No se ha podido actualizar la Butaca con id: %d", b.getId()));
			} else {					
				logger.info(String.format("Se ha actualizado la Butaca con id: %d", b.getId()));
			}			
		} catch (Exception ex) {
			logger.warning(String.format("Error al actualizar la Butaca con id: %d", b.getId()));
		}				
	}
	public void CancelaReservaButaca(Butaca b,String horario) {
		String sql = "DELETE FROM Butaca_Horario WHERE id_butaca = ? and horario = ?;";
		
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement pStmt = con.prepareStatement(sql)) {

			pStmt.setInt(1, b.getId());
			pStmt.setString(2, horario);
				
			if (pStmt.executeUpdate() != 1) {					
				logger.warning(String.format("No se ha podido actualizar la Butaca con id: %d", b.getId()));
			} else {					
				logger.info(String.format("Se ha actualizado la Butaca con id: %d", b.getId()));
			}			
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
			
			logger.info(String.format("Se ha encontrado correctamente la pelicula con titulo: %s", titulo));			
		} catch (Exception e) {
			logger.warning(String.format("Error al encontrar la pelicula con titulo: %s", titulo));						
		}		
		
		return p;
	}
	public void meterEntrada(int id_butaca, int id_peli, String horario, int id_carrito) {
		String sql = "INSERT INTO Entrada (id_butaca, id_carrito, id_peli, horario) VALUES (?,?,?,?)\n";
		
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement Stmt = con.prepareStatement(sql)) {
			Stmt.setInt(1, id_butaca);
			Stmt.setInt(2, id_carrito);
			Stmt.setInt(3, id_peli);
			Stmt.setString(4, horario);
			
			if (!Stmt.execute()) {
				logger.info(String.format("Entrada insertada en la BBDD"));
		    }
			
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
	        }
	    } catch (Exception e) {
	        logger.warning(String.format("Error al encontrar el Carrito para el dni %s: %s", dni, e.getMessage()));
	    }
	    return id_carrito;
	}
	public int numeroEntradas(String dni, String titulo, String horario) {
	    int totalEntradas = 0;
	    try (Connection con = DriverManager.getConnection(connectionString)) {
	        // Obtén el ID del carrito asociado al cliente
	        int idCarrito = obtenerIdCarritoPorDni(dni);
	        if (idCarrito != 0) {
	            // Obtén el ID de la película con el título dado
	            int idPelicula = obtenerIdPeliculaPorTitulo(titulo);
	            if (idPelicula != 0) {
	                // Contar las entradas en la BD que coincidan con los criterios
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
	    } catch (Exception e) {
	        logger.warning(String.format("Error al obtener el ID de la película con título: %s. Detalles: %s", titulo, e.getMessage()));
	    }
	    return idPelicula;
	}
	public int contarEntradas(int idCarrito, int idPelicula, String horario) {
	    int cantidad = 0;
	    String sql = "SELECT COUNT(*) AS total FROM Entrada WHERE id_carrito = ? AND id_peli = ? AND horario = ?";
	    try (Connection con = DriverManager.getConnection(connectionString);
	         PreparedStatement pStmt = con.prepareStatement(sql)) {
	        pStmt.setInt(1, idCarrito);
	        pStmt.setInt(2, idPelicula);
	        pStmt.setString(3, horario);
	        ResultSet rs = pStmt.executeQuery();
	        if (rs.next()) {
	            cantidad = rs.getInt("total");
	        }
	        rs.close();
	    } catch (Exception e) {
	        logger.warning(String.format("Error al contar entradas para carrito: %d, película: %d, horario: %s. Detalles: %s",
	                                     idCarrito, idPelicula, horario, e.getMessage()));
	    }
	    return cantidad;
	}
	public void comprarCarrito(String dni) {
		if (properties.get("cleanButacas").equals("true")) {	
			String sql1 = "DELETE FROM Carrito WHERE cliente_dni = ?;";
			
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt1 = con.prepareStatement(sql1)) {
				pStmt1.setString(1, dni);
		        if (!pStmt1.execute()) {
		        	logger.info("Se han comprado las entradas");
		        }
			} catch (Exception ex) {
				logger.warning(String.format("Error al comprar las Entradas: %s", ex.getMessage()));
			}
		}
	}
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
	        } catch (Exception e) {
	            con.rollback();
	            throw e;
	        }
	    } catch (Exception ex) {
	        logger.warning(String.format("Error al comprar las Entradas: %s", ex.getMessage()));
	    }
	}
	public void comprarCarrito3(String dni) {
	    String sql1 = "DELETE FROM Butaca_Horario WHERE id_butaca IN ("
	                + "SELECT id_butaca FROM Entrada WHERE id_carrito = (SELECT id FROM Carrito WHERE cliente_dni = ?));";
	    String sql2 = "DELETE FROM Entrada WHERE id_carrito = (SELECT id FROM Carrito WHERE cliente_dni = ?);";
	    String sql3 = "DELETE FROM Carrito WHERE cliente_dni = ?;";
	    
	    try (Connection con = DriverManager.getConnection(connectionString)) {
	        con.setAutoCommit(false);

	        try (PreparedStatement pStmt1 = con.prepareStatement(sql1);
	             PreparedStatement pStmt2 = con.prepareStatement(sql2);
	             PreparedStatement pStmt3 = con.prepareStatement(sql3)) {

	            pStmt1.setString(1, dni);
	            int butacasHorarioEliminadas = pStmt1.executeUpdate();
	            logger.info(String.format("%d butacas_horario eliminadas", butacasHorarioEliminadas));

	            pStmt2.setString(1, dni);
	            int entradasEliminadas = pStmt2.executeUpdate();
	            logger.info(String.format("%d entradas eliminadas", entradasEliminadas));

	            pStmt3.setString(1, dni);
	            int carritosEliminados = pStmt3.executeUpdate();
	            logger.info(String.format("%d carritos eliminados", carritosEliminados));

	            con.commit();
	        } catch (Exception e) {
	            con.rollback();
	            throw e;
	        }
	    } catch (Exception ex) {
	        logger.warning(String.format("Error al comprar las Entradas: %s", ex.getMessage()));
	    }
	}

}
