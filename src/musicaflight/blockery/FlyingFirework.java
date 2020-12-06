
package musicaflight.blockery;

import java.awt.Color;

import musicaflight.blockery.Block.BlockColor;
import musicaflight.avianutils.*;

public class FlyingFirework {

	Color color;
	AvianPoint startPos, finalPos;
	float finalDistFromCenter, distFromCenter = 0;
	float firstParticleSize, particleSize;
	int particles = 8;
	float[] angles = new float[particles];
	float[] particlesX = new float[particles],
			particlesY = new float[particles];
	float randomSmoothness;
	String message;
	float messageY;

	public FlyingFirework(float x, float y, BlockColor color, String message) {
		this.message = message;
		switch (color) {
			case RED:
				this.color = new Color(255, 0, 76);
				break;
			case ORANGE:
				this.color = new Color(255, 165, 48);
				break;
			case GREEN:
				this.color = new Color(80, 236, 140);
				break;
			case BLUE:
				this.color = new Color(17, 124, 255);
				break;
		}
		startPos = new AvianPoint(x, y);
		// finalPos = new AvianPoint(x, y);
		finalPos = new AvianPoint(AvianMath.randomInt(439) + 531, AvianMath.randomInt(48) + 97);
		firstParticleSize = particleSize = 4f;
		finalDistFromCenter = 10;
		for (int i = 0; i < angles.length; i++)
			// angles[i] = AvianMath.randomInt(360);
			angles[i] = (360f / particles) * i;
		randomSmoothness = (AvianMath.randomFloat() * 15f) + 5f;
	}

	public FlyingFirework(float x, float y, BlockColor color) {
		switch (color) {
			case RED:
				this.color = new Color(255, 0, 76);
				break;
			case ORANGE:
				this.color = new Color(255, 165, 48);
				break;
			case GREEN:
				this.color = new Color(80, 236, 140);
				break;
			case BLUE:
				this.color = new Color(17, 124, 255);
				break;
		}
		startPos = new AvianPoint(x, y);
		// finalPos = new AvianPoint(x, y);
		finalPos = new AvianPoint(AvianMath.randomInt(439) + 531, AvianMath.randomInt(48) + 97);
		firstParticleSize = particleSize = 4f;
		finalDistFromCenter = 10;
		for (int i = 0; i < angles.length; i++)
			// angles[i] = AvianMath.randomInt(360);
			angles[i] = (360f / particles) * i;
		randomSmoothness = (AvianMath.randomFloat() * 15f) + 5f;
	}

	public FlyingFirework() {
		color = new Color(255, 255, 0);
		startPos = new AvianPoint((AvianApp.getWidth() / 2) - 292, 358);
		finalPos = new AvianPoint(23 + AvianMath.randomInt(100), 23);
		firstParticleSize = particleSize = 4f;
		finalDistFromCenter = 10;
		for (int i = 0; i < angles.length; i++)
			angles[i] = AvianMath.randomInt(360);
		randomSmoothness = (AvianMath.randomFloat() * 15f) + 5f;
	}

	boolean firework;

	public void logic() {
		startPos.setX(AvianMath.glide(startPos.getX(), finalPos.getX(), randomSmoothness));
		startPos.setY(AvianMath.glide(startPos.getY(), finalPos.getY(), randomSmoothness));
		if (startPos.getX() < finalPos.getX() && startPos.getY() < finalPos.getY())
			firework = startPos.getX() >= finalPos.getX() - 1 && startPos.getY() >= finalPos.getY() - 1;
		else
			firework = startPos.getX() <= finalPos.getX() + 1 && startPos.getY() <= finalPos.getY() + 1;
		if (firework) {
			distFromCenter = AvianMath.glide(distFromCenter, finalDistFromCenter, 10);
			particleSize -= .04f;
			messageY = AvianMath.glide(messageY, -5, 15);
		}
		for (int i = 0; i < particlesX.length; i++)
			particlesX[i] = (float) (finalPos.getX() + (AvianMath.cos(angles[i]) * distFromCenter));

		for (int i = 0; i < particlesY.length; i++)
			particlesY[i] = (float) (finalPos.getY() + (AvianMath.sin(angles[i]) * distFromCenter));

	}

	public void render() {
		AvianColor acolor = new AvianColor(color.getRed(), color.getGreen(), color.getBlue(), (int) ((particleSize / firstParticleSize) * 255f));
		if (firework) {
			for (int i = 0; i < angles.length; i++) {
				EnvironmentGame.particle.setX(particlesX[i]);
				EnvironmentGame.particle.setY(particlesY[i]);
				EnvironmentGame.particle.setW(particleSize);
				EnvironmentGame.particle.setH(particleSize);
				EnvironmentGame.particle.render(acolor);
			}
			if (message != null)
				Fonts.NewCicleSemi_18.drawString(message, (int) finalPos.getX(), (int) (finalPos.getY() + messageY), new AvianColor(255, 255, 255, (int) ((particleSize / firstParticleSize) * 255f)), AvianFont.ALIGN_CENTER, AvianFont.ALIGN_CENTER);

		} else {
			EnvironmentGame.particle.setX(startPos.getX());
			EnvironmentGame.particle.setY(startPos.getY());
			EnvironmentGame.particle.setW(6);
			EnvironmentGame.particle.setH(6);
			EnvironmentGame.particle.render(acolor);
		}
	}

}
