
package irixstudios.blockery;

import java.io.File;
import java.util.ArrayList;

import irixstudios.blockery.MainClass.Screen;
import musicaflight.avianutils.*;

public class WelcomeScreen {

	float textY = 150;
	StringBuilder sb = new StringBuilder(System.getProperty("user.name"));
	float caretInput;
	boolean userDone = false;
	boolean newUser = false;
	String[] names;
	int slot;
	float section;

	public static enum Name {
		ACCEPTABLE,
		EMPTY,
		EXISTS,
		OFFENSIVE,
		NOT_RECOMMENDED,
		INAPPROPRIATE,
		CUBEITE,
		YOMAMA;
	}

	Name name = Name.ACCEPTABLE;

	public boolean nameIncludes(String arg) {
		return sb.toString().toLowerCase().contains(arg.toLowerCase());
	}

	public WelcomeScreen() {
		File dir = new File("C:\\Blockery");
		File[] files = dir.listFiles();
		ArrayList<String> names = new ArrayList<String>();
		for (File f : files) {
			String[] split = f.getName().split("\\.");
			if (split[split.length - 1].equals("blockeryuser")) {
				names.add(f.getName().substring(0, f.getName().length() - ".blockeryuser".length()));
			}
		}
		names.add("+ New...");
		if (names.size() == 2) {
			BlockeryCore.username = names.get(0);
			BlockeryCore.userUnknown = false;
			MainClass.screen = Screen.MAIN;
			FileCabinet.loadUser();
		} else if (names.size() < 2)
			newUser = true;

		this.names = names.toArray(new String[names.size()]);

		name = Name.ACCEPTABLE;

		checkStatement: if (sb.length() <= 0)
			name = Name.EMPTY;
		else if (sb.toString().equals("Cubeite"))
			name = Name.CUBEITE;
		else if (nameIncludes("yo") && nameIncludes("mama"))
			name = Name.YOMAMA;
		else {
			for (String s : names)
				if (s.equals(sb.toString())) {
					name = Name.EXISTS;
					break checkStatement;
				}

			for (String s : BlockeryPrivate.offensive)
				if (nameIncludes(s)) {
					name = Name.OFFENSIVE;
					break checkStatement;
				}

			for (String s : BlockeryPrivate.inappropriate)
				if (nameIncludes(s)) {
					name = Name.INAPPROPRIATE;
					break checkStatement;
				}

			for (String s : BlockeryPrivate.notRecommended)
				if (nameIncludes(s)) {
					name = Name.NOT_RECOMMENDED;
					break checkStatement;
				}

		}

		section = 500f / (float) this.names.length;
	}

	public void mouse() {
		int my = (int) ((AvianApp.getHeight()) - AvianInput.getMouseY());
		slot = (int) ((my - 100f) / section);
		if (AvianInput.isMouseButtonDown(0) && !newUser) {
			if (slot == names.length - 1) {
				textY = 150;
				newUser = true;
				Audio.accept.play();
			} else if (slot >= 0) {
				BlockeryCore.username = names[slot];
				FileCabinet.loadUser();
				Audio.accept.play();
				MainClass.screen = Screen.MAIN;
			}
		}
	}

	public void keyboard() {
		if (userDone || !newUser)
			return;

		if (AvianInput.isKeyDown(AvianInput.KEY_ENTER) && sb.length() > 0 && (name == Name.ACCEPTABLE || name == Name.CUBEITE || name == Name.NOT_RECOMMENDED || name == Name.YOMAMA)) {
			BlockeryCore.username = sb.toString();
			userDone = true;
			return;
		}

		caretInput = 0;

		if (AvianInput.isKeyDown(AvianInput.KEY_BACKSPACE) && sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);

		char newChar = AvianInput.getTypedCharacter();

		boolean isSpecial = false;

		String specialChars = "`~!@#$%^&*()-_=[{]};:|<>,./?'\"\\";

		for (int i = 0; i < specialChars.length(); i++)
			if (newChar == specialChars.charAt(i)) {
				isSpecial = true;
				break;
			}

		if (Character.isAlphabetic(newChar) || Character.isDigit(newChar) || Character.isSpaceChar(newChar) || newChar == '.' || isSpecial) {
			if (!Character.isSpaceChar(newChar))
				sb.append(newChar);
			else if (sb.length() > 0)
				sb.append(newChar);

		}

		name = Name.ACCEPTABLE;

		checkStatement: if (sb.length() <= 0)
			name = Name.EMPTY;
		else if (sb.toString().equals("Cubeite"))
			name = Name.CUBEITE;
		else if (nameIncludes("yo") && nameIncludes("mama"))
			name = Name.YOMAMA;
		else {
			for (String s : names)
				if (s.equals(sb.toString())) {
					name = Name.EXISTS;
					break checkStatement;
				}

			for (String s : BlockeryPrivate.offensive)
				if (nameIncludes(s)) {
					name = Name.OFFENSIVE;
					break checkStatement;
				}

			for (String s : BlockeryPrivate.inappropriate)
				if (nameIncludes(s)) {
					name = Name.INAPPROPRIATE;
					break checkStatement;
				}

			for (String s : BlockeryPrivate.notRecommended)
				if (nameIncludes(s)) {
					name = Name.NOT_RECOMMENDED;
					break checkStatement;
				}

		}

	}

