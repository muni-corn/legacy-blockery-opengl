
package musicaflight.blockery;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;

import musicaflight.blockery.MainMenu.MainMenuScreen;
import musicaflight.blockery.UpdateChecker.UpdateStatus;
import musicaflight.avianutils.*;

public class MainClass extends AvianApp {

	public static enum Screen {
		WELCOME,
		MAIN,
		ENVIRONMENT,
		ENDLESS,
		BLOCKJUICE;
	}

	public static Screen screen = Screen.WELCOME;
	public static SoundtrackPlaying music = SoundtrackPlaying.NONE;

	static enum SoundtrackPlaying {
		NONE,
		MAIN,
		ENVIRONMENT,
		ENVIRONMENT_RUSH,
		OPTIONS;
	}

	public static int shader;

	public static WelcomeScreen welcome;
	public static MainMenu main;
	public static EnvironmentGame env;
	public static EndlessGame end;
	public static BlockeryCore core;
	public static UpdateChecker uc;

	static File u = new File(UpdateChecker.getRunningJARFolder() + "\\BlockeryUpdate.jar");
	static File i = new File(UpdateChecker.getRunningJARFolder() + "\\install.jar");

	public void construct() {
		main = new MainMenu();
		env = new EnvironmentGame();
		end = new EndlessGame();
		core = new BlockeryCore();
		uc = new UpdateChecker();
		welcome = new WelcomeScreen();

		if (i.exists())
			System.out.println(i.delete() ? "Installer deleted." : "Failed to delete installer.");
		if (u.exists())
			System.out.println(u.delete() ? "Update file deleted." : "Failed to delete update file.");

		// main.listFoldersInFolder(new File("C:\\"));

	}

	public void setupGL() {
		glEnable(GL_POLYGON_SMOOTH);
		glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
		glEnable(GL_TEXTURE_2D);
		glShadeModel(GL_SMOOTH);
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);

