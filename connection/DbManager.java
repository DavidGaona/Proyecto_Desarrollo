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
    private DBconnect dBconnect;
    private Connection connection;

    public DbManager(String user, String password, String dataBaseName, String host) {
        dBconnect = new DBconnect(user, password, dataBaseName, host);
        connection = null;
    }

    public int saveNewClient(Client client) {
        String saveQuery, selectQuery, logQuery;
        int numRows, id = -1;
        saveQuery = "INSERT INTO public.client(client_name, client_last_name, client_document_number, client_email, client_address, client_type, client_document_type)" +
                " VALUES ('" + client.getName() + "', '" + client.getLastName() + "', '" +
                client.getDocumentId() + "', '" + client.getEmail() + "', '" + client.getDirection() + "'," +
                client.getType() + ", " + client.getDocumentType() + ")" + " ON CONFLICT (client_id) DO NOTHING";

        selectQuery = "SELECT client_id FROM public.client WHERE client_document_number = '" + client.getDocumentId() + "' AND client_document_type = " +
                client.getDocumentType() + ";";

        try {
            Statement statement = connection.createStatement();
            numRows = statement.executeUpdate(saveQuery);
            ResultSet resultSet = statement.executeQuery(selectQuery);
            resultSet.next();
            id = resultSet.getInt(1);
            System.out.println(id);
            logQuery = "INSERT INTO public.client_date(client_id, user_id, join_date)" +
                    " VALUES ( " + id + ", " + Login.currentLoggedUser + ", current_timestamp(0) )";
            System.out.println(id);
            statement.executeUpdate(logQuery);
            AlertBox.display("Operación exitosa", "Cliente creado", "");
            System.out.println(numRows);
            return 1;
        } catch (SQLException e) {
            AlertBox.display("Error", " Error al crear cliente", "");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return -1;
    }

    public int editClient(Client client) {
        int numRows;
        String sql_update = "UPDATE public.client " +
                "SET client_name = '" + client.getName() + "', client_last_name = '" + client.getLastName() +
                "', client_document_number = '" + client.getDocumentId() + "', client_email = '" + client.getEmail() +
                "', client_address = '" + client.getDirection() + "', client_type = " + client.getType() +
                ", client_document_type = " + client.getDocumentType() +
                " WHERE client_id = " + client.getId();
        try {
            Statement statement = connection.createStatement();
            numRows = statement.executeUpdate(sql_update);
            AlertBox.display("Operación exitosa", "Cliente editado", "");
            System.out.println("up " + numRows);
            return 1;
        } catch (SQLException e) {
            AlertBox.display("Error", " Error al editar cliente", "");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return -1;
    }

    public Client loadClient(String documentNumber, short clientDocumentType) {

        String sql_select = "SELECT client_id, client_name, client_last_name, client_document_type," +
                " client_email, client_address, client_type " +
                "FROM public.client WHERE client_document_number = '" + documentNumber + "'" + " AND " + "client_document_type = " + clientDocumentType;
        try {

            System.out.println("consultando en la base de datos");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql_select);
            resultSet.next();
            Client client = new Client(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getShort(4),
                    documentNumber,
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getShort(7)
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

        return new Client();
    }

    public int loginUser(String documento_id_usuario, String password) {
        String sql_select = "SELECT user_password, user_type, user_state, up_to_date_password, user_id" +
                " FROM public.user WHERE user_document_number = '" + documento_id_usuario + "'";

        try {
            System.out.println("consultando en la base de datos");
            if (connection == null) {
                AlertBox.display("Login view", "No se pudo establecer conexión", "Con el sistema");
                return -1;
            }
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql_select);
            String hashedPasswordFromBD;
            short user_type;
            if (!resultSet.next()) {
                System.out.println("No se pudo encontrar el usuario");
                AlertBox.display("Error", "Contraseña y/o id incorrectos", "");
                return -1;
            }

            hashedPasswordFromBD = resultSet.getString(1);
            user_type = resultSet.getShort(2);
            final BCrypt.Result resultCompare = BCrypt.verifyer().verify(password.toCharArray(), hashedPasswordFromBD);
            if (!resultCompare.verified) {
                System.out.println("CONTRASEÑA INVALIDA");
                AlertBox.display("Error", "Contraseña y/o id incorrectos", "");
                return -1;
            }

            if (!resultSet.getBoolean(3)) {
                AlertBox.display("Error", "No tiene permisos para ingresar", "contacte a un administrador");
                return -1;
            }

            if (!resultSet.getBoolean(4))
                return 3;
            Login.currentLoggedUser = resultSet.getInt(5);
            return user_type;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("ERROR Fatal en la base de datos");
            AlertBox.display("Error Login", "No se pudo establecer conexión", "con el sistema");
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        return -1;
    }

    public int saveNewUser(User user) {
        int numRows, id;
        String saveQuery, selectQuery, logQuery;
        final String hashWillBeStored = BCrypt.withDefaults().hashToString(12, user.getDocumentIdNumber().toCharArray());
        saveQuery = "INSERT INTO public.user(user_name, user_last_name, user_document_number, user_type, user_state, user_password, user_document_type)" +
                " VALUES('" + user.getName() + "','" + user.getLastName() + "','" + user.getDocumentIdNumber() + "'," + user.getType() +
                "," + user.getState() + ",'" + hashWillBeStored + "', " + user.getDocumentType() + ")" + " ON CONFLICT (user_id) DO NOTHING";

        selectQuery = "SELECT user_id FROM public.\"user\" WHERE user_document_number = '" + user.getDocumentIdNumber() + "' AND user_document_type = " + user.getDocumentType() + ";";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(saveQuery);
            ResultSet resultSet = statement.executeQuery(selectQuery);
            resultSet.next();
            id = resultSet.getInt(1);
            logQuery = "INSERT INTO public.user_creation_date" +
                    " VALUES ( " + id + ", " + Login.currentLoggedUser + ", current_timestamp(0));";
            statement.executeUpdate(logQuery);
            AlertBox.display("Operación exitosa", "Usuario creado", "");
            return 1;
        } catch (Exception e) {
            AlertBox.display("Error", " Error al crear usuario", "");
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public int editUser(User user) {
        int numRows;
        String sql_update = "UPDATE public.\"user\"" +
                " SET user_name = '" + user.getName() + "', user_last_name = '" + user.getLastName() +
                "', user_document_number = '" + user.getDocumentIdNumber() + "', user_type = " + user.getType() +
                ", user_document_type = " + user.getDocumentType() + ", user_state = " + user.getState() +
                ", up_to_date_password = " + user.isPasswordReset() +
                " WHERE user_id = " + user.getId() + ";";

        //String sql_log = "INSERT INTO  ";//ToDo edit logging
        try {
            Statement statement = connection.createStatement();
            System.out.println("user id: " + user.getId());
            numRows = statement.executeUpdate(sql_update);
            //statement.executeQuery();
            AlertBox.display("Operación exitosa", "Usuario editado", "");
            System.out.println("up " + numRows);
            return 1;
        } catch (SQLException e) {
            AlertBox.display("Error", " Error al editar usuario", "");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return -1;
    }

    public User loadUser(String documentNumber, short userDocumentType) {

        String sql_select = "SELECT user_id, user_name, user_last_name, user_document_number, user_document_type," +
                " user_type, user_state, up_to_date_password " +
                "FROM public.user WHERE user_document_number = '" + documentNumber + "'" + " AND " + "user_document_type = " + userDocumentType;
        try {

            System.out.println("Consultando en la base de datos");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql_select);
            resultSet.next();
            User user = new User(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getShort(5),
                    resultSet.getShort(6),
                    resultSet.getBoolean(7),
                    resultSet.getBoolean(8)
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
        return new User();
    }

    public boolean checkPassword(int userId, String password) {
        String sql_select = "SELECT user_password" +
                " FROM public.user WHERE user_id = " + userId + ";";

        try {
            System.out.println("consultando en la base de datos ");
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

    public void changePassword(int userId, String password) {
        final String encryptedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        String sql_update =
                "UPDATE public.user " +
                        "SET user_password = '" + encryptedPassword + "', up_to_date_password = true " +
                        "WHERE user_id = " + userId + ";";

        String sql_select =
                "select user_type from public.user where user_id = " + userId + ";";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql_update);
            AlertBox.display("Logrado", "La contraseña fue cambiada", "con éxito");
            ResultSet resultSet = statement.executeQuery(sql_select);
            resultSet.next();
            Login.currentWindow.set(resultSet.getShort(1) + 1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void openDBConnection() {
        connection = dBconnect.getConnection();
    }

    public void closeDBConnection() {
        dBconnect.closeConnection(connection);
    }

}
