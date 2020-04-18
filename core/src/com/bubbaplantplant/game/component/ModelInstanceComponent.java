package com.bubbaplantplant.game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class ModelInstanceComponent implements Component {

    ModelInstance modelInstance;

    public ModelInstanceComponent(ModelInstance modelInstance) {
        this.modelInstance = modelInstance;
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }
}
