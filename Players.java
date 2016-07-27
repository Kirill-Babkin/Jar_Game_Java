import java.io.*;

public class Players {
	public static void Save(Player[] players) {
		try (
			FileOutputStream fos = new FileOutputStream("players.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
		) {
			oos.writeObject(players);
		}catch (IOException ioe) {
			System.out.println("Problem saving players.");
			ioe.printStackTrace();
		}
	}
	
	public static Player[] load() {
		Player[] players = new Player[0];
		try (
			FileInputStream fis =  new FileInputStream("players.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
		) {
			players = (Player[]) ois.readObject();
		} catch (IOException ioe) {
			System.out.println("Problem reading file.");
			System.out.println("This happend probably because you havent saved any players yet.\n");
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Problem loading players.");
		}
		return players;
	}
}