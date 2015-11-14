package game.io;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import game.Barrel;
import game.Main;

public class IOBase {
	public static final Version version = new Version();
	public final static String rootDirectory = System.getProperty("user.dir") + "\\BadGuysGame";
	public final static String resourcesDirectory = rootDirectory + "\\Resources";
	public final static String serverRootDirectory = "http://178.248.252.60/~xfukv01/BGG";
	private final static byte[] serverIP = new byte[] { (byte) 178, (byte) 248, (byte) 252, 60 };

	public static boolean isServerReachable() throws UnknownHostException, IOException {
		return InetAddress.getByAddress(serverIP).isReachable(5000);
	}

	public static void loadSaveIfPresent() throws IOException {
		Logging.logStartSectionTag("GAMELOAD");
		Logging.log("Looking for a save.");
		if (Files.exists(Paths.get(rootDirectory + "\\Save.dat"))) {
			byte[] data = Files.readAllBytes(Paths.get(rootDirectory + "\\Save.dat"));
			ByteBuffer buffer = ByteBuffer.wrap(data);
			Main.money = buffer.getInt();
			int index = 0;
			while (0 != buffer.remaining() && index < Main.barrels.length) {
				Barrel currentBarrel = Main.barrels[index];
				currentBarrel.gameProperties[0].fastUpgrade(buffer.getInt());
				currentBarrel.gameProperties[1].fastUpgrade(buffer.getInt());
				currentBarrel.gameProperties[2].fastUpgrade(buffer.getInt());
				index++;
			}
			Logging.log("The save was loaded.");
		}
		Logging.logEndSectionTag("GAMELOAD");
	}

	public static void save() throws IOException {
		Logging.logStartSectionTag("GAMESAVE");
		ByteBuffer buffer = ByteBuffer.allocate(4 + 12 * Main.barrels.length);
		buffer.putInt(Main.money);
		for (int i = 0; i < Main.barrels.length; i++) {
			for (int j = 0; j < 3; j++) {
				buffer.putInt(Main.barrels[i].gameProperties[j].upgradeLevel);
			}
		}
		Files.write(Paths.get(rootDirectory + "\\Save.dat"), buffer.array());
		Logging.log("The Game was saved.");
		Logging.logEndSectionTag("GAMESAVE");
	}
}
