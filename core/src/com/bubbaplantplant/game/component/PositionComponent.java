package com.bubbaplantplant.game.component;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.ashley.core.Component;

public class PositionComponent implements Component {
    Vector3 position;

    public PositionComponent(Vector3 position) {
        this.position = position;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }
}
