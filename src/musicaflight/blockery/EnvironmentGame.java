
package musicaflight.blockery;

import static org.lwjgl.opengl.GL11.*;

import java.text.DecimalFormat;
import java.util.Iterator;

import musicaflight.blockery.Block.BlockColor;
import musicaflight.blockery.EnvBlockProducer.ProducerType;
import musicaflight.blockery.MainClass.Screen;
import musicaflight.avianutils.*;

public class EnvironmentGame extends Game {

	int mouseX, mouseY;
	boolean mouseClickedOnce;

	int red, orange, blue, green;
	float pollution;
	float pps;
	boolean rush, gameOver, highScore, paused;

	float pauseScale;

	int ms, s, m, h;
	String pollutionTime;
	int smokeInfoTimer, smokeInfoSin;
	boolean showingPounds = true;
	float dangerSin;
	float sinOfDangerSin;

	float pauseSin;

	float nextFireworkTimer, nextFirework, fireworkTimer, counterTrigger;

	private float gOverSinInput, gOverZ;
	private static float gOverSin;
	private int moveTimer, previousBlockoins, newBlockoins, saveCount;
	private float finalBlocks;
	AvianColor color;

	private float r, g, b, Fr, Fg, Fb;
	private float newColorCount;

	public static AvianRectangle pauseBG = new AvianRectangle(0, 0, AvianApp.getWidth(), AvianApp.getHeight());

	AvianMesh gameOverMesh;

	int slotOffset;

	EnvBlockProducer cott = new EnvBlockProducer(ProducerType.COTTAGE);
	EnvBlockProducer fact = new EnvBlockProducer(ProducerType.FACTORY);
	EnvBlockProducer powh = new EnvBlockProducer(ProducerType.POWERHOUSE);
	EnvBlockProducer mpow = new EnvBlockProducer(ProducerType.MEGAPOWERHOUSE);
	EnvBlockProducer tree = new EnvBlockProducer(ProducerType.TREE);

	EnvBlockProducer[] producers = new EnvBlockProducer[] { cott, fact, powh,
			mpow, tree };

	Button rpButton = new Button("Play Again", Images.rpIcon, new AvianPoint(-35, 150), new AvianPoint(50, 50), new AvianPoint(-85, 150), new AvianPoint(150, 50), AvianColor.get(232, 115, 121));
	Button mmButton = new Button("Main Menu", Images.mmIcon, new AvianPoint(35, 150), new AvianPoint(50, 50), new AvianPoint(85, 150), new AvianPoint(150, 50), AvianColor.get(125, 209, 213));
	Button pausemmButton = new Button("Main Menu", Images.mmIcon, new AvianPoint(0, 150), new AvianPoint(50, 50), new AvianPoint(0, 150), new AvianPoint(150, 50), AvianColor.get(125, 209, 213));

	AvianMesh pollutionMeter, gui, cage, light;
	String meshLoc = "/res/models/Prism.obj";

	public EnvironmentGame() {
		super();
		blocks = new Block[14][10];
		for (int r = 0; r < blocks.length; r++)
			for (int c = 0; c < blocks[r].length; c++)
				blocks[r][c] = new Block(r, c);
		gui = new AvianMesh(meshLoc, (531f + (438f / 2f)) - (AvianApp.getWidth() / 2f), -11, (97f + (47f / 2f)) - (AvianApp.getHeight() / 2f), 438f, 22f, 47f);
		pollutionMeter = new AvianMesh(meshLoc, (469f + (31f / 2f)) - (AvianApp.getWidth() / 2f), -11, (50f + (500f / 2f)) - (AvianApp.getHeight() / 2f), 31f, 22f, 500f);
		cage = new AvianMesh(meshLoc, (422f + (15f / 2f)) - (AvianApp.getWidth() / 2f), -3, (65f + (485f / 2f)) - (AvianApp.getHeight() / 2f), 15f, 6f, 485f);
		light = new AvianMesh(meshLoc, (422f + (15f / 2f)) - (AvianApp.getWidth() / 2f), -3, (50f + (15f / 2f)) - (AvianApp.getHeight() / 2f), 15f, 6f, 15f);

		gameOverMesh = new AvianMesh("/res/models/GameOver.obj", 0, 0, 0, 8, 20, 8);
	}

