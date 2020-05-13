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

    /*
     @Test public void testSaveNewUser() { //caso exitoso
     scenario();
     assertEquals("Operacion exitosa", (user.saveNewUser(168, "pipe", "jimenez", "451", (short) 0, (short) 0, true, 35)));
     }
     */

    /**
     * prueba para comprobar un usuario que ya existe
     */
    @Test
    public void testSaveNewUserCreado() { //usuario existente
        scenario();
        assertEquals("El usuario ya se encuentra creado", (user.saveNewUser(36, "Bigueloncho", "Beyes Bendoza", "444", (short) 0, (short) 0, true, 35)));
    }

    //****************************editUser********************

    /**
     * prueba de exito al editar un usuario
     */
    @Test
    public void testEditUser() { //editar exitoso
        scenario();
        assertEquals("Usuario editado con exito", (user.editUser(151, "Camilo", "Paez", "56", (short) 0, (short) 1, true, false)));
    }

    /*documento existente toca hacerlo manual
     */

    //****************************loadUser********************

    /**
     * prueba de carga exitosa de un usuario
     */
    @Test
    public void testLoadUser() { //carga correcta
        scenario();
        assertEquals("Papi", user.loadUser("000", "Cédula de ciudadanía").getName());
    }

    //****************************loginUser********************

    /**
     * prueba de logueo exitoso de un usuario
     */
    @Test
    public void testLoginUser() { //login correcto
        scenario();
        assertTrue(0 <= user.loginUser("666", (short) 0, "666"));
    }

    @Test
    public void testLoginUserIncorrecto() { //password incorrecto
        scenario();
        assertFalse(0 <= user.loginUser("000", (short) 1, "432346"));
    }


    @Test
    public void testCheckPasswordIncorrecto() { //check incorrecto
        scenario();
        assertFalse(true == user.checkPassword(152, "46"));
    }


}