	int count;
	float greetInput;

	public void logic() {
		caretInput += 1.75;
		if (!BlockeryCore.userUnknown) {
			MainClass.screen = Screen.MAIN;
			FileCabinet.loadUser();
			return;
		}
		if (!userDone)
			textY = AvianMath.glide(textY, 0, 10f);
		else {
			count++;
			textY = AvianMath.glide(textY, 150, 10f);
		}

		if (count >= 300) {
			greetInput += 3;
			if (greetInput > 90) {
				MainClass.screen = Screen.MAIN;
			}
		}

	}

	AvianRectangle r = new AvianRectangle(0, 0, 3, 50);
	AvianRectangle highlight = new AvianRectangle(0, 0, AvianApp.getWidth(), 0);

	AvianColor warning = new AvianColor(230, 160, 100);

	public void render2D() {
		float alpha = 255f - ((textY / 150f) * 255f);

		AvianColor color = sb.length() > 0 && name == Name.ACCEPTABLE ? AvianColor.get(150, 150, 150, alpha) : AvianColor.get(250, 120, 120, alpha);

		warning.setA(alpha);

		if (newUser) {
			Fonts.NewCicleSemi_20.drawString("Hi! Looks like I don't know you. Could I have your name?", (int) (textY + 150), AvianColor.get(150, 150, 150, alpha), AvianFont.ALIGN_CENTER);

			switch (name) {
				case ACCEPTABLE:
					Fonts.NewCicleSemi_20.drawString("Press the enter key to confirm.", (int) (textY + 450), color, AvianFont.ALIGN_CENTER);
					break;
				case CUBEITE:
					Fonts.NewCicleSemi_20.drawString("Excuse me?", (int) (textY + 450), color = warning, AvianFont.ALIGN_CENTER);
					break;
				case EMPTY:
					Fonts.NewCicleSemi_20.drawString("Please type something before continuing.", (int) (textY + 450), color, AvianFont.ALIGN_CENTER);
					break;
				case EXISTS:
					Fonts.NewCicleSemi_20.drawString("This name already exists.", (int) (textY + 450), color, AvianFont.ALIGN_CENTER);
					break;
				case OFFENSIVE:
					Fonts.NewCicleSemi_20.drawString("Whoa, watch your language!", (int) (textY + 450), color, AvianFont.ALIGN_CENTER);
					break;
				case YOMAMA:
					Fonts.NewCicleSemi_20.drawString("My mama what?", (int) (textY + 450), color = warning, AvianFont.ALIGN_CENTER);
					break;
				case INAPPROPRIATE:
					Fonts.NewCicleSemi_20.drawString("That's not OK.", (int) (textY + 450), color, AvianFont.ALIGN_CENTER);
					break;
				case NOT_RECOMMENDED:
					Fonts.NewCicleSemi_20.drawString("...I'll let it go.", (int) (textY + 450), color = warning, AvianFont.ALIGN_CENTER);
					break;
				default:
					break;

			}
			Fonts.NewCicleSemi_50.drawString(sb.toString(), (int) (textY + 300), color, AvianFont.ALIGN_CENTER);

			AvianColor caret = new AvianColor(color.getR(), color.getG(), color.getB(), color.getA());
			caret.setA((float) (Math.abs(AvianMath.cos(caretInput)) * 255f) - (255f - alpha));

			r.setX(500 + (Fonts.NewCicleSemi_50.getWidth(sb.toString()) / 2));
			r.setY(275 + textY);
			r.render(caret);
			if (userDone) {
				Fonts.NewCicleSemi_20.drawString("Nice to meet you, " + BlockeryCore.username + "! Welcome to Blockery.", (int) (textY + 150), new AvianColor(150, 150, 150, (255f - alpha - (255f - ((float) AvianMath.cos(greetInput) * 255f)))), AvianFont.ALIGN_CENTER);
			}

		} else {
			if (slot >= 0) {
				highlight.setY(100f + ((float) slot * section) + textY);
				highlight.setH(section);
				highlight.render(new AvianColor(255, 255, 255, 100));
			}
			Fonts.NewCicleSemi_20.drawString("And you are...?", (int) (textY + 50), new AvianColor(150, 150, 150, alpha), AvianFont.ALIGN_CENTER);
			for (int i = 0; i < names.length; i++) {
				Fonts.NewCicleSemi_23.drawString(names[i], (int) (100 + (i * section) + textY + (section / 2f)), new AvianColor(150, 150, 150, alpha), AvianFont.ALIGN_CENTER);
			}
		}
	}
}
