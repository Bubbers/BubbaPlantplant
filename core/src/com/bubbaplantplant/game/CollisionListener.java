package com.bubbaplantplant.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.bubbaplantplant.game.component.CollisionComponent;

import java.util.List;

public class CollisionListener extends ContactListener {

    private List<Entity> entities;

    public CollisionListener(List<Entity> entities) {
        this.entities = entities;
    }

    @Override
    public void onContactStarted(int userValue0, boolean match0, int userValue1, boolean match1) {
        Entity entity0 = entities.get(userValue0);
        Entity entity1 = entities.get(userValue1);
        setOnCollision(entity0, entity1);
        setOnCollision(entity1, entity0);
    }

    @Override
    public void onContactEnded(int userValue0, boolean match0, int userValue1, boolean match1) {
        Entity entity0 = entities.get(userValue0);
        Entity entity1 = entities.get(userValue1);
        removeOnCollision(entity0, entity1);
        removeOnCollision(entity1, entity0);
    }

    private void setOnCollision(Entity entity0, Entity entity1) {
        CollisionComponent onCollisionComponent = entity0.getComponent(CollisionComponent.class);
        if (onCollisionComponent != null) {
            onCollisionComponent.addCollidingWith(entity1);
        }
    }

    private void removeOnCollision(Entity entity0, Entity entity1) {
        CollisionComponent onCollisionComponent = entity0.getComponent(CollisionComponent.class);
        if (onCollisionComponent != null) {
            onCollisionComponent.removeCollidingWith(entity1);
        }
    }
}