		glLightModelfv(GL_LIGHT_MODEL_AMBIENT, AvianUtils.asFlippedFloatBuffer(0.05f, 0.05f, 0.05f, 1f));

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		AvianApp.getCam().setFieldOfView(20);
		AvianApp.getCam().applyPerspectiveMatrix();

		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glEnable(GL_LIGHT1);
		glEnable(GL_LIGHT2);
		glLightfv(GL_LIGHT1, GL_POSITION, AvianUtils.asFlippedFloatBuffer((float) AvianApp.getWidth() * 2, AvianApp.getHeight(), 0, 1f));
		glLightfv(GL_LIGHT2, GL_POSITION, AvianUtils.asFlippedFloatBuffer((float) -AvianApp.getWidth() * 2, -AvianApp.getHeight(), 0, 1f));
		shader = AvianUtils.loadShaders("/res/shaders/pixel_phong_lighting.vs", "/res/shaders/pixel_phong_lighting.fs");

	}

	public void keyboard() {
		switch (screen) {
			case MAIN:
				break;
			case ENVIRONMENT:
				env.keyboard();
				break;
			case BLOCKJUICE:
				break;
			case ENDLESS:
				end.keyboard();
				break;
			case WELCOME:
				welcome.keyboard();
				break;
			default:
				break;
		}

	}

	int mouseX, mouseY;

	public void mouse() {
		mouseX = (int) ((AvianInput.getMouseX()) - (AvianApp.getWidth() / 2));
		mouseY = (int) ((AvianApp.getHeight() - AvianInput.getMouseY()) - (AvianApp.getHeight() / 2));
		switch (screen) {
			case MAIN:
				main.mouse();
				break;
			case ENVIRONMENT:
				env.mouse();
				break;
			case BLOCKJUICE:
				break;
			case ENDLESS:
				end.mouse();
				break;
			case WELCOME:
				welcome.mouse();
				break;
			default:
				break;
		}
		uc.mouse();
	}

	public void logic() {

		// Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		// Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
		//
		// for (Thread t : threadArray)
		// System.out.println(t.getName());
		// System.out.println("\n");

		redgreen = AvianMath.glide(redgreen, 235f / 255f, 10000000f);
		blue = AvianMath.glide(blue, 239f / 255f, 10000000f);
		switch (screen) {
			case MAIN:
				main.logic();
				break;
			case ENDLESS:
				end.logic();
				break;
			case ENVIRONMENT:
				env.logic();
				break;
			case BLOCKJUICE:
				break;
			case WELCOME:
				welcome.logic();
				break;
			default:
				break;
		}
		uc.logic();
	}

	float redgreen = 0, blue = 0;

	public void render() {
		if (MainMenu.screen != MainMenuScreen.SETTINGS && screen != Screen.MAIN) {
			if (!env.paused && !end.paused)
				glClearColor(235f / 255f, 235f / 255f, 239f / 255f, 1f);
			else
				glClearColor(150f / 255f, 150f / 255f, 154f / 255f, 1f);
		} else {
			float red = 235f - ((MainMenu.settingsYOffset / -AvianApp.getHeight()) * 185f);
			float green = 235f - ((MainMenu.settingsYOffset / -AvianApp.getHeight()) * 175f);
			float blue = 239f - ((MainMenu.settingsYOffset / -AvianApp.getHeight()) * 175f);
			glClearColor(red / 255f, green / 255f, blue / 255f, 1f);
		}

		glLightfv(GL_LIGHT0, GL_POSITION, AvianUtils.asFlippedFloatBuffer(mouseX, 100f, mouseY, -0.5f));

		AvianApp.getCam().applyTranslations();

		AvianApp.getCam().applyPerspectiveMatrix();

		// AvianUtils.startNanoWatch();

		switch (screen) {
			case MAIN:
				main.render();
				break;
			case ENDLESS:
				end.render();
				break;
			case ENVIRONMENT:
				env.render();
				break;
			case BLOCKJUICE:
				break;
			case WELCOME:
				break;
			default:
				break;
		}
		// AvianUtils.stopNanoWatch();
		// list3D.add(Long.parseLong(AvianUtils.getNanoDifference()));

		AvianApp.getCam().applyAvianOrthoMatrix();

		AvianUtils.startNanoWatch();

		switch (screen) {
			case MAIN:
				main.render2D();
				break;
			case ENDLESS:
				end.render2D();
				break;
			case ENVIRONMENT:
				env.render2D();
				break;
			case BLOCKJUICE:
				break;
			case WELCOME:
				welcome.render2D();
				break;
			default:
				break;
		}
		if (uc.yOffset > -35)
			uc.render2D();

		// AvianUtils.stopNanoWatch();
		// list2D.add(Long.parseLong(AvianUtils.getNanoDifference()));

		// double cumu3D = 0, cumu2D = 0;
		//
		// for (long l : list3D) {
		// cumu3D += l;
		// }
		//
		// for (long l : list2D) {
		// cumu2D += l;
		// }
		//
		// DecimalFormat format = new DecimalFormat("0");
		// DecimalFormat secondFormat = new DecimalFormat("0.0000");
		// String average3D = format.format(cumu3D / list3D.size());
		// String average2D = format.format(cumu2D / list2D.size());
		//
		// String average = average3D + "   /   " + average2D;
		// String last = list3D.get(list3D.size() - 1) + "   /   " + list2D.get(list2D.size() - 1);
		// String seconds = secondFormat.format((cumu3D / list3D.size()) / 1_000_000_000.0) + "   /   " + secondFormat.format((cumu2D / list2D.size()) / 1_000_000_000.0);
		// String lastSeconds = secondFormat.format(list3D.get(list3D.size() - 1) / 1_000_000_000.0) + "   /   " + secondFormat.format(list2D.get(list2D.size() - 1) / 1_000_000_000.0);
		//
		// if (printTimer > 50) {
		// System.out.println("AVG: " + average + "\t\tLST: " + last + "\t\tSEC: " + seconds + "\t\tLSC: " + lastSeconds);
		// printTimer = 0;
		// }

	}

	public String customTitle() {
		return ("Blockery");
	}

	//
	// ArrayList<Long> list3D = new ArrayList<Long>(),
	// list2D = new ArrayList<Long>();
	//
	// int printTimer;

	public void checkAudio() {
		switch (screen) {
			case BLOCKJUICE:
				break;
			case ENDLESS:
				if (music != SoundtrackPlaying.NONE) {
					AvianApp.stopAllSound();
					music = SoundtrackPlaying.NONE;
				}
				break;
			case ENVIRONMENT:
				if (env.rush && !env.gameOver) {
					if (!env.paused) {
						if (music != SoundtrackPlaying.ENVIRONMENT_RUSH) {
							AvianApp.stopAllSound();
							if (BlockeryCore.musicLevel > 0f)
								Audio.rush.play();
							music = SoundtrackPlaying.ENVIRONMENT_RUSH;
						}
					} else {
						if (music != SoundtrackPlaying.NONE) {
							AvianApp.stopAllSound();
							if (BlockeryCore.soundLevel > 0f)
								Audio.pause.play();
							music = SoundtrackPlaying.NONE;
						}
					}
				} else if (!env.gameOver) {
					if (!env.paused) {
						if (music != SoundtrackPlaying.ENVIRONMENT) {
							AvianApp.stopAllSound();
							if (BlockeryCore.musicLevel > 0f)
								Audio.environment.play();
							music = SoundtrackPlaying.ENVIRONMENT;
						}
					} else {
						if (music != SoundtrackPlaying.NONE) {
							AvianApp.stopAllSound();
							if (BlockeryCore.soundLevel > 0f)
								Audio.pause.play();
							music = SoundtrackPlaying.NONE;
						}
					}
				} else {
					if (music != SoundtrackPlaying.NONE) {
						AvianApp.stopAllSound();
						if (env.highScore && BlockeryCore.musicLevel > 0f)
							Audio.highscore.play();
						else if (BlockeryCore.musicLevel > 0f)
							Audio.gameOver.play();
						music = SoundtrackPlaying.NONE;
					}
				}
				break;
			case MAIN:
				if (music != SoundtrackPlaying.MAIN) {
					AvianApp.stopAllSound();
					Audio.main.play();
					Audio.settings.play();
					music = SoundtrackPlaying.MAIN;
				}
				break;
			case WELCOME:
			default:
				if (music != SoundtrackPlaying.NONE) {
					AvianApp.stopAllSound();
					music = SoundtrackPlaying.NONE;
				}
				break;

		}
	}

	public static void kill() {

		uc.updateThread = null;

		if (uc.status != UpdateStatus.FINISHED) {
			if (i.exists())
				System.out.println(i.delete() ? "Installer deleted." : "Failed to delete installer.");
			if (u.exists())
				System.out.println(u.delete() ? "Update file deleted." : "Failed to delete update file.");
		}
		FileCabinet.saveAll();

		AvianApp.close();

	}

	public static void main(String[] args) {
		MainClass host = new MainClass();
		host.setResizable(false);
		host.addFontBank(new Fonts());
		host.addImageBank(new Images());
		host.addAudioBank(new Audio());
		host.start();
	}

}
