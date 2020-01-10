import java.sql.*;


public class DbManager {
    private DBconnect fachada;
    private Connection conexion;

    public DbManager(String usuario,String password,String nombreBaseDeDatos,String host){
        fachada = new DBconnect(usuario, password, nombreBaseDeDatos,host);
        conexion = null;
    }

    public void saveNewClient(Client client) {
        String sql_guardar;
        int numFilas;
        sql_guardar = "INSERT INTO public.cliente(nombre_cliente, apellidos_cliente, documento_id_cliente, email_cliente, direccion_cliente, tipo_cliente, tipo_documento)" +
                " VALUES ('" + client.getName() + "', '" + client.getLastName() + "', '" +
                client.getDocumentId() + "', '" + client.getEmail() + "', '" + client.getDirection() + "'," +
                client.getType() + ", " + client.getDocumentType() + ")" + " ON CONFLICT (id_cliente) DO NOTHING";
        try {
            Statement sentencia = conexion.createStatement();
            numFilas = sentencia.executeUpdate(sql_guardar);
            System.out.println("up " + numFilas);

        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void editClient(Client client) {

        int numFilas;
        String sql_update = "UPDATE public.cliente" +
                "SET nombre_cliente = " + client.getName() + ", apellidos_cliente = " + client.getLastName() + ", documento_id_cliente = " + client.getDocumentId() +
                "email_cliente = " + client.getEmail() + ", direccion_cliente = " + client.getDirection() + ", tipo_cliente = " + client.getType() +
                ", tipo_documento = " + client.getDocumentType() + "WHERE documento_id_cliente = " + client.getDocumentId();
        try {
            Statement sentencia = conexion.createStatement();
            numFilas = sentencia.executeUpdate(sql_update);
            System.out.println("up " + numFilas);

        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Client loadClient(){
        Client client = new Client();
        String sql_select = "SELECT nombre_cliente, apellidos_cliente, documento_id_cliente, email_cliente," +
                "direccion_cliente, tipo_cliente, tipo_documento " +
                "FROM public.cliente";
        try{

            System.out.println("consultando en la base de datos");
            Statement sentencia = conexion.createStatement();
            ResultSet tabla = sentencia.executeQuery(sql_select);

            client.setName(tabla.getString(1));
            client.setLastName(tabla.getString(2));
            client.setDocumentId(tabla.getString(3));
            client.setEmail(tabla.getString(4));
            client.setDirection(tabla.getString(5));
            client.setType(tabla.getInt(6));
            client.setDocumentType(tabla.getInt(7));

            return client;
        }
        catch(SQLException e){ System.out.println(e);
            System.out.println("Problema en la base de datos tabla: buses");
        }
        catch(Exception e){ System.out.println(e);
            System.out.println("ERROR Fatal en la base de datos");
        }

        return client;
    }
    public boolean abrirConexionBD(){
        conexion = fachada.getConnetion();
        return conexion != null;
    }
    public void cerrarConexionBD(){
        fachada.closeConnection(conexion);
    }

}
