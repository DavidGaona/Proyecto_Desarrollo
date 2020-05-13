package test;

import controller.DaoClient;
import junit.framework.TestCase;
import org.junit.Test;

public class testDaoClient extends TestCase {
    private DaoClient client;

    public void scenario() {
        client = new DaoClient();
    }
    /**TODOS LOS METODOS QUE ESTÁN ENTRE COMENTARIO SIMPLE (/* gris) DEBEN SER DESCOMENTADOS,
     * ESTÁN ASÍ PORQUE ALTERAN LA BASE DE DATOS Y SU EJECUCIÓN REPETITIVA PUEDE GENERAR ERRORES
     */

    /**
     *Prueba para crear un nuevo cliente de forma exitosa
     */
    /*
    @Test
    public void testSaveNewClient() {
        scenario();
        assertEquals("Cliente creado con exito", client.saveNewClient(102, "felipe", "pelaez", (short) 0, "678", "pablo@pablez.com", "calle 1 # 10", (short) 0, "Palmira", 36));
    }
     */

    /**
     * Prueba para añadir a un nuevo cliente pero ya se encuentra registrado en la base de datos
     */
    @Test
    public void testSaveNewClientExistente() {
        scenario();
        assertEquals("Este cliente ya se encuentra registrado", client.saveNewClient(40, "Miguel",
                "Reyes", (short) 0, "114", "reyes.miguel@correounivalle.edu.co",
                "Cra 20 #13-20", (short) 0, "Cali", 36));
    }

    /**
     * Prueba para editar un cliente exitosamente
     */
    @Test
    public void testEditClient() {
        scenario();
        assertEquals("Cliente editado exito", client.editClient(40, "Miguel", "Reyes",
                (short) 0, "114", "reyes.miguel@correounivalle.edu.co", "Cra 48 #13-54",
                (short) 0, "Cali"));
    }

    /*
     * Documento ya existente hay que hacerlo manual
     **/

    /**
     * Prueba de metodo que carga el cliente en la interfaz
     */
    @Test
    public void testLoadClient() {
        scenario();
        assertEquals("Miguel", client.loadClient("114", "Cédula de ciudadanía").getName());
    }

    /**
     * Prueba de metodo que carga el número telefónico de un cliente
     */
    @Test
    public void testLoadPhoneNumbers() {
        scenario();
        assertEquals(Long.valueOf("3230000013"), client.loadPhoneNumbers(25).get(0));
    }

    /**
     * Prueba de metodo que añade una nueva linea a un cliente
     */
    /*
    @Test
    public void testAddNewClientLine(){
        scenario();
        assertTrue(("Plan y número agredado con éxito" == client.addNewClientLine(40,"A",false))||
                ("Plan y número agredado con éxito, factura ya disponible" ==client.addNewClientLine(40,"A",false)));
    }

     */

    /**
     * Prueba de metodo que extrae el plan asignado a un número de teléfono
     */
    @Test
    public void testGetPhonePlan() {
        scenario();
        assertEquals("Plan Colombia Virtual", client.getPhonePlan(Long.valueOf("3230000013")));
    }

    /**
     * Prueba de metodo que pone en cola de espera un plan para cambiarlo en el próximo pago
     */
    /*
    @Test
    public void testQueueNewPlan(){
        scenario();
        assertEquals("Plan pendiente por cambio, sera cambiado en su proximo pago",client.queueNewPlan(40,Long.valueOf("3230000075"),"Plan Colombia Virtual"));
    }

     */

    /**
     * Prueba de metodo que paga un plan dado un numero telefónico y un banco
     */
    @Test
    public void testPayPlan() {
        scenario();
        assertTrue(("No tiene facturas por pagar" == client.payPlan(Long.valueOf("3230000075"), 40, "Banco Agrario")) ||
                ("Factura pagada con éxito y cambio exitoso al nuevo plan" == client.payPlan(Long.valueOf("3230000075"), 40, "Banco Agrario")) ||
                ("Factura pagada con éxito" == client.payPlan(Long.valueOf("3230000075"), 40, "Banco Agrario")));
    }

    /**
     * Prueba de metodo que paga un plan dado un numero telefónico y un banco.
     * caso de banco no encontrado
     * (test puede fallar si no tiene facturas por pagar)
     */
    @Test
    public void testPayPlanWrongBank() {
        scenario();
        assertEquals("Banco no encontrado", client.payPlan(Long.valueOf("3230000075"), 40, "Banco BBVA"));
    }

    /**
     * prueba de metodo que muestra cuánto tiene que pagar el cliente
     * (Puede presentar fallo en test dependiendo de si tiene facturas por pagar o no)
     */
    @Test
    public void testGetValueToPay() {
        scenario();
        assertEquals(0.0, client.getValueToPay(25));
    }

    /**
     * Prueba de metodo que dice si el cliente tiene o no facturas por pagar
     */
    @Test
    public void testCheckForBills() {
        scenario();
        assertTrue(("Tiene facturas por pagar" == client.checkForBills(Long.valueOf("3230000013"))) ||
                ("No tiene facturas pendientes" == client.checkForBills(Long.valueOf("3230000013"))));
    }

    /**
     * prueba de metodo que cancela una linea de un cliente
     */
    /*
    @Test
    public void testCancelLine(){
        scenario();
        assertEquals("Linea cancelada",client.cancelLine(Long.valueOf("3230000075")));
    }

     */

    /**
     * Prueba de metodo que cancela la deuda perteneciente a una linea
     * (test puede fallar dependiendo de si la linea se encuentra creada)
     */
    @Test
    public void testCancelLineDebt() {
        scenario();
        assertEquals("Linea cancelada queda con una deuda de: ", client.cancelLineDebt(Long.valueOf("3230000075")).substring(0, 40));
    }

    /**
     * Prueba de metodo que cancela una linea telefónica y transfiere la deuda a otra linea del mismo cliente
     */
    /*
    @Test
    public void testCancelLineTransferCost(){
        scenario();
        assertEquals("Linea cancelada el costo de la factura fue transferido a su otra linea",client.cancelLineTransferCost(Long.valueOf("3230000075"),40));
    }

     */

    /**
     * Prueba de metodo que verifica si un cliente tiene una deuda pendiente
     */
    @Test
    public void testHasDebt() {
        scenario();
        assertEquals(false, client.hasDebt(40));
    }

    /**
     * Prueba de metodo que devuelve el monto de deuda que tiene un cliente
     * (test puede fallar dependiendo de si se crean facturas)
     */
    @Test
    public void testGetDept() {
        scenario();
        assertEquals(0.0, client.getDept(25));
    }

    /**
     * prueba de metodo que paga una deuda de un cliente
     * (test puede fallar si no hay deuda para el cliente dado
     */
    @Test
    public void testPayDebt() {
        scenario();
        assertEquals("Deuda pagada con éxito", client.payDebt(40, "Bancolombia", 36));
    }

    /**
     * prueba de metodo que paga una deuda de un cliente.
     * caso de un banco no encontrado
     */
    @Test
    public void testPayDebtWrongBank() {
        scenario();
        assertEquals("Banco no encontrado", client.payDebt(40, "Banco BBVA", 36));
    }

    /**
     * Prueba de metodo que verifica si un cliente ya pagó
     * (test puede fallar dependiendo del estado de las facturas)
     */
    @Test
    public void testHasCancelled() {
        scenario();
        assertEquals(true, client.hasCancelled(40));
    }


}
