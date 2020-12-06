
package musicaflight.blockery;

import java.awt.Font;

import musicaflight.avianutils.AvianFont;
import musicaflight.avianutils.FontBank;

public class Fonts implements FontBank {

	public static AvianFont Sanitrixie, BonvenoCF, Vegur_Light,
			SF_Digital_Readout_30, SF_Digital_Readout_54, NewCicleSemi_16,
			NewCicleSemi_18, NewCicleSemi_20, NewCicleSemi_23,
			NewCicleSemi_23_BOLD, NewCicleSemi_35, NewCicleSemi_50;

	public void initFonts() {
		Sanitrixie = new AvianFont("/res/fonts/SANITRIXIE.TTF", 20f);
		BonvenoCF = new AvianFont("/res/fonts/BonvenoCF-Light.otf", 20f);
		Vegur_Light = new AvianFont("/res/fonts/Vegur-L 0602.otf", 20f);
		SF_Digital_Readout_30 = new AvianFont("/res/fonts/SF Digital Readout Heavy.ttf", 30);
		SF_Digital_Readout_54 = new AvianFont("/res/fonts/SF Digital Readout Heavy.ttf", 54);
		NewCicleSemi_16 = new AvianFont("/res/fonts/New_Cicle_Semi.ttf", 16f);
		NewCicleSemi_18 = new AvianFont("/res/fonts/New_Cicle_Semi.ttf", 18f);
		NewCicleSemi_20 = new AvianFont("/res/fonts/New_Cicle_Semi.ttf", 20f);
		NewCicleSemi_23 = new AvianFont("/res/fonts/New_Cicle_Semi.ttf", 23f);
		NewCicleSemi_23_BOLD = new AvianFont("/res/fonts/New_Cicle_Semi.ttf", 23f, Font.BOLD);
		NewCicleSemi_35 = new AvianFont("/res/fonts/New_Cicle_Semi.ttf", 35f);
		NewCicleSemi_50 = new AvianFont("/res/fonts/New_Cicle_Semi.ttf", 50f);
	}

}
