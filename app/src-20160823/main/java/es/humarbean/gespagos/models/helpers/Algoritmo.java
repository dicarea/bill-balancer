package es.humarbean.gespagos.models.helpers;

import java.math.BigDecimal;
import java.util.List;

import es.humarbean.gespagos.models.Cuenta;
import es.humarbean.gespagos.models.Grupo;
import es.humarbean.gespagos.models.Pago;

public class Algoritmo {

    private static BigDecimal PESO_NENE = new BigDecimal(0.50d);

    public void calcularDeudas(Cuenta cuenta) {
        List<Grupo> grupos = cuenta.getGrupos();

        /* Calcular total de pagos en la cuenta. */
        BigDecimal totalPagos = BigDecimal.ZERO;
        for (Grupo grupo : grupos) {
            BigDecimal pagosPersona = this.getPagosGrupo(grupo);
            totalPagos = totalPagos.add(pagosPersona);
        }

        /* Calcular total de personas en la cuenta. */
        BigDecimal numPersonas = BigDecimal.ZERO;
        for (Grupo grupo : grupos) {
            numPersonas = numPersonas.add(getNumPersonas(grupo));
        }

        /* Precio por persona. */
        BigDecimal ppp = totalPagos.divide(numPersonas);

        /* Se guarda el precio por grupo. */
        for (Grupo grupo : grupos) {
            grupo.setDeuda(ppp.multiply(this.getNumPersonas(grupo)));
        }

        /* Se descuenta lo ya pagado por cada grupo. */
        for (Grupo grupo : grupos) {
            BigDecimal pagosGrupo = this.getPagosGrupo(grupo);
            grupo.setDeuda(grupo.getDeuda().subtract(pagosGrupo));
        }

        /* Ya se tiene la deuda neta de cada grupo. */

    }

    //**********************************

    private BigDecimal getNumPersonas(Grupo grupo) {
        BigDecimal numAdultos = grupo.getNumAdultos();
        BigDecimal numNenes = grupo.getNumNenes().multiply(PESO_NENE);
        BigDecimal personas = numAdultos.add(numNenes);
        return personas;
    }

    private BigDecimal getPagosGrupo(Grupo grupo) {
        BigDecimal aportacionPersona = BigDecimal.ZERO;
        List<Pago> aportaciones = grupo.getPagos();
        for (Pago ticket : aportaciones) {
            aportacionPersona = aportacionPersona.add(ticket.getImporte());
        }
        return aportacionPersona;
    }
}
