package com.bubbaplantplant.game.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector3;
import com.bubbaplantplant.game.component.CollisionComponent;
import com.bubbaplantplant.game.component.PickUpableComponent;
import com.bubbaplantplant.game.component.PlayerComponent;
import com.bubbaplantplant.game.component.PositionComponent;

import java.util.List;

public class PickUpOnCollisionSystem extends EntitySystem {
    @Override
    public void update(float deltaTime) {
        Entity playerEntity = getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
        PositionComponent playerPosition = playerEntity.getComponent(PositionComponent.class);
        List<Entity> collidingWith = playerEntity.getComponent(CollisionComponent.class).getCollidingWith();

        for (Entity entity : collidingWith) {
            if (entity.getComponent(PickUpableComponent.class) != null) {
                entity.getComponent(PositionComponent.class).setPosition(new Vector3(0.0f, 1.0f, 1.0f).add(playerPosition.getPosition()));
            }
        }
    }
}
