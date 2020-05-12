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
     * public void testSaveNewBank(){
     * scenario();
     * assertEquals("Operación Realizada",(bank.saveNewBank("Caja Social","157486","458451454")));
     * }
     */
    @Test
    public void testEditBank() {
        scenario();
        assertTrue(bank.editBank(true, "420").equals("Operación Realizada"));     ////actualizar valores
    }

    @Test
    public void testLoadBank() {
        scenario();
        assertEquals("69", bank.loadBank("Banco Agrario").getNIT());
    }

    @Test
    public void testLoadAllBanks() {          /**el numero esperado depende del numero de bancos que hay en la BD**/
        scenario();
        assertEquals(3, bank.loadAllBanks(true).length);    //////////////actualizar valores
    }
}
