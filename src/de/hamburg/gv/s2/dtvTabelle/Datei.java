package de.hamburg.gv.s2.dtvTabelle;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Datei-Klasse (eine DTV-Excel-Datei)
 * 
 * @author Florian Timm, LGV Hamburg
 * @author Stefanie Boese, BWVI Hamburg
 * @version 2016.10.28
 */

public class Datei {
    File file;
    String name;
    String blatt = "";

    /**
     * Konstruktor
     * @param file Datei aus Auswahldialog
     */
    public Datei(File file) {
	this.file = file;
	
	// Sucht nach 20?? im Dateinamen um hierdurch Bezeichnung herauszufinden (Jahreszahl zwischen 2000-2099)
	Pattern pat = Pattern.compile("20\\d{2}");
	Matcher mat = pat.matcher(file.getName());
	if (mat.find()) {
	    this.setName(mat.group());
	}

	// 
	try {
	    Workbook wb = WorkbookFactory.create(this.getFile());
	    if (wb.getNumberOfSheets() > 0) {
		this.blatt = wb.getSheetName(0);
	    } else {
		this.blatt = "";
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public Datei(File file, String name) {
	this.file = file;
	this.name = name;
    }

    public File getFile() {
	return file;
    }

    public void setFile(File file) {
	this.file = file;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return the blatt
     */
    public String getBlatt() {
	return blatt;
    }

    /**
     * @param blatt
     *            the blatt to set
     */
    public void setBlatt(String blatt) {
	this.blatt = blatt;
    }

    public String[] getBlaetter() {
	String[] array = null;
	try {
	    Workbook wb = WorkbookFactory.create(this.getFile());
	    array = new String[wb.getNumberOfSheets()];
	    for (int i = 0; i < wb.getNumberOfSheets(); i++) {
		array[i] = wb.getSheetName(i);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return array;
    }

}
