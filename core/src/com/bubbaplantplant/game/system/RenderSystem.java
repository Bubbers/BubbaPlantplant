package com.bubbaplantplant.game.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.bubbaplantplant.game.component.ModelInstanceComponent;

import java.util.ArrayList;

public class RenderSystem extends EntitySystem {

    private final btCollisionWorld collisionWorld;
    private DebugDrawer debugDrawer;
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Environment environment;

    public RenderSystem(btCollisionWorld collisionWorld, DebugDrawer debugDrawer) {
        super(1000);
        this.collisionWorld = collisionWorld;
        this.debugDrawer = debugDrawer;
    }

    @Override
    public void addedToEngine(Engine engine) {
        // Create camera sized to screens width/height with Field of View of 75 degrees
        camera = new PerspectiveCamera(
                60,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());

        // Move the camera 3 units back along the z-axis and look at the origin
        camera.position.set(0f, 5f, 7f);
        camera.lookAt(0f, 0f, 0f);

        // Near and Far (plane) repesent the minimum and maximum ranges of the camera in, um, units
        camera.near = 0.1f;
        camera.far = 300.0f;

        camera.update();

        // A ModelBatch is like a SpriteBatch, just for models.  Use it to batch up geometry for OpenGL
        modelBatch = new ModelBatch();

        // Finally we want some light, or we wont see our color.  The environment gets passed in during
        // the rendering process.  Create one, then create an Ambient ( non-positioned, non-directional ) light.
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1.0f));
    }

    @Override
    public void update(float deltaTime) {

        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(Family.one(ModelInstanceComponent.class).get());
        ArrayList<ModelInstance> models = new ArrayList<>();
        for (Entity entity: entities) {
            ModelInstanceComponent component = entity.getComponent(ModelInstanceComponent.class);
            ModelInstance modelInstance = component.getModelInstance();
            models.add(modelInstance);
        }
        modelBatch.begin(camera);
        modelBatch.render(models, environment);
        modelBatch.end();
//        debugDrawer.begin(camera);
//        collisionWorld.debugDrawWorld();
//        debugDrawer.end();
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }
}
