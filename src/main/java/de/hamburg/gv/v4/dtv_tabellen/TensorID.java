package de.hamburg.gv.v4.dtv_tabellen;

public class TensorID implements Comparable<TensorID> {

    private final int tensor;
    private final String zstNr;
    private final String name;

    public TensorID(int id, String zstNr, String name) {
        this.tensor = id;
        this.zstNr = zstNr;
        this.name = name;
    }

    public int getTensor() {
        return tensor;
    }

    public String getZstNr() {
        return zstNr;
    }

    @Override
    public int compareTo(TensorID other) {
        return Integer.compare(this.tensor, other.tensor);
    }

    @Override
    public String toString() {
        return "TensorID{" +
                "tensor=" + tensor +
                ", zstNr='" + zstNr + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
    public String getName() {
        return name;
    }
    
}
