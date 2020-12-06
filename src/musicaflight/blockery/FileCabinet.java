
package musicaflight.blockery;

import java.text.SimpleDateFormat;
import java.util.Date;

import musicaflight.avianutils.AvianDataFile;

public class FileCabinet {

	public static void loadUser() {
		AvianDataFile userData = new AvianDataFile("C:\\Blockery\\" + BlockeryCore.username + ".blockeryuser");

		BlockeryCore.blockoins = userData.retrieveElementInt("Blockoins");
		BlockeryCore.lastDateString = userData.retrieveElement("Date");

		MainClass.env.totalBlocks = userData.retrieveElementDouble("EnvBlocks");
		MainClass.env.cott.owned(userData.retrieveElementInt("EnvCottages"));
		MainClass.env.fact.owned(userData.retrieveElementInt("EnvFactories"));
		MainClass.env.powh.owned(userData.retrieveElementInt("EnvPowerhouses"));
		MainClass.env.mpow.owned(userData.retrieveElementInt("EnvMegapowerhouses"));
		MainClass.env.tree.owned(userData.retrieveElementInt("EnvTrees"));
		MainClass.env.pollution = userData.retrieveElementFloat("EnvPollution");
		MainClass.env.red = userData.retrieveElementInt("EnvRed");
		MainClass.env.orange = userData.retrieveElementInt("EnvOrange");
		MainClass.env.green = userData.retrieveElementInt("EnvGreen");
		MainClass.env.blue = userData.retrieveElementInt("EnvBlue");

		MainClass.end.totalBlocks = userData.retrieveElementDouble("EndBlocks");
		MainClass.end.bkit.owned(userData.retrieveElementInt("EndBlockKits"));
		MainClass.end.cott.owned(userData.retrieveElementInt("EndCottages"));
		MainClass.end.fact.owned(userData.retrieveElementInt("EndFactories"));
		MainClass.end.mine.owned(userData.retrieveElementInt("EndMines"));
		MainClass.end.powh.owned(userData.retrieveElementInt("EndPowerhouses"));
		MainClass.end.mpow.owned(userData.retrieveElementInt("EndMegapowerhouses"));
		MainClass.end.bldo.owned(userData.retrieveElementInt("EndBlockDoublers"));
	}

	public static void saveUser() {
		if (BlockeryCore.username != null) {
			AvianDataFile userData = new AvianDataFile("C:\\Blockery\\" + BlockeryCore.username + ".blockeryuser");

			userData.setElement("Blockoins", BlockeryCore.blockoins);
			userData.setElement("Date", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()));

			userData.setElement("EnvBlocks", MainClass.env.totalBlocks);
			userData.setElement("EnvCottages", MainClass.env.cott.owned());
			userData.setElement("EnvFactories", MainClass.env.fact.owned());
			userData.setElement("EnvPowerhouses", MainClass.env.powh.owned());
			userData.setElement("EnvMegapowerhouses", MainClass.env.mpow.owned());
			userData.setElement("EnvTrees", MainClass.env.tree.owned());
			userData.setElement("EnvPollution", MainClass.env.pollution);
			userData.setElement("EnvRed", MainClass.env.red);
			userData.setElement("EnvOrange", MainClass.env.orange);
			userData.setElement("EnvGreen", MainClass.env.green);
			userData.setElement("EnvBlue", MainClass.env.blue);

			userData.setElement("EndBlocks", MainClass.end.totalBlocks);
			userData.setElement("EndBlockKits", MainClass.end.bkit.owned());
			userData.setElement("EndCottages", MainClass.end.cott.owned());
			userData.setElement("EndFactories", MainClass.end.fact.owned());
			userData.setElement("EndMines", MainClass.end.mine.owned());
			userData.setElement("EndPowerhouses", MainClass.end.powh.owned());
			userData.setElement("EndMegapowerhouses", MainClass.end.mpow.owned());
			userData.setElement("EndBlockDoublers", MainClass.end.bldo.owned());

			userData.flushElements();
		}
	}

	public static void loadCore() {
		AvianDataFile coreData = new AvianDataFile("C:\\Blockery\\do_not_remove.blockery");

		for (int i = 0; i < BlockeryCore.highScores.length; i++) {
			BlockeryCore.highScores[i] = coreData.retrieveElementFloat("Highscore" + i);
		}
		BlockeryCore.musicLevel = coreData.retrieveElementFloat("Music");
		BlockeryCore.soundLevel = coreData.retrieveElementFloat("Sound");
	}

	public static void saveCore() {
		AvianDataFile coreData = new AvianDataFile("C:\\Blockery\\do_not_remove.blockery");

		for (int i = 0; i < BlockeryCore.highScores.length; i++)
			coreData.setElement("Highscore" + i, BlockeryCore.highScores[i]);

		coreData.setElement("Music", BlockeryCore.musicLevel);
		coreData.setElement("Sound", BlockeryCore.soundLevel);

		coreData.flushElements();
	}

	public static void saveAll() {
		saveCore();
		saveUser();
	}

}
