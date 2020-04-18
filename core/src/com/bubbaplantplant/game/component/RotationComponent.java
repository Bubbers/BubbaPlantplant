package com.bubbaplantplant.game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Quaternion;

public class RotationComponent implements Component {

    private Quaternion quaternion = new Quaternion();

    public Quaternion getQuaternion() {
        return quaternion;
    }

    public void setQuaternion(Quaternion quaternion) {
        this.quaternion = quaternion;
    }
}