	protected void initializeBlocks() {
		blocks = new Block[14][10];
		for (int r = 0; r < blocks.length; r++)
			for (int c = 0; c < blocks[r].length; c++)
				blocks[r][c] = new Block(r, c);
	}

	public void keyboard() {
		if (AvianInput.isKeyDown(AvianInput.KEY_S) && (AvianInput.isKeyDown(AvianInput.KEY_LEFT_CONTROL) || AvianInput.isKeyDown(AvianInput.KEY_RIGHT_CONTROL)))
			FileCabinet.saveUser();
	}

	public void mouse() {
		mouseX = (int) ((AvianInput.getMouseX()) - (AvianApp.getWidth() / 2));
		mouseY = (int) ((AvianApp.getHeight() - AvianInput.getMouseY()) - (AvianApp.getHeight() / 2));

		// slotOffset += Mouse.getDWheel() / 120;

		if (gameOver) {
			mmButton.hover(mouseX, mouseY);
			rpButton.hover(mouseX, mouseY);
		} else if (paused)
			pausemmButton.hover(mouseX, mouseY);

		if (AvianInput.isMouseButtonDown(0) && !mouseClickedOnce) {
			if (!gameOver) {
				if (!paused) {
					beginElimination(mouseX, mouseY);
					for (EnvBlockProducer bp : producers)
						bp.clicked(mouseX, mouseY);
				}
				if ((mouseX > (734 - (AvianApp.getWidth() / 2))) && (mouseX < ((734 + 31) - (AvianApp.getWidth() / 2))) && (mouseY > (50 - (AvianApp.getHeight() / 2))) && (mouseY < ((50 + 31) - (AvianApp.getHeight() / 2))) && !paused) {
					paused = true;
					if (BlockeryCore.soundLevel > 0f)
						Audio.pause.play();
				} else if (pausemmButton.clicked() && paused) {
					paused = false;
					for (int r = 0; r < blocks.length; r++)
						for (int c = 0; c < blocks[r].length; c++) {
							blocks[r][c] = new Block(r, c);
						}

					MainClass.screen = Screen.MAIN;
					if (BlockeryCore.soundLevel > 0f)
						Audio.accept.play();
				} else if (paused) {
					paused = false;
					if (BlockeryCore.soundLevel > 0f)
						Audio.accept.play();
				}
			} else {
				if (mmButton.clicked()) {
					if (BlockeryCore.soundLevel > 0f)
						Audio.accept.play();
					gameOver = false;
					MainClass.screen = Screen.MAIN;
				}
				if (rpButton.clicked()) {
					gameOver = false;
					if (BlockeryCore.soundLevel > 0f)
						Audio.beginGame.play();
					FileCabinet.saveUser();
				}
			}
			mouseClickedOnce = true;
		} else if (!AvianInput.isMouseButtonDown(0))
			mouseClickedOnce = false;

	}

	public void logic() {
		bps = 0;
		for (EnvBlockProducer bp : producers)
			bps += bp.owned() * bp.BPS();

		if (rush)
			dangerSin += 3;
		else {
			dangerSin %= 90;
			dangerSin--;
			if (dangerSin < 0)
				dangerSin = 0;
		}
		sinOfDangerSin = Math.abs(AvianMath.sin(dangerSin));

		pausemmButton.logic();

		if (!paused) {

			if (!gameOver) {
				counterLogic();
				blockLogic();
				smokeLogic();
				itemLogic();
				moveTimer = 0;
				gOverSinInput = 0;
			} else {
				mmButton.logic();
				rpButton.logic();
				gameOverLogic();
			}
			pauseScale = 7;
			pauseSin = 0;
		} else {
			pauseScale = AvianMath.glide(pauseScale, 6, 10);
			pauseSin++;
		}
		if (!gameOver)
			autoSave();
	}

