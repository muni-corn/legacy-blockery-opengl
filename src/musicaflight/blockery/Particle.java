
package musicaflight.blockery;

import musicaflight.blockery.Block.BlockColor;

import java.awt.Color;

//Board Dimensions = 300 x 420

public class Particle {

	private float x, y, z, pitch, yaw, roll, yV, zV, pitchV, yawV, rollV;

	public float lifetime = 1f, age;

	Color prismColor = new Color(0, 0, 0, 0);

	public Particle(float x, float z, BlockColor color) {
		this.x = x;
		this.y = -11f;
		this.z = z;
		yV = (float) (Math.random() * -20f) - 2.5f;
		zV = (float) (Math.random() * 5f) - 2.5f;
		pitchV = (float) (Math.random() * 5f) - 2.5f;
		yawV = (float) (Math.random() * 5f) - 2.5f;
		rollV = (float) (Math.random() * 5f) - 2.5f;
		switch (color) {
			case RED:
				prismColor = new Color(255, 0, 76);
				break;
			case BLUE:
				prismColor = new Color(17, 124, 255);
				break;
			case GREEN:
				prismColor = new Color(80, 236, 140);
				break;
			case ORANGE:
				prismColor = new Color(255, 165, 48);
				break;
		}
	}

	float seconds;

	public void logic() {
		seconds += .01f;
		age = seconds;
		zV = (9.81f * seconds);
		y += yV;
		z += zV;
		pitch += pitchV;
		yaw += yawV;
		roll += rollV;
	}

	public Color getColor() {
		return prismColor;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

}
