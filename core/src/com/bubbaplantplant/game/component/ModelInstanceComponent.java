package com.bubbaplantplant.game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

public class ModelInstanceComponent implements Component {

    ModelInstance modelInstance;
    btCollisionObject collisionObject;

    public ModelInstanceComponent(ModelInstance modelInstance) {
        this.modelInstance = modelInstance;
    }

    public ModelInstanceComponent withCollisionObject(btCollisionObject collisionObject) {
        this.collisionObject = collisionObject;
        return this;
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public btCollisionObject getCollisionObject() {
        return collisionObject;
    }
}
