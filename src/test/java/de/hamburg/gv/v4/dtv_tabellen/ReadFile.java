package de.hamburg.gv.v4.dtv_tabellen;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;

public class ReadFile {

    private static File file;

    @BeforeAll
    public static void setUp() {

        // Delete the file before running the test to ensure a clean state
        file = new File("src/test/resources/test_export.xlsx");
        if (file.exists()) {
            file.delete();
        }
        // Set the system property to avoid the warning about the default locale
        ArrayList<Datei> dateien = new ArrayList<>();
        dateien.add(new Datei(new File("src/test/resources/DTV_DTVw_2016.xlsx")));
        dateien.add(new Datei(new File("src/test/resources/DTV_DTVw_2017.xlsx")));
        dateien.add(new Datei(new File("src/test/resources/DTV_DTVw_2018.xlsx")));
        dateien.add(new Datei(new File("src/test/resources/DTV_DTVw_2019.xlsx")));
        dateien.add(new Datei(new File("src/test/resources/DTV_DTVw_2020.xlsx")));
        dateien.add(new Datei(new File("src/test/resources/DTV_DTVw_2021.xlsx")));
        dateien.add(new Datei(new File("src/test/resources/DTV_DTVw_2022.xlsx")));
        dateien.add(new Datei(new File("src/test/resources/DTV_DTVw_2023.xlsx")));
        dateien.add(new Datei(new File("src/test/resources/DTV_DTVw_2024.xlsx")));

        JFrame frame = new JFrame();

        new DataFormer(frame, file, dateien, 3);

    }

    @Test
    public void fileCreated() {
        assertTrue(file.exists(), "Die Datei wurde nicht erstellt: " + file.getAbsolutePath());
    }

    @Test
    public void fileIsNotEmpty() {
        assertTrue(file.length() > 0, "Die Datei ist leer: " + file.getAbsolutePath());
    }
}
