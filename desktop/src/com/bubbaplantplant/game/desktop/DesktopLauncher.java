package com.bubbaplantplant.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bubbaplantplant.game.BubbaPlantplantApplication;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.samples = 4;
		new LwjglApplication(new BubbaPlantplantApplication(), config);
	}
}
