
package musicaflight.blockery;

import musicaflight.avianutils.AvianImage;
import musicaflight.avianutils.ImageBank;

public class Images implements ImageBank {

	public static AvianImage cottage, factory, powerhouse, megapowerhouse, tree,
			blockdoubler, blockkit, mine;
	public static AvianImage mmIcon, rpIcon, envIcon, endIcon, setIcon,
			quitIcon, acceptIcon, cancelIcon, blockoin, paused, arrowR, arrowL,
			userCard;
	public static AvianImage pg, floppy;

	public void initImages() {
		blockkit = new AvianImage("/res/photos/blockkit.png");
		cottage = new AvianImage("/res/photos/cottage.png");
		factory = new AvianImage("/res/photos/factory.png");
		mine = new AvianImage("/res/photos/mine.png");
		powerhouse = new AvianImage("/res/photos/powerhouse.png");
		megapowerhouse = new AvianImage("/res/photos/megapowerhouse.png");
		tree = new AvianImage("/res/photos/tree.png");
		blockdoubler = new AvianImage("/res/photos/blockdoubler.png");
		blockoin = new AvianImage("/res/photos/blockoin.png");
		paused = new AvianImage("/res/photos/paused.png");
		mmIcon = new AvianImage("/res/photos/mainmenuicon.png");
		rpIcon = new AvianImage("/res/photos/replayicon.png");
		envIcon = new AvianImage("/res/photos/envicon.png");
		endIcon = new AvianImage("/res/photos/endlessicon.png");
		setIcon = new AvianImage("/res/photos/settingsicon.png");
		quitIcon = new AvianImage("/res/photos/quiticon.png");
		acceptIcon = new AvianImage("/res/photos/doneicon.png");
		cancelIcon = new AvianImage("/res/photos/cancelicon.png");
		pg = new AvianImage("/res/photos/pg.png");
		floppy = new AvianImage("/res/photos/floppy.png");
		arrowR = new AvianImage("/res/photos/arrowr.png");
		arrowL = new AvianImage("/res/photos/arrowl.png");
		userCard = new AvianImage("/res/photos/usercard.png");
	}

}
