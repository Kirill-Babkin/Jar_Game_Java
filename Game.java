import java.io.Console;
import java.util.Arrays;
import java.util.Random;
import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.ArrayList;


public class Game {
	
	public static void main(String[] args) {
		Console console = System.console();
		Menu mainMenu = new Menu("1. Admin mode.\n2. Play a Quick game.\n3. Score Table.\n4. exit.\n\n");
		Menu adminMenu = new Menu("1. Create a Jar.\n2. Load a Player.\n3. Add a Player.\n4. Remove a Player\n\n");
		redirectUser(greetUser(console, mainMenu), adminMenu, console, mainMenu);
	}
	
	public static Jar setJar (Console c){
		boolean flag = false;
		String typeOfObject="";
		int maxAmount;
		Jar jar = null;
		while (!flag) {
			try { 
				if(typeOfObject.equals("")){
					typeOfObject = c.readLine("Please enter the name of the objet you want to fill up the jar with.  ");
				}
				maxAmount = Integer.parseInt(c.readLine("Please enter the max amount of "+typeOfObject+" that can fit in this jar.  "));
				jar = new Jar(typeOfObject,maxAmount);
				flag = true;
			} catch (IllegalArgumentException iae){
				c.printf("Sorry but %s\n", iae.getMessage());
			}
		}
		return jar;
	}
	
	public static void startGame(Player p,Jar j, Console c, Menu m, Menu mM) {
		int numToGuess = 0;
		int userGuess = 0;
		Player player  = p;
		String response;
		boolean flag = true;
		boolean isNewGame = false;
		Random random = new Random();
		displayMaxItems(j,c);
		numToGuess = random.nextInt(j.getAmountOfItems())+1;
		do{ // a do while loop that starts a round for this jar.
			int tryes = 0;
			numToGuess = random.nextInt(j.getAmountOfItems())+1;
			flag = true;
			while (flag) { // while loop that runs until user takes a right guess.
				tryes++;
				c.printf("Plese take a guess how many %s(s) does the Jar containes.\n", j.getTypeOfItem());
				userGuess = Integer.parseInt(c.readLine("Guess the number.  "));
				flag = validateResult(p, userGuess,numToGuess,tryes, c, j, j.getAmountOfItems(), m, mM);
				isNewGame = isNewGame(flag);
				if (!flag) {continue;}
				toLowToHigh(userGuess, numToGuess, c);
				
			}
		} while(isNewGame);	
	}