	public void render() {

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);

		gOverSin = (float) AvianMath.specialrtSin(gOverSinInput, 2);

		if (gameOver) {
			glPushMatrix();

			gameOverMesh.setShader(MainClass.shader);
			gameOverMesh.setY((1f - gOverSin) * 750f);
			gameOverMesh.setZ(gOverZ);
			gameOverMesh.render(AvianColor.get(150, 150, 150));
			mmButton.render();
			rpButton.render();

			glTranslatef(0, gOverSin * -50000f, 0);
		}

		if (!paused) {

			block.setShader(MainClass.shader);
			if (gOverSin < 1) {
				gui.setY(-11f);
				for (int r = 0; r < blocks.length; r++)
					for (int c = 0; c < blocks[r].length; c++)
						if (blocks[r][c] != null) {
							block.setXYZ(blocks[r][c].getX(), blocks[r][c].getY(), blocks[r][c].getZ());
							block.render(blocks[r][c].getBlockColor());
						}

				cott.render();
				fact.render();
				powh.render();
				mpow.render();
				tree.render();

				gui.setY(-11f);

				gui.setShader(MainClass.shader);
				gui.setXYWH((531f + (438f / 2f)) - (AvianApp.getWidth() / 2f), (97f + (47f / 2f)) - (AvianApp.getHeight() / 2f), 438f, 47f); // Block Counter
				gui.render(AvianColor.get(0, 140, 232));
				gui.setXYWH((531f + (188f / 2f)) - (AvianApp.getWidth() / 2f), (50f + (31f / 2f)) - (AvianApp.getHeight() / 2f), 188f, 31f); // BPS Counter
				gui.render(AvianColor.get(0, 170, 197));

				gui.setXYWH((734 + (31f / 2f)) - (AvianApp.getWidth() / 2f), (50f + (31f / 2f)) - (AvianApp.getHeight() / 2f), 31f, 31f); // Pause Button
				gui.render(AvianColor.get(170, 170, 170));
				gui.set(744.5f - (AvianApp.getWidth() / 2f), -5f, (50f + (31f / 2f)) - (AvianApp.getHeight() / 2f), 4f, 22f, 16f); // Pause Button
				gui.render(AvianColor.get(200, 200, 200));
				gui.set(754.5f - (AvianApp.getWidth() / 2f), -5f, (50f + (31f / 2f)) - (AvianApp.getHeight() / 2f), 4f, 22f, 16f); // Pause Button
				gui.render(AvianColor.get(200, 200, 200));

				gui.setXYWH((781f + (188f / 2f)) - (AvianApp.getWidth() / 2f), (50f + (31f / 2f)) - (AvianApp.getHeight() / 2f), 188f, 31f); // PPS Counter
				gui.render(AvianColor.get((233f + (sinOfDangerSin * 22f)), (156f - (sinOfDangerSin * 156f)), 0));

				pollutionMeter.setShader(MainClass.shader);
				if (pollution < 10000) {
					pollutionMeter.setD(((10000f - (float) pollution) / 10000f) * 500f);
					pollutionMeter.setZ((50f + (pollutionMeter.getD() / 2f)) - (AvianApp.getHeight() / 2f));
					pollutionMeter.render(AvianColor.get(248, 248, 248));
				}

				if (pollution > 0) {
					pollutionMeter.setD((((float) pollution) / 10000f) * 500f);
					pollutionMeter.setZ((550f - (pollutionMeter.getD() / 2f)) - (AvianApp.getHeight() / 2f));
					pollutionMeter.render(AvianColor.get(50, 50, 50));
				}

				cage.setShader(MainClass.shader);
				cage.setXYWH((422f + (15f / 2f)) - (AvianApp.getWidth() / 2f), (65f + (485f / 2f)) - (AvianApp.getHeight() / 2f), 15f, 485f);
				cage.render(AvianColor.get(248, 248, 248));
				cage.setX((062f + (15f / 2f)) - (AvianApp.getWidth() / 2f));
				cage.render(AvianColor.get(248, 248, 248));

				cage.setXYWH((77f + (345f / 2f)) - (AvianApp.getWidth() / 2f), (535f + (15f / 2f)) - (AvianApp.getHeight() / 2f), 345, 15);
				cage.render(AvianColor.get(248, 248, 248));

				light.setShader(MainClass.shader);
				light.setX((422f + (15f / 2f)) - (AvianApp.getWidth() / 2f));
				light.render(AvianColor.get(240, (240f - (sinOfDangerSin * 240f)), (240f - (sinOfDangerSin * 240f))));
				light.setX((062f + (15f / 2f)) - (AvianApp.getWidth() / 2f));
				light.render(AvianColor.get(240, (240f - (sinOfDangerSin * 240f)), (240f - (sinOfDangerSin * 240f))));
			}
		} else {
			pausemmButton.render();
		}
		if (gameOver)
			glPopMatrix();

		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);

	}

	public void render2D() {

		if (!gameOver) {
			if (!paused) {
				Fonts.NewCicleSemi_23.drawString("blocks", 963, 140, AvianColor.get(255, 255, 255), AvianFont.ALIGN_LEFT);
				Fonts.SF_Digital_Readout_54.drawString(format.format(totalBlocks), 900, 140, AvianColor.get(255, 255, 255), AvianFont.ALIGN_RIGHT);
				Fonts.NewCicleSemi_18.drawString(format2.format(bps) + " BPS", 625, 66, AvianColor.get(255, 255, 255), AvianFont.ALIGN_CENTER, AvianFont.ALIGN_CENTER);
				Fonts.NewCicleSemi_18.drawString(format2.format(pps) + " PPS", 875, 66, AvianColor.get(255, 255, 255), AvianFont.ALIGN_CENTER, AvianFont.ALIGN_CENTER);

				float sinSin = AvianMath.sin(smokeInfoSin);
				float cosSin = AvianMath.cos(smokeInfoSin);

				Fonts.NewCicleSemi_18.drawString(pollution + " / 10000 pounds", ((781f + (188f / 2f) + ((AvianMath.randomInt(5) * sinOfDangerSin) - (2.5f * sinOfDangerSin)))), (35f - (10f * sinSin)), AvianColor.get((233f + (sinOfDangerSin * 22f)), (156f - (sinOfDangerSin * 156f)), 0, (255f * sinSin)), AvianFont.ALIGN_CENTER);
				Fonts.SF_Digital_Readout_30.drawString(((pollution / 10000f) * 100f) + "%", ((781f + (188f / 2f) + ((AvianMath.randomInt(5) * sinOfDangerSin) - (2.5f * sinOfDangerSin)))), (58f - (10f * sinSin)), AvianColor.get((233f + (sinOfDangerSin * 22f)), (156f - (sinOfDangerSin * 156f)), 0, (255f * sinSin)), AvianFont.ALIGN_CENTER);

				if (pps > 0) {
					Fonts.NewCicleSemi_18.drawString("CO2 overload in", (781f + (188f / 2f) + ((AvianMath.randomInt(5) * sinOfDangerSin) - (2.5f * sinOfDangerSin))), (35f - (10f * cosSin)), AvianColor.get((233f + (sinOfDangerSin * 22f)), (156f - (sinOfDangerSin * 156f)), 0, (255f * cosSin)), AvianFont.ALIGN_CENTER);
					Fonts.SF_Digital_Readout_30.drawString(pollutionTime, ((781f + (188f / 2f) + ((AvianMath.randomInt(3) * sinOfDangerSin) - (1.5f * sinOfDangerSin)))), (58f - (10f * cosSin)), AvianColor.get((233f + (sinOfDangerSin * 22f)), (156f - (sinOfDangerSin * 156f)), 0, (255f * cosSin)), AvianFont.ALIGN_CENTER);
				}

				boolean onLeaderboard = false;

				for (int i = 0; i < BlockeryCore.highScores.length; i++) {
					if (totalBlocks > BlockeryCore.highScores[i]) {
						onLeaderboard = true;
						switch (i) {
							case 0:
								AvianUtils.drawDualFontString(Fonts.NewCicleSemi_23_BOLD, Fonts.NewCicleSemi_23, "High Score ", format.format(totalBlocks), 250, 55, AvianColor.get(150, 200, 150), false);
								break;
							case 1:
								AvianUtils.drawDualFontString(Fonts.NewCicleSemi_23_BOLD, Fonts.NewCicleSemi_23, "High Score ", format.format(BlockeryCore.highScores[0]), 250, 55, AvianColor.get(150, 150, 150), false);
								break;
							case 2:
								Fonts.NewCicleSemi_16.drawString("High Score " + format.format(BlockeryCore.highScores[0]), 250, 30, AvianColor.get(150, 150, 150), AvianFont.ALIGN_BOTTOM);
								AvianUtils.drawDualFontString(Fonts.NewCicleSemi_23_BOLD, Fonts.NewCicleSemi_23, "2nd ", format.format(BlockeryCore.highScores[1]), 250, 55, AvianColor.get(150, 150, 150), false);
								break;
							case 3:
								Fonts.NewCicleSemi_16.drawString("High Score " + format.format(BlockeryCore.highScores[0]), 250, 30, AvianColor.get(150, 150, 150), AvianFont.ALIGN_BOTTOM);
								AvianUtils.drawDualFontString(Fonts.NewCicleSemi_23_BOLD, Fonts.NewCicleSemi_23, "3rd ", format.format(BlockeryCore.highScores[2]), 250, 55, AvianColor.get(150, 150, 150), false);
								break;
							case 4:
								Fonts.NewCicleSemi_16.drawString("High Score " + format.format(BlockeryCore.highScores[0]), 250, 30, AvianColor.get(150, 150, 150), AvianFont.ALIGN_BOTTOM);
								AvianUtils.drawDualFontString(Fonts.NewCicleSemi_23_BOLD, Fonts.NewCicleSemi_23, "4th ", format.format(BlockeryCore.highScores[3]), 250, 55, AvianColor.get(150, 150, 150), false);
								break;

						}
						break;
					}
				}

				if (!onLeaderboard) {
					Fonts.NewCicleSemi_16.drawString("High Score " + format.format(BlockeryCore.highScores[0]), 250, 30, AvianColor.get(150, 150, 150), AvianFont.ALIGN_BOTTOM);
					AvianUtils.drawDualFontString(Fonts.NewCicleSemi_23_BOLD, Fonts.NewCicleSemi_23, "5th ", format.format(BlockeryCore.highScores[4]), 250, 55, AvianColor.get(150, 150, 150), false);
				}

				for (EnvBlockProducer bp : producers)
					bp.render2D();

				glEnableClientState(GL_VERTEX_ARRAY);
				glEnableClientState(GL_COLOR_ARRAY);

				Iterator<BlockFirework> iter = particles.iterator();
				while (iter.hasNext()) {
					BlockFirework p = iter.next();
					p.render();
				}

				glDisableClientState(GL_VERTEX_ARRAY);
				glDisableClientState(GL_COLOR_ARRAY);
			} else {
				pausemmButton.render2D();
				//				Images.paused.draw(new AvianPoint(AvianAppCore.getWidth() / 2, AvianAppCore.getHeight() / 2), pauseScale,  ((7f - pauseScale) * 255f));
				Fonts.NewCicleSemi_20.drawString("Click anywhere to resume...", 350, AvianColor.get(255, 255, 255, ((255f / 2f) + (AvianMath.sin(pauseSin) * (255f / 2)))), AvianFont.ALIGN_CENTER);
			}
		} else {
			mmButton.render2D();
			rpButton.render2D();
			Images.blockoin.render(15f, 165 + gOverZ, ((gOverZ / -150f) * 255f));
			Fonts.NewCicleSemi_16.drawString(format.format(previousBlockoins), 36, gOverZ + 178, AvianColor.get(150, 150, 150, ((gOverZ / -150f) * 255f)), AvianFont.ALIGN_LEFT);
			color = AvianColor.get(color.getR(), color.getG(), color.getB(), ((gOverZ / -150f) * 255f));
			Fonts.NewCicleSemi_23.drawString("Cash reward", (AvianApp.getWidth() / 2) - 300, 490 + gOverZ, color, AvianFont.ALIGN_LEFT);
			Images.blockoin.render((AvianApp.getWidth() / 2f) - 300f, 500 + gOverZ, ((gOverZ / -150f) * 255f));
			Fonts.NewCicleSemi_18.drawString(format.format(newBlockoins), (AvianApp.getWidth() / 2) - 280, gOverZ + 515, color, AvianFont.ALIGN_LEFT);
			glEnableClientState(GL_VERTEX_ARRAY);
			glEnableClientState(GL_COLOR_ARRAY);
			if (highScore) {
				Fonts.NewCicleSemi_23.drawString("New High Score!", (AvianApp.getWidth() / 2) - 300, gOverZ + 365, AvianColor.get(r, g, b, ((gOverZ / -150f) * 255f)), AvianFont.ALIGN_LEFT);
				Iterator<Firework> iter = fireworks.iterator();
				while (iter.hasNext())
					iter.next().render();

			} else
				Fonts.NewCicleSemi_23.drawString("Final Score", (AvianApp.getWidth() / 2) - 300, gOverZ + 365, AvianColor.get(150, 150, 150, ((gOverZ / -150f) * 255f)), AvianFont.ALIGN_LEFT);

			Iterator<FlyingFirework> iterCoin = coinParticles.iterator();
			while (iterCoin.hasNext()) {
				FlyingFirework p = iterCoin.next();
				if (p.particleSize > .04f)
					p.render();
				else
					iterCoin.remove();
			}
			glDisableClientState(GL_VERTEX_ARRAY);
			glDisableClientState(GL_COLOR_ARRAY);

			Fonts.NewCicleSemi_50.drawString(format.format(finalBlocks), (AvianApp.getWidth() / 2) - 300, gOverZ + 415, AvianColor.get(r, g, b, ((gOverZ / -150f) * 255f)), AvianFont.ALIGN_LEFT);
			Fonts.NewCicleSemi_23_BOLD.drawString("Leaderboard", (AvianApp.getWidth() / 2) + 300, gOverZ + 365, AvianColor.get(150, 150, 150, ((gOverZ / -150f) * 255f)), AvianFont.ALIGN_RIGHT);
			for (int i = 0; i < BlockeryCore.highScores.length; i++) {
				Fonts.NewCicleSemi_23_BOLD.drawString((i + 1) + ".", (AvianApp.getWidth() / 2) + 150, gOverZ + 400 + (30 * i), AvianColor.get(150, 150, 150, ((gOverZ / -150f) * 255f)), AvianFont.ALIGN_LEFT);
				Fonts.NewCicleSemi_23.drawString(format.format(BlockeryCore.highScores[i]), (AvianApp.getWidth() / 2) + 300, gOverZ + 400 + (30 * i), AvianColor.get(150, 150, 150, ((gOverZ / -150f) * 255f)), AvianFont.ALIGN_RIGHT);
			}
		}
	}

	public void beginElimination(float x, float y) {
		int row = (int) ((((y + 11f) + (AvianApp.getHeight() / 2f)) - 95f) / 32f);
		int col = (int) ((((x + 11f) + (AvianApp.getWidth() / 2f)) - 105f) / 32f);

		boolean isGreaterThanOne = false;

		if ((row >= 0) && (row <= (blocks.length - 1)) && (col >= 0) && (col <= (blocks[0].length - 1))) {
			if (blocks[row][col] == null)
				return;

			if ((row > 0) && (blocks[row - 1][col] != null) && (blocks[row - 1][col].getBlockColorEnum() == blocks[row][col].getBlockColorEnum()))
				isGreaterThanOne = true;

			if ((row < (blocks.length - 1)) && (blocks[row + 1][col] != null) && (blocks[row + 1][col].getBlockColorEnum() == blocks[row][col].getBlockColorEnum()))
				isGreaterThanOne = true;

			if ((row < (blocks.length - 1)) && (blocks[row + 1][col] != null) && (blocks[row + 1][col].getBlockColorEnum() == blocks[row][col].getBlockColorEnum()))
				isGreaterThanOne = true;

			if ((col > 0) && (blocks[row][col - 1] != null) && (blocks[row][col - 1].getBlockColorEnum() == blocks[row][col].getBlockColorEnum()))
				isGreaterThanOne = true;

			if ((col < (blocks[0].length - 1)) && (blocks[row][col + 1] != null) && (blocks[row][col + 1].getBlockColorEnum() == blocks[row][col].getBlockColorEnum()))
				isGreaterThanOne = true;

			Audio.badBlock.playWithRandomPitch();

			if (isGreaterThanOne)
				eliminateBlocks(row, col, blocks[row][col].getBlockColorEnum());

		}
	}

	public void eliminateBlocks(int row, int col, BlockColor color) {
		if ((row < 0) || (row > (blocks.length - 1)) || (col < 0) || (col > (blocks[0].length - 1)) || (blocks[row][col] == null))
			return;

		if (blocks[row][col].getBlockColorEnum() == color) {
			switch (color) {
				case BLUE:
					blue++;
					break;
				case GREEN:
					green++;
					break;
				case ORANGE:
					orange++;
					break;
				case RED:
					red++;
					break;
				default:
					break;

			}
			particles.add(new BlockFirework(blocks[row][col].getX() + (AvianApp.getWidth() / 2), blocks[row][col].getZ() + (AvianApp.getHeight() / 2), blocks[row][col].getBlockColorEnum(), ""));
			blocks[row][col] = null;

			newBlocks++;

			eliminateBlocks(row - 1, col, color);
			eliminateBlocks(row + 1, col, color);
			eliminateBlocks(row, col - 1, color);
			eliminateBlocks(row, col + 1, color);
		}
	}

	protected void autoSave() {
		saveCount++;
		if (saveCount >= 12000) {
			FileCabinet.saveUser();
			saveCount = 0;
		}
	}

	// TODO Fancy smoke clouds

	private void smokeLogic() {
		smokeInfoTimer++;
		if (smokeInfoTimer >= 500) {
			if (showingPounds && (pps > 0))
				showingPounds = false;
			else
				showingPounds = true;
			smokeInfoTimer = 0;
		}
		if (pps <= 0) {
			showingPounds = true;
			smokeInfoTimer = 0;
		}
		if (pollution <= 0) {
			showingPounds = false;
			smokeInfoTimer = 0;
		}
		if (!paused) {
			if (showingPounds) {
				smokeInfoSin += 3;
				if (smokeInfoSin > 90)
					smokeInfoSin = 90;
			} else {
				smokeInfoSin -= 3;
				if (smokeInfoSin < 0)
					smokeInfoSin = 0;
			}
		}
		pps = 0;
		for (EnvBlockProducer bp : producers)
			pps += bp.owned() * bp.PPS();
		pollution += (pps / 100f);

		if (pollution > 10000)
			pollution = 10000;
		else if (pollution < 0)
			pollution = 0;

		ms = (int) ((10000 - pollution) / (pps / 1000f));
		s = (ms / 1000) % 60;
		m = (ms / (60 * 1000)) % 60;
		h = ms / (60 * 60 * 1000);
		DecimalFormat format = new DecimalFormat("00");
		rush = ((ms <= 60000) && (pps > 0)) ? true : false;
		pollutionTime = rush ? (format.format(s) + "." + format.format(((ms % 1000) / 10))) : (h + ":" + format.format(m) + ":" + format.format(s));
		if (pollution >= 10000)
			gameOver();

	}

	protected void itemLogic() {
		for (EnvBlockProducer bp : producers)
			bp.logic();
	}

	private void gameOver() {
		fireworks.clear();
		particles.clear();
		coinParticles.clear();
		gameOver = true;
		finalBlocks = (float) totalBlocks;
		checkForHighScore(finalBlocks);
		previousBlockoins = BlockeryCore.blockoins;
		int colorOfTheGame = AvianMath.randomInt(4);
		switch (colorOfTheGame) {
			case 0:
				color = AvianColor.get(255, 0, 76);
				newBlockoins = red / 10;
				break;
			case 1:
				color = AvianColor.get(255, 165, 48);
				newBlockoins = orange / 10;
				break;
			case 2:
				color = AvianColor.get(80, 236, 140);
				newBlockoins = green / 10;
				break;
			case 3:
				color = AvianColor.get(17, 124, 255);
				newBlockoins = blue / 10;
				break;
		}
		BlockeryCore.blockoins += newBlockoins;
		resetGame();
	}

	private void checkForHighScore(float score) {
		for (int i = 0; i < BlockeryCore.highScores.length; i++)
			if (score > BlockeryCore.highScores[i]) {
				highScore = i == 0;
				for (int j = 4; j > i; j--)
					BlockeryCore.highScores[j] = BlockeryCore.highScores[j - 1];

				BlockeryCore.highScores[i] = score;
				break;
			}

	}

	private void gameOverLogic() {
		//		TODO gameOverMesh.logic(10);
		moveTimer++;
		if (moveTimer >= 200)
			gOverZ = AvianMath.glide(gOverZ, -150, 10);

		if ((moveTimer >= 400) && ((moveTimer % 3) == 1))
			if (previousBlockoins < BlockeryCore.blockoins) {
				previousBlockoins++;
				coinParticles.add(new FlyingFirework());
				if (BlockeryCore.soundLevel > 0f)
					Audio.coin.play();
			}

		Iterator<FlyingFirework> iterCoin = coinParticles.iterator();
		while (iterCoin.hasNext()) {
			FlyingFirework p = iterCoin.next();
			if (p.particleSize > .04f)
				p.logic();
			else
				iterCoin.remove();
		}
		if (gOverSinInput < 90) {
			gOverSinInput += 1;
			if (gOverSinInput > 90)
				gOverSinInput = 90;
		}
		if (highScore) {
			fireworkTimer += .1f;
			nextFireworkTimer += .1f;
			newColorCount += .1f;
			if ((newColorCount >= 1) && (moveTimer >= 200)) {
				Fr = AvianMath.randomInt(156) + 75;
				Fg = AvianMath.randomInt(156) + 75;
				Fb = AvianMath.randomInt(156) + 75;
				newColorCount = 0;
			}
			r = AvianMath.glide(r, Fr, 20);
			g = AvianMath.glide(g, Fg, 20);
			b = AvianMath.glide(b, Fb, 20);
			if ((nextFireworkTimer > nextFirework) && (fireworkTimer >= 1.5) && (fireworkTimer <= 10)) {
				fireworks.add(new Firework(true));
				nextFirework = (AvianMath.randomFloat() / 3f);
				nextFireworkTimer = 0;
			}
			Iterator<Firework> iter = fireworks.iterator();
			while (iter.hasNext()) {
				Firework f = iter.next();
				f.logic();
				if (f.particleSize <= 1) {
					iter.remove();
				}
			}
		} else
			r = Fr = g = Fg = b = Fb = 150;

	}

	protected void resetGame() {
		for (int r = 0; r < blocks.length; r++)
			for (int c = 0; c < blocks[r].length; c++)
				blocks[r][c] = new Block(r, c);

		smokeInfoTimer = smokeInfoSin = 0;

		red = orange = blue = green = newBlocks = 0;
		totalBlocks = pollution = 0;
		bps = pps = 0;
		rush = paused = false;

		ms = s = m = h = 0;

		nextFireworkTimer = nextFirework = fireworkTimer = counterTrigger = 0;

		gOverSinInput = gOverZ = gOverSin = 0;
		moveTimer = saveCount = 0;

		r = g = b = Fr = Fg = Fb = 0;
		newColorCount = 0;

		for (EnvBlockProducer bp : producers)
			bp.reset();

		FileCabinet.saveUser();
	}
}
