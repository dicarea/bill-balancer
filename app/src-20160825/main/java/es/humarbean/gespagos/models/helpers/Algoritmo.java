package es.humarbean.gespagos.models.helpers;

import java.math.BigDecimal;
import java.util.List;

import es.humarbean.gespagos.models.Cuenta;
import es.humarbean.gespagos.models.Group;
import es.humarbean.gespagos.models.Payment;

public class Algoritmo {

    private static BigDecimal PESO_NENE = new BigDecimal(0.50d);

    public void calcularDeudas(Cuenta cuenta) {
        List<Group> groups = cuenta.getGroups();

        /* Calcular total de pagos en la cuenta. */
        BigDecimal totalPagos = BigDecimal.ZERO;
        for (Group group : groups) {
            BigDecimal pagosPersona = this.getPagosGrupo(group);
            totalPagos = totalPagos.add(pagosPersona);
        }

        /* Calcular total de personas en la cuenta. */
        BigDecimal numPersonas = BigDecimal.ZERO;
        for (Group group : groups) {
            numPersonas = numPersonas.add(getNumPersonas(group));
        }

        /* Precio por persona. */
        BigDecimal ppp = totalPagos.divide(numPersonas);

        /* Se guarda el precio por grupo. */
        for (Group group : groups) {
            group.setDeuda(ppp.multiply(this.getNumPersonas(group)));
        }

        /* Se descuenta lo ya pagado por cada grupo. */
        for (Group group : groups) {
            BigDecimal pagosGrupo = this.getPagosGrupo(group);
            group.setDeuda(group.getDeuda().subtract(pagosGrupo));
        }

        /* Ya se tiene la deuda neta de cada grupo. */

    }

    //**********************************

    private BigDecimal getNumPersonas(Group group) {
        BigDecimal numAdultos = group.getNumAdultos();
        BigDecimal numNenes = group.getNumNenes().multiply(PESO_NENE);
        BigDecimal personas = numAdultos.add(numNenes);
        return personas;
    }

    private BigDecimal getPagosGrupo(Group group) {
        BigDecimal aportacionPersona = BigDecimal.ZERO;
        List<Payment> aportaciones = group.getPagos();
        for (Payment ticket : aportaciones) {
           // aportacionPersona = aportacionPersona.add(ticket.getImporte());
        }
        return aportacionPersona;
    }
}
