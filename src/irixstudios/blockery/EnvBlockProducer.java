
package irixstudios.blockery;

import static org.lwjgl.opengl.GL11.*;

import java.text.DecimalFormat;

import musicaflight.avianutils.*;

public class EnvBlockProducer {

	public static enum ProducerType {

		COTTAGE(
				0,
				"Cottage House",
				200,
				1,
				0.5,
				Images.cottage),
		FACTORY(
				1,
				"Factory",
				1500,
				5,
				2,
				Images.factory),
		POWERHOUSE(
				2,
				"Powerhouse",
				20000,
				75,
				50,
				Images.powerhouse),
		MEGAPOWERHOUSE(
				3,
				"Megapowerhouse",
				250000,
				1000,
				750,
				Images.megapowerhouse),
		TREE(
				4,
				"Tree",
				100,
				0,
				-5,
				Images.tree),;

		public double price, addBPS, addPPS;
		public AvianImage icon;
		public String name;
		public int slot;

		ProducerType(int slot, String name, double price, double bps, double pps, AvianImage icon) {
			this.slot = slot;
			this.price = price;
			addBPS = bps;
			addPPS = pps;
			this.icon = icon;
			this.name = name;
		}
	}

	private double price;
	private int owned;

	int slot;

	int ms, s, m, h;
	String time;

	boolean hover = false;

	ProducerType type;

	float left, right, top, bottom, y;

	Firework firework;

	AvianPoint center;

	DecimalFormat format = new DecimalFormat("#,##0");

	public EnvBlockProducer(ProducerType type) {
		this.type = type;
		left = 561f - (AvianApp.getWidth() / 2f);
		right = (561f + 408f) - (AvianApp.getWidth() / 2f);
		slot = type.slot;
		top = ((160f) - (AvianApp.getHeight() / 2f)) + (81.2f * slot);
		bottom = top + (32.6f * 2f);

		owned = 0;
		price = type.price;

		center = new AvianPoint((left + right) / 2f, (top + bottom) / 2f);

	}

	double dimSin = 90;

	double hoverSin;
	int imgX, textX, dim;

	final int smoothness = 5;

	public void logic() {

		int slot = this.slot + MainClass.env.slotOffset;

		if (slot >= 0 && slot <= 4) {
			top = AvianMath.glide(top, ((160f) - (AvianApp.getHeight() / 2f)) + (81.2f * slot), smoothness);
			y = AvianMath.glide(y, -11, smoothness);
		} else if (slot < 0) {
			top = AvianMath.glide(top, ((160f) - (AvianApp.getHeight() / 2f)), smoothness);
			y = AvianMath.glide(y, -11 + (100 * slot), smoothness);
		} else if (slot > 4) {
			top = AvianMath.glide(top, ((160f) - (AvianApp.getHeight() / 2f)) + (81.2f * 4), smoothness);
			y = AvianMath.glide(y, -11 + (100 * (-slot + 4)), smoothness);
		}
		bottom = top + (32.6f * 2f);
		center.setY((top + bottom) / 2f);

		if (firework != null && firework.particleSize > .04f)
			firework.logic();
		dim = calcDim();
		if (type == ProducerType.TREE)
			price = ProducerType.TREE.price * Math.pow(3, owned);

		if (!affordable()) {
			if (dimSin < 90) {
				dimSin += 2;
				if (dimSin > 90)
					dimSin = 90;
			}
		} else {
			if (dimSin > 0) {
				dimSin -= 2;
				if (dimSin < 0)
					dimSin = 0;
			}
		}
		if (hover) {
			hoverSin += 3.6;
			if (hoverSin > 90)
				hoverSin = 90;
		} else {
			hoverSin -= 3.6;
			if (hoverSin < 0)
				hoverSin = 0;
		}

		if (MainClass.env.bps > 0) {
			ms = (int) ((price - (int) MainClass.env.totalBlocks) / (MainClass.env.bps / 1000.0));
			s = ms / 1000 % 60;
			m = ms / (60 * 1000) % 60;
			h = ms / (60 * 60 * 1000);
			DecimalFormat format = new DecimalFormat("00");
			time = (h + ":" + format.format(m) + ":" + format.format(s));
		}
	}

