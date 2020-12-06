
package musicaflight.blockery;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;

import musicaflight.blockery.MainClass.Screen;
import musicaflight.avianutils.*;

public class UpdateChecker {

	protected static enum UpdateStatus {
		CHECKING,
		ERROR,
		AVAILABLE,
		NO_UPDATE,
		DOWNLOADING,
		FINISHED;
	}

	int yOffset;
	int count;
	UpdateStatus status = UpdateStatus.CHECKING;
	AvianFileDownloader afd = new AvianFileDownloader("https://www.dropbox.com/s/vyaab8uut202a40/blockeryversion.txt?dl=1"),
			updateDownloader,
			installerDownloader = new AvianFileDownloader(getRunningJARFolder() + "\\install.jar", "https://www.dropbox.com/s/evr5cdzdkq6owgs/install.jar?dl=1");

	public Thread checkThread, updateThread, installThread;

	String error;

	public UpdateChecker() {
		// System.out.println(getRunningJARFolder());
	}

	public static String getRunningJARFolder() {
		try {
			try {
				return new File(MainClass.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (URISyntaxException e) {}
		return null;
	}

	public void keyboard() {

	}

	int mx, my;

	public void mouse() {
		mx = (int) AvianInput.getMouseX();
		my = (int) (AvianApp.getHeight() - AvianInput.getMouseY());
		if (AvianInput.isMouseButtonDown(0)) {
			if (status == UpdateStatus.AVAILABLE) {
				if (my <= 35 + yOffset2) {
					status = UpdateStatus.DOWNLOADING;
					updateThread.start();
					installThread.start();
					Audio.accept.play();
				}
			}
		}
	}

	boolean getURL = true;
	float seconds;

	float x;

	float yOffset2;

	public void logic() {
		if (MainClass.screen != Screen.MAIN && MainClass.screen != Screen.WELCOME)
			yOffset2 = AvianMath.glide(yOffset2, -30, 10f);
		else
			yOffset2 = AvianMath.glide(yOffset2, 0, 10f);

		if (status == UpdateStatus.NO_UPDATE || status == UpdateStatus.ERROR)
			count++;
		if (count >= 200) {
			yOffset = yOffset < -40 ? -40 : yOffset - 1;
		}
		if (yOffset > -35) {
			x += 0.15f;
			if (x >= 35) {
				x -= 35;
			}
			seconds += 0.01f;
			if (updateDownloader != null && updateDownloader.isFinished())
				status = UpdateStatus.FINISHED;
			switch (status) {
				case AVAILABLE:
					availableBar.setW(AvianMath.glide(availableBar.getW(), AvianApp.getWidth(), 15f));

					if (getURL) {
						updateDownloader = new AvianFileDownloader(getRunningJARFolder() + "\\BlockeryUpdate.jar", "https://www.dropbox.com/s/l0mxdantgvimswd/BlockeryUpdate.jar?dl=1", false);
						updateThread = new Thread(updateDownloader, "Blockery Update Downloader");
						installThread = new Thread(installerDownloader, "Blockery Installer Downloader");
						getURL = false;
					}
					break;
				case CHECKING:
					if (checkThread == null) {
						checkThread = new Thread(afd, "Blockery Update Checker");
						checkThread.start();
					}
					if (seconds >= 30f) {
						status = UpdateStatus.ERROR;
						error = "Request timed out.";
					}
					if (afd.isFinished()) {
						if (afd.getFileLines() != null) {
							if (!(afd.getFileLines()[0].equals(AvianApp.getVersion())))
								status = UpdateStatus.AVAILABLE;
							else
								status = UpdateStatus.NO_UPDATE;
						} else {
							status = UpdateStatus.ERROR;
							error = "An error while checking for an update.";
						}
					}
					break;
				case DOWNLOADING:
					availableBar.setW(AvianMath.glide(availableBar.getW(), 0, 15f));
					availableBar.setX(AvianApp.getWidth() - availableBar.getW());
					break;
				case FINISHED:
					try {
						Runtime.getRuntime().exec("java -jar \"" + getRunningJARFolder() + "\\install.jar" + "\"");
						MainClass.kill();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				case NO_UPDATE:
					break;
				case ERROR:
					if (checkThread != null)
						checkThread = null;
					break;
				default:
					break;
			}
		}
	}

	AvianRectangle progressBar = new AvianRectangle(0, 0, 0, 35);
	AvianRectangle availableBar = new AvianRectangle(0, 0, 0, 35);
	AvianRectangle bg = new AvianRectangle(0, 0, 1000, 50);

	public void render2D() {
		bg.setH(35);

		progressBar.setY(yOffset + yOffset2);
		availableBar.setY(yOffset + yOffset2);
		bg.setY(yOffset + yOffset2);

		float alpha = 255f - ((yOffset / -20f) * 255f);
		// bg.render(new AvianColor(235, 235, 239, alpha));

		switch (status) {
			case AVAILABLE:
				for (int i = 0; i < 30; i++)
					Images.pg.render(-35 + (i * 35) + x, yOffset + yOffset2);
				availableBar.render(new AvianColor(80, 236, 140, alpha));
				Fonts.NewCicleSemi_20.drawString("Blockery " + afd.getFileLines()[0] + " is available! Click to download...", (int) ((35 / 2) + yOffset2), new AvianColor(100, 100, 100, alpha), AvianFont.ALIGN_CENTER);
				break;
			case CHECKING:
				for (int i = 0; i < 30; i++)
					Images.pg.render(-35 + (i * 35) + x, yOffset + yOffset2);
				Fonts.NewCicleSemi_20.drawString("Checking for updates...", (int) ((35 / 2) + yOffset + yOffset2), new AvianColor(100, 100, 100, alpha), AvianFont.ALIGN_CENTER);
				break;
			case DOWNLOADING:
				for (int i = 0; i < 30; i++)
					Images.pg.render(-35 + (i * 35) + x, yOffset + yOffset2);
				progressBar.setW((((float) updateDownloader.getProgress() / 100f) * 1000f));
				availableBar.render(new AvianColor(80, 236, 140, alpha));
				progressBar.render(new AvianColor(80, 236, 140, alpha));
				Fonts.NewCicleSemi_20.drawString("Downloading " + afd.getFileLines()[0] + "... " + getRemainingTime() + " remaining", (int) ((35 / 2) + yOffset + yOffset2), new AvianColor(100, 100, 100, alpha), AvianFont.ALIGN_CENTER);
				Fonts.NewCicleSemi_16.drawString("Blockery will restart shortly after the download finishes. Do not shut down your computer or close Blockery. That's just rude.", (int) (45 + yOffset + yOffset2), new AvianColor(135, 135, 139, alpha - (yOffset2 / -30f) * 255f), AvianFont.ALIGN_CENTER);
				break;
			case FINISHED:
				break;
			case NO_UPDATE:
				Fonts.NewCicleSemi_20.drawString("Blockery is up to date. :)", (int) ((35 / 2) + yOffset + yOffset2), new AvianColor(100, 100, 100, alpha), AvianFont.ALIGN_CENTER);

				break;
			case ERROR:
				availableBar.setW(1000);
				availableBar.render(new AvianColor(255, 0, 76, alpha));
				Fonts.NewCicleSemi_20.drawString(error + " :(", (int) ((35 / 2) + yOffset + yOffset2), new AvianColor(235, 235, 239, alpha), AvianFont.ALIGN_CENTER);

				break;
			default:
				break;
		}
	}

	public String getRemainingTime() {
		if (updateDownloader.isSaving() && installerDownloader.isSaving()) {
			DecimalFormat df = new DecimalFormat("0.#");
			long bytesRemaining = updateDownloader.getOnlineFileSize() - updateDownloader.getBytesDownloaded();
			bytesRemaining += installerDownloader.getOnlineFileSize() - installerDownloader.getBytesDownloaded();

			double speed = updateDownloader.getBytesDownloaded() / (System.currentTimeMillis() - updateDownloader.getDownloadBeginTime());
			speed += installerDownloader.getBytesDownloaded() / (System.currentTimeMillis() - installerDownloader.getDownloadBeginTime());
			int millis = (int) (bytesRemaining / speed);

			if (millis >= 60 * 60 * 1000) {
				String time = df.format(millis / (60.0 * 60.0 * 1000.0));
				return time + (time.equals("1") ? " hour" : " hours");
			} else if (millis >= 60 * 1000) {
				String time = df.format(millis / (60.0 * 1000.0));
				return time + (time.equals("1") ? " minute" : " minutes");
			} else {
				String time = new DecimalFormat("0").format(millis / (1000.0));
				return time + (time.equals("1") ? " second" : " seconds");
			}
		}
		return "???";
	}
}
