package test;

import controller.DaoClient;
import junit.framework.TestCase;
import org.junit.Test;

public class testDaoClient extends TestCase {
    private DaoClient client;

    public void scenario() {
        client = new DaoClient();
    }

    /**
     *Prueba para crear un nuevo cliente de forma exitosa
     */
    /*
    @Test
    public void testSaveNewClient() {
        scenario();
        assertEquals("Cliente creado con exito", client.saveNewClient(102, "felipe", "pelaez", (short) 0, "678", "pablo@pablez.com", "calle 1 # 10", (short) 0, "Palmira", 36));
    }
     */

    /**
     * Prueba para añadir a un nuevo cliente pero ya se encuentra registrado en la base de datos
     */
    @Test
    public void testSaveNewClientExistente() {
        scenario();
        assertEquals("Este cliente ya se encuentra registrado", client.saveNewClient(40, "Miguel",
                "Reyes", (short) 0, "114", "reyes.miguel@correounivalle.edu.co",
                "Cra 20 #13-20", (short) 0, "Cali", 36));
    }

    /**
     * Prueba para editar un cliente exitosamente
     */
    @Test
    public void testEditClient() {
        scenario();
        assertEquals("Cliente editado exito", client.editClient(40, "Miguel", "Reyes",
                (short) 0, "114", "reyes.miguel@correounivalle.edu.co", "Cra 48 #13-54",
                (short) 0, "Cali"));
    }

    /*
     * Documento ya existente hay que hacerlo manual
     **/

    @Test
    public void testLoadClient() {
        scenario();
        assertEquals("Miguel", client.loadClient("114", "Cédula de ciudadanía").getName());
    }
}
