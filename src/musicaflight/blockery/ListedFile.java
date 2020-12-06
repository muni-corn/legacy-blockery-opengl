
package musicaflight.blockery;

import java.io.File;

import musicaflight.avianutils.*;

@Deprecated
public class ListedFile extends File {

	private static final long serialVersionUID = 1999103606807052323L;

	public ListedFile(String filepath, int slot) {
		super(filepath);
		left = -120;
		right = 440;
		top = -245;
		y = -11;
		bottom = top + 20;
		this.slot = slot;

		center = new AvianPoint((left + right) / 2f, (top + bottom) / 2f);
	}

	public static int filesToShowAtOnce = 21;

	int slot;

	boolean hover = false;

	float left, right, top, bottom, y;
	AvianPoint center;

	int imgX, textX;

	final float smoothness = 5;
	int newSlot;

	public void logic() {
		newSlot = this.slot + MainMenu.slotOffset;
		if (newSlot >= 0 && newSlot < filesToShowAtOnce) {
			top = AvianMath.glide(top, -245 + (25 * newSlot), smoothness);
			y = AvianMath.glide(y, -11, smoothness);
		} else if (newSlot < 0) {
			top = AvianMath.glide(top, -245, smoothness);
			y = AvianMath.glide(y, -11 + (100 * newSlot), smoothness);
		} else if (newSlot > filesToShowAtOnce - 1) {
			top = AvianMath.glide(top, -245 + (25 * (filesToShowAtOnce - 1)), smoothness);
			y = AvianMath.glide(y, -11 + (100 * (filesToShowAtOnce - newSlot - 1)), smoothness);
		}
		bottom = top + 20;

		center.setY((top + bottom) / 2f);
	}

	public void render() {
		MainMenu.prism.set(MainMenu.settingsXOffset + (AvianApp.getWidth()) + center.getX(), y, center.getY() + (MainMenu.settingsYOffset + (AvianApp.getHeight())), right - left, 22, bottom - top);
		MainMenu.prism.render(AvianColor.get(100, 100, 100));
	}

	AvianRectangle r = new AvianRectangle();

	public void render2D() {
		if (newSlot < 0 || newSlot > filesToShowAtOnce - 1)
			return;
		if (hover) {
			r.set(left + 1500 + MainMenu.settingsXOffset, top + (AvianApp.getHeight() / 2), right - left, bottom - top);
			r.render(AvianColor.get(255, 255, 255, 50));
		}
		// if (isFile())
		// Images.file.draw(390 + MainMenu.settingsXOffset + (AvianAppCore.getWidth()), (int) (center.getY()) + (AvianAppCore.getHeight() / 2) - 8 + (MainMenu.settingsYOffset + (AvianAppCore.getHeight())));
		// else if (isDirectory())
		// Images.folder.draw(390 + MainMenu.settingsXOffset + (AvianAppCore.getWidth()), (int) (center.getY()) + (AvianAppCore.getHeight() / 2) - 8 + (MainMenu.settingsYOffset + (AvianAppCore.getHeight())));
		Fonts.NewCicleSemi_16.drawString(this.getName(), (int) (430 + MainMenu.settingsXOffset + (AvianApp.getWidth())), (int) ((int) (center.getY()) + (AvianApp.getHeight() / 2) + (MainMenu.settingsYOffset + (AvianApp.getHeight()))), AvianColor.get(255, 255, 255), AvianFont.ALIGN_LEFT, AvianFont.ALIGN_CENTER);
	}

	public boolean hover(int x, int y) {
		if (x >= left && x <= right && y >= top && y <= bottom && newSlot >= 0 && newSlot <= filesToShowAtOnce - 1)
			return hover = true;
		return hover = false;
	}

	public boolean clicked() {
		return hover;
	}

}
