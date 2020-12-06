
package musicaflight.blockery;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.util.ArrayList;

import musicaflight.blockery.Block.BlockColor;
import musicaflight.blockery.MainClass.Screen;
import musicaflight.blockery.UpdateChecker.UpdateStatus;
import musicaflight.blockery.WelcomeScreen.Name;
import musicaflight.avianutils.*;

public class MainMenu {

	public MainMenu() {
		loadUsers();
		AvianApp.addKeyListener(new AvianKeyboard() {
			//			if () {
			//				FileCabinet.saveUser();
			//				BlockeryCore.usernkkame = sb.toString();
			//				FileCabinet.loadUser();
			//				screen = MainMenuScreen.SETTINGS;
			//				Audio.accept.play();
			//				newUser = false;
			//				return;
			//			}
			//
			//			caretInput = 0;
			//
			//			if (AvianInput.isKeyDown(AvianInput.KEY_BACKSPACE) && (sb.length() > 0))
			//				sb.deleteCharAt(sb.length() - 1);
			//
			//			char newChar = AvianInput.getTypedCharacter();
			//
			//			boolean isSpecial = false;
			//
			//			String specialChars = "`~!@#$%^&*()-_=[{]};:|<>,./?'\"\\";
			//
			//			for (int i = 0; i < specialChars.length(); i++)
			//				if (newChar == specialChars.charAt(i)) {
			//					isSpecial = true;
			//					break;
			//				}
			//
			//			if (Character.isAlphabetic(newChar) || Character.isDigit(newChar) || Character.isSpaceChar(newChar) || (newChar == '.') || isSpecial) {
			//				sb.append(newChar);
			//			}
			//
			//			name = Name.ACCEPTABLE;
			//
			//			checkStatement: if (sb.length() <= 0)
			//				name = Name.EMPTY;
			//			else if (sb.toString().equals("Cubeite"))
			//				name = Name.CUBEITE;
			//			else if (nameIncludes("yo") && nameIncludes("mama"))
			//				name = Name.YOMAMA;
			//			else {
			//				for (String s : names)
			//					if (s.equals(sb.toString())) {
			//						name = Name.EXISTS;
			//						break checkStatement;
			//					}
			//
			//				for (String s : BlockeryPrivate.offensive)
			//					if (nameIncludes(s)) {
			//						name = Name.OFFENSIVE;
			//						break checkStatement;
			//					}
			//
			//				for (String s : BlockeryPrivate.inappropriate)
			//					if (nameIncludes(s)) {
			//						name = Name.INAPPROPRIATE;
			//						break checkStatement;
			//					}
			//
			//				for (String s : BlockeryPrivate.notRecommended)
			//					if (nameIncludes(s)) {
			//						name = Name.NOT_RECOMMENDED;
			//						break checkStatement;
			//					}
			//
			//			}
			//		}

			@Override
			public void press(int key) {
				// TODO Auto-generated method stub

			}

			@Override
			public void type(char text) {
				sb.append(text);

				name = Name.ACCEPTABLE;

				checkStatement: if (sb.length() <= 0)
					name = Name.EMPTY;
				else if (sb.toString().equals("Cubeite"))
					name = Name.CUBEITE;
				else if (nameIncludes("yo") && nameIncludes("mama"))
					name = Name.YOMAMA;
				else {
					for (String s : names)
						if (s.equals(sb.toString())) {
							name = Name.EXISTS;
							break checkStatement;
						}

					for (String s : BlockeryPrivate.offensive)
						if (nameIncludes(s)) {
							name = Name.OFFENSIVE;
							break checkStatement;
						}

					for (String s : BlockeryPrivate.inappropriate)
						if (nameIncludes(s)) {
							name = Name.INAPPROPRIATE;
							break checkStatement;
						}

					for (String s : BlockeryPrivate.notRecommended)
						if (nameIncludes(s)) {
							name = Name.NOT_RECOMMENDED;
							break checkStatement;
						}

				}

			}

			@Override
			public void repeat(int key) {
				// TODO Auto-generated method stub

			}

			@Override
			public void release(int key) {
				if (!newUser)
					return;
				if (key == (AvianInput.KEY_ENTER) && (sb.length() > 0) && (name != Name.EMPTY && name != Name.INAPPROPRIATE && name != Name.OFFENSIVE)) {
					FileCabinet.saveUser();
					BlockeryCore.username = sb.toString();
					FileCabinet.loadUser();
					screen = MainMenuScreen.SETTINGS;
					Audio.accept.play();
					newUser = false;
				} else if (key == AvianInput.KEY_BACKSPACE && sb.length() > 0) {
					sb.deleteCharAt(sb.length() - 1);
				}
			}

		});
	}

	public static enum MainMenuScreen {
		MAIN,
		SETTINGS,
		USER_SELECTION,
		DATA,
		CONFIRM_DELETE,
		MORE,
		SUPPORT;
	}

	public static MainMenuScreen screen = MainMenuScreen.MAIN;

	int mouseX, mouseY;

	float topButtonsX = 600, bottomButtonsX = 700;

