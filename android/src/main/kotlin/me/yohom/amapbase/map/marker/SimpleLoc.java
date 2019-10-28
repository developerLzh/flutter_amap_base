package me.yohom.amapbase.map.marker;

public class SimpleLoc {
    public double lat;
    public double lng;
    public float bearing;
    public int dis;//距离 米
    public int time;//时间 秒

    @Override
    public String toString() {
        return "SimpleLoc{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", bearing=" + bearing +
                ", dis=" + dis +
                ", time=" + time +
                '}';
    }
}
