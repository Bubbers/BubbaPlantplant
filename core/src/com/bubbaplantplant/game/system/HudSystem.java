package com.bubbaplantplant.game.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class HudSystem extends EntitySystem {

    private Stage stage;
    private Table table;
    private Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

    public HudSystem() {
        super(1000000);
    }

    @Override
    public void addedToEngine(Engine engine) {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        table.align(Align.bottom);
        stage.addActor(table);

        Container<Label> labelLeft = new Container<>(new Label("bot left", labelStyle));
        labelLeft.align(Align.left);
        table.add(labelLeft);
        Container<Label> labelRight = new Container<>(new Label("bot right", labelStyle));
        labelRight.align(Align.right);
        table.add(labelRight);
    }

    @Override
    public void update(float deltaTime) {
        stage.act(deltaTime);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

}
