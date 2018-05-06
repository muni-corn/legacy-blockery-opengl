
package irixstudios.blockery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import musicaflight.avianutils.AvianApp;
import musicaflight.avianutils.AvianMesh;

public class BlockeryCore {

	public static String username;
	public static boolean userUnknown = true;
	public static String lastDateString, newDateString;
	public static int blockoins;
	public static double[] highScores = new double[5];
	public static AvianMesh cube = new AvianMesh("/res/models/Prism.obj");
	public static float musicLevel, soundLevel;

	@SuppressWarnings("unused")
	public BlockeryCore() {
		FileCabinet.loadCore();

		AvianApp.setMusicVolume(musicLevel);
		AvianApp.setSFXVolume(soundLevel);

		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date();
		newDateString = format.format(date);

		double seconds;

		if (lastDateString != null) {
			Date lastDate = null, newDate = null;
			try {
				lastDate = format.parse(lastDateString);
				newDate = format.parse(newDateString);
			} catch (ParseException e) {}

			long difference = newDate.getTime() - lastDate.getTime();
			seconds = difference / 1000.0;
		} else
			seconds = 0;

	}

	public static void resetStats() {
		blockoins = 0;
		FileCabinet.saveUser();
	}

}