	private static boolean validateResult(Player p, int userGuess, int numToGuess, int tryes, Console c, Jar j, int amountOfItems, Menu m, Menu mM ) {
		if (userGuess == numToGuess) {
				int score = p.calcScore(amountOfItems,tryes );
				c.printf("Congrats!!! You won this round and it took you %d attemp(s).\n", tryes);
				c.printf("The max amount of items was %d and it took you %d tryes based on that you score is %d \n\n",
						 amountOfItems,
						 tryes, 
						 score);
						
				// here i need to find player in my array of players remove it and add a player with updated score;
				// 1.1 i need to load my players
				List<Player> playersAsList = new ArrayList<Player>(Arrays.asList(Players.load()));
				// i need to loop arround and find a player with the same name..
				ListIterator<Player> listIterator = playersAsList.listIterator();
				while(listIterator.hasNext()){
					Player playerFromList = listIterator.next();
					if (playerFromList.getName().equals(p.getName())){
						if(playerFromList.getScore() < score){
							p.setScore(score);
							listIterator.remove();
							listIterator.add(p);
							Players.Save(playersAsList.toArray(new Player[playersAsList.size()]));
						}
					}
				}
				String response = c.readLine("If you want to play another round enter 'y'. "+
											 "and if you want to exit enter any character.  ").toLowerCase();
			if(response.toCharArray()[0] == 'y') {
				c.printf("We filling up the Jar with new amount. Good luck. \n");
				
				displayMaxItems(j,c);
				try{
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
				return false;
			}
			if(response.toCharArray()[0] != 'y') {
				c.printf("Thank you for playing, termnation session now...\n");
				redirectUser(greetUser(c,mM),m, c, mM);
			}
			
		}
		return true;
	}
	
	private static void displayMaxItems(Jar j, Console c) {
		c.printf("The maximum amount of %s, this Jar can hold is %d.\n", j.getTypeOfItem(), j.getAmountOfItems());
	}
	
	private static boolean isNewGame(boolean flag){
		if(flag == false) {
			return true;
		}
		return false;
	}
	
	private static void toLowToHigh(int userGuess, int numToGuess, Console c){
		if (userGuess > numToGuess ) {
			c.printf("The number you entered is too high..\n\n");
		}else {
			c.printf("The number you entered is too low..\n\n");
		}
	}
	
	private static int greetUser(Console c, Menu m){
		c.printf("Hello and wellcome to the Jar Game Java v1.0\n\n");
		int option = m.getOption();
		boolean flag = true;
		while(flag){
			try{
				displayMenu(c, m);
				option = Integer.parseInt(c.readLine("\nPlease enter a number next to the option to make a selection.  "));
				if (option > 4 || option <= 0){
					c.printf("Please enter a number that assosiated with menu option. \n");
					continue;
				}
				return option;
			} catch (NumberFormatException nfe) {
				c.printf("Please use numeric characters to make a selection. %s\n", nfe.getMessage());
			}
		}
		return option;
	}
	
	private static int getUserOption(Menu m){
		int option = 0;
		boolean flag = true;
		return 0;
	}
	
	private static void displayMenu(Console c, Menu m){
		c.printf(m.getMenuMessage());
	}
	
	private static Jar redirectUser(int option, Menu m, Console c, Menu mM){
		int adminOption = 0;
		int main_option = option;
		Jar jar = null;
		Player player = null;
		boolean flagMain = true;
		while (flagMain){
			
			while (option == 1){
				c.printf("+++++++++++++++++++++++++++ADMIN+++++++++++++++++++++++++++++\n");
				adminOption = displayAdmin(c,m);
				while(adminOption == 1){
					jar = setJar(c);
					break;
				}while (adminOption == 2) {
					if (jar == null){
						startGame(loadPlayer(c), setJar(c), c, m, mM); 
					} else {
						startGame(loadPlayer(c), jar, c, m, mM); 	
					}
				} while (adminOption == 3){
					boolean flag = false;
					String playerName = c.readLine("Enter players name.  ");
					// wee need to create a player instance and send name in to the constractor.
					flag = addPlayer(createPlayer(playerName));
					if (flag){
						break;
					}
				}
				while (adminOption == 4) {
					removePlayer(c);
					redirectUser(greetUser(c,mM),m, c, mM);
					break;
				}
			} while (option == 2) {
				Player p = new Player();
				startGame(p, createRandomJar(), c, m, mM);
			} while (option == 3) {
				showScoreTable(c);
				redirectUser(greetUser(c,mM),m, c, mM);
			} while (option == 4) {
				System.exit(1);
			}
		}
		return jar;
	}
	
	private static int displayAdmin(Console c, Menu m){
		return greetUser(c,m);
	}
	
	private static Jar createRandomJar(){
		String[] presetItems = {"Aples", "Jelly Beans", "Bears", "Tomatos"};
		Random ran = new Random();
		int randAmount = ran.nextInt(100) + 1;
		Jar randomJar = new Jar(presetItems[ran.nextInt(presetItems.length)],randAmount);
		return randomJar;
	}
	
	public static Player createPlayer(String playerName) {
		Player newPlayer = new Player(playerName);
		return newPlayer;
	}
	
	public static boolean addPlayer(Player player) {
		Player[] players = Players.load();
		List<Player> playersAsList = new LinkedList<Player>(Arrays.asList(players));
		boolean flag = false;
		if (playersAsList.isEmpty()){
			playersAsList.add(player);
			Players.Save(playersAsList.toArray(new Player[playersAsList.size()]));
			System.out.println("Player was succesfully saved in empty list.\n ");
			System.out.println("\n<<<<<< To use this player plese  use a load player option. >>>>> \n");
			flag = true;
		} else {
			ListIterator<Player> listIterator = playersAsList.listIterator();
			while(listIterator.hasNext()) {
				Player p = listIterator.next();
				System.out.println(p.getName()+" --- "+ player.getName());
				if (p.getName().equals(player.getName())) {
					flag = false;
					break;
				}else {
					flag = true;
				}
			}
			if (flag) {
				playersAsList.add(player);
				Players.Save(playersAsList.toArray(new Player[playersAsList.size()]));
				System.out.println("Player was succesfully saved.\n ");
				System.out.println("\n<<<<<< To use this player plese  use a load player option. >>>>> \n");
				return true;
			} else {
				System.out.println("Sorry could not save this player the name alredy exists.");
				return false;
			}
		}
		return flag;
	}
	
//	 load a user this method is going to take a aray of players as argument and present the list to a user 
//	 then user will need to enter the name of a player and application will send this player object into 
//	 the game for the score to be recorded. 
	public static Player loadPlayer(Console c){
		Player player = null;
		List<Player> playersAsList = new ArrayList<Player>(Arrays.asList(Players.load()));
		String playerSelected;
		c.printf("\nSaved Players are\\is: \n");
		if (playersAsList.isEmpty()) {
			c.printf("There are no players yet.");
			c.printf("Please Add new Player.");
			playerSelected = c.readLine("Please enter name.  ");
			if (addPlayer(new Player(playerSelected)));
				playersAsList = new ArrayList<Player>(Arrays.asList(Players.load()));
		}
		ListIterator<Player> listIterator = playersAsList.listIterator();
		while(listIterator.hasNext()){
			c.printf(listIterator.next().getName()+"\n");
		}
		playerSelected = c.readLine("To make a selection please enter players name:  ");
		for(Player playerInList : playersAsList){
			if (playerInList.getName().equals(playerSelected)){
				c.printf("Found %s, importing player now\n", playerInList.getName());
				player = playerInList;
				return player;
			} else {
				c.printf("There is no such player exist plese check the name.");
				return loadPlayer(c);
			}
		}
		return player;
	}
	
	public static void showScoreTable(Console c) {
		List<Player> playersAsList = new ArrayList<Player>(Arrays.asList(Players.load()));
		ListIterator<Player> listIterator = playersAsList.listIterator();
		c.printf("Now Showing score table..\n");
		if (!listIterator.hasNext()) {
			c.printf("There are no players\n");
		} else {
			while(listIterator.hasNext()){
				Player p = listIterator.next();
				c.printf("Name: %s Score: %d \n", p.getName(), p.getScore());
			}
		}
	}	
	
	public static void showAllPlayers(Player[] players){
		List<Player> playersAsList = new ArrayList<Player>(Arrays.asList(players));
		ListIterator<Player> listIterator = playersAsList.listIterator();
		if (!listIterator.hasNext()) {
			System.out.println("There are no players\n");
		}else {
			while(listIterator.hasNext()){
				Player p = listIterator.next();
				System.out.println(p.getName());
			}
		}
	}
	
	public static boolean RemovePlayerFromFile(String name){
		List<Player> playersAsList = new ArrayList<Player>(Arrays.asList(Players.load()));
		ListIterator<Player> listIterator = playersAsList.listIterator();
			if (!listIterator.hasNext()) {
				return false;
			}else {
				while(listIterator.hasNext()){
					Player p = listIterator.next();
					if ( p.getName().equals(name)){
						listIterator.remove();
						Players.Save(playersAsList.toArray(new Player[playersAsList.size()]));
						return true;		 
					} else {
						return false;
					}
				}
			}
		return false;
	}
	
	public static void removePlayer(Console c){
		// i need to remove a player that user enters. 
		showAllPlayers(Players.load());
		String playersName = c.readLine("Please enter players name that you wish to delete.  ");
		if(RemovePlayerFromFile(playersName)){
			c.printf("Player was successfuly removed.\n");
			showAllPlayers(Players.load());
		}else {
			c.printf("Could not remove player. No such player found.\n");
		}
	}
}