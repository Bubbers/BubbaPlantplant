package com.bubbaplantplant.game.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.bubbaplantplant.game.component.PlayerComponent;
import com.bubbaplantplant.game.util.FrameRateLabel;

public class HudSystem extends EntitySystem {

    private Stage stage;
    private Table table;
    private Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
    private static final boolean SHOW_FPS = false;
    private Label scoreLabel;
    private int prevScore = 0;

    public HudSystem() {
        super(1000000);
    }

    @Override
    public void addedToEngine(Engine engine) {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        VerticalGroup root = new VerticalGroup();
        root.setFillParent(true);
        root.setDebug(true);
        stage.addActor(root);

        table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        if(SHOW_FPS) {
            table.add(new FrameRateLabel(labelStyle)).expandX().align(Align.left);
        }
        scoreLabel = new Label("score: " + prevScore, labelStyle);
        table.add(scoreLabel).expandX().align(Align.right).colspan(SHOW_FPS ? 1 : 2);
        table.row();

        HorizontalGroup midFill = new HorizontalGroup();
        table.add(midFill).expandY().align(Align.top);
        table.row();

        createBotLabel("bot left", Align.left);
        createBotLabel("bot right", Align.right);
    }

    private void createBotLabel(String s, int alignment) {
        Label label = new Label(s, labelStyle);
        table.add(label).expandX().align(alignment);
    }

    @Override
    public void update(float deltaTime) {
        Entity player = getEngine().getEntitiesFor(Family.one(PlayerComponent.class).get()).get(0);
        int score = player.getComponent(PlayerComponent.class).getScore();
        if (score != prevScore) {
            scoreLabel.setText("score: " + score);
            prevScore = score;
        }
        stage.act(deltaTime);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

}
