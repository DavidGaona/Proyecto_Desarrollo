package connection;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.*;


public class DBconnect {

    String url, user, password;
    Connection connection = null;
    Statement instruction;
    ResultSet table;

    public DBconnect(String user, String password, String dataBaseName, String host) {
        url = "jdbc:postgresql://" + host + ":5432/" + dataBaseName;
        this.user = user;
        this.password = password;
    }

    public Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.out.println("No se pudo cargar el driver.");
        }
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Conexion  exitosa con la base de datos");
            return connection;
        } catch (SQLException e) {
            System.out.println("Error: No se pudo conectar a la Base de datos" + e.getMessage());
            return null;
        }

    }

    public Connection getconnetion() {
        try {
            if (connection == null || connection.isClosed())
                return this.connect();
        } catch (SQLException e) {
            System.out.println("Error de al momento de conectarse");
        }
        return connection;
    }

    public void closeConnection(Connection c) {
        try {
            if (connection != null || !connection.isClosed()) {
                c.close();
            }

        } catch (SQLException e) {
            System.out.println("No se pudo cerrar la coneccion.");
        }
    }

}
