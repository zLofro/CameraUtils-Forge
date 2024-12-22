package me.lofro.camerautils.utils;

import net.minecraft.util.Mth;

public class ZoomTrack {

    private final float from;
    private final float to;
    private final int totalTime;
    private int time;

    public ZoomTrack(float from, float to, int totalTime) {
        this.from = from;
        this.to = to;
        this.totalTime = totalTime;
    }

    public void tick() {
        time++;
    }

    public float getCurrentFOV() {
        float perc = Mth.clamp((float) time / (float) totalTime, 0F, 1F);
        return Mth.lerp(perc, from, to);
    }

}