	Button environment = new Button("Environment", Images.envIcon, new AvianPoint(-60, 30), new AvianPoint(100, 100), new AvianPoint(-135, 30), new AvianPoint(250, 100), BlockColor.BLUE.getColor()),
			endless = new Button("Endless", Images.endIcon, new AvianPoint(60, 30), new AvianPoint(100, 100), new AvianPoint(135, 30), new AvianPoint(250, 100), BlockColor.GREEN.getColor()),
			settings = new Button("Settings", Images.setIcon, new AvianPoint(-60, 150), new AvianPoint(100, 100), new AvianPoint(-135, 150), new AvianPoint(250, 100), BlockColor.ORANGE.getColor()),
			quit = new Button("Quit", Images.quitIcon, new AvianPoint(60, 150), new AvianPoint(100, 100), new AvianPoint(135, 150), new AvianPoint(250, 100), BlockColor.RED.getColor()),
			data = new Button("Manage Data", Images.floppy, new AvianPoint(105, 0), new AvianPoint(200, 50), new AvianPoint(105, 0), new AvianPoint(200, 50), BlockColor.BLUE.getColor()),
			more = new Button("More", Images.arrowR, new AvianPoint(295, 90), new AvianPoint(50, 50), new AvianPoint(330, 90), new AvianPoint(120, 50), new AvianColor(150, 150, 150)),
			support = new Button("Support", Images.arrowL, new AvianPoint(-295, 90), new AvianPoint(50, 50), new AvianPoint(-330, 90), new AvianPoint(120, 50), new AvianColor(150, 150, 150));

	public static AvianMesh prism = new AvianMesh("/res/models/Prism.obj", -110, -11, -60, 150, 22, 50);

	Button sure = new Button("Yes", Images.acceptIcon, new AvianPoint(-35, 150), new AvianPoint(50, 50), new AvianPoint(-85, 150), new AvianPoint(150, 50), BlockColor.RED.getColor());
	Button no = new Button("NO!", Images.cancelIcon, new AvianPoint(35, 150), new AvianPoint(50, 50), new AvianPoint(85, 150), new AvianPoint(150, 50), BlockColor.GREEN.getColor());

	Button delEnv = new Button("Delete Environment", Images.envIcon, new AvianPoint(-35, -35), new AvianPoint(50, 50), new AvianPoint(-135, -35), new AvianPoint(250, 50), BlockColor.BLUE.getColor());
	Button delEnd = new Button("Delete Endless", Images.endIcon, new AvianPoint(35, -35), new AvianPoint(50, 50), new AvianPoint(135, -35), new AvianPoint(250, 50), BlockColor.GREEN.getColor());
	Button delUsr = new Button("Delete Stats", Images.userCard, new AvianPoint(-35, 35), new AvianPoint(50, 50), new AvianPoint(-135, 35), new AvianPoint(250, 50), BlockColor.ORANGE.getColor());
	Button delAll = new Button("Delete Everything", Images.mmIcon, new AvianPoint(35, 35), new AvianPoint(50, 50), new AvianPoint(135, 35), new AvianPoint(250, 50), BlockColor.RED.getColor());

	Button done = new Button("Done", Images.acceptIcon, new AvianPoint(0, 200), new AvianPoint(50, 50), new AvianPoint(0, 200), new AvianPoint(150, 50), new AvianColor(125, 209, 213));
	Button dataDone = new Button("Done", Images.acceptIcon, new AvianPoint(0, 200), new AvianPoint(50, 50), new AvianPoint(0, 200), new AvianPoint(150, 50), new AvianColor(125, 209, 213));

	public static float settingsYOffset, settingsXOffset;
	float musicPY, soundPY, notYouX;

	Name name = Name.ACCEPTABLE;

	boolean mouseClicked, animateLogo, musicHover, soundHover, userHover;

	final float gearScale = 150;

	AvianMesh smallGear = new AvianMesh("/res/models/8Gear.obj", (-AvianApp.getWidth() / 2) + (gearScale * 1.5f), 0, AvianApp.getHeight() / 2, gearScale, gearScale, gearScale);
	AvianMesh mediumGear = new AvianMesh("/res/models/16Gear.obj", -AvianApp.getWidth() / 2, 0, AvianApp.getHeight() / 2, gearScale, gearScale, gearScale);
	AvianMesh largeGear = new AvianMesh("/res/models/32Gear.obj", AvianApp.getWidth() / 2, 0, AvianApp.getHeight() / 2, gearScale, gearScale, gearScale);

	final float logoScale = 10;

	AvianMesh B = new AvianMesh("/res/models/B.obj", 0, 0, -AvianMath.randomInt(2000) - 150, logoScale, logoScale * 2, logoScale);
	AvianMesh L = new AvianMesh("/res/models/L.obj", 0, 0, -AvianMath.randomInt(2000) - 150, logoScale, logoScale * 2, logoScale);
	AvianMesh O = new AvianMesh("/res/models/O.obj", 0, 0, -AvianMath.randomInt(2000) - 150, logoScale, logoScale * 2, logoScale);
	AvianMesh C = new AvianMesh("/res/models/C.obj", 0, 0, -AvianMath.randomInt(2000) - 150, logoScale, logoScale * 2, logoScale);
	AvianMesh K = new AvianMesh("/res/models/K.obj", 0, 0, -AvianMath.randomInt(2000) - 150, logoScale, logoScale * 2, logoScale);
	AvianMesh E = new AvianMesh("/res/models/E.obj", 0, 0, -AvianMath.randomInt(2000) - 150, logoScale, logoScale * 2, logoScale);
	AvianMesh R = new AvianMesh("/res/models/R.obj", 0, 0, -AvianMath.randomInt(2000) - 150, logoScale, logoScale * 2, logoScale);
	AvianMesh Y = new AvianMesh("/res/models/Y.obj", 0, 0, -AvianMath.randomInt(2000) - 150, logoScale, logoScale * 2, logoScale);

