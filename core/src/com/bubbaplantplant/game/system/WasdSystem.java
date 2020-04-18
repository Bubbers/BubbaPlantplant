package com.bubbaplantplant.game.system;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.bubbaplantplant.game.component.PlayerComponent;
import com.bubbaplantplant.game.component.PositionComponent;

public class WasdSystem extends EntitySystem {

    private static final float SPEED = 5f;

    public WasdSystem() {
    }

    @Override
    public void update(float deltaTime) {
        PositionComponent posComponent = getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get()).first().getComponent(PositionComponent.class);

        Vector3 delta = new Vector3();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            delta.z = -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            delta.z = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            delta.x = -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            delta.x = 1;
        }

        posComponent.getPosition().add(delta.nor().scl(SPEED*deltaTime));
    }
}
