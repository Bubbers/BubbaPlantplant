package com.bubbaplantplant.game.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestNotMeRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.bubbaplantplant.game.component.*;

import java.util.List;

public class PickUpOnCollisionSystem extends EntitySystem {

    private Camera camera;
    private btCollisionWorld collisionWorld;
    private List<Entity> entities;
    private Entity entityHeld = null;

    public PickUpOnCollisionSystem(Camera camera, btCollisionWorld collisionWorld, List<Entity> entities) {
        this.camera = camera;
        this.collisionWorld = collisionWorld;
        this.entities = entities;
    }

    @Override
    public void update(float deltaTime) {
        Entity playerEntity = getEngine().getEntitiesFor(Family.all(PlayerComponent.class).get()).first();
        PositionComponent playerPosition = playerEntity.getComponent(PositionComponent.class);
        List<Entity> collidingWith = playerEntity.getComponent(CollisionComponent.class).getCollidingWith();

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !collidingWith.isEmpty() && entityHeld == null) {
            Ray pickRay = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());

            Vector3 rayFrom = pickRay.origin;
            Vector3 rayTo = pickRay.direction.scl(50f).add(rayFrom);

            ClosestRayResultCallback callback = new ClosestNotMeRayResultCallback(
                    playerEntity.getComponent(ModelInstanceComponent.class).getCollisionObject());
            collisionWorld.rayTest(rayFrom, rayTo, callback);

            if (callback.hasHit()) {
                Entity entityClicked = entities.get(callback.getCollisionObject().getUserValue());

                if (entityClicked.getComponent(PickUpableComponent.class) != null && collidingWith.contains(entityClicked)) {
                    entityHeld = entityClicked;
                }
            }
            callback.dispose();
        } else if (entityHeld != null && Gdx.input.isKeyPressed(Input.Keys.E)) {
            Vector3 positionToMoveTo = playerPosition.getPosition();
            entityHeld.getComponent(PositionComponent.class).getPosition().set(positionToMoveTo.x, positionToMoveTo.y, positionToMoveTo.z);
            entityHeld = null;
        }

        if (entityHeld != null) {
            RotationComponent playerRotationComponent = playerEntity.getComponent(RotationComponent.class);
            PositionComponent entityHeldComponent = entityHeld.getComponent(PositionComponent.class);
            entityHeldComponent.getPosition().set(0.0f, 1.0f, 1.0f).mul(playerRotationComponent.getQuaternion()).add(playerPosition.getPosition());
            System.out.println(entityHeldComponent.getPosition());
        }
    }
}
