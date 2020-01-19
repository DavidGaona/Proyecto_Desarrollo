package connection;

//import at.favre.lib.crypto.bcrypt.BCrypt;

import model.Client;
import model.User;
import utilities.AlertBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


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

    public int loginUser(String id_usuario, String password) {
        String sql_select = "SELECT id_usuario" +
                "FROM public.usuario WHERE id_usuario = '" + id_usuario + "'";

        try {
            System.out.println("consultando en la base de datos");
            Statement sentencia = conexion.createStatement();
            ResultSet tabla = sentencia.executeQuery(sql_select);
            tabla.next();
            String hashedPasswordFromBD = tabla.getString(1);
            //final BCrypt.Result resultCompare = BCrypt.verifyer().verify(password.toCharArray(), hashedPasswordFromBD);
            //if(!resultCompare.verified){
            //    System.out.println("CONTRASEÑA INVALIDA");
            //    return -1;
            //}

            /* ToDo */

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
        String sql_guardar;
        //final String hashWillBeStored = BCrypt.withDefaults().hashToString(12,user.getUserPassword.toCharArray());
        String hashWillBeStored = "";
        sql_guardar = "INSERT INTO public.usuario(nombre_usuario,apellidos_usuario,documento_id_usuario,tipo_usuario,estado_usuario,pass_usuario)" +
                " VALUES('" + user.getUserName() + "','" + user.getUserLastName() + "','" + user.getUserIdDocumentNumber() + "'," + user.getUserType() +
                "," + user.getUserState() + ",'" + hashWillBeStored + "')" + " ON CONFLICT (id_usuario) DO NOTHING";
        try {
            Statement sentencia = conexion.createStatement();
            numRows = sentencia.executeUpdate(sql_guardar);
            return numRows;

        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return -1;
    }


    public boolean openDBConnection() {
        conexion = fachada.getconnetion();
        return conexion != null;
    }

    public void closeDBConnection() {
        fachada.closeConnection(conexion);
    }

}
