
package irixstudios.blockery;

import static org.lwjgl.opengl.GL11.*;

import java.text.DecimalFormat;

import musicaflight.avianutils.*;

public class EndlBlockProducer {

	public static enum EndlProducerType {
		BLOCKDOUBLER(
				0,
				"Block Doubler",
				5000,
				0,
				Images.blockdoubler,
				"Doubles the amount of blocks collected in one click."),
		BLOCKKIT(
				1,
				"Block Kit",
				50,
				.1,
				Images.blockkit,
				"The official Blockery block-making kit! (Ages 4 and up.)"),
		COTTAGE(
				2,
				"Cottage House",
				200,
				1,
				Images.cottage,
				"An older cottage home recovered from ye olde Prisimia."),
		FACTORY(
				3,
				"Factory",
				1500,
				5,
				Images.factory,
				"Standard-class factory for mass-producing blocks."),
		MINE(
				4,
				"Blockium Mine",
				8250,
				25,
				Images.mine,
				"A mine containing the very minerals from which blocks are made."),
		POWERHOUSE(
				5,
				"Powerhouse",
				20000,
				75,
				Images.powerhouse,
				"Amped-up factory for major mass production."),
		MEGAPOWERHOUSE(
				6,
				"Megapowerhouse",
				250000,
				1000,
				Images.megapowerhouse,
				"A factory that requires an unthinkable amount of power to mass-produce blocks at a killer rate.");

		public double initprice, addBPS;
		public AvianImage icon;
		public String name;
		public int slot;
		public String description;

		EndlProducerType(int slot, String name, double price, double bps, AvianImage icon, String description) {
			this.slot = slot;
			this.initprice = price;
			addBPS = bps;
			this.icon = icon;
			this.name = name;
			this.description = description;
		}
	}

	private double price;
	private int owned;

	int slot;

	int ms, s, m, h;
	String time;

	boolean hover = false;

	EndlProducerType type;

	float left, right, top, bottom, y;

	Firework firework;

	AvianPoint center;

	DecimalFormat format = new DecimalFormat("#,##0");
	DecimalFormat format2 = new DecimalFormat("+#,##0.#;-#,##0.#");

	public EndlBlockProducer(EndlProducerType type) {
		this.type = type;
		left = 561f - (AvianApp.getWidth() / 2f);
		right = (561f + 408f) - (AvianApp.getWidth() / 2f);
		slot = type.slot;
		top = ((160f) - (AvianApp.getHeight() / 2f)) + (81.2f * slot);
		bottom = top + (32.6f * 2f);

		owned = 0;

		center = new AvianPoint((left + right) / 2f, (top + bottom) / 2f);

	}

	double dimSin = 90;

	double hoverSin;
	int imgX, textX, dim;

	final int smoothness = 5;
	int newSlot;

	public void logic() {
		newSlot = this.slot + MainClass.end.slotOffset;
		if (newSlot >= 0 && newSlot <= 4) {
			top = AvianMath.glide(top, ((160f) - (AvianApp.getHeight() / 2f)) + (81.2f * newSlot), smoothness);
			y = AvianMath.glide(y, -11, smoothness);
		} else if (newSlot < 0) {
			top = AvianMath.glide(top, ((160f) - (AvianApp.getHeight() / 2f)), smoothness);
			y = AvianMath.glide(y, -11 + (100 * newSlot), smoothness);
		} else if (newSlot > 4) {
			top = AvianMath.glide(top, ((160f) - (AvianApp.getHeight() / 2f)) + (81.2f * 4), smoothness);
			y = AvianMath.glide(y, -11 + (100 * (4 - newSlot)), smoothness);
		}
		bottom = top + (32.6f * 2f);
		center.setY((top + bottom) / 2f);

		price = type.initprice * Math.pow((type == EndlProducerType.BLOCKDOUBLER) ? 2.3 : 1.3, owned);

		if (firework != null && firework.particleSize > .04f)
			firework.logic();
		dim = calcDim();

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

		if (MainClass.end.bps > 0) {
			ms = (int) ((price - (int) MainClass.end.totalBlocks) / (MainClass.end.bps / 1000.0));
			s = ms / 1000 % 60;
			m = ms / (60 * 1000) % 60;
			h = ms / (60 * 60 * 1000);
			DecimalFormat format = new DecimalFormat("00");
			time = (h + ":" + format.format(m) + ":" + format.format(s));
		}
	}

