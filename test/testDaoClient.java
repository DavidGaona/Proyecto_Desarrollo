package test;

import controller.DaoClient;
import junit.framework.TestCase;
import org.junit.Test;

public class testDaoClient extends TestCase {
    private DaoClient client;

    public void scenario() {
        client = new DaoClient();
    }


    //****************************saveNewClient********************

    /**
     * @Test public void testSaveNewClient(){ //caso exitoso
     * scenario();
     * assertEquals("Cliente creado con exito",client.saveNewClient(102,"felipe","pelaez",(short)0,"678","pablo@pablez.com","calle 1 # 10",(short)0,36));
     * }
     */


    @Test
    public void testSaveNewClientExistente() { //cliente existente
        scenario();
        assertEquals("Este cliente ya se encuentra registrado", client.saveNewClient(40, "Miguel", "Reyes", (short) 0, "114", "reyes.miguel@correounivalle.edu.co", "Cra 20 #13-20", (short) 0, 36));
    }

    //****************************editClient********************
    @Test
    public void testEditClient() {
        scenario();
        assertEquals("Cliente editado exito", client.editClient(40, "Miguel", "Reyes", (short) 0, "114", "reyes.miguel@correounivalle.edu.co", "Cra 48 #13-54", (short) 0));
    }

    /**
     * Documento ya existente hay que hacerlo manual
     **/

    //****************************loadClient********************
    @Test
    public void testLoadClient() {
        scenario();
        assertEquals("Miguel", client.loadClient("114", "Cédula de ciudadanía").getName());
    }
}
