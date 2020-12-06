
package musicaflight.blockery;

import static org.lwjgl.opengl.GL11.*;

import musicaflight.blockery.Block.BlockColor;
import musicaflight.avianutils.*;

public class BlockeryLoadIcon {

	int width;

	float sin;
	float alpha1, alpha2, alpha3, alpha4;
	AvianRectangle r1 = new AvianRectangle(0, 0, 10, 10);
	AvianRectangle r2 = new AvianRectangle(15, 0, 10, 10);
	AvianRectangle r3 = new AvianRectangle(15, 15, 10, 10);
	AvianRectangle r4 = new AvianRectangle(0, 15, 10, 10);
	boolean sound = true;

	public void logic() {
		sin += 5;
		// if (sound) {
		// Audio.loading.play();
		// sound = false;
		// }
	}

	public void render(int x, int y) {
		glPushMatrix();

		glTranslatef(x, y, 0);
		glRotatef(45, 0, 0, 1);

		alpha1 = (255f / 2f) + (AvianMath.specialrtSin(sin + 270, 1) * (255.0f / 2.0f));
		alpha2 = (255f / 2f) + (AvianMath.specialrtSin(sin + 180, 1) * (255.0f / 2.0f));
		alpha3 = (255f / 2f) + (AvianMath.specialrtSin(sin + 90, 1) * (255.0f / 2.0f));
		alpha4 = (255f / 2f) + (AvianMath.specialrtSin(sin, 1) * (255.0f / 2.0f));

		AvianColor red = BlockColor.RED.getColor();
		AvianColor orange = BlockColor.ORANGE.getColor();
		AvianColor green = BlockColor.GREEN.getColor();
		AvianColor blue = BlockColor.BLUE.getColor();

		AvianColor newRed = new AvianColor(red.getR(), red.getG(), red.getB(), (int) alpha1);
		AvianColor newOrange = new AvianColor(orange.getR(), orange.getG(), orange.getB(), (int) alpha2);
		AvianColor newGreen = new AvianColor(green.getR(), green.getG(), green.getB(), (int) alpha3);
		AvianColor newBlue = new AvianColor(blue.getR(), blue.getG(), blue.getB(), (int) alpha4);

		r1.render(newRed);
		r2.render(newOrange);
		r3.render(newGreen);
		r4.render(newBlue);

		glPopMatrix();
	}

}
