package de.hamburg.gv.v4.dtv_tabellen;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Datenumformer
 * 
 * @author Florian Timm, LGV Hamburg
 * @author Stefanie Boese, BVM Hamburg
 * @version 2016.10.28
 */

public class DataFormer extends JDialog {
	private static final long serialVersionUID = 1L;
	JTextArea jta;
	File datei;
	ArrayList<Datei> dateien;
	ArrayList<Map<Integer, Datensatz>> data;
	int runden;

	/**
	 * @param frame   Frame des Hauptfensters
	 * @param datei   Datei, in die geschrieben werden soll
	 * @param dateien Liste der einzulesenen Dateien
	 * @param runden  Anzahl der 10er-Potenzen, auf die gerundet werden soll
	 */
	public DataFormer(Frame frame, File datei, ArrayList<Datei> dateien, int runden) {
		super(frame);
		this.datei = datei;
		this.dateien = dateien;
		this.runden = runden;

		// GUI
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());

		jta = new JTextArea();
		JScrollPane jsp = new JScrollPane(jta);
		cp.add(jsp, BorderLayout.CENTER);

		this.setPreferredSize(new Dimension(400, 400));
		this.pack();
		this.setVisible(true);

		// Datenprozess
		umformen();
	}

	/**
	 * Export starten
	 */
	@SuppressWarnings("deprecation")
	private void umformen() {
		data = new ArrayList<Map<Integer, Datensatz>>();
		Set<Integer> tensoren = new HashSet<Integer>();

		// Daten einlesen aus Quelldateien
		for (int i = 0; dateien.size() > i; i++) {
			Datei d = dateien.get(i);
			Map<Integer, Datensatz> dateiDS = new HashMap<Integer, Datensatz>();
			jta.append("Lese Datei \"" + d.getFile().getName() + "\" ein...\n");
			try {
				Workbook wb = WorkbookFactory.create(d.getFile());
				Sheet sh = wb.getSheet(d.getBlatt());

				// Zeilen durchgehen
				for (int j = 2; j <= sh.getLastRowNum(); j++) {
					Row r = sh.getRow(j);
					// System.out.println(j);
					if (r == null) {
						// System.out.println("leer");
					} else {
						// jta.append(r.getCell(1).getStringCellValue()+"\n");
						Datensatz ds = new Datensatz();

						if (r.getCell(2) != null && r.getCell(2).getCellTypeEnum() == CellType.NUMERIC) {
							// Tensor
							ds.setTensor((int) r.getCell(2).getNumericCellValue());
						} else {
							continue; // Zeile überspringen, wenn Tensor fehlt
						}

						if (r.getCell(1) != null && r.getCell(1).getCellTypeEnum() == CellType.STRING) {
							// Zählstelle als String
							ds.setZstNr(r.getCell(1).getStringCellValue());
						}

						if (r.getCell(3) != null && r.getCell(3).getCellTypeEnum() == CellType.STRING) {
							ds.setZaehlstelle(r.getCell(3).getStringCellValue());
						}

						if (r.getCell(4) != null && r.getCell(4).getCellTypeEnum() == CellType.NUMERIC) {
							ds.setDtv((int) r.getCell(4).getNumericCellValue());
						}

						if (r.getCell(5) != null && r.getCell(5).getCellTypeEnum() == CellType.NUMERIC) {
							ds.setDtvw((int) r.getCell(5).getNumericCellValue());
						}

						if (r.getCell(6) != null && r.getCell(6).getCellTypeEnum() == CellType.NUMERIC) {
							double sv = r.getCell(6).getNumericCellValue();
							if (sv < 1. && sv > 0.) {
								sv *= 100;
							}
							ds.setSv((int) Math.round(sv));
						}

						if (r.getCell(7) != null && r.getCell(7).getCellTypeEnum() == CellType.STRING) {
							ds.setAnmerkung(r.getCell(7).getStringCellValue());
						}

						if (r.getCell(8) != null && r.getCell(8).getCellTypeEnum() == CellType.STRING) {
							ds.setErhebung(r.getCell(8).getStringCellValue());
						}

						dateiDS.put(ds.getTensor(), ds);
						tensoren.add(ds.getTensor());

					}
				}
				jta.append(dateiDS.size() + " Zeilen eingelesen\n");

			} catch (EncryptedDocumentException e) {
				jta.append("Fehler: Datei ist verschlüsselt!\n");
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				jta.append("Fehler: Dateiformat wurde nicht erkannt!\n");
				e.printStackTrace();
			} catch (IOException e) {
				jta.append("Fehler: Datei konnte nicht gelesen werden!\n");
				e.printStackTrace();
			}
			data.add(dateiDS);
		}

		List<Integer> tensorenS = new ArrayList<Integer>(tensoren);
		Collections.sort(tensorenS);

		// ENDE: Daten einlesen

		// Neue Datei erzeugen

		try {
			// Excel erzeugen
			Workbook wb = new XSSFWorkbook();
			Sheet mensch = wb.createSheet("DTV (Download)");
			Sheet edv = wb.createSheet("DTV (EDV)");

			Short row = 0;

			// Überschriften schreiben
			Row r1 = mensch.createRow(row++);
			r1.createCell(0).setCellValue("Zählstelle");
			r1.createCell(1).setCellValue("Tensor");
			r1.createCell(2).setCellValue("Bezeichnung");
			r1.createCell(3).setCellValue("Kategorie");

			// weitere Überschriften für jedes Jahr
			for (short jahrId = 0; jahrId < dateien.size(); jahrId++) {
				r1.createCell((short) jahrId + 4).setCellValue(dateien.get(jahrId).getName());
			}

			// Datenzeilen
			int letzteDatei = dateien.size() - 1;
			for (int tensorRowId = 0; tensorRowId < tensorenS.size(); tensorRowId++) {
				int tensor = tensorenS.get(tensorRowId);
				for (int valueRowId = 0; valueRowId < 5; valueRowId++) {
					try {
						Row r = mensch.createRow(row++);
						int dateiId = letzteDatei;
						while (data.get(dateiId).get(tensor) == null) {
							if (dateiId > 0) {
								dateiId -= 1;
							} else {
								continue;
							}
						}
						Datensatz zst = data.get(dateiId).get(tensor);
						r.createCell(0).setCellValue(zst.getZstNr());
						r.createCell(2).setCellValue(zst.getZaehlstelle());
						r.createCell(1).setCellValue(tensor);
						switch (valueRowId) {
						case 0:
							r.createCell(3).setCellValue("DTV (Kfz/24h)");
							for (short jahrId = 0; jahrId < data.size(); jahrId++) {
								if (data.get(jahrId).get(tensor) != null && data.get(jahrId).get(tensor).getDtv() > 0) {
									r.createCell((short) jahrId + 4)
											.setCellValue(runden(data.get(jahrId).get(tensor).getDtv()));
								}
							}
							break;
						case 1:
							r.createCell(3).setCellValue("DTVw (Kfz/24h)");
							for (short jahrId = 0; jahrId < data.size(); jahrId++) {
								if (data.get(jahrId).get(tensor) != null
										&& data.get(jahrId).get(tensor).getDtvw() > 0) {
									r.createCell((short) jahrId + 4)
											.setCellValue(runden(data.get(jahrId).get(tensor).getDtvw()));
								}
							}
							break;
						case 2:
							r.createCell(3).setCellValue("SV-Anteil am DTVw (%)");
							for (short j = 0; j < data.size(); j++) {
								if (data.get(j).get(tensor) != null && data.get(j).get(tensor).getSv() > 0) {
									r.createCell((short) j + 4).setCellValue(data.get(j).get(tensor).getSv());
								}
							}
							break;
						case 3:
							r.createCell(3).setCellValue("Erhebungsmethode");
							for (short j = 0; j < data.size(); j++) {
								if (data.get(j).get(tensor) != null) {
									r.createCell((short) j + 4).setCellValue(data.get(j).get(tensor).getErhebung());
								}
							}
							break;
						// Anmerkung
						case 4:
							r.createCell(3).setCellValue("Anmerkung");
							for (short j = 0; j < data.size(); j++) {
								if (data.get(j).get(tensor) != null) {
									r.createCell((short) j + 4).setCellValue(data.get(j).get(tensor).getAnmerkung());
								}
							}
							break;
						}

					} catch (Exception e) {
						e.printStackTrace();
						jta.append("Fehler!");
					}

				}
			}
			jta.append("Menschenlesbare Version fertig!");

			row = 0;

			r1 = edv.createRow(row++);
			r1.createCell(0).setCellValue("ZStNr");
			r1.createCell(1).setCellValue("Tensor");
			r1.createCell(2).setCellValue("Zaehlstelle");

			for (short j = 0; j < dateien.size(); j++) {
				r1.createCell((short) j * 5 + 3).setCellValue(dateien.get(j).getName() + "_DTV");
				r1.createCell((short) j * 5 + 4).setCellValue(dateien.get(j).getName() + "_DTVw");
				r1.createCell((short) j * 5 + 5).setCellValue(dateien.get(j).getName() + "_SV_am_DTVw");
				r1.createCell((short) j * 5 + 6).setCellValue(dateien.get(j).getName() + "_Erhebung");
				r1.createCell((short) j * 5 + 7).setCellValue(dateien.get(j).getName() + "_Anmerkung");
			}

			for (int i = 0; i < tensorenS.size(); i++) {
				int tensor = tensorenS.get(i);
				Row r = edv.createRow(row++);

				int l = letzteDatei;
				while (data.get(l).get(tensor) == null) {
					if (l > 0) {
						l -= 1;
					} else {
						continue;
					}
				}
				Datensatz zst = data.get(l).get(tensor);
				if (zst != null) {
					r.createCell(0).setCellValue(zst.getZstNr());
					r.createCell(2).setCellValue(zst.getZaehlstelle());
				}
				r.createCell(1).setCellValue(tensor);

				for (short j = 0; j < dateien.size(); j++) {

					if (data.get(j).get(tensor) != null && data.get(j).get(tensor).getDtv() >= 0) {
						r.createCell((short) j * 5 + 3).setCellValue(runden(data.get(j).get(tensor).getDtv()));
					}

					if (data.get(j).get(tensor) != null && data.get(j).get(tensor).getDtvw() >= 0) {
						r.createCell((short) j * 5 + 4).setCellValue(runden(data.get(j).get(tensor).getDtvw()));
					}

					if (data.get(j).get(tensor) != null && data.get(j).get(tensor).getSv() >= 0) {
						r.createCell((short) j * 5 + 5).setCellValue(data.get(j).get(tensor).getSv());
					}

					if (data.get(j).get(tensor) != null) {
						r.createCell((short) j * 5 + 6).setCellValue(data.get(j).get(tensor).getErhebung());
						r.createCell((short) j * 5 + 7).setCellValue(data.get(j).get(tensor).getAnmerkung());
					}

				}

			}

			FileOutputStream fileOut = new FileOutputStream(datei);
			wb.write(fileOut);
			fileOut.close();
			wb.close();
			jta.append("Fertig!\n");
		} catch (Exception e) {
			e.printStackTrace();
			jta.append("Fehler!\n");
		}
		jta.append("Ende!");
	}

	/**
	 * Rundet eine Zahl auf die in der Variable runden angegebenen Länge
	 * 
	 * @param zahl Zahl, die gerundet werden soll
	 * @return gerundete Zahl
	 */
	private int runden(int zahl) {
		if (runden != 0) {
			double dahl = (double) zahl;
			return (int) (Math.round(dahl / Math.pow(10, runden)) * Math.pow(10, runden));
		}
		return zahl;
	}

}
