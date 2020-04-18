package com.bubbaplantplant.game.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.bubbaplantplant.game.component.ModelInstanceComponent;
import com.bubbaplantplant.game.component.PositionComponent;
import com.bubbaplantplant.game.component.RotationComponent;

public class ModelTransformUpdaterSystem extends EntitySystem {

    private btCollisionWorld collisionWorld;

    public ModelTransformUpdaterSystem(btCollisionWorld collisionWorld) {
        this.collisionWorld = collisionWorld;
    }

    @Override
    public void update(float deltaTime) {
        collisionWorld.performDiscreteCollisionDetection();

        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(Family.all(ModelInstanceComponent.class).get());
        for (Entity entity : entities) {
            PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
            RotationComponent rotationComponent = entity.getComponent(RotationComponent.class);

            ModelInstanceComponent component = entity.getComponent(ModelInstanceComponent.class);
            ModelInstance modelInstance = component.getModelInstance();
            btCollisionObject collisionObject = component.getCollisionObject();
            if (rotationComponent != null) {
                System.out.println("rot " + rotationComponent.getQuaternion().getAngle());
                modelInstance.transform.set(rotationComponent.getQuaternion());
            }
            if (positionComponent != null) {
                modelInstance.transform.setTranslation(positionComponent.getPosition());
            }
            if (collisionObject != null) {
                collisionObject.setWorldTransform(modelInstance.transform);
            }
        }
    }
}
