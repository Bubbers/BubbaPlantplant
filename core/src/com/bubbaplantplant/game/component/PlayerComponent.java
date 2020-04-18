package com.bubbaplantplant.game.component;

import com.badlogic.ashley.core.Component;

public class PlayerComponent implements Component {
    private int score = 0;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
