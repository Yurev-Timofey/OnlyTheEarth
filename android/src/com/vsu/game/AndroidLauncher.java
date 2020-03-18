package com.vsu.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		config.useWakelock = true; //Игра не будет переходить в спящий режим
		config.useCompass = false; //Отключаем компасс за ненадобностью

		getWindow().getDecorView().setSystemUiVisibility(							//Убираем экранные кнопки
				getWindow().getDecorView().SYSTEM_UI_FLAG_HIDE_NAVIGATION |
						getWindow().getDecorView().SYSTEM_UI_FLAG_FULLSCREEN |
						getWindow().getDecorView().SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

		initialize(new MyGame(), config);


	}
}
