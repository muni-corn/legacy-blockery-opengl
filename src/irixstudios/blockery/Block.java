
package irixstudios.blockery;

import musicaflight.avianutils.*;

public class Block {

	BlockColor color;

	AvianColor prismColor = new AvianColor(0, 0, 0, 0);

	float x, y, z, Fx, Fy, Fz;

	float smoothness;

	public static enum BlockColor {
		RED(
				new AvianColor(255, 0, 76)),
		ORANGE(
				new AvianColor(255, 165, 48)),
		GREEN(
				new AvianColor(80, 236, 140)),
		BLUE(
				new AvianColor(17, 124, 255));
		private AvianColor color;

		BlockColor(AvianColor color) {
			this.color = color;
		}

		public AvianColor getColor() {
			return color;
		}
	}

	public int row, col;

	public Block(int row, int col) {
		this.row = row;
		this.col = col;
		Fx = x = ((32f * col) + 105f) - (AvianApp.getWidth() / 2f);
		Fy = y = 10000f + (AvianMath.randomInt(20000));
		Fz = z = ((32f * row) + 95f) - (AvianApp.getHeight() / 2f);
		int randColor = (AvianMath.randomInt(4));
		switch (randColor) {
			case 0:
				color = BlockColor.RED;
				prismColor = color.getColor();
				break;
			case 1:
				color = BlockColor.ORANGE;
				prismColor = color.getColor();
				break;
			case 2:
				color = BlockColor.GREEN;
				prismColor = color.getColor();
				break;
			case 3:
				color = BlockColor.BLUE;
				prismColor = color.getColor();
				break;
		}
		smoothness = (AvianMath.randomFloat() * 10) + 5;
	}

	public Block(float x, float z, int row, int col) {
		this(row, col);
		Fx = this.x = x;
		Fy = y = -11;
		Fz = this.z = -350;
		velocity = 7.5f;
	}

	public BlockColor getBlockColorEnum() {
		return color;
	}

	public AvianColor getBlockColor() {
		return prismColor;
	}

	float velocity;

	public void logic(int row, int col) {
		velocity += 15f / 100f;
		Fx = (((32f * col) + 105f) - (AvianApp.getWidth() / 2f));
		Fy = (-11);
		Fz = (((32f * row) + 95f) - (AvianApp.getHeight() / 2f));
		x = AvianMath.glide(x, Fx, 10);
		y = AvianMath.glide(y, Fy, smoothness);
		z += velocity;
		if (z >= Fz) {
			z = Fz;
			velocity *= -.3f;
		}
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

}
