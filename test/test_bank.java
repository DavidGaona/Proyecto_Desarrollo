package test;
    import controller.DaoBank;
import junit.framework.TestCase;

public class test_bank extends TestCase {
    private DaoBank bank;

    public void scenario(){
        bank = new DaoBank();
    }
  /*  public void testSave_bank(){
        scenario();
        assertEquals("Operación Realizada",(bank.saveNewBank("Caja Social","157486","458451454")));
    } */
    public void testSet_state_bank(){
        scenario();
        assertTrue(bank.editBank(false,"2").equals("Operación Realizada"));
    }
}
