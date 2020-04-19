package com.bubbaplantplant.game.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.bubbaplantplant.game.component.ModelInstanceComponent;
import com.bubbaplantplant.game.component.MultiModelComponent;

public class MultiModelSystem extends EntitySystem {

    @Override
    public void update(float deltaTime) {
        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(Family.all(MultiModelComponent.class, ModelInstanceComponent.class).get());
        for (Entity entity : entities) {
            ModelInstanceComponent modelComponent = entity.getComponent(ModelInstanceComponent.class);
            ModelInstance prevModelInstance = modelComponent.getModelInstance();
            modelComponent.setModelInstance(entity.getComponent(MultiModelComponent.class).getSelectedModel());
            modelComponent.getModelInstance().transform.set(prevModelInstance.transform);
        }
    }
}
