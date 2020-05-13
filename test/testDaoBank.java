package test;

import controller.DaoBank;
import junit.framework.TestCase;
import org.junit.Test;

public class testDaoBank extends TestCase {
    private DaoBank bank;

    public void scenario() {
        bank = new DaoBank();
    }


    /**
     * Prueba que crea un banco nuevo
     */
    /*
    public void testSaveNewBank() {
        scenario();
        assertEquals("Operación Realizada", (bank.saveNewBank("Caja Social", "157486", "458451454")));
    }
    */

    /**
     * prueba que edita un banco de forma exitosa
     */
    @Test
    public void testEditBank() {
        scenario();
        assertTrue(bank.editBank(true, "452452452").equals("Operación Realizada"));     ////actualizar valores
    }

    /**
     * Prueba de carga de los datos de un banco
     */
    @Test
    public void testLoadBank() {
        scenario();
        assertEquals("69", bank.loadBank("Banco Agrario").getNIT());
    }

    /**
     * Prueba de metodo que carga todos los bancos en el comboBox
     */
    @Test
    public void testLoadAllBanks() {          /*el numero esperado depende del numero de bancos que hay en la BD**/
        scenario();
        assertEquals(2, bank.loadAllBanks(true).length);    //////////////actualizar valores
    }
}
