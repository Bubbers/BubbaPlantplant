package com.bubbaplantplant.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.UBJsonReader;
import com.bubbaplantplant.game.component.ModelInstanceComponent;
import com.bubbaplantplant.game.component.PlayerComponent;
import com.bubbaplantplant.game.component.PositionComponent;
import com.bubbaplantplant.game.system.HudSystem;
import com.bubbaplantplant.game.system.ModelTransformUpdaterSystem;
import com.bubbaplantplant.game.system.MoveToTargetSystem;
import com.bubbaplantplant.game.system.RenderSystem;

import java.util.ArrayList;
import java.util.List;

public class BubbaPlantplantApplication extends ApplicationAdapter {

    public static final int PLAYER_CONTACT_FLAG = 2;
    public static final int FLOOR_CONTACT_FLAG = 8;

    private Engine engine;
    private HudSystem hud;
    private btCollisionWorld collisionWorld;

    private List<Entity> entities = new ArrayList<>();

    private DebugDrawer debugDrawer;

    @Override
    public void create() {
        Bullet.init();

        btDefaultCollisionConfiguration collisionConfig = new btDefaultCollisionConfiguration();
        btCollisionDispatcher dispatcher = new btCollisionDispatcher(collisionConfig);
        btDbvtBroadphase broadphase = new btDbvtBroadphase();
        collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);

        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        collisionWorld.setDebugDrawer(debugDrawer);

        engine = new Engine();

        createPlayerEntity();
        createFlowerEntity();
        Vector3 floorDimensions = createFloorEntity();

        //engine.addSystem(new WasdSystem());
        RenderSystem renderSystem = new RenderSystem(collisionWorld, debugDrawer);
        engine.addSystem(renderSystem);
        engine.addSystem(new MoveToTargetSystem(renderSystem.getCamera(), floorDimensions));

        hud = new HudSystem();
        engine.addSystem(hud);

        engine.addSystem(new ModelTransformUpdaterSystem(collisionWorld));
    }

    private Vector3 createFloorEntity() {
        ModelInstance floorInstance = initFloorInstance();
        Entity floorEntity = new Entity();
        floorEntity.add(new ModelInstanceComponent(floorInstance));
        btCollisionObject floorBox = new btCollisionObject();
        Vector3 floorDimensions = new Vector3(5.0f, 0.25f, 5.0f);
        floorBox.setCollisionShape(new btBoxShape(floorDimensions));
        floorBox.setCollisionFlags(floorBox.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        floorBox.setContactCallbackFlag(FLOOR_CONTACT_FLAG);
        floorBox.setUserValue(entities.size());
        collisionWorld.addCollisionObject(floorBox);
        engine.addEntity(floorEntity);
        entities.add(floorEntity);

        return floorDimensions;
    }

    private void createFlowerEntity() {
        ModelInstance flowerInstance = initInstance("box.g3db");
        Entity flowerEntity = new Entity();
        btCollisionObject flowerCollisionObject = new btCollisionObject();
        flowerCollisionObject.setCollisionShape(new btBoxShape(new Vector3(1.0f, 1.0f, 1.0f)));
        flowerCollisionObject.setCollisionFlags(flowerCollisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        flowerCollisionObject.setContactCallbackFilter(PLAYER_CONTACT_FLAG);
        flowerEntity.add(new PositionComponent(new Vector3(1.0f, 0.0f, 1.0f)));
        flowerEntity.add(new ModelInstanceComponent(flowerInstance).withCollisionObject(flowerCollisionObject));
        flowerCollisionObject.setUserValue(entities.size());
        collisionWorld.addCollisionObject(flowerCollisionObject);
        flowerEntity.add(new PlayerComponent());
        entities.add(flowerEntity);
        engine.addEntity(flowerEntity);
    }

    private void createPlayerEntity() {
        ModelInstance playerInstance = initInstance("bubba.g3db");
        Entity playerEntity = new Entity();
        playerEntity.add(new PositionComponent(new Vector3(0.0f, 0.0f, 0.0f)));
        btCollisionObject playerCollisionObject = new btCollisionObject();
        playerCollisionObject.setCollisionShape(new btBoxShape(new Vector3(1.0f, 2.0f, 1.0f)));
        playerCollisionObject.setCollisionFlags(playerCollisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        playerCollisionObject.setContactCallbackFlag(PLAYER_CONTACT_FLAG);
        playerEntity.add(new ModelInstanceComponent(playerInstance).withCollisionObject(playerCollisionObject));
        playerCollisionObject.setUserValue(entities.size());
        collisionWorld.addCollisionObject(playerCollisionObject);
        PlayerComponent playerComponent = new PlayerComponent();
        playerEntity.add(playerComponent);
        entities.add(playerEntity);
        engine.addEntity(playerEntity);

        new CollisionListener(playerComponent);
    }

    private ModelInstance initInstance(String modelFileName) {
        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        Model model = modelLoader.loadModel(Gdx.files.getFileHandle(modelFileName, Files.FileType.Internal));
        return new ModelInstance(model);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void render() {
        // You've seen all this before, just be sure to clear the GL_DEPTH_BUFFER_BIT when working in 3D
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        engine.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
