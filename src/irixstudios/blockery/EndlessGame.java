
package irixstudios.blockery;

import static org.lwjgl.opengl.GL11.*;

import java.util.Iterator;

import irixstudios.blockery.Block.BlockColor;
import irixstudios.blockery.EndlBlockProducer.EndlProducerType;
import irixstudios.blockery.MainClass.Screen;
import musicaflight.avianutils.*;

public class EndlessGame extends Game {

	int mouseX, mouseY;
	boolean mouseClickedOnce;

	boolean paused;

	float pauseScale;

	float pauseSin;

	AvianColor color;

	public static AvianRectangle particle = new AvianRectangle(),
			pauseBG = new AvianRectangle(0, 0, AvianApp.getWidth(), AvianApp.getHeight());

	AvianMesh pollutionMeter, gui, cage, light;

	EndlBlockProducer bkit = new EndlBlockProducer(EndlProducerType.BLOCKKIT);
	EndlBlockProducer cott = new EndlBlockProducer(EndlProducerType.COTTAGE);
	EndlBlockProducer fact = new EndlBlockProducer(EndlProducerType.FACTORY);
	EndlBlockProducer mine = new EndlBlockProducer(EndlProducerType.MINE);
	EndlBlockProducer powh = new EndlBlockProducer(EndlProducerType.POWERHOUSE);
	EndlBlockProducer mpow = new EndlBlockProducer(EndlProducerType.MEGAPOWERHOUSE);
	EndlBlockProducer bldo = new EndlBlockProducer(EndlProducerType.BLOCKDOUBLER);

	EndlBlockProducer[] producers = new EndlBlockProducer[] { bkit, cott, fact,
			mine, powh, mpow, bldo };

	Button pausemmButton = new Button("Main Menu", Images.mmIcon, new AvianPoint(0, 150), new AvianPoint(50, 50), new AvianPoint(0, 150), new AvianPoint(150, 50), AvianColor.get(125, 209, 213));

	private int saveCount;

	int slotOffset;

	protected EndlessGame() {
		super();
		initializeBlocks();
		gui = new AvianMesh("/res/models/Prism.obj", (531f + (438f / 2f)) - (AvianApp.getWidth() / 2f), -11, (97f + (47f / 2f)) - (AvianApp.getHeight() / 2f), 438f, 22f, 47f);
		pollutionMeter = new AvianMesh("/res/models/Prism.obj", (469f + (31f / 2f)) - (AvianApp.getWidth() / 2f), -11, (50f + (500f / 2f)) - (AvianApp.getHeight() / 2f), 31f, 22f, 500f);
		cage = new AvianMesh("/res/models/Prism.obj", (422f + (15f / 2f)) - (AvianApp.getWidth() / 2f), -3, (65f + (485f / 2f)) - (AvianApp.getHeight() / 2f), 15f, 6f, 485f);
		light = new AvianMesh("/res/models/Prism.obj", (422f + (15f / 2f)) - (AvianApp.getWidth() / 2f), -3, (50f + (15f / 2f)) - (AvianApp.getHeight() / 2f), 15f, 6f, 15f);

	}

	protected void initializeBlocks() {
		blocks = new Block[14][12];
		for (int r = 0; r < blocks.length; r++)
			for (int c = 0; c < blocks[r].length; c++)
				blocks[r][c] = new Block(r, c);
	}

	protected void keyboard() {
		if (AvianInput.isKeyDown(AvianInput.KEY_S) && (AvianInput.isKeyDown(AvianInput.KEY_LEFT_CONTROL) || AvianInput.isKeyDown(AvianInput.KEY_RIGHT_CONTROL)))
			FileCabinet.saveUser();
		if (AvianInput.isKeyDown(AvianInput.KEY_UP))
			slotOffset++;
		else if (AvianInput.isKeyDown(AvianInput.KEY_DOWN))
			slotOffset--;

	}

