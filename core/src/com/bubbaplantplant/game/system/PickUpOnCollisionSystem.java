package com.bubbaplantplant.game.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.bubbaplantplant.game.component.CollisionComponent;
import com.bubbaplantplant.game.component.PickUpableComponent;
import com.bubbaplantplant.game.component.PlayerComponent;

import java.util.List;

public class PickUpOnCollisionSystem extends EntitySystem {
    @Override
    public void update(float deltaTime) {
        Entity playerEntity = getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
        List<Entity> collidingWith = playerEntity.getComponent(CollisionComponent.class).getCollidingWith();

        for (Entity entity : collidingWith) {
            if (entity.getComponent(PickUpableComponent.class) != null) {
                System.out.println("Will pick this item up");
            }
        }
    }
}
