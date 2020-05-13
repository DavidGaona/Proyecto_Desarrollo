package test;

import controller.DaoPlan;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import junit.framework.TestCase;
import model.Extras;
import org.junit.Test;

public class testDaoPlan extends TestCase {
    private DaoPlan plan;
    private Extras extra;
    private ObservableList<Extras> result;

    @Test
    public void scenario() {
        plan = new DaoPlan();
        extra = new Extras(3, "Minutos Italia", 1, 0);

        result = FXCollections.observableArrayList();
        result.add(extra);
    }

    /**
     * prueba de saveNewPlan, que agrega un nuevo plan
     * se testea un caso exitoso
     */
    /*
    @Test
    public void testSaveNewPlan() {  //caso exitoso
        scenario();
        assertEquals("Plan registrado con exito", plan.saveNewPlan("plan express", "1212", "1212", "1212", "1212", result));
    }
    */

    /**
     * prueba del metodo editPlan, que se encarga de editar los planes
     */
    @Test
    public void testEditPlan() {
        scenario();
        assertEquals("Plan editado con exito", plan.editPlan("Plan Colombia Virtual", "25000", "1000", "50000", "30000", result));
    }

    /**
     * pueba de metodo saveNewVoiceMins
     * @return
     */
    /*
    @Test
    public void testSaveNewVoiceMins() {
        scenario();
        assertEquals("Operación realizada con exito", plan.saveNewVoiceMins("Minutos Finlandia", "3080"));
    }
     */

    /**
     *Prueba para guardar una nueva app
     */
    /*
    @Test
    public void testSaveApp() {
        scenario();
        assertEquals("Operación realizada con exito", plan.saveApp("Megas Discord Nitro", "5000"));
    }
     */

    /**
     * Prueba de loadPlan, donde se prueba como carga un plan.
     * caso exitoso
     */
    @Test
    public void testLoadPlan() {
        scenario();
        assertEquals(4, plan.loadPlan("Plan Colombia Virtual").getId());
    }


}