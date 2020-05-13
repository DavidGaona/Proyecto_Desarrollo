package test;

import controller.DaoChart;
import junit.framework.TestCase;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class testDaoChart extends TestCase {
    public DaoChart chart;
    public ArrayList arrayF;

    @Test
    public void scenario() {
        chart = new DaoChart();
        arrayF = new ArrayList();

    }

    /**
     * prueba para tabla que saca los datos de los clientes
     */
    @Test
    public void testGetDataAboutClientsNC() {
        scenario();
        assertNotNull(chart.getDataAboutClientsNC(true));
    }

    /**
     * prueba de metodo que obtiene los datos
     */
    @Test
    public void testGetDataPlansPerMonths() {
        scenario();
        assertNotNull(chart.getDataPlansPerMonths(LocalDate.parse("2020-01-01", DateTimeFormatter.ISO_LOCAL_DATE), (LocalDate.parse("2020-06-01", DateTimeFormatter.ISO_LOCAL_DATE))));
    }

    /**
     * prueba de tabla que genera los datos de los planes en un rango dado
     */
    @Test
    public void testGetDataPlansOnRange() {
        scenario();
        assertNotNull(chart.getDataPlansOnRange(LocalDate.parse("2020-01-01", DateTimeFormatter.ISO_LOCAL_DATE), (LocalDate.parse("2020-06-01", DateTimeFormatter.ISO_LOCAL_DATE))));
    }

    /**
     * prueba de tabla que genera datos de los planes en un rango dado, prueba de fallo.
     * al introducir un rango en el que no hay datos, la función devuelve NULL
     */
    @Test
    public void testGetDataPlansOnRangeWrong() {
        scenario();
        assertNull(chart.getDataPlansOnRange(LocalDate.parse("2020-01-01", DateTimeFormatter.ISO_LOCAL_DATE), (LocalDate.parse("2020-01-01", DateTimeFormatter.ISO_LOCAL_DATE))));
    }

    /**
     * prueba de tabla que muestra los clientes mas antiguos
     */
    @Test
    public void testGetOldestClients() {
        scenario();
        assertNotNull(chart.getOldestClients(10));
    }

    /**
     * prueba de tabla que muestra los clientes mas antiguos con fallo
     * al poner una cantidad por fuera del rando, la función devuelve null
     */
    @Test
    public void testGetOldestClientsWrong() {
        scenario();
        assertNull(chart.getOldestClients(30));
    }

    /**
     * prueba para tabla que devuelve los mejores clientes
     */
    @Test
    public void testGetHighestPayers() {
        scenario();
        assertNotNull(chart.getHighestPayers(10));
    }

    /**
     * prueba para tabla que devuelve los mejores clientes
     * la función devuelve null al introducir un numero de clientes
     * por fuera del rango establecido
     */
    @Test
    public void testGetHighestPayersWrong() {
        scenario();
        assertNull(chart.getHighestPayers(60));
    }

    /**
     * pruebas del grafico que muestra los clientes que han cancelado un plan
     */
    @Test
    public void testGetCancelledClientsOnRange() {
        scenario();
        assertNotNull(chart.getCancelledClientsOnRange(LocalDate.parse("2020-01-01", DateTimeFormatter.ISO_LOCAL_DATE), (LocalDate.parse("2020-06-01", DateTimeFormatter.ISO_LOCAL_DATE))));
    }

    /**
     * pruebas del grafico que muestra los clientes que han cancelado un plan
     * la función devuelve null al introducir un intervalo de tiempo no valido,
     * como la misma fecha de inicio y final
     */
    @Test
    public void testGetCancelledClientsOnRangeWrong() {
        scenario();
        assertNull(chart.getCancelledClientsOnRange(LocalDate.parse("2020-01-01", DateTimeFormatter.ISO_LOCAL_DATE), (LocalDate.parse("2020-01-01", DateTimeFormatter.ISO_LOCAL_DATE))));
    }


}
