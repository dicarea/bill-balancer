package es.humarbean.gespagos.models;

import java.math.BigDecimal;
import java.util.List;

public class Group {
    private Integer id;
    private String nombre;
    private List<Payment> pagos;
    private BigDecimal deuda;
    private BigDecimal numAdultos;
    private BigDecimal numNenes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getDeuda() {
        return deuda;
    }

    public void setDeuda(BigDecimal deuda) {
        this.deuda = deuda;
    }


    public List<Payment> getPagos() {
        return pagos;
    }

    public void setPagos(List<Payment> pagos) {
        this.pagos = pagos;
    }

    public BigDecimal getNumAdultos() {
        return numAdultos;
    }

    public void setNumAdultos(BigDecimal numAdultos) {
        this.numAdultos = numAdultos;
    }

    public BigDecimal getNumNenes() {
        return numNenes;
    }

    public void setNumNenes(BigDecimal numNenes) {
        this.numNenes = numNenes;
    }
}
