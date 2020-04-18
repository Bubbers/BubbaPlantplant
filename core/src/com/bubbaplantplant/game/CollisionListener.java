package com.bubbaplantplant.game;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.bubbaplantplant.game.component.PlayerComponent;

public class CollisionListener extends ContactListener {

    private PlayerComponent playerComponent;

    public CollisionListener(PlayerComponent playerComponent) {
        this.playerComponent = playerComponent;
    }

    @Override
    public boolean onContactAdded(int userValue0, int partId0, int index0, boolean match0, int userValue1, int partId1, int index1, boolean match1) {
        playerComponent.setScore(playerComponent.getScore() + 1);
        return true;
    }
}
