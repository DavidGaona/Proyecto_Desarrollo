package test;

import controller.DaoBill;
import junit.framework.TestCase;
import org.junit.Test;

public class testDaoBill extends TestCase {
    private DaoBill bill;

    @Test
    public void scenario() {
        bill = new DaoBill();
    }

    @Test
    /**
     * Prueba de metodo GenerateBills, el cual genera las facturas con los datos almacenados en la base de datos
     */
    public void testGenerateBills() {
        scenario();
        assertTrue((bill.generateBills().equals("Las facturas ya fueron generadas"))
                || bill.generateBills().equals("Operación realizada con exíto, facturas generadas 1".substring(0, 60)));
    }

    /**
     * prueba de getAllBills,, donde se descargan las facturas en el equipo
     */
    @Test
    public void testGetAllBills() {
        scenario();
        assertEquals("Se han creado los PDF con exito",bill.getAllBills("/home/camilo/Downloads"));
    }

    /**
     * Prueba para descargar facturas, como se evidencia una ruta
     * no permitida, como lo es la capteta raiz /
     */
    @Test
    public void testGetAllBillsWrong() {
        scenario();
        assertEquals("El lugar donde desea guardar no existe o no tiene permisos de escritura",bill.getAllBills("/dev"));
    }
}
