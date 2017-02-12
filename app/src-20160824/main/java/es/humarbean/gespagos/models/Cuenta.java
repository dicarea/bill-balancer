package es.humarbean.gespagos.models;

import java.util.Date;
import java.util.List;

public class Cuenta {
    private Integer id;
    private String nombre;
    private Date fecha;
    private String lugar;
    private List<Group> mGroups;

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Group> getGroups() {
        return mGroups;
    }

    public void setGroups(List<Group> groups) {
        this.mGroups = groups;
    }
}
