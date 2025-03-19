package me.lofro.camerautils.data.types;

public class ConfigData {

    public double zoom;
    public double zoomSensibility;

    public double from;
    public int time;

    public ConfigData() {
        zoom = 0.1;
        zoomSensibility = 0.01;
        from = 1;
        time = 1;
    }

    public ConfigData(double zoom, double zoomSensibility, double from, int time) {
        this.zoom = zoom;
        this.zoomSensibility = zoomSensibility;
        this.from = from;
        this.time = time;
    }

}