	protected void mouse() {
		mouseX = (int) ((AvianInput.getMouseX()) - (AvianApp.getWidth() / 2));
		mouseY = (int) ((AvianApp.getHeight() - AvianInput.getMouseY()) - (AvianApp.getHeight() / 2));

		if (paused)
			pausemmButton.hover(mouseX, mouseY);

		for (EndlBlockProducer bp : producers)
			bp.hover(mouseX, mouseY);

		if (AvianInput.isMouseButtonDown(0) && !mouseClickedOnce) {
			if (!paused) {
				beginElimination(mouseX, mouseY);
				for (EndlBlockProducer bp : producers)
					bp.clicked(mouseX, mouseY);

			}
			if (mouseX > 938 - (AvianApp.getWidth() / 2) && mouseX < 938 + 31 - (AvianApp.getWidth() / 2) && mouseY > 50 - (AvianApp.getHeight() / 2) && mouseY < 50 + 31 - (AvianApp.getHeight() / 2) && !paused) {
				paused = true;
				if (BlockeryCore.soundLevel > 0f)
					Audio.pause.play();
			} else if (pausemmButton.clicked() && paused) {
				paused = false;
				for (int r = 0; r < blocks.length; r++)
					for (int c = 0; c < blocks[r].length; c++)
						blocks[r][c] = new Block(r, c);

				MainClass.screen = Screen.MAIN;
				if (BlockeryCore.soundLevel > 0f)
					Audio.accept.play();
			} else if (paused) {
				paused = false;
				if (BlockeryCore.soundLevel > 0f)
					Audio.accept.play();
			} else if (mouseX >= 61f && mouseX <= 469f) {
				if (mouseY < 160f - 300f)
					slotOffset++;
				else if (mouseY > (160f - 300f) + (81.2f * 4f) + (81.2))
					slotOffset--;
			}

			mouseClickedOnce = true;
		} else if (!AvianInput.isMouseButtonDown(0))
			mouseClickedOnce = false;

		slotOffset += AvianInput.getScroll();

	}

	protected void logic() {
		if (slotOffset > 0)
			slotOffset = 0;
		else if (slotOffset < 5 - producers.length)
			slotOffset = 5 - producers.length;

		bps = 0;
		for (EndlBlockProducer bp : producers)
			bps += bp.owned() * bp.BPS();

		pausemmButton.logic();

		counterLogic();
		blockLogic();
		itemLogic();
		autoSave();

		if (!paused) {
			pauseScale = 7;
			pauseSin = 0;
		} else {
			pauseScale = AvianMath.glide(pauseScale, 6, 10);
			pauseSin++;
		}
	}

	protected void render() {

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);

		glPushMatrix();

		glTranslatef(0, 0, 0);

		if (!paused) {

			gui.setY(-11f);
			for (int r = 0; r < blocks.length; r++)
				for (int c = 0; c < blocks[r].length; c++)
					if (blocks[r][c] != null) {
						block.setXYZ(blocks[r][c].getX(), blocks[r][c].getY(), blocks[r][c].getZ());
						block.setShader(MainClass.shader);
						block.render(blocks[r][c].getBlockColor());
					}

			for (EndlBlockProducer bp : producers)
				bp.render();

			gui.setY(-11f);

			gui.setXYWH((531f + (438f / 2f)) - (AvianApp.getWidth() / 2f), (97f + (47f / 2f)) - (AvianApp.getHeight() / 2f), 438f, 47f); // Block Counter
			gui.setShader(MainClass.shader);
			gui.render(AvianColor.get(0, 140, 232));
			gui.setXYWH((531f + (392f / 2f)) - (AvianApp.getWidth() / 2f), (50f + (31f / 2f)) - (AvianApp.getHeight() / 2f), 392f, 31f); // BPS Counter
			gui.setShader(MainClass.shader);
			gui.render(AvianColor.get(0, 170, 197));

			gui.setXYWH((938 + (31f / 2f)) - (AvianApp.getWidth() / 2f), (50f + (31f / 2f)) - (AvianApp.getHeight() / 2f), 31f, 31f); // Pause Button
			gui.setShader(MainClass.shader);
			gui.render(AvianColor.get(170, 170, 170));
			gui.set(948.5f - (AvianApp.getWidth() / 2f), -5f, (50f + (31f / 2f)) - (AvianApp.getHeight() / 2f), 4f, 22f, 16f); // Pause Button
			gui.setShader(MainClass.shader);
			gui.render(AvianColor.get(200, 200, 200));
			gui.set(958.5f - (AvianApp.getWidth() / 2f), -5f, (50f + (31f / 2f)) - (AvianApp.getHeight() / 2f), 4f, 22f, 16f); // Pause Button
			gui.setShader(MainClass.shader);
			gui.render(AvianColor.get(200, 200, 200));

			cage.setXYWH((486f + (15f / 2f)) - (AvianApp.getWidth() / 2f), (65f + (485f / 2f)) - (AvianApp.getHeight() / 2f), 15f, 485f);
			cage.setShader(MainClass.shader);
			cage.render(AvianColor.get(248, 248, 248));
			cage.setX((062f + (15f / 2f)) - (AvianApp.getWidth() / 2f));
			cage.setShader(MainClass.shader);
			cage.render(AvianColor.get(248, 248, 248));

			cage.setXYWH((77f + (409f / 2f)) - (AvianApp.getWidth() / 2f), (535f + (15f / 2f)) - (AvianApp.getHeight() / 2f), 409, 15);
			cage.setShader(MainClass.shader);
			cage.render(AvianColor.get(248, 248, 248));

			light.setX((486f + (15f / 2f)) - (AvianApp.getWidth() / 2f));
			light.setShader(MainClass.shader);
			light.render(AvianColor.get(240, 240, 240));
			light.setX((062f + (15f / 2f)) - (AvianApp.getWidth() / 2f));
			light.setShader(MainClass.shader);
			light.render(AvianColor.get(240, 240, 240));
		} else
			pausemmButton.render();

