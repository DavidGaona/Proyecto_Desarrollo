package test;

import controller.DaoChart;
import junit.framework.TestCase;
import org.junit.Test;

public class testDaoChart extends TestCase {
    public DaoChart chart;

    @Test
    public void scenario() {
        chart = new DaoChart();
    }

    /**
     * prueba para tabla que saca los datos de los clientes
     */
    @Test
    public void testGetDataAboutClientsNC() {
        scenario();
        assertNotNull(chart.getDataAboutClientsNC(true));
    }

    @Test
    public void testGetDataPlansPerMonths(){
        scenario();
       //assertEquals("prueba",chart.getDataPlansPerMonths(2020-01-01,2020-06-01));
    }

}
