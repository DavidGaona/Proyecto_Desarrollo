package test;
    import controller.DaoClient;
    import junit.framework.TestCase;
    import org.junit.Test;

public class testDaoClient extends TestCase {
    private DaoClient client;

    public void scenario(){
        client = new DaoClient();
    }

    @Test
    public void testSaveNewClient(){
        scenario();
        assertEquals("Cliente creado con exito",client.saveNewClient(100,"pablo","pablez",(short)0,"676","pablo@pablez.com","calle 1 # 10",(short)0,36));
    }

}
