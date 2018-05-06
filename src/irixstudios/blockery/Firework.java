
package irixstudios.blockery;

import irixstudios.blockery.Block.BlockColor;
import musicaflight.avianutils.*;

public class Firework {

	AvianColor color;
	float centerX, centerY;
	float finalDistFromCenter, distFromCenter = 0;
	float firstParticleSize, particleSize;
	int particles = 16;
	float[] angles = new float[particles];
	float[] particlesX = new float[particles],
			particlesY = new float[particles];

	public Firework(boolean useBlockColors) {
		if (useBlockColors) {
			int randColor = AvianMath.randomInt(4);
			switch (randColor) {
				case 0:
					color = BlockColor.RED.getColor();
					break;
				case 1:
					color = BlockColor.ORANGE.getColor();
					break;
				case 2:
					color = BlockColor.GREEN.getColor();
					break;
				case 3:
					color = BlockColor.BLUE.getColor();
					break;
			}
		} else
			color = new AvianColor(AvianMath.randomInt(256), AvianMath.randomInt(256), AvianMath.randomInt(256));

		centerX = (float) (AvianMath.randomInt(AvianApp.getWidth()));
		centerY = (float) (AvianMath.randomInt(AvianApp.getHeight()));
		firstParticleSize = particleSize = (float) (AvianMath.randomInt(10) + 5f);
		finalDistFromCenter = (float) ((AvianMath.randomInt(100)) + 15f);
		for (int i = 0; i < angles.length; i++)
			angles[i] = (360f / particles) * i;

		Audio.firework.playWithRandomPitch();
	}

	public Firework(float x, float y) {
		int randColor = AvianMath.randomInt(4);
		switch (randColor) {
			case 0:
				color = BlockColor.RED.getColor();
				break;
			case 1:
				color = BlockColor.ORANGE.getColor();
				break;
			case 2:
				color = BlockColor.GREEN.getColor();
				break;
			case 3:
				color = BlockColor.BLUE.getColor();
				break;
		}
		centerX = x;
		centerY = y;
		firstParticleSize = particleSize = 5f;
		finalDistFromCenter = 50;
		for (int i = 0; i < angles.length; i++) {
			angles[i] = (360f / particles) * i;
		}
	}

	float yV;
	float seconds;
	float fallY;

	public void logic() {
		particleSize -= .04f;
		seconds += .01f;
		yV = .1f * seconds;
		fallY += yV;
		distFromCenter = AvianMath.glide(distFromCenter, finalDistFromCenter, 10);
		for (int i = 0; i < particlesX.length; i++) {
			particlesX[i] = (float) (centerX + (AvianMath.cos(angles[i]) * distFromCenter));
		}
		for (int i = 0; i < particlesY.length; i++) {
			particlesY[i] = (float) (centerY + (AvianMath.sin(angles[i]) * distFromCenter)) + fallY;
		}
	}

	public void render() {
		for (int i = 0; i < angles.length; i++) {
			Game.particle.set(particlesX[i], particlesY[i], particleSize, particleSize);
			Game.particle.render(new AvianColor(color.getR(), color.getG(), color.getB(), (int) ((particleSize / firstParticleSize) * 255f)));
		}
	}

}
