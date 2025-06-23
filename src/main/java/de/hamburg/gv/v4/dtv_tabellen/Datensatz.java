package de.hamburg.gv.v4.dtv_tabellen;

import java.util.Map;

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

    public void setAnmerkung(String anmerkung, Map<String, String> map) {
        String a = map.get(anmerkung);
        if (a != null) {
            this.anmerkung = a;
        } else {
            this.anmerkung = anmerkung;
        }
    }

    public String getErhebung() {
        return erhebung;
    }

    public void setErhebung(String erhebung, Map<String, String> map) {
        String e = map.get(erhebung);
        if (e != null) {
            this.erhebung = e;
        } else {
            this.erhebung = erhebung;
        }
    }

    @Override
    public String toString() {
        return "Datensatz [zaehlstelle=" + zaehlstelle + ", zstNr=" + zstNr + ", anmerkung=" + anmerkung + ", erhebung="
                + erhebung + ", tensor=" + tensor + ", dtv=" + dtv + ", dtvw=" + dtvw + ", sv=" + sv + "]";
    }

    public Datensatz copy() {
        Datensatz d = new Datensatz();
        d.zaehlstelle = this.zaehlstelle;
        d.zstNr = this.zstNr;
        d.anmerkung = this.anmerkung;
        d.erhebung = this.erhebung;
        d.tensor = this.tensor;
        d.dtv = this.dtv;
        d.dtvw = this.dtvw;
        d.sv = this.sv;
        return d;
    }

}
