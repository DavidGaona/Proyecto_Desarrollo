package test;
    import controller.DaoUser;
import junit.framework.TestCase;

public class TestDaoUser extends TestCase {
    private DaoUser user;

    public void scenario(){
        user = new DaoUser();
    }
    //**************************** testsaveNewUser********************
    public void testsaveNewUser(){ //caso exitoso
        scenario();
        assertEquals("Operacion exitosa",(user.saveNewUser(166,"pipe","malo","439",(short)0,(short)0,true,35)));
    }

    public void testsaveNewUserCreado(){
        scenario();
        assertEquals("El usuario ya se encuentra creado",(user.saveNewUser(162,"pipe","malo","435",(short)0,(short)0,true,35)));
    }


}