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
	                + " id_peli INTEGER NOT NULL,\n"
	                + " horario STRING NOT NULL,\n"
	                + " FOREIGN KEY(id_butaca) REFERENCES Butaca(id) ON DELETE CASCADE,\n"
	                + " FOREIGN KEY(id_peli) REFERENCES Pelicula(id) ON DELETE CASCADE\n);";

	        String sql4 = "CREATE TABLE IF NOT EXISTS Carrito (\n"
	                + " cliente_dni TEXT,\n"
	                + " entrada_id INTEGER,\n"
	                + " numero_entradas INTEGER NOT NULL DEFAULT 0,\n"
	                + " PRIMARY KEY(cliente_dni, entrada_id),\n"
	                + " FOREIGN KEY(cliente_dni) REFERENCES Cliente(dni) ON DELETE CASCADE,\n"
	                + " FOREIGN KEY(entrada_id) REFERENCES Entrada(id) ON DELETE CASCADE\n);";
	        
	        String sql5 = "CREATE TABLE IF NOT EXISTS Butaca (\n"
	                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                + " fila INTEGER NOT NULL,\n"
	                + " columna INTEGER NOT NULL,\n"
	                + " vip INTEGER NOT NULL,\n"
	                + " estado INTEGER NOT NULL,"
	                + " id_sala INTEGER NOT NULL\n);";

	        try (Connection con = DriverManager.getConnection(connectionString);
	             PreparedStatement pStmt1 = con.prepareStatement(sql1);
	             PreparedStatement pStmt2 = con.prepareStatement(sql2);
	             PreparedStatement pStmt3 = con.prepareStatement(sql3);
	             PreparedStatement pStmt4 = con.prepareStatement(sql4);
	        	 PreparedStatement pStmt5 = con.prepareStatement(sql5)) {

	        	if (!pStmt1.execute()) logger.info("Tabla Cliente creada o ya existía.");
	        	if (!pStmt2.execute()) logger.info("Tabla Pelicula creada o ya existía.");
	        	if (!pStmt3.execute()) logger.info("Tabla Entrada creada o ya existía.");
	        	if (!pStmt4.execute()) logger.info("Tabla Carrito creada o ya existía.");
	        	if (!pStmt5.execute()) logger.info("Tabla Butaca creada o ya existía.");

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

	        StringBuilder insertPelicula = new StringBuilder("INSERT OR IGNORE INTO Pelicula (id_sala, titulo, director, tipo, duracion, rutafoto, horarios, tresd) VALUES ");
	        insertPelicula.append("(1, 'Inception', 'Christopher Nolan', 'ACCION', 2.5, 'resource/images/cine.jpg', '2024-10-11 16:00,2024-10-11 18:00', 1),")
	                .append("(2, 'The Godfather', 'Francis Ford Coppola', 'DRAMA', 2.9, 'resource/images/cine.jpg', '2024-10-11 17:00,2024-10-11 19:00', 0),")
	                .append("(3, 'Dune', 'Denis Villeneuve', 'CIENCIA_FICCION', 2.8, 'resource/images/cine.jpg', '2024-10-11 16:00,2024-10-11 18:00', 1),")
	                .append("(1, 'Interstellar', 'Christopher Nolan', 'CIENCIA_FICCION', 3.0, 'resource/images/cine.jpg', '2024-10-12 20:00,2024-10-12 22:00', 1),")
	                .append("(2, 'Titanic', 'James Cameron', 'ROMANCE', 3.2, 'resource/images/cine.jpg', '2024-10-12 20:00,2024-10-13 22:00', 0),")
	                .append("(3, 'Pulp Fiction', 'Quentin Tarantino', 'CRIMEN', 2.8, 'resource/images/cine.jpg', '2024-10-12 14:00,2024-10-12 16:00', 0),")
	                .append("(4, 'The Matrix', 'The Wachowskis', 'ACCION', 2.7, 'resource/images/cine.jpg', '2024-10-11 16:00,2024-10-11 18:00', 1),")
	                .append("(1, 'The Dark Knight', 'Christopher Nolan', 'ACCION', 2.7, 'resource/images/cine.jpg', '2024-10-13 16:00,2024-10-13 18:00', 1),")
	                .append("(4, 'Spirited Away', 'Hayao Miyazaki', 'ANIMACION', 2.1, 'resource/images/cine.jpg', '2024-10-12 14:00,2024-10-12 16:00', 0),")
	                .append("(2, 'Forrest Gump', 'Robert Zemeckis', 'DRAMA', 2.4, 'resource/images/cine.jpg', '2024-10-13 18:00,2024-10-13 20:00', 0),")
	                .append("(3, 'Avatar', 'James Cameron', 'CIENCIA_FICCION', 2.9, 'resource/images/cine.jpg', '2024-10-12 18:00,2024-10-12 20:00', 1),")
	                .append("(4, 'Coco', 'Lee Unkrich', 'ANIMACION', 2.0, 'resource/images/cine.jpg', '2024-10-13 10:00,2024-10-13 12:00', 0),")
	                .append("(2, 'La La Land', 'Damien Chazelle', 'MUSICAL', 2.3, 'resource/images/cine.jpg', '2024-10-12 16:00,2024-10-12 18:00', 0),")
	                .append("(3, 'Gladiator', 'Ridley Scott', 'ACCION', 2.6, 'resource/images/cine.jpg', '2024-10-13 14:00,2024-10-13 16:00', 0);");

	        String insertEntrada = "INSERT OR IGNORE INTO Entrada (id_butaca, id_peli, horario) VALUES\n"
	                + "(1, 1, '2024-10-11 16:00'),\n"
	                + "(2, 1, '2024-10-11 18:00'),\n"
	                + "(3, 4, '2024-10-12 20:00'),\n"
	                + "(4, 4, '2024-10-12 22:00'),\n"
	                + "(5, 8, '2024-10-13 16:00'),\n"
	                + "(6, 8, '2024-10-13 18:00'),\n"
	                + "(7, 2, '2024-10-11 17:00'),\n"
	                + "(8, 2, '2024-10-11 19:00'),\n"
	                + "(9, 5, '2024-10-12 20:00'),\n"
	                + "(10, 5, '2024-10-13 22:00'),\n"
	                + "(11, 10, '2024-10-13 18:00'),\n"
	                + "(12, 14, '2024-10-12 16:00'),\n"
	                + "(13, 3, '2024-10-11 16:00'),\n"
	                + "(14, 3, '2024-10-11 18:00'),\n"
	                + "(15, 6, '2024-10-12 14:00'),\n"
	                + "(16, 6, '2024-10-12 16:00'),\n"
	                + "(17, 12, '2024-10-12 18:00'),\n"
	                + "(18, 12, '2024-10-12 20:00'),\n"
	                + "(19, 15, '2024-10-13 14:00'),\n"
	                + "(20, 15, '2024-10-13 16:00');";

	        String insertCarrito = "INSERT OR IGNORE INTO Carrito (cliente_dni, entrada_id, numero_entradas) VALUES\n"
	                + "('11111111A', 1, 1),\n"
	                + "('22222222B', 2, 1),\n"
	                + "('33333333C', 3, 1),\n"
	                + "('44444444D', 4, 1),\n"
	                + "('55555555E', 5, 1),\n"
	                + "('66666666F', 6, 1),\n"
	                + "('77777777G', 7, 1),\n"
	                + "('88888888H', 8, 1),\n"
	                + "('99999999I', 9, 1),\n"
	                + "('00000000J', 10, 1),\n"
	                + "('11111111A', 11, 1),\n"
	                + "('22222222B', 12, 1),\n"
	                + "('33333333C', 13, 1),\n"
	                + "('44444444D', 14, 1),\n"
	                + "('55555555E', 15, 1),\n"
	                + "('66666666F', 16, 1),\n"
	                + "('77777777G', 17, 1),\n"
	                + "('88888888H', 18, 1),\n"
	                + "('99999999I', 19, 1),\n"
	                + "('00000000J', 20, 1);";

	        try (Connection con = DriverManager.getConnection(connectionString);
	             PreparedStatement insertStmt1 = con.prepareStatement(insertCliente);
	             PreparedStatement insertStmt2 = con.prepareStatement(insertPelicula.toString());
	             PreparedStatement insertStmt3 = con.prepareStatement(insertEntrada);
	             PreparedStatement insertStmt4 = con.prepareStatement(insertCarrito)) {

	            if (!insertStmt1.execute() && !insertStmt2.execute() && !insertStmt3.execute() && !insertStmt4.execute()) {
	                logger.info("Se han insertado los datos correctamente");
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
			Entrada entrada;
			boolean enc = false;
			while (rs.next()) {
				entrada = obtenerEntradaPorID(rs.getInt("entrada_id"));
				for(Entrada e: carrito.keySet()) {
					if (e.equals(entrada)) {
						enc = true;
					}
				}
				if (!enc) {
					carrito.put(entrada, rs.getInt("numero_entradas"));
				}
			}
			
			rs.close();
			
			logger.info(String.format("Se ha cargado el carrito del usuario con DNI: %s",dni));			
		} catch (Exception e) {
			logger.warning(String.format("Error recuperar los comics: %s", e.getMessage()));						
		}		
		
		return carrito;
	}
	public Entrada obtenerEntradaPorID(int id) {
		Entrada e = null;
		String sql = "SELECT * FROM Entrada WHERE id = ?";
		
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {			
			
			pStmt.setInt(1, id);
			ResultSet rs = pStmt.executeQuery();			

			if (rs.next()) {
				Pelicula p = obtenerPeliculaporID(id);
				e = new Entrada(obtenerButacaporId(id), p.getId(),p.getTitulo() , id, id, rs.getString("horario"));
			}
			
			rs.close();
			
			logger.info(String.format("Se ha encontrado correctamente la entrada con id: %s", e.getId()));			
		} catch (Exception exception) {
			logger.warning(String.format("Error al encontrar la entrada con id: %s", e.getId()));						
		}		
		
		return e;
	}
	public Butaca obtenerButacaporId(int id) {
		
		String sql = "SELECT * FROM Butaca WHERE id = ?";
		Butaca b = null;
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {			
			
			pStmt.setInt(1, id);
			ResultSet rs = pStmt.executeQuery();			

			if (rs.next()) {
				boolean vip,estado;
				if (rs.getInt("vip") == 1) {
					vip = true;
				} else vip = false;
				if (rs.getInt("estado") == 1) {
					estado = true;
				} else {estado = false;}
				b = new Butaca(rs.getInt("fila"), Columna.valueOf(rs.getString("columna")) , vip, estado , rs.getInt("id_sala"));
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
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {			
			
			pStmt.setInt(1, id);
			ResultSet rs = pStmt.executeQuery();			

			if (rs.next()) {
				boolean tresd;
				if (rs.getInt("tresd") == 1) {
					tresd = true;
				} else {
					tresd = false;
				}
				p = new Pelicula(rs.getString("titulo"), Genero.valueOf(rs.getString("tipo")), 
						rs.getFloat("duracion"), rs.getString("director"), 
						rs.getString("rutafoto"), tresd, rs.getString("horarios"));
			}
			
			rs.close();
			
			logger.info(String.format("Se ha encontrado correctamente la Butaca con id: %d", id));			
		} catch (Exception e) {
			logger.warning(String.format("Error al encontrar la Butaca con id: %d", id));						
		}		
		
		return p;
	}
	public int obtenerButacaporIddeEntrada(int id) {
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
		//Sólo se borran los datos si la propiedad cleanBBDD es true
		if (properties.get("cleanButacas").equals("true")) {	
			String sql1 = "DELETE FROM Butaca;";
			
	        //Se abre la conexión y se crea un PreparedStatement para borrar los datos de cada tabla
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt1 = con.prepareStatement(sql1)) {
				
				//Se ejecutan las sentencias de borrado de las tablas
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
	            boolean estado = Boolean.parseBoolean(campos[3]);
	            int idSala = Integer.parseInt(campos[4]);
	            
	            // Crear la butaca y añadirla a la lista
	            Butaca butaca = new Butaca(fila, columna, vip, estado, idSala);
	            butacas.add(butaca);
	        }
	        
	        sc.close();
	    } catch (Exception ex) {
	        logger.warning(String.format("Error leyendo Butacas.txt: %s", ex.getMessage()));
	    }
	    
	    return butacas;
	}
	public void meterButaca(Butaca b) {
		String sql = "INSERT INTO Butaca (id, fila, columna, vip, estado, id_sala) VALUES (?, ?, ?, ?, ?, ?);";
		
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement Stmt = con.prepareStatement(sql)) {
			int estado,vip;
			if (b.isVip()) {
				vip = 1;
			} else vip = 0; 
			if (b.isEstado()) {
				estado = 1;
			} else estado = 0;
			
				Stmt.setInt(1, b.getId());
				Stmt.setInt(2, b.getFila());
				Stmt.setString(3, b.getColumna().toString());
				Stmt.setInt(4, vip);
				Stmt.setInt(5, estado);
				Stmt.setInt(6, b.getId_sala());
				
				
				if (!Stmt.execute()) {
					logger.info(String.format("Butaca con id: %d insertado en la BBDD", b.getId()));
			    }
			
		} catch (Exception ex) {
			logger.warning(String.format("Error al insertar nueva butaca: %s", ex.getMessage()));
		}			
		
	}
	public List<Butaca> getButacas(int id_sala) {
		List<Butaca> butacas = new ArrayList<>();
		String sql = "SELECT * FROM Butaca WHERE id_sala = ?";
		
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {			
			pStmt.setInt(1, id_sala);
			
			ResultSet rs = pStmt.executeQuery();
			Butaca b;
			
			while (rs.next()) {
				boolean vip,estado;
				if (rs.getInt("vip") == 1) {
					vip = true;
				} else vip = false;
				if (rs.getInt("estado") == 1) {
					estado = true;
				} else {estado = false;}
				b = new Butaca(rs.getInt("fila"), Columna.valueOf(rs.getString("columna")) , vip, estado , rs.getInt("id_sala"));
				butacas.add(b);
			}
			
			rs.close();
			
			logger.info(String.format("Se ha obtenido la lista de butacas."));			
		} catch (Exception ex) {
			logger.warning(String.format("Error al obtener la lista de butacas: %s", ex.getMessage()));						
		}		
		
		return butacas;
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
				p = new Pelicula(rs.getString("titulo"), Genero.valueOf(rs.getString("tipo")), 
						rs.getFloat("duracion"), rs.getString("director"), 
						rs.getString("rutafoto"), tresd, rs.getString("horarios"));
				String[] campos = rs.getString("horarios").split(",");
	            for (String h : campos) { 
	                if (!horarios.containsKey(h)) { // Evitar sobrescribir horarios existentes
	                    horarios.put(h, p);
	                }
	            }
				
			}
			
			rs.close();
			
			logger.info(String.format("Se ha obtenido la lista de butacas."));			
		} catch (Exception ex) {
			logger.warning(String.format("Error al obtener la lista de butacas: %s", ex.getMessage()));						
		}		
		
		return horarios;
	}
	

}