	AvianRectangle r = new AvianRectangle(0, 0, 3, 0);

	File currentFolder;

	AvianMesh[] letters = new AvianMesh[] { B, L, O, C, K, E, R, Y };

	public static int slotOffset;

	DestroyBox db;

	public void mouse() {
		mouseX = (int) ((AvianInput.getMouseX()) - (AvianApp.getWidth() / 2));
		mouseY = (int) ((AvianApp.getHeight() - AvianInput.getMouseY()) - (AvianApp.getHeight() / 2));
		switch (screen) {
			case MAIN:
				environment.hover(mouseX, mouseY);
				endless.hover(mouseX, mouseY);
				settings.hover(mouseX, mouseY);
				quit.hover(mouseX, mouseY);

				more.hover(mouseX, mouseY);
				support.hover(mouseX, mouseY);
				if (AvianInput.isMouseButtonDown(0) && !mouseClicked) {
					if (environment.clicked()) {
						MainClass.env.initializeBlocks();
						MainClass.screen = Screen.ENVIRONMENT;
						if (BlockeryCore.soundLevel > 0f)
							Audio.beginGame.play();
					}
					if (endless.clicked()) {
						MainClass.end.initializeBlocks();
						MainClass.screen = Screen.ENDLESS;
						if (BlockeryCore.soundLevel > 0f)
							Audio.beginGame.play();
					}
					if (settings.clicked()) {
						screen = MainMenuScreen.SETTINGS;
						if (BlockeryCore.soundLevel > 0f)
							Audio.accept.play();
					}
					if (quit.clicked() && (MainClass.uc.status != UpdateStatus.DOWNLOADING)) {
						MainClass.kill();
					}
					if (more.clicked()) {
						screen = MainMenuScreen.MORE;
						more.setTitle("Back");
						more.setIcon(Images.arrowL);
						Audio.accept.play();
					}
					if (support.clicked()) {
						screen = MainMenuScreen.SUPPORT;
						support.setTitle("Back");
						support.setIcon(Images.arrowR);
						Audio.accept.play();
					}
					mouseClicked = true;
				} else if (!AvianInput.isMouseButtonDown(0))
					mouseClicked = false;

				break;
			case SETTINGS:
				int userWidth = (int) Fonts.NewCicleSemi_35.getWidth(BlockeryCore.username);
				boolean widthTooSmall = userWidth - 200 <= 0;
				done.hover(mouseX, mouseY);
				musicHover = (mouseX >= -240) && (mouseX <= 0) && (mouseY >= (-85 + settingsYOffset + AvianApp.getHeight())) && (mouseY <= (-35 + settingsYOffset + AvianApp.getHeight()));
				soundHover = (mouseX >= -240) && (mouseX <= 0) && (mouseY >= (-25 + settingsYOffset + AvianApp.getHeight())) && (mouseY <= (25 + settingsYOffset + AvianApp.getHeight()));
				userHover = (mouseX >= 5) && (mouseX <= (widthTooSmall ? 205 : 205 + userWidth - 200)) && (mouseY >= (-85 + settingsYOffset + AvianApp.getHeight())) && (mouseY <= (-35 + settingsYOffset + AvianApp.getHeight()));
				data.hover(mouseX, mouseY);

				if (AvianInput.isMouseButtonDown(0)) {

					if (musicHover)
						AvianApp.setMusicVolume(BlockeryCore.musicLevel = ((mouseX + 205f) / 200f) < 0f ? 0f : (((mouseX + 205f) / 200f) > 1f ? 1f : (mouseX + 205f) / 200f));
					else if (soundHover)
						AvianApp.setSFXVolume(BlockeryCore.soundLevel = ((mouseX + 205f) / 200f) < 0f ? 0f : (((mouseX + 205f) / 200f) > 1f ? 1f : (mouseX + 205f) / 200f));

					if (!mouseClicked) {
						if (done.clicked()) {
							screen = MainMenuScreen.MAIN;
							if (BlockeryCore.soundLevel > 0f)
								Audio.saved.play();
							FileCabinet.saveCore();
						}
						if (userHover) {
							loadUsers();
							screen = MainMenuScreen.USER_SELECTION;
							Audio.cancel.play();
						}
						if (data.clicked()) {
							screen = MainMenuScreen.DATA;
							Audio.accept.play();
						}
					}

					mouseClicked = true;

				} else if (!AvianInput.isMouseButtonDown(0))
					mouseClicked = false;
				break;
			case USER_SELECTION:
				int my = (int) ((AvianApp.getHeight()) - AvianInput.getMouseY());
				slot = (int) ((my - 100f) / section);
				if (AvianInput.isMouseButtonDown(0) && !newUser) {
					if (!mouseClicked) {
						if (slot == (names.length - 1)) {
							newUser = true;
							Audio.accept.play();
						} else if (slot >= 0) {
							FileCabinet.saveUser();
							BlockeryCore.username = names[slot];
							FileCabinet.loadUser();
							Audio.accept.play();
							screen = MainMenuScreen.SETTINGS;
						}
					}
					mouseClicked = true;
				} else if (!AvianInput.isMouseButtonDown(0)) {
					mouseClicked = false;
				}
				break;
			case DATA:
				dataDone.hover(mouseX, mouseY);
				delEnv.hover(mouseX, mouseY);
				delEnd.hover(mouseX, mouseY);
				delUsr.hover(mouseX, mouseY);
				delAll.hover(mouseX, mouseY);

				if (AvianInput.isMouseButtonDown(0)) {
					if (!mouseClicked) {
						if (dataDone.clicked()) {
							screen = MainMenuScreen.SETTINGS;
							Audio.accept.play();
						}
						if (delEnv.clicked()) {
							db = new DestroyBox("ENV");
							screen = MainMenuScreen.CONFIRM_DELETE;
							Audio.confirmDelete.play();
						}
						if (delEnd.clicked()) {
							db = new DestroyBox("END");
							screen = MainMenuScreen.CONFIRM_DELETE;
							Audio.confirmDelete.play();
						}
						if (delUsr.clicked()) {
							db = new DestroyBox("USR");
							screen = MainMenuScreen.CONFIRM_DELETE;
							Audio.confirmDelete.play();
						}
						if (delAll.clicked()) {
							db = new DestroyBox("ALL");
							screen = MainMenuScreen.CONFIRM_DELETE;
							Audio.confirmDelete.play();
						}
					}
					mouseClicked = true;

				} else if (!AvianInput.isMouseButtonDown(0)) {
					mouseClicked = false;
				}
				break;
			case CONFIRM_DELETE:
				sure.hover(mouseX, mouseY);
				no.hover(mouseX, mouseY);
				if (AvianInput.isMouseButtonDown(0)) {
					if (!mouseClicked && deleteCount <= 0) {
						if (sure.clicked()) {
							db.execute();
						}
						if (no.clicked()) {
							screen = MainMenuScreen.DATA;
							Audio.accept.play();
						}
					}
					mouseClicked = true;
				} else if (!AvianInput.isMouseButtonDown(0)) {
					mouseClicked = false;
				}

				break;
			case MORE:
				more.hover(mouseX, mouseY);
				if (AvianInput.isMouseButtonDown(0) && !mouseClicked) {
					if (more.clicked()) {
						screen = MainMenuScreen.MAIN;
						more.setTitle("More");
						more.setIcon(Images.arrowR);
						Audio.cancel.play();
					}
					mouseClicked = true;
				} else if (!AvianInput.isMouseButtonDown(0))
					mouseClicked = false;
				break;
			case SUPPORT:
				support.hover(mouseX, mouseY);
				if (AvianInput.isMouseButtonDown(0) && !mouseClicked) {
					if (support.clicked()) {
						screen = MainMenuScreen.MAIN;
						support.setTitle("Support");
						support.setIcon(Images.arrowL);
						Audio.cancel.play();
					}
					mouseClicked = true;
				} else if (!AvianInput.isMouseButtonDown(0))
					mouseClicked = false;
				break;
			default:
				break;
		}
	}