		glPopMatrix();

		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);

	}

	protected void render2D() {

		if (!paused) {
			Fonts.NewCicleSemi_23.drawString("blocks", 963, 140, AvianColor.get(255, 255, 255), AvianFont.ALIGN_RIGHT);
			Fonts.SF_Digital_Readout_54.drawString(format.format(totalBlocks), 900, 140, AvianColor.get(255, 255, 255), AvianFont.ALIGN_RIGHT);
			Fonts.NewCicleSemi_18.drawString(format2.format(bps) + " BPS", (531 + (392 / 2)), 66, AvianColor.get(255, 255, 255), AvianFont.ALIGN_CENTER, AvianFont.ALIGN_CENTER);

			for (EndlBlockProducer bp : producers)
				bp.render2D();

			glEnableClientState(GL_VERTEX_ARRAY);
			glEnableClientState(GL_COLOR_ARRAY);

			Iterator<BlockFirework> iter = particles.iterator();
			while (iter.hasNext())
				iter.next().render();

			glDisableClientState(GL_VERTEX_ARRAY);
			glDisableClientState(GL_COLOR_ARRAY);
		} else {
			pausemmButton.render2D();
			//TODO			Images.paused.draw(new AvianPoint(AvianAppCore.getWidth() / 2, AvianAppCore.getHeight() / 2), pauseScale,  ((7.0 - pauseScale) * 255.0));
			Fonts.NewCicleSemi_20.drawString("Click anywhere to resume...", 350, AvianColor.get(255, 255, 255, ((255f / 2f) + (AvianMath.sin(pauseSin) * (255f / 2f)))), AvianFont.ALIGN_BOTTOM);
		}

	}

	protected void beginElimination(float x, float y) {
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

	protected void eliminateBlocks(int row, int col, BlockColor color) {
		if (row < 0 || row > blocks.length - 1 || col < 0 || col > blocks[0].length - 1 || blocks[row][col] == null)
			return;

		if (blocks[row][col].getBlockColorEnum() == color) {
			int newerBlocks = (int) Math.pow(2, bldo.owned());
			if (newerBlocks > 1)
				particles.add(new BlockFirework(blocks[row][col].getX() + (AvianApp.getWidth() / 2), blocks[row][col].getZ() + (AvianApp.getHeight() / 2), blocks[row][col].getBlockColorEnum(), "+" + newerBlocks));
			else
				particles.add(new BlockFirework(blocks[row][col].getX() + (AvianApp.getWidth() / 2), blocks[row][col].getZ() + (AvianApp.getHeight() / 2), blocks[row][col].getBlockColorEnum(), ""));

			newBlocks += newerBlocks;

			blocks[row][col] = null;

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

	protected void itemLogic() {
		for (EndlBlockProducer bp : producers)
			bp.logic();
	}

	protected void resetGame() {
		for (int r = 0; r < blocks.length; r++)
			for (int c = 0; c < blocks[r].length; c++)
				blocks[r][c] = new Block(r, c);

		newBlocks = 0;
		totalBlocks = bps = 0;
		paused = false;

		saveCount = 0;

		for (EndlBlockProducer bp : producers)
			bp.reset();

		FileCabinet.saveUser();
	}

}