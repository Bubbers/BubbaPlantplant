package com.bubbaplantplant.game.system;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bubbaplantplant.game.component.PlayerComponent;
import com.bubbaplantplant.game.component.PositionComponent;
import com.bubbaplantplant.game.component.RotationComponent;

public class WasdSystem extends EntitySystem {


    public static final Vector3 UP = new Vector3(0, 1, 0);
    private static final float SPEED = 5f;

    @Override
    public void update(float deltaTime) {
        Entity player = getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
        PositionComponent posComponent = player.getComponent(PositionComponent.class);
        RotationComponent rotComponent = player.getComponent(RotationComponent.class);

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
        float angle = new Vector2(delta.x, delta.z).angle();
        rotComponent.getQuaternion().set(UP, 360 - angle + 90);

        posComponent.getPosition().add(delta.nor().scl(SPEED*deltaTime));
    }
}
