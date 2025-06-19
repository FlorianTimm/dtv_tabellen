package de.hamburg.gv.v4.dtv_tabellen;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

/**
 * Programm zum Umwandeln von DTV-Tabellen für BVM
 * 
 * @author Florian Timm, LGV Hamburg
 * @author Stefanie Boese, BVM Hamburg
 * @version 2016.10.28
 */
public class App extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	// Globale Variablen erzeugen
	DateiTabelle tableModell;
	ArrayList<Datei> dateien;

	// Globale GUI-Elemente
	JTable table;
	JComboBox<String> jcbRunden;
	JCheckBox cbRunden;
	JTextField transTablePath;

	/**
	 * Konstruktur GUI
	 */
	public App() {
		// Grundlegende Fenster-Einstellungen
		super("DTV-Tabelle erzeugen");
		dateien = new ArrayList<Datei>();
		this.setPreferredSize(new Dimension(600, 400));
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());

		JPanel oben = new JPanel();
		oben.setLayout(new BorderLayout());
		cp.add(oben, BorderLayout.NORTH);

		this.transTablePath = new JTextField();
		this.transTablePath.setEditable(false);
		this.transTablePath.setText("(keine Übersetzungstabelle)");
		this.transTablePath.setHorizontalAlignment(JTextField.CENTER);
		oben.add(this.transTablePath, BorderLayout.CENTER);

		JButton transTableButton = new JButton("auswählen...");
		transTableButton.setActionCommand("transTable");
		transTableButton.addActionListener(this);
		// Button zum Speichern der Tabelle
		oben.add(transTableButton, BorderLayout.EAST);

		// Laden-Button
		JButton laden = new JButton("Datensätze laden...");
		laden.setActionCommand("laden");
		laden.addActionListener(this);
		oben.add(laden, BorderLayout.SOUTH);

		// Tabelle erzeugen
		table = new JTable() {
			private static final long serialVersionUID = 1L;

			// Determine editor to be used by row
			public TableCellEditor getCellEditor(int row, int column) {
				int modelColumn = convertColumnIndexToModel(column);

				if (modelColumn == 2) {
					String[] items = dateien.get(row).getBlaetter();
					JComboBox<String> comboBox = new JComboBox<String>(items);
					DefaultCellEditor dce = new DefaultCellEditor(comboBox);
					return dce;
				} else {
					return super.getCellEditor(row, column);
				}
			}
		};
		aktualisiereTabelle();

		JScrollPane jsp = new JScrollPane(table);
		cp.add(jsp, BorderLayout.CENTER);

		// Menü-Leiste
		JMenuBar jmb = new JMenuBar();
		JMenu jm = new JMenu("Info");
		JMenuItem jmi = new JMenuItem("Lizenzbedingungen");
		jmi.addActionListener(this);
		jm.add(jmi);
		jmb.add(jm);
		this.setJMenuBar(jmb);

		// Rechter Bereich (hoch, löschen, runter)
		JPanel hr = new JPanel();
		hr.setLayout(new GridLayout(3, 1));
		JButton hoch = new JButton("^");
		hoch.setActionCommand("hoch");
		hoch.addActionListener(this);
		hr.add(hoch);
		JButton entf = new JButton("Entf");
		entf.setActionCommand("entf");
		entf.addActionListener(this);
		hr.add(entf);
		JButton runter = new JButton("v");
		runter.setActionCommand("runter");
		runter.addActionListener(this);
		hr.add(runter);
		cp.add(hr, BorderLayout.EAST);

		// Unterer Bereich
		JPanel sueden = new JPanel();
		sueden.setLayout(new BorderLayout());

		// Rundungseinstellungen
		cbRunden = new JCheckBox("Daten runden");
		cbRunden.setSelected(true);
		sueden.add(cbRunden, BorderLayout.WEST);
		String[] jcbS = { "auf 1 Stelle  (123460)", "auf 2 Stellen (123500)", "auf 3 Stellen (123000)",
				"auf 4 Stellen (120000)" };
		jcbRunden = new JComboBox<String>(jcbS);
		jcbRunden.setSelectedIndex(2);
		sueden.add(jcbRunden, BorderLayout.CENTER);

		// Speichern-Button
		JButton speichern = new JButton("Speichern und Umformen...");
		speichern.setActionCommand("speichern");
		speichern.addActionListener(this);
		sueden.add(speichern, BorderLayout.SOUTH);

		cp.add(sueden, BorderLayout.SOUTH);

		// Fenster darstellen
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	/*
	 * Wird ausgel�st, wenn Button oder Men�-Punkt geklickt wird
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		switch (ae.getActionCommand()) {
			case "laden":
				laden();
				break;
			case "speichern":
				speichern();
				break;
			case "hoch":
				hoch();
				break;
			case "runter":
				runter();
				break;
			case "entf":
				entf();
				break;
			case "transTable":
				transTable();
				break;

			case "Lizenzbedingungen...":
				String txt = "Programmiert von Florian Timm, LGV (2025)\n\nSoftware verwendet:\nApache POI\nCopyright 2003-2016 The Apache Software Foundation\n\nThis product includes software developed by\nThe Apache Software Foundation (https://www.apache.org/).\n\nThis product contains parts that were originally based on software from BEA.\nCopyright (c) 2000-2003, BEA Systems, <http://www.bea.com/>.\n\nThis product contains W3C XML Schema documents. Copyright 2001-2003 (c)\nWorld Wide Web Consortium (Massachusetts Institute of Technology, European\nResearch Consortium for Informatics and Mathematics, Keio University)\n\nThis product contains the Piccolo XML Parser for Java\n(http://piccolo.sourceforge.net/). Copyright 2002 Yuval Oren.\n\nThis product contains the chunks_parse_cmds.tbl file from the vsdump program.\nCopyright (C) 2006-2007 Valek Filippov (frob@df.ru)\n\nThis product contains parts of the eID Applet project \n(http://eid-applet.googlecode.com). Copyright (c) 2009-2014\nFedICT (federal ICT department of Belgium), e-Contract.be BVBA (https://www.e-contract.be),\nBart Hanssens from FedICT\n";
				JOptionPane.showMessageDialog(null, txt);
				break;
		}
	}

	/**
	 * Ausgelöst durch Klick auf "Daten laden"
	 */
	private void laden() {
		JFileChooser jfc = new JFileChooser("G:\\AG_Verkehrsdaten\\Projekte\\DTV-Tabelle\\Originaldateien");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel-Tabellen", "xls", "xlsx");
		jfc.setFileFilter(filter);
		jfc.setMultiSelectionEnabled(true);
		jfc.setAcceptAllFileFilterUsed(false);
		int ok = jfc.showOpenDialog(this);

		// Wenn Datei ausgewählt wurde
		if (ok == JFileChooser.APPROVE_OPTION) {
			File[] dateienAuswahl = jfc.getSelectedFiles();
			for (int i = 0; dateienAuswahl.length > i; i++) {
				// Dateien zur ArrayList hinzufügen
				dateien.add(new Datei(dateienAuswahl[i]));
			}
		}

		// Tabelle aktualisieren (neue Daten hinzufügen)
		aktualisiereTabelle();
	}

	/**
	 * Lädt die Daten aus der ArrayList dateien und stellt diese in der Tabelle da
	 * z.B. nach dem Laden oder Umsortieren von Dateien
	 */
	private void aktualisiereTabelle() {
		table.setModel(new DateiTabelle(dateien));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(25);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(2).setPreferredWidth(200);
		table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setToolTipText("Datentabelle auswählen (KEINE Formeln)");
		table.getColumnModel().getColumn(2).setCellRenderer(renderer);

	}

	/**
	 * Löst den Exportvorgang aus nach Klick auf Speichern
	 */
	private void speichern() {
		// Mindestens 1 Datei muss eingelesen worden sein
		if (dateien.size() > 0) {
			// Datei-Speichern-Fenster
			JFileChooser jfc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel-Tabellen", "xlsx");
			jfc.setFileFilter(filter);
			jfc.setAcceptAllFileFilterUsed(false);
			int ok = jfc.showSaveDialog(this);
			// Speicherort ausgewählt
			if (ok == JFileChooser.APPROVE_OPTION) {
				File datei = jfc.getSelectedFile();
				// Dateiendung prüfen und ggf. anhängen
				if (!datei.getName().toLowerCase().endsWith(".xlsx")) {
					datei = new File(datei.getAbsolutePath() + ".xlsx");
				}

				// Rundung einstellen
				int runden = 0;
				if (cbRunden.isSelected()) {
					runden = jcbRunden.getSelectedIndex() + 1;
				}

				if (transTablePath.getText().equals("(keine Übersetzungstabelle)")) {
					// Exportklasse anstarten
					new DataFormer(this, datei, dateien, runden);
				} else {
					File transTable = new File(transTablePath.getText());
					if (transTable.exists()) {
						// Exportklasse anstarten
						new DataFormer(this, datei, dateien, runden, transTable);
					} else {
						JOptionPane.showMessageDialog(null, "Übersetzungstabelle nicht gefunden!");
					}
				}

			}
		} else {
			// Fehlermeldung falls keine Datei geladen war
			JOptionPane.showMessageDialog(null, "Bitte vorher Dateien laden!");
		}
	}

	/**
	 * wird durch Klick auf den Nach-Oben-Schieben-Button ausgelöst Verschiebt
	 * ausgewählten Eintrag in der ArrayList nach oben und aktualisiert Tabelle
	 */
	private void hoch() {
		int i = table.getSelectedRow();
		// Auswahl darf nicht oberster Eintrag oder leer sein
		if (i > 0 && i != -1) {
			// Eintrag verschieben
			Datei dat = dateien.get(i);
			dateien.set(i, dateien.get(i - 1));
			dateien.set(i - 1, dat);
			aktualisiereTabelle();
			// wählt Eintrag wieder aus (um ggf. weiterzuschieben)
			table.getSelectionModel().setSelectionInterval(i - 1, i - 1);
		}
	}

	/**
	 * wird durch Klick auf den Nach-Unten-Schieben-Button ausgelöst Verschiebt
	 * ausgewählten Eintrag in der ArrayList nach unten und aktualisiert Tabelle
	 */
	private void runter() {
		int i = table.getSelectedRow();
		// Auswahl darf nicht untester Eintrag oder leer sein
		if (i < (dateien.size() - 1) && i != -1) {
			// Eintrag verschieben
			Datei dat = dateien.get(i);
			dateien.set(i, dateien.get(i + 1));
			dateien.set(i + 1, dat);
			aktualisiereTabelle();
			// wählt Eintrag wieder aus (um ggf. weiterzuschieben)
			table.getSelectionModel().setSelectionInterval(i + 1, i + 1);
		}
	}

	/**
	 * wird durch Klick auf den Entfernen-Button ausgelöst Löscht aktuell
	 * ausgewählten Eintrag und aktualisiert Tabelle
	 */
	private void entf() {
		int i = table.getSelectedRow();
		if (i != -1) {
			dateien.remove(i);
			aktualisiereTabelle();
		}
	}

	private void transTable() {
		JFileChooser jfc = new JFileChooser("G:\\AG_Verkehrsdaten\\Projekte\\DTV-Tabelle\\Übersetzungstabellen");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel-Tabellen", "xls", "xlsx");
		jfc.setFileFilter(filter);
		jfc.setAcceptAllFileFilterUsed(false);
		int ok = jfc.showOpenDialog(this);

		if (ok == JFileChooser.APPROVE_OPTION) {
			File datei = jfc.getSelectedFile();
			if (datei != null) {
				this.transTablePath.setText(datei.getAbsolutePath());
			}
		}
	}

	public static void main(String[] args) {
		new App();

	}

}
