package test;

import org.junit.jupiter.api.*;
import db.BBDD;
import java.io.FileReader;
import java.sql.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
//IAG ChatGPT
//Test de crear la base de datos
class CrearBBDD {
    private BBDD bbdd;
    private Connection connection;
    private String connectionString;

    @BeforeEach
    void setUp() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileReader("resource/conf/fichero.properties"));
        connectionString = properties.getProperty("connection");
        bbdd = new BBDD();
        connection = DriverManager.getConnection(connectionString);
    }

    @Test
    @DisplayName("Test creación de tablas usando connectionString del archivo properties")
    void testCrearBBDD() throws Exception {
        bbdd.crearBBDD();

        String[] expectedTables = {
            "Cliente", "Pelicula", "Entrada", "Carrito", "Butaca",
            "Butaca_Horario", "InicioSesion", "Horarios", "EntradasCompradas"
        };

        for (String tableName : expectedTables) {
            try (PreparedStatement ps = connection.prepareStatement(
                "SELECT name FROM sqlite_master WHERE type='table' AND name=?")) {
                ps.setString(1, tableName);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "La tabla " + tableName + " no fue creada.");
                }
            }
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
