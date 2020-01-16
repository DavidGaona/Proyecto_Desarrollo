package connection;

import model.Client;

import java.sql.*;


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
            return numFilas;

        } catch (SQLException e) {
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
            System.out.println("up " + numRows);

        } catch (SQLException e) {
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
            return client;
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Problema en la base de datos tabla: cliente");
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("ERROR Fatal en la base de datos");
        }

        return new Client("", "", (short) -1, "", "", "", (short) -1);
    }

    public boolean abrirConexionBD() {
        conexion = fachada.getconnetion();
        return conexion != null;
    }

    public void cerrarConexionBD() {
        fachada.closeConnection(conexion);
    }

}