	public void render() {
		MainClass.end.gui.set(center.getX(), y, (top + (32.6f / 2f)), 408f, 22, 32.6f);
		AvianColor topPanel = (type == EndlProducerType.BLOCKDOUBLER) ? new AvianColor(0, 112 - dim < 0 ? 0 : 112 - dim, 192 - dim) : new AvianColor(232 - dim, 115 - dim, 121 - dim);
		MainClass.end.gui.setShader(MainClass.shader);
		MainClass.end.gui.render(topPanel);

		MainClass.end.gui.set(center.getX(), y, (bottom - (32.6f / 2f)), 408f, 22, 32.6f);
		MainClass.end.gui.setShader(MainClass.shader);
		MainClass.end.gui.render(new AvianColor(125 - dim, 209 - dim, 213 - dim));

		// if (hover) {
		// MainClass.end.gui.setXYZWHD(center.getX() - 330f, 0, (MainClass.end.mouseY - (32.6f)), 210f, 22, 32.6f);
		// AvianColor infoTopPanel = (type == EndlProducerType.BLOCKDOUBLER) ? new AvianColor(0, 112, 192) : new AvianColor(232, 115, 121);
		// MainClass.end.gui.render(infoTopPanel, MainClass.shader);
		//
		// MainClass.end.gui.setXYZWHD(center.getX() - 330f, 0, MainClass.end.mouseY + (32.6f / 2), 210f, 22, 32.6f * 2);
		// MainClass.end.gui.render(new AvianColor(125, 209, 213), MainClass.shader);
		//
		// }
	}

	public void render2D() {
		if (newSlot < 0 || newSlot > 4)
			return;
		String bps = (type == EndlProducerType.BLOCKDOUBLER) ? "block value x2" : format2.format(type.addBPS) + " BPS";
		String ownage = (type == EndlProducerType.BLOCKDOUBLER) ? "current multiplier x" + (int) (Math.pow(2, owned)) : owned + " owned";

		type.icon.render(center.getX() + 266, center.getY() + 269.4f);
		Fonts.NewCicleSemi_23.drawString(type.name, (int) (center.getX() + 591 - 265), (int) (center.getY() + 187 + 107.4), new AvianColor(255 - dim, 255 - dim, 255 - dim), AvianFont.ALIGN_LEFT);
		Fonts.NewCicleSemi_16.drawString("costs " + format.format(price), (int) (center.getX() + 591 - 265), (int) (center.getY() + 217 + 107.4), new AvianColor(255 - dim, 255 - dim, 255 - dim), AvianFont.ALIGN_LEFT);
		Fonts.NewCicleSemi_16.drawString(bps, (int) (center.getX() + 954 - 265), (int) (center.getY() + 184 + 107.4), new AvianColor(255 - dim, 255 - dim, 255 - dim), AvianFont.ALIGN_RIGHT);
		Fonts.NewCicleSemi_16.drawString(ownage, (int) (center.getX() + 954 - 265), (int) (center.getY() + 217 + 107.4), new AvianColor(255 - dim, 255 - dim, 255 - dim), AvianFont.ALIGN_RIGHT);

		// TODO Affordable Timers
		// if (!affordable() && (MainClass.end.bps > 0))
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
		return MainClass.end.totalBlocks >= price;
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

	public boolean hover(int x, int y) {
		if (x >= left && x <= right && y >= top && y <= bottom && newSlot >= 0 && newSlot <= 4)
			return hover = true;
		return hover = false;
	}

	public boolean clicked(int x, int y) {
		if (hover(x, y)) {
			if (affordable()) {
				MainClass.end.totalBlocks -= price;
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
