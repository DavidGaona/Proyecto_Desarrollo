package connection;

//import at.favre.lib.crypto.bcrypt.BCrypt;

import at.favre.lib.crypto.bcrypt.BCrypt;
import model.Client;
import model.User;
import utilities.AlertBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;


public class DbManager {
    private DBconnect fachada;
    private Connection conexion;

    public DbManager(String usuario, String password, String nombreBaseDeDatos, String host) {
        fachada = new DBconnect(usuario, password, nombreBaseDeDatos, host);
        conexion = null;
    }

    public int saveNewClient(Client client) {
        String sql_guardar;
        int numFilas;
        sql_guardar = "INSERT INTO public.cliente(nombre_cliente, apellidos_cliente, documento_id_cliente, email_cliente, direccion_cliente, tipo_cliente, tipo_documento)" +
                " VALUES ('" + client.getName() + "', '" + client.getLastName() + "', '" +
                client.getDocumentId() + "', '" + client.getEmail() + "', '" + client.getDirection() + "'," +
                client.getType() + ", " + client.getDocumentType() + ")" + " ON CONFLICT (id_cliente) DO NOTHING";
        try {
            Statement sentencia = conexion.createStatement();
            numFilas = sentencia.executeUpdate(sql_guardar);
            AlertBox.display("Operación exitosa", "Cliente creado", "");
            return numFilas;

        } catch (SQLException e) {
            AlertBox.display("Error", " Error al crear el cliente", "");
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return -1;
    }

    public void editClient(Client client) {
        int numRows;
        String sql_update = "UPDATE public.cliente" +
                " SET nombre_cliente = '" + client.getName() + "', apellidos_cliente = '" + client.getLastName() +
                "', documento_id_cliente = '" + client.getDocumentId() + "', email_cliente = '" + client.getEmail() +
                "', direccion_cliente = '" + client.getDirection() + "', tipo_cliente = " + client.getType() +
                ", tipo_documento = " + client.getDocumentType() +
                " WHERE documento_id_cliente = '" + client.getDocumentId() + "'";
        try {
            Statement sentencia = conexion.createStatement();
            numRows = sentencia.executeUpdate(sql_update);
            AlertBox.display("Operación exitosa", "Cliente editado", "");
            System.out.println("up " + numRows);
        } catch (SQLException e) {
            AlertBox.display("Error", " Error al editar al cliente", "");
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Client loadClient(String documentNumber) {

        String sql_select = "SELECT nombre_cliente, apellidos_cliente, tipo_documento, documento_id_cliente," +
                " email_cliente, direccion_cliente, tipo_cliente " +
                "FROM public.cliente WHERE documento_id_cliente = '" + documentNumber + "'";
        try {

            System.out.println("consultando en la base de datos");
            Statement sentencia = conexion.createStatement();
            ResultSet tabla = sentencia.executeQuery(sql_select);
            tabla.next();
            Client client = new Client(
                    tabla.getString(1),
                    tabla.getString(2),
                    tabla.getShort(3),
                    tabla.getString(4),
                    tabla.getString(5),
                    tabla.getString(6),
                    tabla.getShort(7)
            );
            System.out.println(client.getName());
            AlertBox.display("Operación exitosa", "Cliente Encontrado", "");
            return client;
        } catch (SQLException e) {
            System.out.println(e);
            AlertBox.display("Error", "Problema en la base de datos", "tabla: cliente");
            //System.out.println("Problema en la base de datos tabla: cliente");
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("ERROR Fatal en la base de datos");
        }

        return new Client("", "", (short) -1, "", "", "", (short) -1);
    }

    public int loginUser(String documento_id_usuario, String password) {
        String sql_select = "SELECT pass_usuario,tipo_usuario" +
                " FROM public.usuario WHERE documento_id_usuario = '" + documento_id_usuario + "'";

        try {
            System.out.println("consultando en la base de datos");
            Statement sentencia = conexion.createStatement();
            ResultSet tabla = sentencia.executeQuery(sql_select);
            String hashedPasswordFromBD;
            short user_type;
            if (!tabla.next()) {
                System.out.println("No se pudo encontrar el usuario");
                return -1;
            }
            hashedPasswordFromBD = tabla.getString(1);
            user_type = tabla.getShort(2);
            final BCrypt.Result resultCompare = BCrypt.verifyer().verify(password.toCharArray(), hashedPasswordFromBD);
            if (!resultCompare.verified) {
                System.out.println("CONTRASEÑA INVALIDA");
                return -1;
            }
            return user_type;
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("ERROR Fatal en la base de datos");
        }

        return -1;

    }

    public int saveNewUser(User user) {
        int numRows;
        String saveQuery;
        final String hashWillBeStored = BCrypt.withDefaults().hashToString(12, user.getDocumentIdNumber().toCharArray());
        saveQuery = "INSERT INTO public.usuario(nombre_usuario, apellidos_usuario, documento_id_usuario, tipo_usuario, estado_usuario, pass_usuario, document_type)" +
                " VALUES('" + user.getName() + "','" + user.getLastName() + "','" + user.getDocumentIdNumber() + "'," + user.getType() +
                "," + user.getState() + ",'" + hashWillBeStored + "', " + user.getDocumentType() + ")" + " ON CONFLICT (id_usuario) DO NOTHING";
        try {
            Statement sentencia = conexion.createStatement();
            numRows = sentencia.executeUpdate(saveQuery);
            AlertBox.display("Operación exitosa", "Usuario creado", "");
            return numRows;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public void editUser(User user) {
        int numRows;
        String sql_update = "UPDATE public.usuario" +
                " SET nombre_usuario = '" + user.getName() + "', apellidos_usuario = '" + user.getLastName() +
                "', documento_id_usuario = '" + user.getDocumentIdNumber() + "', tipo_cliente = " + user.getType() +
                ", tipo_documento = " + user.getDocumentType() + ", document_type = " + user.getDocumentType() +
                " WHERE documento_id_usuario = '" + user.getDocumentIdNumber() + "'";
        try {
            Statement sentencia = conexion.createStatement();
            numRows = sentencia.executeUpdate(sql_update);
            AlertBox.display("Operación exitosa", "Usuario editado", "");
            System.out.println("up " + numRows);
        } catch (SQLException e) {
            AlertBox.display("Error", " Error al editar al usuario", "");
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
    } //loadUser

    public User loadUser(String documentNumber) {

        String sql_select = "SELECT nombre_usuario, apellidos_usuario, documento_id_usuario, document_type," +
                " tipo_usuario, estado_usuario " +
                "FROM public.usuario WHERE documento_id_usuario = '" + documentNumber + "'";
        try {

            System.out.println("Consultando en la base de datos");
            Statement sentencia = conexion.createStatement();
            ResultSet tabla = sentencia.executeQuery(sql_select);
            tabla.next();
            User user = new User(
                    tabla.getString(1),
                    tabla.getString(2),
                    tabla.getString(3),
                    tabla.getShort(4),
                    tabla.getShort(5),
                    tabla.getBoolean(6)
            );
            System.out.println(user.getName());
            AlertBox.display("Operación exitosa", "Usuario Encontrado", "");
            return user;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            AlertBox.display("Error", "Problema en la base de datos", "tabla: usuario");
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("ERROR Fatal en la base de datos");
        }
        return new User("", "", "", (short) -1, (short) -1, false);
    }

    public void openDBConnection() {
        conexion = fachada.getConnetion();
    }

    public void closeDBConnection() {
        fachada.closeConnection(conexion);
    }

}
