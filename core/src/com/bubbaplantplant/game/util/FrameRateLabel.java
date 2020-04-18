package com.bubbaplantplant.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.TimeUtils;

public class FrameRateLabel extends Label {
    long lastTimeCounted;
    private float sinceChange;

    public FrameRateLabel(LabelStyle labelStyle) {
        super("FPS: 0", labelStyle);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        update();
        super.draw(batch, parentAlpha);
    }

    public void update() {
        long delta = TimeUtils.timeSinceMillis(lastTimeCounted);
        lastTimeCounted = TimeUtils.millis();

        sinceChange += delta;
        if(sinceChange >= 1000) {
            sinceChange = 0;
            setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        }
    }
}