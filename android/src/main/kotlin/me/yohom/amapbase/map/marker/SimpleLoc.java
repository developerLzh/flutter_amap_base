package me.yohom.amapbase.map.marker;

public class SimpleLoc {
    public double lat;
    public double lng;
    public Float bearing;

    @Override
    public String toString() {
        return "SimpleLoc{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", bearing=" + bearing +
                '}';
    }
}
