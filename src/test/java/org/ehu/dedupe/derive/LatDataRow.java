package org.ehu.dedupe.derive;

import org.ehu.dedupe.data.DataRow;

public class LatDataRow extends DataRow {

    private Float lat;

    public LatDataRow(Comparable<Integer> id1, Comparable<Integer> id2, Boolean duplicate) {
        super(id1, id2, duplicate);
        this.lat = lat;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }
}
