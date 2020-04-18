package com.bubbaplantplant.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.utils.UBJsonReader;
import com.bubbaplantplant.game.component.ModelInstanceComponent;
import com.bubbaplantplant.game.component.PlayerComponent;
import com.bubbaplantplant.game.component.PositionComponent;
import com.bubbaplantplant.game.system.HudSystem;
import com.bubbaplantplant.game.system.MoveToTargetSystem;
import com.bubbaplantplant.game.system.RenderSystem;

public class BubbaPlantplantApplication extends ApplicationAdapter {

    private Engine engine;
    private HudSystem hud;

    @Override
    public void create() {
        Bullet.init();

        ModelInstance floorInstance = initFloorInstance();
        ModelInstance playerInstance = initPlayerInstance();

        engine = new Engine();

        Entity playerEntity = new Entity();
        playerEntity.add(new PositionComponent(new Vector3(0.0f, 0.25f, 0.0f)));
        playerEntity.add(new ModelInstanceComponent(playerInstance));
        playerEntity.add(new PlayerComponent());
        engine.addEntity(playerEntity);

        Entity floorEntity = new Entity();
        floorEntity.add(new ModelInstanceComponent(floorInstance));
        engine.addEntity(floorEntity);

        //engine.addSystem(new WasdSystem());
        RenderSystem renderSystem = new RenderSystem();
        engine.addSystem(renderSystem);
        engine.addSystem(new MoveToTargetSystem(renderSystem.getCamera(), new Vector3(10f, 0.5f, 10f)));

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
        Model model = modelLoader.loadModel(Gdx.files.getFileHandle("box.g3db", Files.FileType.Internal));
        // Now create an instance.  Instance holds the positioning data, etc of an instance of your model
        ModelInstance playerInstance = new ModelInstance(model);
        playerInstance.transform.setToScaling(0.25f, 0.25f, 0.25f);
        return playerInstance;
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
