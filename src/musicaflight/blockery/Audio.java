
package musicaflight.blockery;

import musicaflight.avianutils.AudioBank;
import musicaflight.avianutils.AvianSound;

public class Audio implements AudioBank {

	public static AvianSound firework, collectBlocks, badBlock, accept, cancel,
			newFactory, delete, pause, coin, beginGame, saved, confirmDelete,
			deleteForever; // SFX
	public static AvianSound highscore, gameOver, rush, environment, main,
			settings; // Music

	public void initAudio() {
		firework = new AvianSound("/res/sounds/Firework.wav", false);
		collectBlocks = new AvianSound("/res/sounds/blockClick.wav", false);
		badBlock = new AvianSound("/res/sounds/badBlockClick.wav", false);
		accept = new AvianSound("/res/sounds/accept.wav", false);
		cancel = new AvianSound("/res/sounds/cancel.wav", false);
		newFactory = new AvianSound("/res/sounds/boughtFactory.wav", false);
		delete = new AvianSound("/res/sounds/deleted.wav", false);
		pause = new AvianSound("/res/sounds/pause.wav", false);
		coin = new AvianSound("/res/sounds/coin.wav", false);
		beginGame = new AvianSound("/res/sounds/beginGame.wav", false);
		saved = new AvianSound("/res/sounds/saved.wav", false);
		confirmDelete = new AvianSound("/res/sounds/confirmDelete.wav", false);
		deleteForever = new AvianSound("/res/sounds/deleteForever.wav", false);

		highscore = new AvianSound("/res/sounds/NewHighScore.wav", false);
		gameOver = new AvianSound("/res/sounds/GameOver.wav", false);
		rush = new AvianSound("/res/sounds/EnvironmentRush.wav", true);
		environment = new AvianSound("/res/sounds/Environment.wav", true);
		main = new AvianSound("/res/sounds/MainMenu.wav", true);
		settings = new AvianSound("/res/sounds/Settings.wav", true);
	}

	public void setMusicVolume(float volume) {
		rush.setVolume(volume);
		environment.setVolume(volume);
		highscore.setVolume(volume);
		gameOver.setVolume(volume);
	}

	public void setSFXVolume(float volume) {
		firework.setVolume(volume);
		collectBlocks.setVolume(volume);
		badBlock.setVolume(volume);
		accept.setVolume(volume);
		cancel.setVolume(volume);
		newFactory.setVolume(volume);
		delete.setVolume(volume);
		pause.setVolume(volume);
		coin.setVolume(volume);
		beginGame.setVolume(volume);
		saved.setVolume(volume);
		confirmDelete.setVolume(volume);
		deleteForever.setVolume(volume);
	}

	public void stopAll() {
		highscore.stop();
		gameOver.stop();
		rush.stop();
		environment.stop();
		main.stop();
		settings.stop();
	}

	public static enum SoundtrackPlaying {
		NONE,
		MAIN,
		ENVIRONMENT,
		ENVIRONMENT_RUSH,
		SETTINGS;
	}

}
