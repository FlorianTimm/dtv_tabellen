package de.hamburg.gv.v4.dtv_tabellen;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * Unit test for simple App.
 */
public class AppTest {

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void loadFile() {
        Datei datei = new Datei(new File("src/test/resources/DTV_DTVw_2016.xlsx"));
        assertTrue(datei.getName().equals("2016"));
        assertTrue(datei.getBlatt().equals("DTV_DTVw"));
    }
}
