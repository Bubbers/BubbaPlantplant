package com.bubbaplantplant.game.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.bubbaplantplant.game.component.BucketComponent;
import com.bubbaplantplant.game.component.CollisionComponent;
import com.bubbaplantplant.game.component.MultiModelComponent;
import com.bubbaplantplant.game.component.TapComponent;

import static com.bubbaplantplant.game.BubbaPlantplantApplication.BUCKET_FILLED_MODEL;

public class WaterFillSystem extends EntitySystem {

    @Override
    public void update(float deltaTime) {
        Entity bucketEntity = getEngine().getEntitiesFor(Family.all(BucketComponent.class).get()).get(0);
        CollisionComponent collisionComp = bucketEntity.getComponent(CollisionComponent.class);
        for (Entity entity : collisionComp.getCollidingWith()) {
            TapComponent component = entity.getComponent(TapComponent.class);
            if (component != null) {
                bucketEntity.getComponent(MultiModelComponent.class).selectModel(BUCKET_FILLED_MODEL);
            }
        }
    }
}
