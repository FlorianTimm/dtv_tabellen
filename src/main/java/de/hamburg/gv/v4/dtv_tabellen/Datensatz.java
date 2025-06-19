package de.hamburg.gv.v4.dtv_tabellen;

/**
 * Datensatz-Klasse (eine Zählung einer Station)
 * 
 * @author Florian Timm, LGV Hamburg
 * @author Stefanie Boese, BVM Hamburg
 * @version 2016.10.28
 */
public class Datensatz {
    String zaehlstelle = "", zstNr = "", anmerkung = "", erhebung = "";
    int tensor = -1, dtv = -1, dtvw = -1, sv = -1;

    public String getZstNr() {
        if (zstNr == null || zstNr.isEmpty()) {
            return "";
        }
        return zstNr;
    }

    public void setZstNr(String zstNr) {
        this.zstNr = zstNr;
    }

    public int getTensor() {
        return tensor;
    }


    public void setTensor(int tensor) {
        this.tensor = tensor;
    }


    public String getZaehlstelle() {
        return zaehlstelle;
    }


    public void setZaehlstelle(String zaehlstelle) {
        this.zaehlstelle = zaehlstelle;
    }


    public int getDtv() {
        return dtv;
    }


    public void setDtv(int dtv) {
        this.dtv = dtv;
    }


    public int getDtvw() {
        return dtvw;
    }


    public void setDtvw(int dtvw) {
        this.dtvw = dtvw;
    }


    public int getSv() {
        return sv;
    }


    public void setSv(int sv) {
        this.sv = sv;
    }

    public String getAnmerkung() {
        return anmerkung;
    }

    public void setAnmerkung(String anmerkung) {
        this.anmerkung = anmerkung;
    }

    public String getErhebung() {
        return erhebung;
    }

    public void setErhebung(String erhebung) {
        this.erhebung = erhebung;
    }

    @Override
    public String toString() {
        return "Datensatz [zaehlstelle=" + zaehlstelle + ", zstNr=" + zstNr + ", anmerkung=" + anmerkung + ", erhebung="
                + erhebung + ", tensor=" + tensor + ", dtv=" + dtv + ", dtvw=" + dtvw + ", sv=" + sv + "]";
    }

}
