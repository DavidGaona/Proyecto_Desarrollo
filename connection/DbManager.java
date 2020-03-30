package connection;

//import at.favre.lib.crypto.bcrypt.BCrypt;

import at.favre.lib.crypto.bcrypt.BCrypt;
import model.Client;
import model.Plan;
import model.User;
import model.Voice;
import utilities.AlertBox;
import view.Login;

import java.sql.*;
import java.util.Arrays;


public class DbManager {
    private DBconnect dBconnect;
    private Connection connection;

    public DbManager(String user, String password, String dataBaseName, String host) {
        dBconnect = new DBconnect(user, password, dataBaseName, host);
        connection = null;
    }

    public String saveNewClient(Client client) {
        String saveQuery, selectQuery, logQuery;
        int numRows, id = -1;
        saveQuery = "INSERT INTO public.client(client_name, client_last_name, client_document_number, client_email, client_address, client_type, client_document_type)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";


        selectQuery = "SELECT client_id FROM public.client WHERE client_document_number = ?  AND client_document_type = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(saveQuery);
            statement.setString(1,client.getName());
            statement.setString(2,client.getLastName());
            statement.setString(3,client.getDocumentId());
            statement.setString(4,client.getEmail());
            statement.setString(5,client.getDirection());
            statement.setShort(6,client.getType());
            statement.setShort(7,client.getDocumentType());
            numRows = statement.executeUpdate();
            System.out.println(numRows);
            statement = connection.prepareStatement(selectQuery);
            statement.setString(1,client.getDocumentId());
            statement.setShort(2,client.getDocumentType());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getInt(1);
            System.out.println(id);
            logQuery = "INSERT INTO public.client_date(client_id, user_id, join_date) VALUES(?, ?, current_timestamp(0))";
            statement = connection.prepareStatement(logQuery);
            statement.setInt(1,id);
            statement.setInt(2,Login.currentLoggedUser);
            numRows = statement.executeUpdate();
            System.out.println(numRows);
            return "Cliente creado con exito";

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Error al crear cliente";
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return "Internal error";
        }
    }

    public String editClient(Client client) {
        int numRows;
        String sql_update = "UPDATE public.client " +
                "SET client_name = ?, client_last_name = ?, client_document_number = ?, client_email = ?, client_address = ?, client_type = ?, client_document_type = ?"+
                " WHERE client_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql_update);
            statement.setString(1,client.getName());
            statement.setString(2,client.getLastName());
            statement.setString(3,client.getDocumentId());
            statement.setString(4,client.getEmail());
            statement.setString(5,client.getDirection());
            statement.setShort(6,client.getType());
            statement.setShort(7,client.getDocumentType());
            statement.setInt(8,client.getId());
            numRows = statement.executeUpdate();
            System.out.println("up " + numRows);
            return "Cliente editado exito";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Error al momento de editar el cliente";
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return "Internal error";
        }
    }

    public Client loadClient(String documentNumber, short clientDocumentType) {

        String sql_select = "SELECT client_id, client_name, client_last_name, client_document_type," +
                " client_email, client_address, client_type " +
                "FROM public.client WHERE client_document_number = ? AND client_document_type = ?";
        Client client = new Client();
        try {
            System.out.println("consultando en la base de datos");
            PreparedStatement statement = connection.prepareStatement(sql_select);
            statement.setString(1,documentNumber);
            statement.setShort(2,clientDocumentType);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            client.setId(resultSet.getInt(1));
            client.setName(resultSet.getString(2));
            client.setLastName(resultSet.getString(3));
            client.setDocumentType(resultSet.getShort(4));
            client.setDocumentId(documentNumber);
            client.setEmail(resultSet.getString(5));
            client.setDirection(resultSet.getString(6));
            client.setType(resultSet.getShort(7));
            return client;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("Internal error");
        }

        return client;
    }

    public int loginUser(String documento_id_usuario, String password) {
        String sql_select = "SELECT user_password, user_type, user_state, up_to_date_password, user_id" +
                " FROM public.user WHERE user_document_number = ?";

        try {
            System.out.println("consultando en la base de datos");
            PreparedStatement statement = connection.prepareStatement(sql_select);
            statement.setString(1,documento_id_usuario);
            ResultSet resultSet = statement.executeQuery();
            String hashedPasswordFromBD;
            short user_type;
            if (!resultSet.next()) {
                System.out.println("No se pudo encontrar el usuario");
                return -1;
            }

            hashedPasswordFromBD = resultSet.getString(1);
            user_type = resultSet.getShort(2);
            final BCrypt.Result resultCompare = BCrypt.verifyer().verify(password.toCharArray(), hashedPasswordFromBD);
            if (!resultCompare.verified) {
                System.out.println("Contraseña invalida");
                return -2;
            }

            if (!resultSet.getBoolean(3)) {
                System.out.println("Cuenta desactivada");
                return -3;
            }

            Login.currentLoggedUser = resultSet.getInt(5);
            if (!resultSet.getBoolean(4))
                return 3;
            
            return user_type;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("ERROR Fatal en la base de datos");
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        return -4;
    }

    public String saveNewUser(User user) {
        int numRows, id;
        String saveQuery, selectQuery, logQuery;
        final String hashWillBeStored = BCrypt.withDefaults().hashToString(12, user.getDocumentIdNumber().toCharArray());
        saveQuery = "INSERT INTO public.user(user_name, user_last_name, user_document_number, user_type, user_state, user_password, user_document_type)" +
                " VALUES(?, ?, ?, ?, ?, ?, ?)";

        selectQuery = "SELECT user_id FROM public.\"user\" WHERE user_document_number = ? AND user_document_type = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(saveQuery);
            statement.setString(1,user.getName());
            statement.setString(2,user.getLastName());
            statement.setString(3,user.getDocumentIdNumber());
            statement.setShort(4,user.getType());
            statement.setBoolean(5,user.getState());
            statement.setString(6,hashWillBeStored);
            statement.setShort(7,user.getDocumentType());
            numRows = statement.executeUpdate();
            System.out.println(numRows);
            statement = connection.prepareStatement(selectQuery);
            statement.setString(1,user.getDocumentIdNumber());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getInt(1);
            logQuery = "INSERT INTO public.user_creation_date" +
                    " VALUES (?, ?, current_timestamp(0))";
            statement = connection.prepareStatement(logQuery);
            statement.setInt(1,id);
            statement.setInt(2,Login.currentLoggedUser);
            numRows = statement.executeUpdate();
            System.out.println(numRows);
            return "Operacion exitosa";

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Error al momento de crear el usuario";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Internal error";
        }
    }

    public String editUser(User user) {
        int numRows;
        if(!user.isPasswordReset()){
            final String hashWillBeStored = BCrypt.withDefaults().hashToString(12, user.getDocumentIdNumber().toCharArray());
            String sql_update = "UPDATE public.\"user\""+
                    " SET user_password = ? WHERE user_id = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(sql_update);
                statement.setString(1,hashWillBeStored);
                statement.setInt(2,user.getId());
                System.out.println("user id: " + user.getId());
                numRows = statement.executeUpdate();
                System.out.println("up " + numRows);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return "Error al editar el usuario";
            } catch (Exception e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
                return "Internal Error";
            }
        }

        String sql_update = "UPDATE public.\"user\"" +
                " SET user_name = ?, user_last_name = ?, user_document_number = ?, user_type = ?, user_document_type = ?, user_state = ?"+
                ", up_to_date_password = ? WHERE user_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql_update);
            statement.setString(1,user.getName());
            statement.setString(2,user.getLastName());
            statement.setString(3,user.getDocumentIdNumber());
            statement.setShort(4,user.getType());
            statement.setShort(5,user.getDocumentType());
            statement.setBoolean(6,user.getState());
            statement.setBoolean(7,user.isPasswordReset());
            statement.setInt(8,user.getId());
            numRows = statement.executeUpdate();
            System.out.println(numRows);
            return "Usuario editado con exito";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Error al crear usuario";
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return "Internal error";
        }
    }

    public User loadUser(String documentNumber, short userDocumentType) {

        String sql_select = "SELECT user_id, user_name, user_last_name, user_document_number, user_document_type," +
                " user_type, user_state, up_to_date_password " +
                "FROM public.user WHERE user_document_number = ? AND user_document_type = ?";
        try {
            System.out.println("Consultando en la base de datos");
            PreparedStatement statement = connection.prepareStatement(sql_select);
            statement.setString(1, documentNumber);
            statement.setShort(2, userDocumentType);
            ResultSet resultSet = statement.executeQuery();
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
            return user;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return new User();
    }

    public boolean checkPassword(int userId, String password) {
        String sql_select = "SELECT user_password" +
                " FROM public.user WHERE user_id = ?";

        try {
            System.out.println("Consultando en la base de datos");
            PreparedStatement statement = connection.prepareStatement(sql_select);
            statement.setInt(1,userId);
            ResultSet table = statement.executeQuery();
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

    public short changePassword(int userId, String password) {
        final String encryptedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        String sql_update =
                "UPDATE public.user " +
                        "SET user_password = ?, up_to_date_password = true " +
                        "WHERE user_id = ?";

        String sql_select =
                "select user_type from public.user where user_id = "+userId;

        try {
            PreparedStatement statement = connection.prepareStatement(sql_update);
            statement.setString(1,encryptedPassword);
            statement.setInt(2,userId);
            statement.executeUpdate();
            ResultSet resultSet = statement.executeQuery(sql_select);
            resultSet.next();
            //Login.currentWindow.set(resultSet.getShort(1) + 1);
            return resultSet.getShort(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public String saveNewPlan(Plan plan){

        int numRows;
        String saveQuery = "INSERT INTO public.plan(plan_name, plan_cost, plan_minutes,plan_data_cap,plan_text_message) "+
                "VALUES(?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(saveQuery);
            statement.setString(1,plan.getPlanName());
            statement.setDouble(2,plan.getPlanCost());
            statement.setInt(3,plan.getPlanMinutes());
            statement.setInt(4,plan.getPlanData());
            statement.setInt(5,plan.getPlanTextMsn());
            numRows = statement.executeUpdate();
            return "Plan registrado con exito";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Error al crear plan";
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return "Internal error";
        }

    }

    public String saveNewVoiceMins(Voice voice){
        int numRows;
        String saveQuery = "INSERT INTO public.plan(voice_name, voice_minutes) "+
                "VALUES(?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(saveQuery);
            statement.setString(1,voice.getVoiceName());
            statement.setInt(2, voice.getVoiceMinutes());
            numRows = statement.executeUpdate();
            System.out.println(numRows);
            return "Operación realizada con exito";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Error al intentar crear los minutos";
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return  "Internal error";
        }
    }
    //**************************** METODOS DEL BANCO ********************
    public String save_bank(String bank_name, String account_number){
        int numRows;
        String sql = "INSERT INTO public.bank(bank_name, account_number, state) VALUES(?,?,true)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,bank_name);
            preparedStatement.setString(2,account_number);
            numRows = preparedStatement.executeUpdate();
            if(numRows>0){
                return "Operación Realizada";
            }
            else{
                return "No se pudo realizar la operación";
            }
        } catch (SQLException e) {
            return  "Error: "+ e.toString();
        } catch (Exception e) {
            return "Error: "+ e.toString();
        }
    }
    public String set_state_bank(boolean state, int bank_id){
        int numRows;
        String sql = "UPDATE bank SET state = ? WHERE bank_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1,state);
            preparedStatement.setInt(2,bank_id);
            numRows = preparedStatement.executeUpdate();
            if(numRows>0){
                return "Operación Realizada";
            }
            else{
                return "No se pudo realizar la operación";
            }
        } catch (SQLException e) {
            return  "Error: "+ e.toString();
        } catch (Exception e) {
            return "Error: "+ e.toString();
        }
    }
    public void openDBConnection() {
        connection = dBconnect.getConnection();
    }

    public void closeDBConnection() {
        dBconnect.closeConnection(connection);
    }

}
