import java.io.Serializable;

public class Player implements Comparable, Serializable {
	private String mName;
	private int mScore = 0;
	
	public Player(String name){
		mName = name;
	}
	public Player(){
		mName = "Unknown";
	}

	public void setScore(int score) {
		mScore = score;
	}
	public int getScore(){
		return mScore;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public String getName(){
		return mName;
	}
	@Override 
	public String toString(){
		return String.format("name: %s score: %s",mName, mScore);
	}
	
	@Override 
	public int compareTo(Object obj){
		Player other = (Player) obj;
		if (equals(other)) {
			return 0;
		}
		if (mScore > other.mScore){
			return 1;
		}
		if (mScore < other.mScore){
			return -1;
		}
		return 0;
	}
	
	// this method calculates score based on the amount of total items in the jar
	// for instanse a player who guessed a number with 5 attempts will get more point if total amount of items
	// in the jar was 50 cmpareing to a different player who guessed the right number also with 5 attamptes 
	// but only had 10 items in the jar.
	
	public int calcScore(int AmoutOfItems, int tryes) {
		int num_1 = tryes - AmoutOfItems;
    	int num_2 = 1 - tryes;
	 	if(num_2 == 0) {
			return (num_1 * -1) * 100;
		}
		return (int) (num_1 / num_2) * 100;
	}
	
}