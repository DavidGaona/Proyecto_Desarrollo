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
        String sql_insert;
        int numRows;
        sql_insert = "INSERT INTO public.client(client_name, client_last_name, client_document_number, client_email, client_address, client_type, client_document_type)" +
                " VALUES ('" + client.getName() + "', '" + client.getLastName() + "', '" +
                client.getDocumentId() + "', '" + client.getEmail() + "', '" + client.getDirection() + "'," +
                client.getType() + ", " + client.getDocumentType() + ")" + " ON CONFLICT (client_id) DO NOTHING";
        try {
            Statement statement = connection.createStatement();
            numRows = statement.executeUpdate(sql_insert);
            AlertBox.display("Operación exitosa", "Cliente creado", "");
            return numRows;

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
        String sql_update = "UPDATE public.client " +
                "SET client_name = '" + client.getName() + "', client_last_name = '" + client.getLastName() +
                "', client_document_number = '" + client.getDocumentId() + "', client_email = '" + client.getEmail() +
                "', client_address = '" + client.getDirection() + "', client_type = " + client.getType() +
                ", client_document_type = " + client.getDocumentType() +
                " WHERE client_document_number = '" + client.getDocumentId() + "' AND client_document_type = " + client.getDocumentType();
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

    public Client loadClient(String documentNumber, short clientDocumentType) {

        String sql_select = "SELECT client_name, client_last_name, client_document_type," +
                " client_email, client_address, client_type " +
                "FROM public.client WHERE client_document_number = '" + documentNumber + "'"+" AND "+"client_document_type = "+clientDocumentType;
        try {

            System.out.println("consultando en la base de datos");
            Statement statement = connection.createStatement();
            ResultSet tabla = statement.executeQuery(sql_select);
            tabla.next();
            Client client = new Client(
                    tabla.getString(1),
                    tabla.getString(2),
                    tabla.getShort(3),
                    documentNumber,
                    tabla.getString(4),
                    tabla.getString(5),
                    tabla.getShort(6)
            );
            System.out.println(client.getName());
            AlertBox.display("Operación exitosa", "Cliente Encontrado", "");
            return client;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            AlertBox.display("Error", "Cliente no encontrado", "");
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("ERROR Fatal en la base de datos");
        }

        return new Client("", "", (short) -1, "", "", "", (short) -1);
    }

    public int loginUser(String documento_id_usuario, String password) {
        String sql_select = "SELECT user_password, user_type, user_state, up_to_date_password" +
                " FROM public.user WHERE user_document_number = '" + documento_id_usuario + "'";

        try {
            System.out.println("consultando en la base de datos");
            if(connection == null){
                AlertBox.display("Login view","No se pudo establecer conexión","Con el sistema");
                return -1;
            }
            Statement statement = connection.createStatement();
            ResultSet table = statement.executeQuery(sql_select);
            String hashedPasswordFromBD;
            short user_type;
            if (!table.next()) {
                System.out.println("No se pudo encontrar el usuario");
                AlertBox.display("Error", "Contraseña o id incorrectos", "");
                return -1;
            }
            hashedPasswordFromBD = table.getString(1);
            user_type = table.getShort(2);
            final BCrypt.Result resultCompare = BCrypt.verifyer().verify(password.toCharArray(), hashedPasswordFromBD);
            if (!resultCompare.verified) {
                System.out.println("CONTRASEÑA INVALIDA");
                AlertBox.display("Error", "Contraseña o id incorrectos", "");
                return -1;
            }

            if (!table.getBoolean(3)) {
                AlertBox.display("Error", "No tiene permisos para ingresar", "contacte a un administrador");
                return -1;
            }
            if(!table.getBoolean(4))
                return 3;
            return user_type;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("ERROR Fatal en la base de datos");
            AlertBox.display("Error Login","No se pudo establecer conexión","con el sistema");
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        return -1;

    }

    public void saveNewUser(User user) {
        int numRows;
        String saveQuery;
        final String hashWillBeStored = BCrypt.withDefaults().hashToString(12, user.getDocumentIdNumber().toCharArray());
        saveQuery = "INSERT INTO public.user(user_name, user_last_name, user_document_number, user_type, user_state, user_password, user_document_type)" +
                " VALUES('" + user.getName() + "','" + user.getLastName() + "','" + user.getDocumentIdNumber() + "'," + user.getType() +
                "," + user.getState() + ",'" + hashWillBeStored + "', " + user.getDocumentType() + ")" + " ON CONFLICT (user_id) DO NOTHING";
        try {
            Statement statement = connection.createStatement();
            numRows = statement.executeUpdate(saveQuery);
            AlertBox.display("Operación exitosa", "Usuario creado", "");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void editUser(User user) {
        int numRows;
        String sql_update = "UPDATE public.user" +
                " SET user_name = '" + user.getName() + "', user_last_name = '" + user.getLastName() +
                "', user_document_number = '" + user.getDocumentIdNumber() + "', user_type = " + user.getType() +
                ", user_document_type = " + user.getDocumentType() + ", user_state = " + user.getState() +
                ", up_to_date_password = " + user.isPasswordReset() +
                " WHERE user_document_number = '" + user.getDocumentIdNumber() + "'";
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

    public User loadUser(String documentNumber,short userDocumentType) {

        String sql_select = "SELECT user_name, user_last_name, user_document_number, user_document_type," +
                " user_type, user_state, up_to_date_password " +
                "FROM public.user WHERE user_document_number = '" + documentNumber + "'"+" AND "+"user_document_type = "+userDocumentType;
        try {

            System.out.println("Consultando en la base de datos");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql_select);
            resultSet.next();
            User user = new User(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getShort(4),
                    resultSet.getShort(5),
                    resultSet.getBoolean(6),
                    resultSet.getBoolean(7)
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
        return new User("", "", "", (short) -1, (short) -1, false, false);
    }

    public boolean checkPassword(String documentNumber, String password){
        String sql_select = "SELECT user_password" +
                " FROM public.user WHERE user_document_number = '" + documentNumber + "'";

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
                "UPDATE public.user " +
                        "SET user_password = '" + encryptedPassword + "', up_to_date_password = true " +
                        "WHERE user_document_number = '" + documentNumber + "';";

        String sql_select =
                "select user_type from public.user where user_document_number = '" + documentNumber + "';";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql_update);
            AlertBox.display("Logrado", "La contraseña fue cambiada", "con éxito");
            ResultSet resultSet = statement.executeQuery(sql_select);
            resultSet.next();
            Login.currentWindow.set(-1);
            Login.currentWindow.set(resultSet.getShort(1) + 1);
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
