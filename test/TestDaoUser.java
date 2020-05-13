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


    /**
     * Prueba de crear un nuevo usuario de forma exitosa
     */
    /*
     @Test public void testSaveNewUser() {
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

    /**
     * prueba de carga exitosa de un usuario
     */
    @Test
    public void testLoadUser() { //carga correcta
        scenario();
        assertEquals("Papi", user.loadUser("000", "Cédula de ciudadanía").getName());
    }

    /**
     * prueba de logueo exitoso de un usuario
     */
    @Test
    public void testLoginUser() { //login correcto
        scenario();
        assertTrue(0 <= user.loginUser("666", (short) 0, "666"));
    }

    /**
     * Prueba que muestra el login incorrecto de un usuario por contraseña errada
     */
    @Test
    public void testLoginUserIncorrecto() { //password incorrecto
        scenario();
        assertFalse(0 <= user.loginUser("000", (short) 1, "432346"));
    }

    /**
     * Verifica que el password introducido sea correcto
     * Prueba orientada a Fallo
     */
    @Test
    public void testCheckPasswordIncorrecto() {
        scenario();
        assertFalse(true == user.checkPassword(152, "46"));
    }

    /**
     * Prueba de cambio de contraseña
     */
    @Test
    public void testchangePassword() {
        scenario();
        assertEquals(1, user.changePassword(151, "56"));
    }


}