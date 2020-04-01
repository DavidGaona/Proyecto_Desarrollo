package test;
    import controller.DaoClient;
    import junit.framework.TestCase;
    import org.junit.Test;

public class testDaoClient extends TestCase {
    private DaoClient client;

    public void scenario(){
        client = new DaoClient();
    }

    /**
    @Test
    public void testSaveNewClient(){ //caso exitoso
        scenario();
        assertEquals("Cliente creado con exito",client.saveNewClient(102,"felipe","pelaez",(short)0,"678","pablo@pablez.com","calle 1 # 10",(short)0,36));
    }
    **/

    @Test
    public void testSaveNewClient(){ //cliente existente
        scenario();
        assertEquals("Este cliente ya se encuentra registrado",client.saveNewClient(100,"pablo","pablez",(short)0,"677","pablo@pablez.com","calle 1 # 10",(short)0,36));
    }

}
