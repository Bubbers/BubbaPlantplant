package com.bubbaplantplant.game;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;

public class CollisionListener extends ContactListener {

    @Override
    public boolean onContactAdded(int userValue0, int partId0, int index0, boolean match0, int userValue1, int partId1, int index1, boolean match1) {
        System.out.println("UserValue0 " + userValue0 + " and userValue1 " + userValue1 );
        return true;
    }
}
