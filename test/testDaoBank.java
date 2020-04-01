package test;
    import controller.DaoBank;
    import junit.framework.TestCase;
    import org.junit.Test;

public class testDaoBank extends TestCase {
    private DaoBank bank;

    public void scenario(){
        bank = new DaoBank();
    }

  /*public void testSaveNewBank(){
        scenario();
        assertEquals("Operación Realizada",(bank.saveNewBank("Caja Social","157486","458451454")));
    } */
    @Test
    public void testEditBank(){
        scenario();
        assertTrue(bank.editBank(false,"2").equals("Operación Realizada"));
    }

    @Test
    public void testLoadBank(){
        scenario();
        assertEquals("333",bank.loadBank("banco3").getNIT());
    }

    @Test
    public void testLoadAllBanks(){          /**el numero esperado depende del numero de bancos que hay en la BD**/
        scenario();
        assertEquals(7,bank.loadAllBanks().length);
    }
}