	float smallGearYaw;
	float sinInput;

	int deleteCount = 0;

	public void logic() {
		if (db != null && db.active) {
			deleteCount++;
		} else
			deleteCount = 0;

		if (deleteCount > 200 && screen == MainMenuScreen.CONFIRM_DELETE) {
			if (db.type.equals("ALL")) {
				loadUsers();
				screen = MainMenuScreen.USER_SELECTION;
			} else
				screen = MainMenuScreen.DATA;
		}

		smallGearYaw += .3f;

		if (musicHover) {
			if (musicPY != 0)
				musicPY = AvianMath.glide(musicPY, 0, 5f);
		} else {
			if (musicPY != 30)
				musicPY = AvianMath.glide(musicPY, 30, 5f);
		}

		if (soundHover) {
			if (soundPY != 0)
				soundPY = AvianMath.glide(soundPY, 0, 5f);
		} else {
			if (soundPY != -30)
				soundPY = AvianMath.glide(soundPY, -30, 5f);
		}

		if (!userHover) {
			if (notYouX != -10)
				notYouX = AvianMath.glide(notYouX, -10, 7f);
		} else {
			if (notYouX != 0)
				notYouX = AvianMath.glide(notYouX, 0, 7f);
		}

		switch (screen) {
			case MAIN:
				settingsXOffset = AvianMath.glide(settingsXOffset, 0, 12.5f);
				settingsYOffset = AvianMath.glide(settingsYOffset, 0, 12.5f);
				environment.logic();
				endless.logic();
				settings.logic();
				quit.logic();
				more.logic();
				support.logic();
				break;
			case SETTINGS:
				settingsXOffset = AvianMath.glide(settingsXOffset, 0, 12.5f);
				settingsYOffset = AvianMath.glide(settingsYOffset, -AvianApp.getHeight(), 12.5f);
				done.logic();
				data.logic();
				break;
			case USER_SELECTION:
				caretInput += 1.75;
				settingsXOffset = AvianMath.glide(settingsXOffset, -AvianApp.getWidth(), 12.5f);
				settingsYOffset = AvianMath.glide(settingsYOffset, -AvianApp.getHeight(), 12.5f);
				break;
			case DATA:
				settingsXOffset = AvianMath.glide(settingsXOffset, 0, 12.5f);
				settingsYOffset = AvianMath.glide(settingsYOffset, -AvianApp.getHeight() * 2, 12.5f);
				dataDone.logic();
				delEnv.logic();
				delEnd.logic();
				delUsr.logic();
				delAll.logic();
				break;
			case CONFIRM_DELETE:
				if (deleteCount <= 0) {
					sure.logic();
					no.logic();
				}
				settingsXOffset = AvianMath.glide(settingsXOffset, AvianApp.getWidth(), 12.5f);
				settingsYOffset = AvianMath.glide(settingsYOffset, -AvianApp.getHeight() * 2, 12.5f);
				break;
			case MORE:
				more.logic();
				settingsXOffset = AvianMath.glide(settingsXOffset, -AvianApp.getWidth(), 12.5f);
				settingsYOffset = AvianMath.glide(settingsYOffset, 0, 12.5f);
				break;
			case SUPPORT:
				support.logic();
				settingsXOffset = AvianMath.glide(settingsXOffset, AvianApp.getWidth(), 12.5f);
				settingsYOffset = AvianMath.glide(settingsYOffset, 0, 12.5f);
				break;
			default:
				break;

		}

		if (db != null) {
			db.xOff = settingsXOffset - 1000;
			db.zOff = settingsYOffset + 1200;
			db.logic();
		}

		sinInput++;
		for (int i = 0; i < letters.length; i++) {
			float sin = AvianMath.sin(sinInput - (i * 45));
			letters[i].setX(AvianMath.glide(letters[i].getX(), settingsXOffset / 4, (i + 1) * 2));
			letters[i].setY(sin * 20);
			letters[i].setZ(AvianMath.glide(letters[i].getZ(), -150f + settingsYOffset, (i + 1) * 2));
		}

		Audio.main.setVolume((1f - (settingsYOffset / -AvianApp.getHeight())) * BlockeryCore.musicLevel);
		Audio.settings.setVolume(((settingsYOffset / -AvianApp.getHeight())) * BlockeryCore.musicLevel);
		if (Audio.settings.getVolume() > BlockeryCore.musicLevel)
			Audio.settings.setVolume(BlockeryCore.musicLevel);
	}

