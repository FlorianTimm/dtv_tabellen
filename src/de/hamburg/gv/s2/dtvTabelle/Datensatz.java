package de.hamburg.gv.s2.dtvTabelle;

/**
 * Datensatz-Klasse (eine Zählung einer Station)
 * 
 * @author Florian Timm, LGV Hamburg
 * @author Stefanie Boese, BWVI Hamburg
 * @version 2016.10.28
 */
public class Datensatz {
    int ebene;
    String zaehlstelle;
    int dtv, dtvw, sv, zstNr = 0;

    /**
     * @return the zstNr
     */
    public int getZstNr() {
	return zstNr;
    }

    public String getZstNrStr() {
	if (zstNr == 0) {
	    return "";
	}
	return (new Integer(zstNr)).toString();
    }

    /**
     * @param zstNr
     *            the zstNr to set
     */
    public void setZstNr(int zstNr) {
	this.zstNr = zstNr;
    }

    String baustelle;

    public Datensatz() {
	// TODO Auto-generated constructor stub
    }

    /**
     * @return the ebene
     */
    public int getEbene() {
	return ebene;
    }

    /**
     * @param ebene
     *            the ebene to set
     */
    public void setEbene(int ebene) {
	this.ebene = ebene;
    }

    /**
     * @return the zaehlstelle
     */
    public String getZaehlstelle() {
	return zaehlstelle;
    }

    /**
     * @param zaehlstelle
     *            the zaehlstelle to set
     */
    public void setZaehlstelle(String zaehlstelle) {
	this.zaehlstelle = zaehlstelle;
    }

    /**
     * @return the dtv
     */
    public int getDtv() {
	return dtv;
    }

    /**
     * @param dtv
     *            the dtv to set
     */
    public void setDtv(int dtv) {
	this.dtv = dtv;
    }

    /**
     * @return the dtvw
     */
    public int getDtvw() {
	return dtvw;
    }

    /**
     * @param dtvw
     *            the dtvw to set
     */
    public void setDtvw(int dtvw) {
	this.dtvw = dtvw;
    }

    /**
     * @return the sv
     */
    public int getSv() {
	return sv;
    }

    /**
     * @param sv
     *            the sv to set
     */
    public void setSv(int sv) {
	this.sv = sv;
    }

    /**
     * @return the baustelle
     */
    public String getBaustelle() {
	return baustelle;
    }

    /**
     * @param baustelle
     *            the baustelle to set
     */
    public void setBaustelle(String baustelle) {
	this.baustelle = baustelle;
    }

}
