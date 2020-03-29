package test;

import controller.DaoBank;
import junit.framework.TestCase;

public class test_bank extends TestCase {
    private DaoBank bank;

    public void scenario(){
        bank = new DaoBank();
    }
    public void testSave_bank(){
        scenario();
        assertTrue(bank.save_bank("Caja Social","458451454").equals("Operación Realizada"));
    }
    public void testSet_state_bank(){
        scenario();
        assertTrue(bank.set_state_bank(false,2).equals("Operación Realizada"));
    }
}