	public void render() {

		data.setXOffset(settingsXOffset);

		topButtonsX = AvianMath.glide(topButtonsX, 0, 6f);
		bottomButtonsX = AvianMath.glide(bottomButtonsX, 0, 8f);

		environment.setXOffset(-topButtonsX + settingsXOffset / 1.4f);
		endless.setXOffset(topButtonsX + settingsXOffset / 1.4f);
		settings.setXOffset(-bottomButtonsX + settingsXOffset / 1.4f);
		quit.setXOffset(bottomButtonsX + settingsXOffset / 1.4f);
		more.setXOffset(bottomButtonsX + settingsXOffset / 1.4f);
		support.setXOffset(-bottomButtonsX + settingsXOffset / 1.4f);

		environment.setZOffset(settingsYOffset);
		endless.setZOffset(settingsYOffset);
		settings.setZOffset(settingsYOffset);
		quit.setZOffset(settingsYOffset);
		more.setZOffset(settingsYOffset);
		support.setZOffset(settingsYOffset);

		done.setZOffset(settingsYOffset + 600);
		done.setXOffset(settingsXOffset);

		dataDone.setZOffset(settingsYOffset + 1200);
		dataDone.setXOffset(settingsXOffset);

		delEnv.setZOffset(settingsYOffset + 1200);
		delEnv.setXOffset(settingsXOffset);
		delEnd.setZOffset(settingsYOffset + 1200);
		delEnd.setXOffset(settingsXOffset);
		delUsr.setZOffset(settingsYOffset + 1200);
		delUsr.setXOffset(settingsXOffset);
		delAll.setZOffset(settingsYOffset + 1200);
		delAll.setXOffset(settingsXOffset);

		sure.setZOffset(settingsYOffset + 1200);
		no.setZOffset(settingsYOffset + 1200);
		sure.setXOffset(settingsXOffset - 1000);
		no.setXOffset(settingsXOffset - 1000);

		data.setZOffset(settingsYOffset + 600);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);

		environment.render();
		endless.render();
		settings.render();
		quit.render();

		more.render();
		support.render();

