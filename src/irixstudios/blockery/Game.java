
package irixstudios.blockery;

import java.text.DecimalFormat;
import java.util.*;

import irixstudios.blockery.Block.BlockColor;
import musicaflight.avianutils.*;

public abstract class Game {

	public static Block[][] blocks;

	List<Firework> fireworks = new ArrayList<Firework>();
	List<BlockFirework> particles = new ArrayList<BlockFirework>();
	List<FlyingFirework> coinParticles = new ArrayList<FlyingFirework>();

	int newBlocks;
	double totalBlocks;
	double bps;

	DecimalFormat format = new DecimalFormat("#,###");
	DecimalFormat format2 = new DecimalFormat("#,###.#");

	org.newdawn.slick.Color color;

	private long counterTrigger;

	AvianMesh block;

	public static AvianRectangle particle = new AvianRectangle();

	public Game() {
		block = new AvianMesh("/res/models/Prism.obj", 22, 22, 22);
	}

	protected abstract void keyboard();

	protected abstract void mouse();

	protected abstract void logic();

	protected abstract void render();

	protected abstract void render2D();

	protected abstract void beginElimination(float x, float y);

	protected abstract void eliminateBlocks(int row, int col, BlockColor color);

	protected abstract void autoSave();

	protected abstract void itemLogic();

	protected abstract void resetGame();

	protected void counterLogic() {
		counterTrigger++;

		if (newBlocks > 0 && counterTrigger % 6 == 1) {
			totalBlocks += (newBlocks / 5) + 1;
			newBlocks -= (newBlocks / 5) + 1;
			Audio.collectBlocks.play();
		}
		totalBlocks += bps / 100.0;
	}

	protected void blockLogic() {
		for (int r = blocks.length - 1; r >= 0; r--)
			for (int c = blocks[0].length - 1; c >= 0; c--)
				if (blocks[r][c] == null)
					if (r > 0) {
						blocks[r][c] = blocks[r - 1][c];
						blocks[r - 1][c] = null;
					} else
						blocks[r][c] = new Block(((33f * c) + 98f) - (AvianApp.getWidth() / 2f), 57f - (AvianApp.getHeight() / 2f), r, c);

		for (int r = 0; r < blocks.length; r++)
			for (int c = 0; c < blocks[r].length; c++)
				if (blocks[r][c] != null)
					blocks[r][c].logic(r, c);

		Iterator<BlockFirework> iter = particles.iterator();
		while (iter.hasNext()) {
			BlockFirework p = iter.next();
			if (p.particleSize > .04f)
				p.logic();
			else
				iter.remove();
		}
	}

	public class BlockFirework {

		AvianColor color;
		AvianPoint position;
		float finalDistFromCenter, distFromCenter = 0;
		float firstParticleSize, particleSize;
		int particles = 8;
		float[] angles = new float[particles];
		float[] particlesX = new float[particles],
				particlesY = new float[particles];
		float randomSmoothness;
		String message;
		float messageY;

		public BlockFirework(float x, float y, BlockColor color, String message) {
			this.message = message;
			switch (color) {
				case RED:
					this.color = new AvianColor(255, 0, 76);
					break;
				case ORANGE:
					this.color = new AvianColor(255, 165, 48);
					break;
				case GREEN:
					this.color = new AvianColor(80, 236, 140);
					break;
				case BLUE:
					this.color = new AvianColor(17, 124, 255);
					break;
			}
			position = new AvianPoint(x, y);
			firstParticleSize = particleSize = 7f;
			finalDistFromCenter = AvianMath.randomInt(5) + 15;
			for (int i = 0; i < angles.length; i++)
				// angles[i] = AvianMath.randomInt(360);
				angles[i] = (360f / particles) * i;
			randomSmoothness = (AvianMath.randomFloat() * 15f) + 5f;
		}

		public void logic() {
			distFromCenter = AvianMath.glide(distFromCenter, finalDistFromCenter, 10);
			particleSize -= .04f;
			messageY = AvianMath.glide(messageY, -5, 15);
			for (int i = 0; i < particlesX.length; i++)
				particlesX[i] = (float) (position.getX() + (AvianMath.cos(angles[i]) * distFromCenter));

			for (int i = 0; i < particlesY.length; i++)
				particlesY[i] = (float) (position.getY() + (AvianMath.sin(angles[i]) * distFromCenter));

		}

		public void render() {
			color.setA((particleSize / firstParticleSize) * 255f);
			for (int i = 0; i < angles.length; i++) {
				particle.setW(particleSize);
				particle.setH(particleSize);
				particle.setX(particlesX[i] - particle.getW() / 2);
				particle.setY(particlesY[i] - particle.getH() / 2);
				particle.render(color);
			}
			if (message != null)
				Fonts.NewCicleSemi_18.drawString(message, (int) position.getX(), (int) (position.getY() + messageY), new AvianColor(255, 255, 255, (int) ((particleSize / firstParticleSize) * 255f)), AvianFont.ALIGN_CENTER, AvianFont.ALIGN_CENTER);
		}
	}

}
