package com.bubbaplantplant.game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

import java.util.ArrayList;
import java.util.List;

public class CollisionComponent implements Component {

     private List<Entity> collidingWith = new ArrayList<>();

    public List<Entity> getCollidingWith() {
        return collidingWith;
    }

    public void addCollidingWith(Entity entitiy) {
        collidingWith.add(entitiy);
    }

    public void removeCollidingWith(Entity entity) {
        collidingWith.remove(entity);
    }
}