		final AvianColor logoColor = new AvianColor(150, 150, 150);
		for (AvianMesh m : letters) {
			m.setShader(MainClass.shader);
			m.render(logoColor);
		}
		if (settingsYOffset != 0) {
			dataDone.render();
			delEnv.render();
			delEnd.render();
			delUsr.render();
			delAll.render();
			done.render();
			data.render();
			if (deleteCount <= 0) {
				sure.render();
				no.render();
			}

			if (db != null)
				db.render();

			smallGear.setYaw(smallGearYaw);
			mediumGear.setYaw(-smallGearYaw / 2);
			largeGear.setYaw(smallGearYaw / 4);

			smallGear.setX((-AvianApp.getWidth() / 2) + (gearScale * 1.5f) + settingsXOffset);
			mediumGear.setX((-AvianApp.getWidth() / 2) + settingsXOffset);
			largeGear.setX((AvianApp.getWidth() / 2) + settingsXOffset);

			smallGear.setZ((AvianApp.getHeight() * 1.5f) + settingsYOffset);
			mediumGear.setZ((AvianApp.getHeight() * 1.5f) + settingsYOffset);
			largeGear.setZ((AvianApp.getHeight() * 1.5f) + settingsYOffset);
			prism.setShader(MainClass.shader);
			if (BlockeryCore.musicLevel > 0f) {
				prism.set(-205 + (BlockeryCore.musicLevel * 100) + settingsXOffset, -11, -60 + settingsYOffset + AvianApp.getHeight(), BlockeryCore.musicLevel * 200, 22, 50);
				prism.render(BlockColor.GREEN.getColor());
			}
			if (BlockeryCore.musicLevel < 1f) {
				prism.set((-5 - ((1f - BlockeryCore.musicLevel) * 100)) + settingsXOffset, -11, -60 + settingsYOffset + AvianApp.getHeight(), (1f - BlockeryCore.musicLevel) * 200, 21, 49);
				prism.render(new AvianColor(0, 50, 0));
			}

			if (BlockeryCore.soundLevel > 0f) {
				prism.set(-205 + (BlockeryCore.soundLevel * 100) + settingsXOffset, -11, settingsYOffset + AvianApp.getHeight(), BlockeryCore.soundLevel * 200, 22, 50);
				prism.render(BlockColor.GREEN.getColor());
			}
			if (BlockeryCore.soundLevel < 1f) {
				prism.set((-5 - ((1f - BlockeryCore.soundLevel) * 100)) + settingsXOffset, -11, settingsYOffset + AvianApp.getHeight(), (1f - BlockeryCore.soundLevel) * 200, 21, 49);
				prism.render(new AvianColor(0, 50, 0));
			}

			final AvianColor gearColor = AvianColor.get(100, 100, 100);
			smallGear.setShader(MainClass.shader);
			mediumGear.setShader(MainClass.shader);
			largeGear.setShader(MainClass.shader);
			smallGear.render(gearColor);
			mediumGear.render(gearColor);
			largeGear.render(gearColor);
		}

	}

	AvianRectangle line = new AvianRectangle(505, 833 + settingsYOffset, 200, 1);

	AvianRectangle re = new AvianRectangle(0, 0, 3, 50);
	AvianRectangle highlight = new AvianRectangle(0, 0, AvianApp.getWidth(), 0);

	AvianColor warning = new AvianColor(230, 160, 100);

	public void render2D() {
		environment.render2D();
		endless.render2D();
		settings.render2D();
		quit.render2D();
		more.render2D();
		support.render2D();
		dataDone.render2D();
		done.render2D();
		data.render2D();
		delEnv.render2D();
		delEnd.render2D();
		delUsr.render2D();
		delAll.render2D();

		if (deleteCount <= 0) {
			sure.render2D();
			no.render2D();
		}

		if (db != null)
			db.render2D();

		Fonts.NewCicleSemi_20.drawString("Settings", (int) settingsXOffset + (AvianApp.getWidth() / 2), (int) (700f + settingsYOffset), new AvianColor(255, 255, 255), AvianFont.ALIGN_CENTER);
		Fonts.NewCicleSemi_23_BOLD.drawString("Music ", (int) ((500 - 105) + settingsXOffset), (int) ((900 - 60) + settingsYOffset), new AvianColor(255, 255, 255), AvianFont.ALIGN_CENTER, AvianFont.ALIGN_CENTER);
		Fonts.NewCicleSemi_23_BOLD.drawString("Sound ", (int) ((500 - 105) + settingsXOffset), (int) (900 + settingsYOffset), new AvianColor(255, 255, 255), AvianFont.ALIGN_CENTER, AvianFont.ALIGN_CENTER);
		//		Images.arrowR.draw(new AvianPoint((570 + notYouX + settingsXOffset), (823 + settingsYOffset)),  (int) (((notYouX + 10) / 10f) * 255f));
		Fonts.NewCicleSemi_20.drawString("Change", (int) (505 + settingsXOffset + notYouX), (int) (830 + settingsYOffset), new AvianColor(255, 255, 255, ((notYouX + 10) / 10f) * 255f), AvianFont.ALIGN_LEFT);
		Fonts.NewCicleSemi_20.drawString((int) (BlockeryCore.musicLevel * 100) + "%", (int) (mouseX + (AvianApp.getWidth() / 2) + settingsXOffset), (int) (800 + settingsYOffset + musicPY), new AvianColor(255, 255, 255, ((30 - musicPY) / 30) * 255f), AvianFont.ALIGN_LEFT, AvianFont.ALIGN_CENTER);
		Fonts.NewCicleSemi_20.drawString((int) (BlockeryCore.soundLevel * 100) + "%", (int) (mouseX + (AvianApp.getWidth() / 2) + settingsXOffset), (int) (940 + settingsYOffset + soundPY), new AvianColor(255, 255, 255, ((soundPY + 30) / 30) * 255f), AvianFont.ALIGN_LEFT, AvianFont.ALIGN_CENTER);
		Fonts.NewCicleSemi_20.drawString("Current User:", (int) (505 + settingsXOffset + notYouX + 10), (int) (830 + settingsYOffset), new AvianColor(255, 255, 255, 255f - (((notYouX + 10) / 10f) * 255f)), AvianFont.ALIGN_LEFT);
		line.setX(505 + settingsXOffset);
		line.setY(833 + settingsYOffset);
		line.render(new AvianColor(255, 255, 255));
		Fonts.NewCicleSemi_35.drawString(BlockeryCore.username != null ? BlockeryCore.username : "", (int) (505 + settingsXOffset), (int) (865 + settingsYOffset), new AvianColor(255, 255, 255), AvianFont.ALIGN_LEFT);
		Fonts.NewCicleSemi_20.drawString("Delete Data", (int) settingsXOffset + (AvianApp.getWidth() / 2), (int) (1300f + settingsYOffset), new AvianColor(255, 255, 255), AvianFont.ALIGN_CENTER);

		// XXX User selection
		AvianColor color = (sb.length() > 0) && name == Name.ACCEPTABLE ? new AvianColor(255, 255, 255) : new AvianColor(250, 65, 65);

		if (newUser) {
			Fonts.NewCicleSemi_20.drawString("Hi! Welcome to Blockery. Could I have your name?", (int) (settingsXOffset + 1500), (int) (150 + (settingsYOffset + 600)), new AvianColor(255, 255, 255), AvianFont.ALIGN_CENTER, AvianFont.ALIGN_CENTER);
			Fonts.NewCicleSemi_50.drawString(sb.toString(), (int) (settingsXOffset + 1500), (int) (300 + (settingsYOffset + 600)), color, AvianFont.ALIGN_CENTER, AvianFont.ALIGN_CENTER);

			switch (name) {
				case ACCEPTABLE:
					Fonts.NewCicleSemi_20.drawString("Press the enter key to confirm.", (int) (450 + (settingsYOffset + 600)), color, AvianFont.ALIGN_CENTER);
					break;
				case CUBEITE:
					Fonts.NewCicleSemi_20.drawString("Excuse me?", (int) (450 + (settingsYOffset + 600)), color = warning, AvianFont.ALIGN_CENTER);
					break;
				case EMPTY:
					Fonts.NewCicleSemi_20.drawString("Please type something before continuing.", (int) (450 + (settingsYOffset + 600)), color, AvianFont.ALIGN_CENTER);
					break;
				case EXISTS:
					Fonts.NewCicleSemi_20.drawString("This name already exists.", (int) (450 + (settingsYOffset + 600)), color, AvianFont.ALIGN_CENTER);
					break;
				case OFFENSIVE:
					Fonts.NewCicleSemi_20.drawString("Whoa, watch your language!", (int) (450 + (settingsYOffset + 600)), color, AvianFont.ALIGN_CENTER);
					break;
				case YOMAMA:
					Fonts.NewCicleSemi_20.drawString("My mama what?", (int) (450 + (settingsYOffset + 600)), color = warning, AvianFont.ALIGN_CENTER);
					break;
				case INAPPROPRIATE:
					Fonts.NewCicleSemi_20.drawString("That's not OK.", (int) (450 + (settingsYOffset + 600)), color, AvianFont.ALIGN_CENTER);
					break;
				case NOT_RECOMMENDED:
					Fonts.NewCicleSemi_20.drawString("...I'll let it go.", (int) (450 + (settingsYOffset + 600)), color = warning, AvianFont.ALIGN_CENTER);
					break;
				default:
					break;

			}
			Fonts.NewCicleSemi_50.drawString(sb.toString(), (int) (settingsXOffset + 1500), (int) (300 + (settingsYOffset + 600)), color, AvianFont.ALIGN_CENTER, AvianFont.ALIGN_CENTER);

			AvianColor caret = new AvianColor(color.getR(), color.getG(), color.getB(), color.getA());
			caret.setA(Math.abs(AvianMath.cos(caretInput)) * 255f);

			re.setX(500 + (Fonts.NewCicleSemi_50.getWidth(sb.toString()) / 2) + (settingsXOffset + 1000));
			re.setY(275 + (settingsYOffset + 600));
			re.render(caret);
		} else if (names != null) {
			if (slot >= 0) {
				highlight.setX(settingsXOffset + 1000);
				highlight.setY(100f + (slot * section) + (settingsYOffset + 600));
				highlight.setH(section);
				highlight.render(new AvianColor(255, 255, 255, 100));
			}
			Fonts.NewCicleSemi_20.drawString("Who are you?", (int) (settingsXOffset + 1500), (int) (50 + (settingsYOffset + 600)), new AvianColor(255, 255, 255), AvianFont.ALIGN_CENTER, AvianFont.ALIGN_CENTER);
			for (int i = 0; i < names.length; i++)
				Fonts.NewCicleSemi_23.drawString(names[i], (int) (settingsXOffset + 1500), (int) (100f + (i * section) + (section / 2f) + (settingsYOffset + 600)), new AvianColor(255, 255, 255), AvianFont.ALIGN_CENTER, AvianFont.ALIGN_CENTER);

		}
	}

	StringBuilder sb = new StringBuilder(System.getProperty("user.name"));
	float caretInput;
	boolean newUser = false;
	String[] names;
	int slot;
	float section;

	private void loadUsers() {
		File[] files = new File("C:\\Blockery").listFiles();
		ArrayList<String> users = new ArrayList<String>();
		for (File f : files) {
			String[] split = f.getName().split("\\.");
			if (split[split.length - 1].equals("blockeryuser"))
				users.add(f.getName().substring(0, f.getName().length() - ".blockeryuser".length()));

		}
		users.add("+ New...");

		if (users.size() == 1)
			newUser = true;

		this.names = users.toArray(new String[users.size()]);

		name = Name.ACCEPTABLE;

		checkStatement: if (sb.length() <= 0)
			name = Name.EMPTY;
		else if (sb.toString().equals("Cubeite"))
			name = Name.CUBEITE;
		else if (nameIncludes("yo") && nameIncludes("mama"))
			name = Name.YOMAMA;
		else {
			for (String s : users)
				if (s.equals(sb.toString())) {
					name = Name.EXISTS;
					break checkStatement;
				}

			for (String s : BlockeryPrivate.offensive)
				if (nameIncludes(s)) {
					name = Name.OFFENSIVE;
					break checkStatement;
				}

			for (String s : BlockeryPrivate.inappropriate)
				if (nameIncludes(s)) {
					name = Name.INAPPROPRIATE;
					break checkStatement;
				}

			for (String s : BlockeryPrivate.notRecommended)
				if (nameIncludes(s)) {
					name = Name.NOT_RECOMMENDED;
					break checkStatement;
				}

		}

		section = 500f / this.names.length;
	}

	AvianMesh box = new AvianMesh("/res/models/Prism.obj", 0, 0, 0, 20, 20, 20);

	private class DestroyBox {

		BoxBlock[][][] entireBox = new BoxBlock[4][4][4];

		float xOff, zOff;

		AvianColor color;

		AvianImage img;

		boolean active = false;

		String type;
		String message;

		public DestroyBox(String type) {
			deleteCount = 0;
			switch (this.type = type) {
				case "ENV":
					color = BlockColor.BLUE.getColor();
					img = Images.envIcon;
					message = "Seriously delete Environment progress?";
					break;
				case "END":
					color = BlockColor.GREEN.getColor();
					img = Images.endIcon;
					message = "Seriously delete Endless progress?";
					break;
				case "USR":
					color = BlockColor.ORANGE.getColor();
					img = Images.userCard;
					message = "Seriously delete personal stats?";
					break;
				case "ALL":
					color = BlockColor.RED.getColor();
					img = Images.mmIcon;
					message = "Delete your entire profile, user file, and everything Blockery that belongs to you?";
					break;
			}

			for (int i = 0; i < entireBox.length; i++)
				for (int j = 0; j < entireBox[i].length; j++)
					for (int k = 0; k < entireBox[i][j].length; k++)
						entireBox[i][j][k] = new BoxBlock(i, j, k);

		}

		public void logic() {
			for (int i = 0; i < entireBox.length; i++)
				for (int j = 0; j < entireBox[i].length; j++)
					for (int k = 0; k < entireBox[i][j].length; k++)
						entireBox[i][j][k].logic();
		}

		public void render() {
			for (int i = 0; i < entireBox.length; i++)
				for (int j = 0; j < entireBox[i].length; j++)
					for (int k = 0; k < entireBox[i][j].length; k++)
						entireBox[i][j][k].render();
		}

		public void render2D() {
			if (!active) {
				if (img != null)
					//					img.draw(new AvianPoint(500 + xOff, 300 + settingsYOffset + 1200), 1, 255);
					Fonts.NewCicleSemi_23.drawString(message, (int) (settingsXOffset - 500), (int) (150 + settingsYOffset + 1200), new AvianColor(255, 255, 255), AvianFont.ALIGN_CENTER, AvianFont.ALIGN_CENTER);
				Fonts.NewCicleSemi_20.drawString("This can't be undone! :(", (int) (settingsXOffset - 500), (int) (180 + settingsYOffset + 1200), new AvianColor(255, 255, 255), AvianFont.ALIGN_CENTER, AvianFont.ALIGN_CENTER);
			}
		}

		public void execute() {
			active = true;
			switch (type) {
				case "ENV":
					MainClass.env.resetGame();
					Audio.delete.play();
					break;
				case "END":
					MainClass.end.resetGame();
					Audio.delete.play();
					break;
				case "USR":
					BlockeryCore.resetStats();
					Audio.delete.play();
					break;
				case "ALL":
					new File("C:\\Blockery\\" + BlockeryCore.username + ".blockeryuser").delete();
					BlockeryCore.username = null;
					Audio.deleteForever.play();
					break;
			}
		}

		private class BoxBlock {

			float x, y, z;
			float xV, yV, zV;
			float pit, yaw, rol;
			float pitV, yawV, rolV;

			public BoxBlock(int x, int y, int z) {
				this.x = x * 20;
				this.y = y * 20;
				this.z = z * 20;
				xV = AvianMath.randomFloat() * 6f - 3f;
				yV = AvianMath.randomFloat() * 6f - 3f;
				zV = AvianMath.randomFloat() * 7f - 7f;
				pitV = AvianMath.randomFloat() * 3f - 1.5f;
				yawV = AvianMath.randomFloat() * 3f - 1.5f;
				rolV = AvianMath.randomFloat() * 3f - 1.5f;
			}

			public void logic() {
				if (active) {
					zV += .0981;
					x += xV;
					y += yV;
					z += zV;
					pit += pitV;
					yaw += yawV;
					rol += rolV;
				}
			}

			public void render() {
				box.setXYZPYR(x + xOff - 30f, y, z - 30f + zOff, pit, yaw, rol);
				box.setShader(MainClass.shader);
				box.render(color);
			}

		}
	}

	public boolean nameIncludes(String arg) {
		return sb.toString().toLowerCase().contains(arg.toLowerCase());
	}

}
