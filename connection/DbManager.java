package connection;

//import at.favre.lib.crypto.bcrypt.BCrypt;

import at.favre.lib.crypto.bcrypt.BCrypt;
import model.Client;
import model.User;
import utilities.AlertBox;
import view.Login;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;


public class DbManager {
    private DBconnect fachada;
    private Connection connection;

    public DbManager(String usuario, String password, String nombreBaseDeDatos, String host) {
        fachada = new DBconnect(usuario, password, nombreBaseDeDatos, host);
        connection = null;
    }

    public int saveNewClient(Client client) {
        String sql_guardar;
        int numFilas;
        sql_guardar = "INSERT INTO public.cliente(nombre_cliente, apellidos_cliente, documento_id_cliente, email_cliente, direccion_cliente, tipo_cliente, tipo_documento)" +
                " VALUES ('" + client.getName() + "', '" + client.getLastName() + "', '" +
                client.getDocumentId() + "', '" + client.getEmail() + "', '" + client.getDirection() + "'," +
                client.getType() + ", " + client.getDocumentType() + ")" + " ON CONFLICT (id_cliente) DO NOTHING";
        try {
            Statement statement = connection.createStatement();
            numFilas = statement.executeUpdate(sql_guardar);
            AlertBox.display("Operación exitosa", "Cliente creado", "");
            return numFilas;

        } catch (SQLException e) {
            AlertBox.display("Error", " Error al crear el cliente", "");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
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
            Statement statement = connection.createStatement();
            numRows = statement.executeUpdate(sql_update);
            AlertBox.display("Operación exitosa", "Cliente editado", "");
            System.out.println("up " + numRows);
        } catch (SQLException e) {
            AlertBox.display("Error", " Error al editar al cliente", "");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public Client loadClient(String documentNumber) {

        String sql_select = "SELECT nombre_cliente, apellidos_cliente, tipo_documento, documento_id_cliente," +
                " email_cliente, direccion_cliente, tipo_cliente " +
                "FROM public.cliente WHERE documento_id_cliente = '" + documentNumber + "'";
        try {

            System.out.println("consultando en la base de datos");
            Statement statement = connection.createStatement();
            ResultSet tabla = statement.executeQuery(sql_select);
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
            System.out.println(e.getMessage());
            AlertBox.display("Error", "Cliente no encontrado", "");
            //System.out.println("Problema en la base de datos tabla: cliente");
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("ERROR Fatal en la base de datos");
        }

        return new Client("", "", (short) -1, "", "", "", (short) -1);
    }

    public int loginUser(String documento_id_usuario, String password) {
        String sql_select = "SELECT pass_usuario, tipo_usuario, estado_usuario, up_to_date_password" +
                " FROM public.usuario WHERE documento_id_usuario = '" + documento_id_usuario + "'";

        try {
            System.out.println("consultando en la base de datos");
            Statement statement = connection.createStatement();
            ResultSet table = statement.executeQuery(sql_select);
            String hashedPasswordFromBD;
            short user_type;
            if (!table.next()) {
                System.out.println("No se pudo encontrar el usuario");
                return -1;
            }
            hashedPasswordFromBD = table.getString(1);
            user_type = table.getShort(2);
            final BCrypt.Result resultCompare = BCrypt.verifyer().verify(password.toCharArray(), hashedPasswordFromBD);
            if (!resultCompare.verified) {
                System.out.println("CONTRASEÑA INVALIDA");
                return -1;
            }

            if (!table.getBoolean(3))
                return -2;

            if(!table.getBoolean(4))
                return 3;
            return user_type;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("ERROR Fatal en la base de datos");
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
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
            Statement statement = connection.createStatement();
            numRows = statement.executeUpdate(saveQuery);
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
                "', documento_id_usuario = '" + user.getDocumentIdNumber() + "', tipo_usuario = " + user.getType() +
                ", document_type = " + user.getDocumentType() + ", estado_usuario = " + user.getState() +
                " WHERE documento_id_usuario = '" + user.getDocumentIdNumber() + "'";
        try {
            Statement statement = connection.createStatement();
            numRows = statement.executeUpdate(sql_update);
            AlertBox.display("Operación exitosa", "Usuario editado", "");
            System.out.println("up " + numRows);
        } catch (SQLException e) {
            AlertBox.display("Error", " Error al editar al usuario", "");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    } //loadUser

    public User loadUser(String documentNumber) {

        String sql_select = "SELECT nombre_usuario, apellidos_usuario, documento_id_usuario, document_type," +
                " tipo_usuario, estado_usuario " +
                "FROM public.usuario WHERE documento_id_usuario = '" + documentNumber + "'";
        try {

            System.out.println("Consultando en la base de datos");
            Statement statement = connection.createStatement();
            ResultSet tabla = statement.executeQuery(sql_select);
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
            AlertBox.display("Error", "Usuario no encontrado", "");
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("ERROR Fatal en la base de datos");
        }
        return new User("", "", "", (short) -1, (short) -1, false);
    }

    public boolean checkPassword(String documentNumber, String password){
        String sql_select = "SELECT pass_usuario" +
                " FROM public.usuario WHERE documento_id_usuario = '" + documentNumber + "'";

        try {
            System.out.println("consultando en la base de datos user: " + documentNumber);
            Statement statement = connection.createStatement();
            ResultSet table = statement.executeQuery(sql_select);
            String hashedPasswordFromBD;
            if (!table.next()) {
                System.out.println("No se pudo encontrar el usuario");
                return false;
            }
            hashedPasswordFromBD = table.getString(1);
            final BCrypt.Result resultCompare = BCrypt.verifyer().verify(password.toCharArray(), hashedPasswordFromBD);
            if (!resultCompare.verified) {
                System.out.println("CONTRASEÑA INVALIDA");
                return false;
            }

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public void changePassword(String documentNumber, String password){
        final String encryptedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        String sql_update =
                "UPDATE public.usuario " +
                "SET pass_usuario = '" + encryptedPassword + "', up_to_date_password = true " +
                "WHERE documento_id_usuario = '" + documentNumber + "';";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql_update);
            AlertBox.display("Logrado", "La contraseña fue cambiada", "con éxito");
            Login.currentWindow.set(Login.currentWindow.get() + 1);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void openDBConnection() {
        connection = fachada.getConnection();
    }

    public void closeDBConnection() {
        fachada.closeConnection(connection);
    }

}
