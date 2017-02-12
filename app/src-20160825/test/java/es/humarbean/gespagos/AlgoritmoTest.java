package es.humarbean.gespagos;

import org.junit.Test;

import java.io.File;

import es.humarbean.gespagos.models.Cuenta;

import static org.junit.Assert.*;

public class AlgoritmoTest {
    @Test
    public void AlgoritmoTest() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("datosPrueba.xml").getFile());
    }
}