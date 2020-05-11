package connection;

//import at.favre.lib.crypto.bcrypt.BCrypt;

import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import utilities.ProjectUtilities;
import view.Login;

import java.sql.*;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class DbManager {
    private DBconnect dBconnect;
    private Connection connection;

    public DbManager(String user, String password, String dataBaseName, String host) {
        dBconnect = new DBconnect(user, password, dataBaseName, host);
        connection = null;
    }

    //**************************** METODOS DEL CLIENTE ********************
    public String saveNewClient(Client client, int currentLoginUser) {
        String saveQuery, selectQuery, logQuery;
        int numRows, id = -1;
        saveQuery = "INSERT INTO public.client(client_name, client_last_name, client_document_number, client_email, client_address, client_type, client_document_type)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";

        selectQuery = "SELECT client_id FROM public.client WHERE client_document_number = ?  AND client_document_type = ?";
        logQuery = "INSERT INTO public.client_date(client_id, user_id, join_date) VALUES(?, ?, current_timestamp(0))";

        try {
            PreparedStatement statement = connection.prepareStatement(saveQuery);
            statement.setString(1, client.getName());
            statement.setString(2, client.getLastName());
            statement.setString(3, client.getDocumentId());
            statement.setString(4, client.getEmail());
            statement.setString(5, client.getDirection());
            statement.setShort(6, client.getType());
            statement.setShort(7, client.getDocumentType());
            numRows = statement.executeUpdate();
            System.out.println(numRows);
            statement = connection.prepareStatement(selectQuery);
            statement.setString(1, client.getDocumentId());
            statement.setShort(2, client.getDocumentType());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getInt(1);
            statement = connection.prepareStatement(logQuery);
            statement.setInt(1, id);
            statement.setInt(2, currentLoginUser);
            numRows = statement.executeUpdate();
            System.out.println(numRows);
            return "Cliente creado con exito";

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Este cliente ya se encuentra registrado";
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return "Ocurrio un error interno del sistema";
        }
    }

    public String editClient(Client client) {
        int numRows;
        String sql_update = "UPDATE public.client " +
                "SET client_name = ?, client_last_name = ?, client_document_number = ?, client_email = ?, client_address = ?, client_type = ?, client_document_type = ?" +
                " WHERE client_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql_update);
            statement.setString(1, client.getName());
            statement.setString(2, client.getLastName());
            statement.setString(3, client.getDocumentId());
            statement.setString(4, client.getEmail());
            statement.setString(5, client.getDirection());
            statement.setShort(6, client.getType());
            statement.setShort(7, client.getDocumentType());
            statement.setInt(8, client.getId());
            numRows = statement.executeUpdate();
            System.out.println("up " + numRows);
            return "Cliente editado exito";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Error al momento de editar el cliente";
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return "Ocurrio un error interno del sistema";
        }
    }

    public ArrayList<Long> loadPhoneNumbers(int clientId) {
        String getNumbersQuery = "SELECT phone_number FROM public.phone where client_id = ?";
        ArrayList<Long> numbers = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(getNumbersQuery);
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                numbers.add(resultSet.getLong(1));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("Ocurrio un error interno del sistema");
        }
        return numbers;
    }

    public String addNewClientLine(int clientId, String planName) {
        String getClientType = "SELECT client_type FROM public.client WHERE client_id = ?";
        String checkForMaxLines = "SELECT count(*) FROM (SELECT phone_number FROM public.phone WHERE client_id = ? LIMIT 5) sq1;";
        String getLastNumberQuery = "SELECT phone_number FROM public.phone WHERE phone_number = (select max(phone_number) from public.phone)";
        String addLineQuery = "INSERT INTO public.phone(phone_number, client_id, plan_id, phone_date) VALUES(?, ?, ?, current_timestamp(0)) ";

        try {
            PreparedStatement statement = connection.prepareStatement(getClientType);
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            short clientType = resultSet.getShort(1);
            if (clientType == (short) 0) {
                statement = connection.prepareStatement(checkForMaxLines);
                statement.setInt(1, clientId);
                resultSet = statement.executeQuery();
                resultSet.next();
                if (resultSet.getInt(1) == 3)
                    return "Numero de lineas máximo excedido";
            }
            int planId = getPlanId(planName);
            if (planId == -1)
                return "Plan no encontrado";
            statement = connection.prepareStatement(getLastNumberQuery);
            resultSet = statement.executeQuery();
            if (!resultSet.next())
                return "Error interno";
            long phoneNumber = resultSet.getLong(1);
            ++phoneNumber;
            statement = connection.prepareStatement(addLineQuery);
            statement.setLong(1, phoneNumber);
            statement.setInt(2, clientId);
            statement.setInt(3, planId);
            statement.executeUpdate();
            return "Plan y número agredado con éxito";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("Ocurrio un error interno del sistema");
        }

        return "Cliente no encontrado";
    }

    public String queueNewPlan(int clientId, long phoneNumber, String planName) {
        String changePlanQuery = "INSERT INTO public.phone_pending(client_id, plan_id, phone_number)  " +
                "VALUES (?, ?, ?);";
        try {
            int planId = getPlanId(planName);
            if (planId == -1)
                return "Plan no encontrado";
            PreparedStatement statement = connection.prepareStatement(changePlanQuery);
            statement.setInt(1, clientId);
            statement.setInt(2, planId);
            statement.setLong(3, phoneNumber);
            statement.executeUpdate();
            return "Plan pendiente por cambio, sera cambiado en su proximo pago";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("Ocurrio un error interno del sistema");
        }

        return "Usuario no encontrado";
    }

    public boolean changeSelectedPlan(long phoneNumber) {
        String selectNewPlan = "SELECT plan_id FROM public.phone_pending WHERE phone_number = ?";
        String changePlanQuery = "UPDATE public.phone SET plan_id = ?, phone_date = current_timestamp(0) WHERE phone_number = ?";
        String deletePendingPlanChange = "DELETE FROM public.phone_pending WHERE phone_number = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(selectNewPlan);
            statement.setLong(1, phoneNumber);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int planId = resultSet.getInt(1);
            statement = connection.prepareStatement(changePlanQuery);
            statement.setInt(1, planId);
            statement.setLong(2, phoneNumber);
            statement.executeUpdate();
            statement = connection.prepareStatement(deletePendingPlanChange);
            statement.setLong(1, phoneNumber);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("Ocurrio un error interno del sistema");
        }

        return false;
    }

    public String getPhonePlan(long phoneNumber) {
        String getPlanIdQuery = "SELECT plan_name FROM " +
                "(SELECT plan_id FROM public.phone WHERE phone_number = ?) sq1 NATURAL JOIN public.plan;";
        try {
            PreparedStatement statement = connection.prepareStatement(getPlanIdQuery);
            statement.setLong(1, phoneNumber);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return "Numero no encontrado";
            return resultSet.getString(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("Ocurrio un error interno del sistema");
        }
        return "Error";
    }

    private int getPlanId(String planName) {
        String getPlanIdQuery = "SELECT plan_id FROM public.plan WHERE plan_name = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(getPlanIdQuery);
            statement.setString(1, planName);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return -1;
            return resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("Ocurrio un error interno del sistema");
        }
        return -1;
    }

    public Client loadClient(String documentNumber, short clientDocumentType) {
        String sql_select = "SELECT client_id, client_name, client_last_name," +
                " client_email, client_address, client_type " +
                "FROM public.client WHERE client_document_number = ? AND client_document_type = ?";

        Client client = new Client();
        try {
            System.out.println("consultando en la base de datos");
            PreparedStatement statement = connection.prepareStatement(sql_select);
            statement.setString(1, documentNumber);
            statement.setShort(2, clientDocumentType);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            client.setId(resultSet.getInt(1));
            client.setName(resultSet.getString(2));
            client.setLastName(resultSet.getString(3));
            client.setDocumentType(clientDocumentType);
            client.setDocumentId(documentNumber);
            client.setEmail(resultSet.getString(4));
            client.setDirection(resultSet.getString(5));
            client.setType(resultSet.getShort(6));
            return client;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("Ocurrio un error interno del sistema");
        }

        return new Client();
    }

    public double getValueToPay(long phoneNumber) {
        String getPlanIdQuery = "SELECT bill_cost FROM public.active_bills WHERE phone_number = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(getPlanIdQuery);
            statement.setLong(1, phoneNumber);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return 0.0;
            return resultSet.getDouble(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("Ocurrio un error interno del sistema");
        }
        return 0.0;
    }

    public String payPlan(long phoneNumber, int userId, String bankName) {
        String selectCurrentBill = "SELECT * FROM public.active_bills WHERE phone_number = ?;";
        String insertToLegacyBills = "INSERT INTO public.legacy_bills(client_id, user_id, bill_cost, bank_id, " +
                "bill_date_legacy, bill_mins_legacy, bill_gb_legacy, bill_msg_legacy, phone_number, bill_payed_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp(0));";
        String deleteFromCurrentBills = "DELETE FROM public.active_bills WHERE phone_number = ?;";

        try {
            PreparedStatement statement = connection.prepareStatement(selectCurrentBill);
            statement.setLong(1, phoneNumber);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return "No tiene facturas por pagar";
            resultSet.getLong(1);
            double billCost = resultSet.getDouble(2);
            Timestamp billDate = resultSet.getTimestamp(3);
            int billMinutes = resultSet.getInt(4);
            int billGb = resultSet.getInt(5);
            int billMsg = resultSet.getInt(6);
            int clientId = resultSet.getInt(7);
            int bankId = getBankId(bankName);
            if (bankId == -1)
                return "Banco no encontrado";
            statement = connection.prepareStatement(insertToLegacyBills);
            statement.setInt(1, clientId);
            statement.setInt(2, userId);
            statement.setDouble(3, billCost);
            statement.setInt(4, bankId);
            statement.setTimestamp(5, billDate);
            statement.setInt(6, billMinutes);
            statement.setInt(7, billGb);
            statement.setInt(8, billMsg);
            statement.setLong(9, phoneNumber);
            statement.executeUpdate();
            statement = connection.prepareStatement(deleteFromCurrentBills);
            statement.setLong(1, phoneNumber);
            statement.executeUpdate();
            if (changeSelectedPlan(phoneNumber))
                return "Factura pagada con éxito y cambio exitoso al nuevo plan";
            return "Factura pagada con éxito";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("Ocurrio un error interno del sistema");
        }
        return "No se pudo realizar el pago por favor intente de nuevo";
    }

    public String checkForBills(long phoneNumber) {
        String checkForBillQuery = "SELECT exists(SELECT 1 FROM public.active_bills WHERE phone_number = ?);";
        try {
            PreparedStatement statement = connection.prepareStatement(checkForBillQuery);
            statement.setLong(1, phoneNumber);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return "Error al cancelar por favor intente mas tarde";

            if (resultSet.getBoolean(1))
                return "Tiene facturas por pagar";
            else
                return "No tiene facturas pendientes";

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("Ocurrio un error interno del sistema");
        }
        return "Error al cancelar por favor intente mas tarde";
    }

    public String cancelLine(long phoneNumber) {
        try {
            removeLine(phoneNumber);
            return "Linea cancelada";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("Ocurrio un error interno del sistema");
        }
        return "error al cancelar, por favor intente mas tarde";
    }

    public String cancelLineDebt(long phoneNumber, int factor) {
        String selectCurrentBill = "SELECT * FROM public.active_bills WHERE phone_number = ?;";
        String addClientToDebt = "INSERT INTO public.debt_bills " +
                "VALUES (?, ?, ?, current_timestamp(0), ?, ?, ?, ?);";
        String deleteFromCurrentBills = "DELETE FROM public.active_bills WHERE phone_number = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(selectCurrentBill);
            statement.setLong(1, phoneNumber);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return "error al cancelar, por favor intente mas tarde";
            resultSet.getLong(1);
            double billCost = resultSet.getDouble(2);
            Timestamp billDate = resultSet.getTimestamp(3);
            int billMinutes = resultSet.getInt(4);
            int billGb = resultSet.getInt(5);
            int billMsg = resultSet.getInt(6);
            int clientId = resultSet.getInt(7);
            statement = connection.prepareStatement(addClientToDebt);
            statement.setLong(1, phoneNumber);
            statement.setDouble(2, billCost * factor);
            statement.setTimestamp(3, billDate);
            statement.setInt(4, billMinutes);
            statement.setInt(5, billGb);
            statement.setInt(6, billMsg);
            statement.setInt(7, clientId);
            statement.executeUpdate();
            statement = connection.prepareStatement(deleteFromCurrentBills);
            statement.setLong(1, phoneNumber);
            statement.executeUpdate();
            removeLine(phoneNumber);
            return "Linea cancelada queda con una deuda de: " + billCost;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("Ocurrio un error interno del sistema");
        }
        return "error al cancelar, por favor intente mas tarde";
    }

    public String cancelLineTransferCost(long phoneNumber, int clientId) {
        String transferBillCost = "" +
                "WITH line_to_change AS (" +
                "SELECT phone_number, (active_bills.bill_cost + (SELECT active_bills.bill_cost FROM active_bills WHERE phone_number = ?)) as cost " +
                "FROM active_bills WHERE client_id = ? AND phone_number != ? LIMIT 1 ) " +
                "UPDATE active_bills SET " +
                "bill_cost = (select cost from line_to_change) " +
                "where phone_number = (select phone_number from line_to_change)";
        String deleteFromCurrentBills = "DELETE FROM public.active_bills WHERE phone_number = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(transferBillCost);
            statement.setLong(1, phoneNumber);
            statement.setInt(2, clientId);
            statement.setLong(3, phoneNumber);
            statement.executeUpdate();
            statement = connection.prepareStatement(deleteFromCurrentBills);
            statement.setLong(1, phoneNumber);
            statement.executeUpdate();
            removeLine(phoneNumber);
            return "Linea cancelada el costo de la factura fue transferido a su otra linea";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("Ocurrio un error interno del sistema");
        }
        return "error al cancelar, por favor intente mas tarde";
    }

    public boolean hasDebt(int clientId) {
        String checkForDebtQuery = "SELECT exists(SELECT 1 FROM public.debt_bills WHERE client_id = ?);";
        try {
            PreparedStatement statement = connection.prepareStatement(checkForDebtQuery);
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getBoolean(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("Ocurrio un error interno del sistema");
        }
        return false;
    }

    private void removeLine(long phoneNumber) throws SQLException {
        String removeFromPendingQuery = "DELETE FROM public.phone_pending WHERE phone_number = ?";
        String cancelLineQuery = "UPDATE public.phone SET client_id = -1 WHERE phone_number = ?";
        PreparedStatement statement = connection.prepareStatement(removeFromPendingQuery);
        statement.setLong(1, phoneNumber);
        statement.executeUpdate();
        statement = connection.prepareStatement(cancelLineQuery);
        statement.setLong(1, phoneNumber);
        statement.executeUpdate();
    }

    public double getDept(int clientId){
        String getDept = "select case when sum(bill_cost) IS NULL then 0 else sum(bill_cost) end as cost from debt_bills where client_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(getDept);
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getDouble(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("Ocurrio un error interno del sistema");
        }
        return 0.0;
    }

    @SuppressWarnings("DuplicatedCode")
    public String payDebt(int clientId, String bankName, int userId){
        String selectCurrentDebt = "SELECT * FROM public.debt_bills WHERE client_id = ?;";
        String insertToLegacyDebt = "INSERT INTO public.debt_bills_legacy " +
                "VALUES (?, ?, ?, ?, current_timestamp(0), ?, ?, ?, ?, ?, ?);";
        String deleteFromDebt = "DELETE FROM public.debt_bills WHERE client_id = ?;";

        try {
            PreparedStatement statement = connection.prepareStatement(selectCurrentDebt);
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            int bankId = getBankId(bankName);
            if (bankId == -1)
                return "Banco no encontrado";
            while (resultSet.next()){
                long phoneNumber = resultSet.getLong(1);
                double billCost = resultSet.getDouble(2);
                Timestamp billDate = resultSet.getTimestamp(3);
                Timestamp billDateCancelled = resultSet.getTimestamp(4);
                int billMinutes = resultSet.getInt(5);
                int billGb = resultSet.getInt(6);
                int billMsg = resultSet.getInt(7);
                statement = connection.prepareStatement(insertToLegacyDebt);
                statement.setLong(1, phoneNumber);
                statement.setDouble(2, billCost);
                statement.setTimestamp(3, billDate);
                statement.setTimestamp(4, billDateCancelled);
                statement.setInt(5, billMinutes);
                statement.setInt(6, billGb);
                statement.setInt(7, billMsg);
                statement.setInt(8, clientId);
                statement.setInt(9, userId);
                statement.setInt(10, bankId);
                statement.executeUpdate();
                statement = connection.prepareStatement(deleteFromDebt);
                statement.setInt(1, clientId);
                statement.executeUpdate();
            }
            return "Deuda pagada con éxito";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println("Ocurrio un error interno del sistema");
        }
        return "No se pudo realizar el pago por favor intente de nuevo";
    }

    //**************************** METODOS DEL USUARIO ********************
    public int loginUser(String documento_id_usuario, short documentType, String password) {
        String sql_select = "SELECT user_password, user_type, user_state, up_to_date_password, user_id" +
                " FROM public.user WHERE user_document_number = ? AND user_document_type = ?";

        try {
            System.out.println("consultando en la base de datos");
            PreparedStatement statement = connection.prepareStatement(sql_select);
            statement.setString(1, documento_id_usuario);
            statement.setShort(2, documentType);
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

    public String saveNewUser(User user, int currentLoginUser) {
        int numRows, id;
        String saveQuery, selectQuery, logQuery;
        final String hashWillBeStored = BCrypt.withDefaults().hashToString(12, user.getDocumentIdNumber().toCharArray());
        saveQuery = "INSERT INTO public.user(user_name, user_last_name, user_document_number, user_type, user_state, user_password, user_document_type)" +
                " VALUES(?, ?, ?, ?, ?, ?, ?)";

        selectQuery = "SELECT user_id FROM public.\"user\" WHERE user_document_number = ? AND user_document_type = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(saveQuery);
            statement.setString(1, user.getName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getDocumentIdNumber());
            statement.setShort(4, user.getType());
            statement.setBoolean(5, user.getState());
            statement.setString(6, hashWillBeStored);
            statement.setShort(7, user.getDocumentType());
            numRows = statement.executeUpdate();
            System.out.println(numRows);
            statement = connection.prepareStatement(selectQuery);
            statement.setString(1, user.getDocumentIdNumber());
            statement.setShort(2, user.getDocumentType());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getInt(1);
            logQuery = "INSERT INTO public.user_creation_date" +
                    " VALUES (?, ?, current_timestamp(0))";
            statement = connection.prepareStatement(logQuery);
            statement.setInt(1, id);
            statement.setInt(2, currentLoginUser);
            numRows = statement.executeUpdate();
            System.out.println(numRows);
            return "Operacion exitosa";

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "El usuario ya se encuentra creado";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Ocurrio un error interno del sistema";
        }
    }

    public String editUser(User user) {
        int numRows;
        if (!user.isPasswordReset()) {
            final String hashWillBeStored = BCrypt.withDefaults().hashToString(12, user.getDocumentIdNumber().toCharArray());
            String sql_update = "UPDATE public.\"user\"" +
                    " SET user_password = ? WHERE user_id = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(sql_update);
                statement.setString(1, hashWillBeStored);
                statement.setInt(2, user.getId());
                System.out.println("user id: " + user.getId());
                numRows = statement.executeUpdate();
                System.out.println("up " + numRows);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return "Error al editar el usuario";
            } catch (Exception e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
                return "Ocurrio un error interno del sistema";
            }
        }

        String sql_update = "UPDATE public.\"user\"" +
                " SET user_name = ?, user_last_name = ?, user_document_number = ?, user_type = ?, user_document_type = ?, user_state = ?" +
                ", up_to_date_password = ? WHERE user_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql_update);
            statement.setString(1, user.getName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getDocumentIdNumber());
            statement.setShort(4, user.getType());
            statement.setShort(5, user.getDocumentType());
            statement.setBoolean(6, user.getState());
            statement.setBoolean(7, user.isPasswordReset());
            statement.setInt(8, user.getId());
            numRows = statement.executeUpdate();
            System.out.println(numRows);
            return "Usuario editado con exito";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Error al editar el usuario";
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return "Ocurrio un error interno del sistema";
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
            return new User(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getShort(5),
                    resultSet.getShort(6),
                    resultSet.getBoolean(7),
                    resultSet.getBoolean(8)
            );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return null;
        }
        return new User();
    }

    public boolean checkPassword(int userId, String password) {
        String sql_select = "SELECT user_password" +
                " FROM public.user WHERE user_id = ?";

        try {
            System.out.println("Consultando en la base de datos");
            PreparedStatement statement = connection.prepareStatement(sql_select);
            statement.setInt(1, userId);
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
                "select user_type from public.user where user_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql_update);
            statement.setString(1, encryptedPassword);
            statement.setInt(2, userId);
            statement.executeUpdate();
            statement = connection.prepareStatement(sql_select);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getShort(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    //**************************** PLAN METHODS ****************************\\
    public String saveNewPlan(Plan plan, ObservableList<Extras> extras) {

        int numRows;
        String saveQuery = "INSERT INTO public.plan(plan_name, plan_cost, plan_minutes, plan_data_cap, plan_text_message, plan_creation_date) " +
                "VALUES(?, ?, ?, ?, ?, current_timestamp(0))";

        String planIdQuery = "SELECT plan_id FROM public.plan WHERE plan_name = ?";
        String voiceQuery = "INSERT INTO public.plan_voice(voice_id, plan_id) VALUES(?, ?)";
        String appsQuery = "INSERT INTO public.plan_apps(app_id, plan_id) VALUES(?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(saveQuery);
            statement.setString(1, plan.getPlanName());
            statement.setDouble(2, plan.getPlanCost());
            statement.setInt(3, plan.getPlanMinutes());
            statement.setInt(4, plan.getPlanData());
            statement.setInt(5, plan.getPlanTextMsn());
            numRows = statement.executeUpdate();
            System.out.println(numRows);
            PreparedStatement select = connection.prepareStatement(planIdQuery);
            select.setString(1, plan.getPlanName());
            ResultSet resultSet = select.executeQuery();
            resultSet.next();
            int planId = resultSet.getInt(1);
            return insertPlanExtras(extras, voiceQuery, appsQuery, planId, true);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Error al crear plan";
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return "Ocurrio un error interno del sistema";
        }
    }

    public String editPlan(Plan plan, ObservableList<Extras> extras) {

        String saveQuery = "UPDATE public.plan SET " +
                "plan_name = ?, plan_cost = ?, plan_minutes = ?, plan_data_cap = ?, " +
                "plan_text_message = ? WHERE plan.plan_name = ?";

        String planIdQuery = "SELECT plan_id FROM public.plan WHERE plan_name = ?";
        String deleteVoiceQuery = "DELETE FROM public.plan_voice WHERE plan_id = ?";
        String deleteAppQuery = "DELETE FROM public.plan_apps WHERE plan_id = ?";
        String voiceQuery = "INSERT INTO public.plan_voice(voice_id, plan_id) VALUES(?, ?)";
        String appsQuery = "INSERT INTO public.plan_apps(app_id, plan_id) VALUES(?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(saveQuery);
            statement.setString(1, plan.getPlanName());
            statement.setDouble(2, plan.getPlanCost());
            statement.setInt(3, plan.getPlanMinutes());
            statement.setInt(4, plan.getPlanData());
            statement.setInt(5, plan.getPlanTextMsn());
            statement.setString(6, plan.getPlanName());
            statement.executeUpdate();
            PreparedStatement select = connection.prepareStatement(planIdQuery);
            select.setString(1, plan.getPlanName());
            ResultSet resultSet = select.executeQuery();
            resultSet.next();
            int planId = resultSet.getInt(1);
            System.out.println(planId);
            statement = connection.prepareStatement(deleteVoiceQuery);
            statement.setInt(1, planId);
            statement.executeUpdate();
            statement = connection.prepareStatement(deleteAppQuery);
            statement.setInt(1, planId);
            statement.executeUpdate();
            return insertPlanExtras(extras, voiceQuery, appsQuery, planId, false);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Error al editar plan";
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return "Ocurrio un error interno del sistema";
        }

    }

    private String insertPlanExtras(ObservableList<Extras> extras, String voiceQuery, String appsQuery, int planId, boolean type) throws SQLException {
        PreparedStatement voiceInsert = connection.prepareStatement(voiceQuery);
        PreparedStatement appsInsert = connection.prepareStatement(appsQuery);
        for (Extras extra : extras) {
            if (extra.getType() == 0) {
                voiceInsert.setInt(1, extra.getId());
                voiceInsert.setInt(2, planId);
                voiceInsert.addBatch();
            } else if (extra.getType() == 1) {
                appsInsert.setInt(1, extra.getId());
                appsInsert.setInt(2, planId);
                appsInsert.addBatch();
            }
        }
        voiceInsert.executeBatch();
        appsInsert.executeBatch();
        if (type)
            return "Plan registrado con exito";
        else
            return "Plan editado con exito";
    }

    public String saveNewVoiceMins(Voice voice) {
        int numRows;
        String saveQuery = "INSERT INTO public.voice(voice_name, voice_minutes) " +
                "VALUES(?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(saveQuery);
            statement.setString(1, voice.getVoiceName());
            statement.setInt(2, voice.getVoiceMinutes());
            numRows = statement.executeUpdate();
            System.out.println(numRows);
            return "Operación realizada con exito";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Error al intentar crear los minutos";
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return "Ocurrio un error interno del sistema";
        }
    }

    public String saveApp(App app) {
        int numRows;
        String saveQuery = "INSERT INTO public.apps(app_name, app_mb_cap) " +
                "VALUES(?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(saveQuery);
            statement.setString(1, app.getAppName());
            statement.setInt(2, app.getAppMb());
            numRows = statement.executeUpdate();
            System.out.println(numRows);
            return "Operación realizada con exito";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Error al intentar crear los megas";
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return "Ocurrio un error interno del sistema";
        }
    }

    public ArrayList<String> loadPlans() {
        ArrayList<String> plans = new ArrayList<>();
        String loadPlanQuery = "SELECT plan_name FROM plan;";
        try {
            PreparedStatement statement = connection.prepareStatement(loadPlanQuery);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                plans.add(resultSet.getString(1));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return plans;
    }

    public Plan loadPlan(String planName) {
        String loadPlanQuery = "SELECT plan_id, plan_name, plan_cost, plan_minutes, plan_data_cap, plan_text_message " +
                "FROM plan where plan_name = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(loadPlanQuery);
            statement.setString(1, planName);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return new Plan(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4),
                    resultSet.getInt(5),
                    resultSet.getInt(6)
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Plan();
        }
    }

    public ObservableList<Extras> loadPlanExtras(int planId) {
        String selectedVoiceQuery = "SELECT voice_id, voice_name, voice_minutes" +
                " FROM (public.voice NATURAL JOIN (SELECT voice_id FROM public.plan_voice WHERE plan_id = ?) sq1) sq2";
        String selectAppQuery = "SELECT app_id, app_name, app_mb_cap " +
                " FROM (public.apps NATURAL JOIN (SELECT app_id FROM public.plan_apps WHERE plan_id = ?) sq1) sq2";
        String unSelectedVoiceQuery = "SELECT public.voice.voice_id, voice_name, voice_minutes" +
                " FROM public.voice LEFT JOIN " +
                "(SELECT sq2.voice_id FROM (public.voice NATURAL JOIN (SELECT voice_id FROM public.plan_voice WHERE plan_id = ?) sq1) sq2) sq3 " +
                "ON public.voice.voice_id = sq3.voice_id WHERE sq3.voice_id IS NULL ";
        String unSelectAppQuery = "SELECT public.apps.app_id, app_name, app_mb_cap " +
                " FROM public.apps LEFT JOIN " +
                "(SELECT app_id FROM (public.apps NATURAL JOIN (SELECT app_id FROM public.plan_apps WHERE plan_id = ?) sq1) sq2) sq3 " +
                "ON public.apps.app_id = sq3.app_id WHERE sq3.app_id IS NULL ";

        ObservableList<Extras> result = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = connection.prepareStatement(selectedVoiceQuery);
            statement.setInt(1, planId);
            ResultSet table = statement.executeQuery();
            while (table.next()) {
                result.add(new Extras(table.getInt(1), table.getString(2), table.getInt(3), true, 0));
            }
            statement = connection.prepareStatement(selectAppQuery);
            statement.setInt(1, planId);
            table = statement.executeQuery();
            while (table.next()) {
                result.add(new Extras(table.getInt(1), table.getString(2), table.getInt(3), true, 1));
            }
            statement = connection.prepareStatement(unSelectedVoiceQuery);
            statement.setInt(1, planId);
            table = statement.executeQuery();
            while (table.next()) {
                result.add(new Extras(table.getInt(1), table.getString(2), table.getInt(3), false, 0));
            }
            statement = connection.prepareStatement(unSelectAppQuery);
            statement.setInt(1, planId);
            table = statement.executeQuery();
            while (table.next()) {
                result.add(new Extras(table.getInt(1), table.getString(2), table.getInt(3), false, 1));
            }
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return result;
    }

    public ObservableList<Extras> listExtras() {
        String selectVoiceQuery = "SELECT voice_id, voice_name, voice_minutes" +
                " FROM public.voice";
        String selectAppQuery = "SELECT app_id, app_name, app_mb_cap " +
                " FROM public.apps";

        ObservableList<Extras> result = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            ResultSet table = statement.executeQuery(selectVoiceQuery);
            while (table.next()) {
                result.add(new Extras(table.getInt(1), table.getString(2), table.getInt(3), false, 0));
            }
            table = statement.executeQuery(selectAppQuery);
            while (table.next()) {
                result.add(new Extras(table.getInt(1), table.getString(2), table.getInt(3), false, 1));
            }
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return result;
    }

    //**************************** BANK METHODS ****************************\\
    public String saveBank(String bankName, String accountNumber, String bankNIT) {
        int numRows;
        String sql = "INSERT INTO public.bank(bank_name, account_number, state, bank_nit) VALUES(?,?,true,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, bankName);
            preparedStatement.setString(2, accountNumber);
            preparedStatement.setString(3, bankNIT);
            numRows = preparedStatement.executeUpdate();
            if (numRows > 0) {
                return "Operación Realizada";
            } else {
                return "No se pudo realizar la operación";
            }
        } catch (SQLException e) {
            return "Error: " + e.toString();
        } catch (Exception e) {
            return "Error: " + e.toString();
        }
    }

    public Bank loadBank(String bankName) {

        String sql_select = "SELECT bank_name, account_number, state, bank_nit " +
                "FROM public.bank WHERE bank_name = ?";
        try {
            System.out.println("Consultando en la base de datos");
            PreparedStatement statement = connection.prepareStatement(sql_select);
            statement.setString(1, bankName);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return new Bank(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getBoolean(3),
                    resultSet.getString(4)
            );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return null;
        }
        return new Bank();
    }

    public String[] loadAllBank() {

        ArrayList<String> banks = new ArrayList<>();

        String sql_select = "SELECT bank_name " +
                "FROM public.bank";
        try {
            System.out.println("Consultando en la base de datos");
            PreparedStatement statement = connection.prepareStatement(sql_select);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                banks.add(resultSet.getString(1));
            }
            return banks.toArray(new String[banks.size()]);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return null;
        }
        return new String[]{};
    }

    public String setStateBank(boolean state, String bankNIT) {
        int numRows;
        String sql = "UPDATE bank SET state = ? WHERE bank_nit = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, state);
            preparedStatement.setString(2, bankNIT);
            numRows = preparedStatement.executeUpdate();
            if (numRows > 0) {
                return "Operación Realizada";
            } else {
                return "No se pudo realizar la operación";
            }
        } catch (SQLException e) {
            return "Error: " + e.toString();
        } catch (Exception e) {
            return "Error: " + e.toString();
        }
    }

    private int getBankId(String bankName) {
        String sql_select = "SELECT bank_id " +
                "FROM public.bank WHERE bank_name = ?";
        try {
            System.out.println("Consultando en la base de datos");
            PreparedStatement statement = connection.prepareStatement(sql_select);
            statement.setString(1, bankName);
            ResultSet resultSet = statement.executeQuery();
            return (resultSet.next()) ? resultSet.getInt(1) : -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return -1;
    }

    public int generateBills() {
        int[] numRows;
        String sql_select = "SELECT phone_number, client_id, plan_cost, plan_minutes, plan_data_cap, plan_text_message " +
                "FROM ((SELECT phone_number, client_id, plan_id FROM public.phone WHERE phone_number " +
                "NOT IN (SELECT phone_number FROM public.active_bills) AND phone_date < current_timestamp(0) AND client_id != -1) " +
                "AS verf_phone NATURAL JOIN public.plan) AS phone_to_plan";
        String sql_save = "INSERT INTO public.active_bills VALUES(?, ?, current_timestamp(0), ?, ?, ?, ?)";
        try {
            cancelServiceForDebt();
            System.out.println("Consultando en la base de datos");
            PreparedStatement statement = connection.prepareStatement(sql_select);
            ResultSet resultSet = statement.executeQuery();
            statement = connection.prepareStatement(sql_save);
            connection.setAutoCommit(false);
            while (resultSet.next()) {
                statement.setLong(1, resultSet.getLong(1));
                statement.setDouble(2, resultSet.getDouble(3));
                statement.setInt(3, (int) (Math.random() * resultSet.getInt(4)));
                statement.setInt(4, (int) (Math.random() * resultSet.getInt(5)));
                statement.setInt(5, (int) (Math.random() * resultSet.getInt(6)));
                statement.setInt(6, resultSet.getInt(2));
                statement.addBatch();
            }
            numRows = statement.executeBatch();
            connection.commit();
            return Arrays.stream(numRows).reduce(0, Integer::sum);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return -2;
        }
    }

    public ArrayList<Bill> getAllBills() {
        ArrayList<Bill> array_bills = new ArrayList<>();
        String sql_select = "SELECT * FROM (public.active_bills NATURAL JOIN public.phone NATURAL JOIN public.client NATURAL JOIN public.plan)";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql_select);
            while (resultSet.next()) {
                array_bills.add(new Bill(
                        resultSet.getDouble(4), resultSet.getDate(5),
                        resultSet.getInt(6), resultSet.getInt(7), resultSet.getInt(8),
                        resultSet.getLong(3), new Client(resultSet.getInt(2),
                        resultSet.getString(10), resultSet.getString(11), resultSet.getShort(16),
                        resultSet.getString(12), resultSet.getString(13), resultSet.getString(14),
                        resultSet.getShort(15)), new Plan(resultSet.getInt(1), resultSet.getString(17),
                        resultSet.getDouble(18), resultSet.getInt(19), resultSet.getInt(20),
                        resultSet.getInt(21)))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array_bills;
    }

    public ArrayList<DataChart> getDataAboutClientsNC(boolean activos) {
        ArrayList<DataChart> data = new ArrayList<DataChart>();
        String sql_select;
        if (activos) {
            sql_select = "SELECT client_type, COUNT(client_id) AS sum FROM public.client GROUP BY client_type";
        } else {
            sql_select = "SELECT client_type, COUNT(client_id) AS sum FROM (SELECT client_id, client_type FROM public.client NATURAL JOIN public.phone) AS result GROUP BY client_type";
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql_select);
            while (resultSet.next()) {
                data.add(
                        new DataChart(ProjectUtilities.convertClientTypeString(resultSet.getShort(1)), resultSet.getLong(2))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;

    }

    public ArrayList<DataChart> getDataPlansPerMonths(Timestamp from, Timestamp to) {
        ArrayList<DataChart> data = new ArrayList<>();
        String sql_select = "SELECT EXTRACT(MONTH FROM phone_date) AS month, COUNT(phone_number) AS sum " +
                "FROM (SELECT * FROM public.phone WHERE phone_date BETWEEN ? AND ?) AS result " +
                "GROUP BY EXTRACT(MONTH FROM phone_date) ORDER BY month DESC";
        try {
            System.out.println("Consultando en la base de datos");
            PreparedStatement statement = connection.prepareStatement(sql_select);
            statement.setTimestamp(1, from);
            statement.setTimestamp(2, to);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                data.add(
                        new DataChart(Month.of(resultSet.getInt(1)).name(), resultSet.getLong(2))
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        return data;
    }

    public ArrayList<DataChart> getDataPlansOnRange(Timestamp from, Timestamp to) {
        ArrayList<DataChart> data = new ArrayList<>();
        String sql_select = "SELECT plan_name, COUNT(phone_number) AS sum " +
                "FROM (SELECT * FROM public.phone NATURAL JOIN public.plan WHERE phone_date BETWEEN ? AND ?) AS result " +
                "GROUP BY plan_name";
        try {
            System.out.println("Consultando en la base de datos");
            PreparedStatement statement = connection.prepareStatement(sql_select);
            statement.setTimestamp(1, from);
            statement.setTimestamp(2, to);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                data.add(
                        new DataChart(resultSet.getString(1), resultSet.getLong(2))
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        return data;
    }

    public ArrayList<DataChart> getOldestClients(int numberOfClients) {
        ArrayList<DataChart> data = new ArrayList<>();
        String sql_select = "SELECT client_id, phone_number, phone_date FROM public.phone NATURAL JOIN public.client ORDER BY phone_date ASC LIMIT ?";
        try {
            System.out.println("Consultando en la base de datos");
            PreparedStatement statement = connection.prepareStatement(sql_select);
            statement.setInt(1, numberOfClients);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                data.add(
                        new DataChart("Id: "+ resultSet.getString(1) + " Fecha de registro: " + resultSet.getTimestamp(3).toString(), resultSet.getLong(2))
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        return data;
    }

    private void cancelServiceForDebt() throws SQLException {
        String sql_select = "SELECT phone_number FROM public.active_bills WHERE " +
                "(SELECT (DATE_PART('year', current_timestamp(0)::timestamptz) - DATE_PART('year', bill_date::timestamptz)) * 12" +
                " + (DATE_PART('month', current_timestamp(0)::timestamptz) - DATE_PART('month', bill_date::timestamptz))) > 2";
        System.out.println("Consultando en la base de datos");
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql_select);
        while (result.next()) {
            cancelLineDebt(result.getLong(1), 3);
        }
    }

    public ArrayList<DataChart> getHighestPayers(int numberOfClients){
        ArrayList<DataChart> data = new ArrayList<>();
        String sql_select = "SELECT client_id, phone_number, SUM(bill_cost) AS total_payed FROM public.legacy_bills GROUP BY client_id, phone_number ORDER BY total_payed DESC LIMIT ?";
        try {
            System.out.println("Consultando en la base de datos");
            PreparedStatement statement = connection.prepareStatement(sql_select);
            statement.setInt(1, numberOfClients);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                data.add(
                        new DataChart("Id: "+ resultSet.getString(1) + " Celular: " + resultSet.getLong(2), (long) resultSet.getDouble(4))
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        return data;
    }

    public void openDBConnection() {
        connection = dBconnect.getConnection();
    }

    public void closeDBConnection() {
        dBconnect.closeConnection(connection);
    }

}
