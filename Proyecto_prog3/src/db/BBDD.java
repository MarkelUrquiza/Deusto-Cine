package db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import domain.Cliente;

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
	                + " FOREIGN KEY(id_peli) REFERENCES Pelicula(id) ON DELETE CASCADE\n);";

	        String sql4 = "CREATE TABLE IF NOT EXISTS Carrito (\n"
	                + " cliente_dni TEXT,\n"
	                + " entrada_id INTEGER,\n"
	                + " numero_entradas INTEGER NOT NULL DEFAULT 0,\n"
	                + " PRIMARY KEY(cliente_dni, entrada_id),\n"
	                + " FOREIGN KEY(cliente_dni) REFERENCES Cliente(dni) ON DELETE CASCADE,\n"
	                + " FOREIGN KEY(entrada_id) REFERENCES Entrada(id) ON DELETE CASCADE\n);";

	        try (Connection con = DriverManager.getConnection(connectionString);
	             PreparedStatement pStmt1 = con.prepareStatement(sql1);
	             PreparedStatement pStmt2 = con.prepareStatement(sql2);
	             PreparedStatement pStmt3 = con.prepareStatement(sql3);
	             PreparedStatement pStmt4 = con.prepareStatement(sql4)) {

	        	if (!pStmt1.execute()) logger.info("Tabla Cliente creada o ya existía.");
	        	if (!pStmt2.execute()) logger.info("Tabla Pelicula creada o ya existía.");
	        	if (!pStmt3.execute()) logger.info("Tabla Entrada creada o ya existía.");
	        	if (!pStmt4.execute()) logger.info("Tabla Carrito creada o ya existía.");

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
	
	        StringBuilder insertPelicula = new StringBuilder("INSERT OR IGNORE INTO Pelicula (titulo, director, tipo, duracion, rutafoto, horarios, tresd) VALUES ");
	        insertPelicula.append("('Inception', 'Christopher Nolan', 'ACCION', 2.5, 'resource/images/cine.jpg', '2024-10-11 14:00,2024-10-11 18:00,2024-10-12 20:00', 1),")
	                .append("('The Godfather', 'Francis Ford Coppola', 'DRAMA', 2.9, 'resource/images/cine.jpg', '2024-10-11 15:00,2024-10-12 17:00', 0),")
	                .append("('Interstellar', 'Christopher Nolan', 'CIENCIA_FICCION', 3.0, 'resource/images/cine.jpg', '2024-10-11 16:00,2024-10-12 19:00,2024-10-13 20:00', 1),")
	                .append("('Titanic', 'James Cameron', 'ROMANCE', 3.2, 'resource/images/cine.jpg', '2024-10-11 17:00,2024-10-12 21:00', 0),")
	                .append("('Pulp Fiction', 'Quentin Tarantino', 'CRIMEN', 2.8, 'resource/images/cine.jpg', '2024-10-11 20:00,2024-10-12 22:00', 0),")
	                .append("('The Dark Knight', 'Christopher Nolan', 'ACCION', 2.7, 'resource/images/cine.jpg', '2024-10-12 14:00,2024-10-13 16:00', 1),")
	                .append("('Forrest Gump', 'Robert Zemeckis', 'DRAMA', 2.4, 'resource/images/cine.jpg', '2024-10-12 15:00,2024-10-13 18:00', 0),")
	                .append("('Avatar', 'James Cameron', 'CIENCIA_FICCION', 2.9, 'resource/images/cine.jpg', '2024-10-12 17:00,2024-10-13 19:00', 1),")
	                .append("('La La Land', 'Damien Chazelle', 'MUSICAL', 2.3, 'resource/images/cine.jpg', '2024-10-12 14:00,2024-10-13 17:00', 0),")
	                .append("('Gladiator', 'Ridley Scott', 'ACCION', 2.6, 'resource/images/cine.jpg', '2024-10-12 18:00,2024-10-13 21:00', 0);");
	
	        String insertEntrada = "INSERT OR IGNORE INTO Entrada (id_butaca, id_peli) VALUES\n"
	                + "(1, 1), (2, 2), (3, 3), (4, 4), (1, 5),\n"
	                + "(2, 6), (3, 7), (4, 8), (1, 9), (2, 10);";
	
	        String insertCarrito = "INSERT OR IGNORE INTO Carrito (cliente_dni, entrada_id, numero_entradas) VALUES\n"
	                + "('11111111A', 1, 2), ('22222222B', 2, 1), ('33333333C', 3, 3),\n"
	                + "('44444444D', 4, 1), ('55555555E', 5, 4), ('66666666F', 6, 2),\n"
	                + "('77777777G', 7, 3), ('88888888H', 8, 1), ('99999999I', 9, 2),\n"
	                + "('00000000J', 10, 3);";
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

}
