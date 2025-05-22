package de.hamburg.gv.s2.dtvTabelle;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * Tabellenmodell für Datei-Tabelle
 * 
 * @author Florian Timm, LGV Hamburg
 * @author Stefanie Boese, BWVI Hamburg
 * @version 2016.10.28
 */
public class DateiTabelle extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    ArrayList<Datei> dateien;

    /**
     * Konstruktor des Tabellen-Modelles
     * 
     * @param dateien
     *            ArrayList mit Datei-Objekten
     */
    public DateiTabelle(ArrayList<Datei> dateien) {
	super();
	this.dateien = dateien;

    }

    /*
     * Gibt Anzahl der Tabellenspalten zurück = 3
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
	return 3;
    }

    /*
     * Gibt Anzahl der Tabellenzeilen zurück = Anzahl Einträge in der ArrayList
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
	return dateien.size();
    }

    /*
     * Gibt Daten für Zelle aus
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int zeile, int spalte) {
	switch (spalte) {
	case 0:
	    // erste Spalte = Bezeichnung der Datei = Jahr
	    return dateien.get(zeile).getName();
	case 1:
	    // zweite Spalte = Name der Datei
	    return dateien.get(zeile).getFile().getName();
	case 2:
	    // dritte Spalte = ausgewähltes Datenblatt
	    return dateien.get(zeile).getBlatt();
	}
	return "";
    }

    /*
     * Gibt Spaltenname zurück
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    public String getColumnName(int zeile) {
	switch (zeile) {
	case 0:
	    return "Jahr";
	case 1:
	    return "Datei";
	case 2:
	    return "Blatt (ungerundet)";
	}
	return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    @SuppressWarnings("unchecked")
    public Class getColumnClass(int c) {
	return getValueAt(0, c).getClass();
    }

    /*
     * Bestimmt, ob Daten der Zelle bearbeitbar sind
     * 
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int row, int spalte) {

	// Spalte 1 und 3 sollen bearbeitbar sein
	switch (spalte) {
	case 0:
	    return true;
	case 1:
	    return false;
	case 2:
	    return true;
	}
	return false;
    }

    /*
     * Löst aus, wenn Daten geändert wurden
     * 
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
     * int, int)
     */
    public void setValueAt(Object value, int row, int column) {
	if (column == 0) {
	    // Bezeichnung geändert
	    dateien.get(row).setName((String) value);
	} else if (column == 2) {
	    // Anderes Blatt ausgewählt
	    String wert = value.toString();
	    dateien.get(row).setBlatt(wert);
	}
    }

}
