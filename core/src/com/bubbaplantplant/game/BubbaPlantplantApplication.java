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
import com.bubbaplantplant.game.system.MoveToTargetSystem;
import com.bubbaplantplant.game.system.RenderSystem;

public class BubbaPlantplantApplication extends ApplicationAdapter {

    public static final int PLAYER_CONTACT_FLAG = 2;
    public static final int FLOOR_CONTACT_FLAG = 8;

    private Engine engine;
    private HudSystem hud;
    private btCollisionWorld collisionWorld;
    private CollisionListener collisionListener;

    private DebugDrawer debugDrawer;

    @Override
    public void create() {
        Bullet.init();



        btDefaultCollisionConfiguration collisionConfig = new btDefaultCollisionConfiguration();
        btCollisionDispatcher dispatcher = new btCollisionDispatcher(collisionConfig);
        btDbvtBroadphase broadphase = new btDbvtBroadphase();
        collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);
        this.collisionListener = new CollisionListener();
        collisionListener.enable();

        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        collisionWorld.setDebugDrawer(debugDrawer);

        ModelInstance floorInstance = initFloorInstance();
        ModelInstance playerInstance = initPlayerInstance();
        ModelInstance flowerInstance = initFlowerInstance();

        engine = new Engine();

        Entity playerEntity = new Entity();
        playerEntity.add(new PositionComponent(new Vector3(0.0f, 0.0f, 0.0f)));
        btCollisionObject playerCollisionObject = new btCollisionObject();
        playerCollisionObject.setCollisionShape(new btBoxShape(new Vector3(1.0f, 2.0f, 1.0f)));
        playerCollisionObject.setCollisionFlags(playerCollisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        playerCollisionObject.setContactCallbackFlag(PLAYER_CONTACT_FLAG);
        playerEntity.add(new ModelInstanceComponent(playerInstance).withCollisionObject(playerCollisionObject));
        playerCollisionObject.setUserValue(2); // TODO Better number here
        collisionWorld.addCollisionObject(playerCollisionObject);
        playerEntity.add(new PlayerComponent());
        engine.addEntity(playerEntity);

        Entity flowerEntity = new Entity();
        btCollisionObject flowerCollisionObject = new btCollisionObject();
        flowerCollisionObject.setCollisionShape(new btBoxShape(new Vector3(1.0f, 1.0f, 1.0f)));
        flowerCollisionObject.setCollisionFlags(flowerCollisionObject.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        flowerCollisionObject.setContactCallbackFilter(PLAYER_CONTACT_FLAG);
        flowerEntity.add(new PositionComponent(new Vector3(1.0f, 0.0f, 1.0f)));
        flowerEntity.add(new ModelInstanceComponent(flowerInstance).withCollisionObject(flowerCollisionObject));
        flowerCollisionObject.setUserValue(1); // TODO Better number here
        collisionWorld.addCollisionObject(flowerCollisionObject);
        flowerEntity.add(new PlayerComponent());
        engine.addEntity(flowerEntity);

        Entity floorEntity = new Entity();
        floorEntity.add(new ModelInstanceComponent(floorInstance));
        btCollisionObject floorBox = new btCollisionObject();
        floorBox.setCollisionShape(new btBoxShape(new Vector3(5.0f, 0.25f, 5.0f)));
        floorBox.setCollisionFlags(floorBox.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        floorBox.setUserValue(0);
        floorBox.setContactCallbackFlag(FLOOR_CONTACT_FLAG);

        collisionWorld.addCollisionObject(floorBox);

        engine.addEntity(floorEntity);

        //engine.addSystem(new WasdSystem());
        RenderSystem renderSystem = new RenderSystem(collisionWorld, debugDrawer);
        engine.addSystem(renderSystem);
        engine.addSystem(new MoveToTargetSystem(collisionWorld, renderSystem.getCamera(), new Vector3(10f, 0.5f, 10f)));

        hud = new HudSystem();
        engine.addSystem(hud);

    }

    private ModelInstance initPlayerInstance() {
        // Model loader needs a binary json reader to decode
        UBJsonReader jsonReader = new UBJsonReader();
        // Create a model loader passing in our json reader
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        // Now load the model by name
        // Note, the model (g3db file ) and textures need to be added to the assets folder of the Android proj
        Model model = modelLoader.loadModel(Gdx.files.getFileHandle("bubba.g3db", Files.FileType.Internal));
        // Now create an instance.  Instance holds the positioning data, etc of an instance of your model
        ModelInstance playerInstance = new ModelInstance(model);
        //playerInstance.transform.setToScaling(0.25f, 0.25f, 0.25f);
        return playerInstance;
    }

    private ModelInstance initFlowerInstance() {
        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        Model model = modelLoader.loadModel(Gdx.files.getFileHandle("box.g3db", Files.FileType.Internal));
        ModelInstance flowerInstance = new ModelInstance(model);
        return flowerInstance;
    }



    private ModelInstance initFloorInstance() {
        ModelBuilder modelBuilder = new ModelBuilder();
        Model floor = modelBuilder.createBox(10f, 0.5f, 10f, new Material(new ColorAttribute(ColorAttribute.Diffuse, Color.DARK_GRAY)), VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position);
        return new ModelInstance(floor);
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