	public void render() {
		MainClass.env.gui.set(center.getX(), y, (top + (32.6f / 2f)), 408f, 22, 32.6f);
		AvianColor topPanel = (type == ProducerType.TREE) ? new AvianColor(80 - dim < 0 ? 0 : 80 - dim, 208 - dim, 138 - dim) : new AvianColor(232 - dim, 115 - dim, 121 - dim);
		MainClass.env.gui.setShader(MainClass.shader);
		MainClass.env.gui.render(topPanel);

		MainClass.env.gui.set(center.getX(), y, (bottom - (32.6f / 2f)), 408f, 22, 32.6f);
		MainClass.env.gui.setShader(MainClass.shader);
		MainClass.env.gui.render(AvianColor.get(125 - dim, 209 - dim, 213 - dim));
	}

	public void render2D() {
		if (slot < 0 || slot > 4)
			return;
		DecimalFormat df = new DecimalFormat("+#,##0.#;-#,##0.#");
		String bps = df.format(type.addBPS) + " BPS";
		String pps = df.format(type.addPPS) + " PPS";

		type.icon.render(center.getX() + 266, center.getY() + 269.4f);
		Fonts.NewCicleSemi_23.drawString(type.name, (int) (center.getX() + 591 - 265), (int) (center.getY() + 187 + 107.4), new AvianColor(255 - dim, 255 - dim, 255 - dim), AvianFont.ALIGN_LEFT);
		Fonts.NewCicleSemi_16.drawString("costs " + format.format(price), (int) (center.getX() + 591 - 265), (int) (center.getY() + 217 + 107.4), new AvianColor(255 - dim, 255 - dim, 255 - dim), AvianFont.ALIGN_LEFT);
		Fonts.NewCicleSemi_16.drawString(bps, (int) (center.getX() + 954 - 265), (int) (center.getY() + 184 + 107.4), new AvianColor(255 - dim, 255 - dim, 255 - dim), AvianFont.ALIGN_RIGHT);
		Fonts.NewCicleSemi_16.drawString(pps, (int) (center.getX() + 954 - 265), (int) (center.getY() + 217 + 107.4), new AvianColor(255 - dim, 255 - dim, 255 - dim), AvianFont.ALIGN_RIGHT);

		// TODO Affordable Timers
		// if (!affordable() && (MainClass.env.bps > 0))
		// AvianUtils.drawDualFontString(Fonts.SF_Digital_Readout_30, Fonts.NewCicleSemi_16, time + " ", "until affordable", (int) (center.getX() + 765 - 265), (int) (center.getY() + 193 +
		// 107.4), new AvianColor(255, 255, 255), true);

		if (firework != null) {
			glEnableClientState(GL_VERTEX_ARRAY);
			glEnableClientState(GL_COLOR_ARRAY);
			firework.render();
			glDisableClientState(GL_VERTEX_ARRAY);
			glDisableClientState(GL_COLOR_ARRAY);
		}
	}

	public boolean affordable() {
		return MainClass.env.totalBlocks >= price;
	}

	public int owned() {
		return owned;
	}

	public void owned(int o) {
		owned = o;
	}

	public double BPS() {
		return type.addBPS;
	}

	public double PPS() {
		return type.addPPS;
	}

	public boolean hover(int x, int y) {
		if (x >= left && x <= right && y >= top && y <= bottom && slot >= 0 && slot <= 4)
			return true;
		return false;
	}

	public boolean clicked(int x, int y) {
		if (hover(x, y)) {
			if (affordable()) {
				MainClass.env.totalBlocks -= price;
				owned++;
				Audio.newFactory.play();
				firework = new Firework(center.getX() + (AvianApp.getWidth() / 2f), center.getY() + (AvianApp.getHeight() / 2f));
			}
			return true;
		}
		return false;
	}

	public int calcDim() {
		return (int) (Math.sin(Math.toRadians(dimSin)) * 115.0);
	}

	public void reset() {
		owned = 0;
		firework = null;
	}

}
