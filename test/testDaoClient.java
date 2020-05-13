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
    /*
    @Test
    public void testSaveNewClient(){ //caso exitoso
        scenario();
        assertEquals("Cliente creado con exito",client.saveNewClient(102,"felipe","pelaez",(short)0,"678","pablo@pablez.com","calle 1 # 10",(short)0,36));
    }
    */


    @Test
    public void testSaveNewClientExistente() { //cliente existente
        scenario();
        assertEquals("Este cliente ya se encuentra registrado", client.saveNewClient(100, "pablo",
                "pablez", (short) 0, "677", "pablo@pablez.com",
                "calle 1 # 10", (short) 0, "Cali", 36));
    }

    //****************************editClient********************
    @Test
    public void testEditClient() {
        scenario();
        assertEquals("Cliente editado exito", client.editClient(100, "pablo", "pablez",
                (short) 0, "677", "pablo@pablez.com",
                "cra 200 # 24 - 89", (short) 0, "Cali"));
    }

    /**
     * Documento ya existente hay que hacerlo manual
     **/

    //****************************loadClient********************
    @Test
    public void testLoadClient() {
        scenario();
        assertEquals("pablo", client.loadClient("677", "Cédula de ciudadanía").getName());
    }
}
