
package musicaflight.blockery;

import musicaflight.avianutils.*;

public class Button {

	String name;
	AvianImage icon;
	float x, z, w, h, yInMatrix, xOffset, zOffset;
	float Sx, Sz, Sw, Sh;
	float Ex, Ez, Ew, Eh;
	AvianColor color;

	boolean hover, clicked;

	float sinInput = -90;

	public Button(String name, AvianImage icon, AvianPoint xy, AvianPoint wh, AvianColor color) {
		this.name = name;
		this.icon = icon;
		this.Sx = Ex = x = xy.getX();
		this.Sz = Ez = z = xy.getY();
		this.Sw = Ew = w = wh.getX();
		this.Sh = Eh = h = wh.getY();
		this.color = color;
	}

	public Button(String name, AvianImage icon, AvianPoint xy, AvianPoint wh, AvianPoint Exy, AvianPoint Ewh, AvianColor color) {
		this.name = name;
		this.icon = icon;
		this.Sx = x = xy.getX();
		this.Sz = z = xy.getY();
		this.Sw = w = wh.getX();
		this.Sh = h = wh.getY();
		this.Ex = Exy.getX();
		this.Ez = Exy.getY();
		this.Ew = Ewh.getX();
		this.Eh = Ewh.getY();
		this.color = color;
	}

	public void logic() {
		yInMatrix = AvianMath.glide(yInMatrix, -11, 10);
		if (!clicked) {
			if (hover) {
				x = AvianMath.glide(x, Ex, 10);
				z = AvianMath.glide(z, Ez, 10);
				w = AvianMath.glide(w, Ew, 10);
				h = AvianMath.glide(h, Eh, 10);
				if (sinInput < 90) {
					sinInput += 5;
					if (sinInput > 90)
						sinInput = 90;
				}
			} else {
				x = AvianMath.glide(x, Sx, 10);
				z = AvianMath.glide(z, Sz, 10);
				w = AvianMath.glide(w, Sw, 10);
				h = AvianMath.glide(h, Sh, 10);
				if (sinInput > -90) {
					sinInput -= 5;
					if (sinInput < -90)
						sinInput = -90;
				}
			}
		} else {
			x = Sx;
			z = Sz;
			w = Sw;
			h = Sh;
			sinInput = -90;
			hover = false;
			clicked = false;
		}
	}

	public void render() {
		BlockeryCore.cube.set(x + xOffset, yInMatrix, z + zOffset, w, 22, h);
		BlockeryCore.cube.setShader(MainClass.shader);
		BlockeryCore.cube.render(color);
	}

	public void render2D() {
		int text = (int) ((AvianMath.sin(sinInput) * (255.0)));
		int iconA = (int) ((AvianMath.sin(sinInput) * (-255.0)));
		Fonts.NewCicleSemi_23.drawString(name, (int) (x + (AvianApp.getWidth() / 2) + xOffset), (int) (z + (AvianApp.getHeight() / 2) + zOffset), new AvianColor(255, 255, 255, text), AvianFont.ALIGN_CENTER, AvianFont.ALIGN_CENTER);
		//		if (icon != null)
		//			icon.draw(new AvianPoint(x + (AvianAppCore.WIDTH / 2f) + xOffset, z + (AvianAppCore.HEIGHT / 2f) + zOffset), 1, iconA);
		//		else
		Fonts.NewCicleSemi_23.drawString(String.valueOf(name.charAt(0)), (int) (x + (AvianApp.getWidth() / 2) + xOffset), (int) (z + (AvianApp.getHeight() / 2) + zOffset), new AvianColor(255, 255, 255, iconA), AvianFont.ALIGN_CENTER, AvianFont.ALIGN_CENTER);
	}

	public void setXOffset(float offset) {
		this.xOffset = offset;
	}

	public void setZOffset(float offset) {
		this.zOffset = offset;
	}

	public void setColor(AvianColor color) {
		this.color = color;
	}

	public void setIcon(AvianImage icon) {
		this.icon = icon;
	}

	public void setTitle(String title) {
		this.name = title;
	}

	public boolean hover(float mx, float my) {
		return hover = mx > (x + xOffset) - (w / 2) && mx < (x + xOffset) + (w / 2) && my > (z + zOffset) - (h / 2) && my < (z + zOffset) + (h / 2);
	}

	public boolean clicked() {
		if (hover) {
			yInMatrix = -33;
			return clicked = true;
		}
		return clicked = false;

	}
}
