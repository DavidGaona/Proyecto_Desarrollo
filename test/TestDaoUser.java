package test;

import controller.DaoUser;
import junit.framework.TestCase;
import org.junit.Test;

public class TestDaoUser extends TestCase {
    private DaoUser user;

    @Test
    public void scenario() {
        user = new DaoUser();
    }
    //****************************testsaveNewUser********************

    @Test
    public void testSaveNewUser() { //caso exitoso
        scenario();
        assertEquals("Operacion exitosa", (user.saveNewUser(168, "pipe", "malo", "451", (short) 0, (short) 0, true, 35)));
    }

    @Test
    public void testSaveNewUserCreado() { //usuario existente
        scenario();
        assertEquals("El usuario ya se encuentra creado", (user.saveNewUser(162, "pipe", "malo", "436", (short) 0, (short) 0, true, 35)));
    }

    //****************************editUser********************
    @Test
    public void testEditUser() { //editar exitoso
        scenario();
        assertEquals("Usuario editado con exito", (user.editUser(162, "pipe", "malo", "435", (short) 0, (short) 0, true, false)));
    }

    //documento existente toca hacerlo manual

    //****************************loadUser********************
    @Test
    public void testLoadUser() { //carga correcta
        scenario();
        assertTrue(169 == user.loadUser("436", "Cédula de ciudadanía").getId());
    }

    //****************************loginUser********************
    @Test
    public void testLoginUser() { //login correcto
        scenario();
        assertEquals(3, user.loginUser("436", (short) 0, "436"));
    }

    @Test
    public void testLoginUserIncorrecto() { //password incorrecto
        scenario();
        assertFalse(3 == user.loginUser("436", (short) 1, "432346"));
    }

    //****************************checkPassword********************
    @Test
    public void testCheckPassword() { //check correcto
        scenario();
        assertEquals(true, user.checkPassword(169, "436"));
    }

    @Test
    public void testCheckPasswordIncorrecto() { //check incorrecto
        scenario();
        assertFalse(true == user.checkPassword(169, "46"));
    }

    //****************************changePassword********************
    @Test
    public void testChangePassword() { //password correcto
        scenario();
        assertEquals(0, user.changePassword(169, "436"));
    }


}