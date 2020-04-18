package com.bubbaplantplant.game.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.bubbaplantplant.game.component.PlayerComponent;
import com.bubbaplantplant.game.component.PositionComponent;

public class MoveToTargetSystem extends EntitySystem {

    private Entity player;
    private Camera camera;
    private btCollisionShape floorShape;
    private btCollisionWorld collisionWorld;
    private Vector3 target = null;
    private static final float SPEED = 3f;

    public MoveToTargetSystem(Camera camera, Vector3 floorDimensions) {
        this.camera = camera;
        floorShape = new btBoxShape(floorDimensions.scl(0.5f));
        btCollisionObject floorBox = new btCollisionObject();
        floorBox.setCollisionShape(floorShape);
        floorBox.setWorldTransform(new Matrix4().setTranslation(0,0,0));
        btDefaultCollisionConfiguration collisionConfig = new btDefaultCollisionConfiguration();
        btCollisionDispatcher dispatcher = new btCollisionDispatcher(collisionConfig);
        btDbvtBroadphase broadphase = new btDbvtBroadphase();
        collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);
        collisionWorld.addCollisionObject(floorBox);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.player = getEngine().getEntitiesFor(Family.one(PlayerComponent.class).get()).first();
    }

    @Override
    public void update(float deltaTime) {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Ray pickRay = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());

            Vector3 rayFrom = pickRay.origin;
            // 50 meters max from the origin
            Vector3 rayTo = pickRay.direction.scl(50f).add(rayFrom);

            ClosestRayResultCallback callback = new ClosestRayResultCallback(rayFrom, rayTo);
            collisionWorld.rayTest(rayFrom, rayTo, callback);

            if (callback.hasHit()) {
                Vector3 hitPoint = new Vector3();
                callback.getHitPointWorld(hitPoint);
                target = hitPoint;
            }
        }
        if (target != null) {
            Vector3 playerPosition = player.getComponent(PositionComponent.class).getPosition();
            Vector3 direction = new Vector3(target.x, playerPosition.y, target.z);
            direction.sub(playerPosition);
            if (direction.len() < SPEED*deltaTime) {
                playerPosition.set(target.x, playerPosition.y, target.z);
                target = null;
            } else {
                direction.nor();
                playerPosition.add(new Vector3(direction.x, 0, direction.z).scl(SPEED * deltaTime));
            }
        }
    }

}