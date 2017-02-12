package es.humarbean.gespagos;

import org.junit.Test;

import java.io.File;

public class AlgoritmoTest {
    @Test
    public void AlgoritmoTest() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("datosPrueba.xml").getFile());
    }
}