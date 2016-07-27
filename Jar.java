import java.util.Random;

public class Jar {
	private String mTypeOfItem;	
	private int mAmoutOfItems = 1;
	
	
	public Jar (String typeOfItem, int amountOfItems) {
		mTypeOfItem = typeOfItem;
		mAmoutOfItems = validateAmountOfItems(amountOfItems);
	}
	//Getters..
	public String getTypeOfItem(){
		return mTypeOfItem;
	}
	
	public int getAmountOfItems(){
		return mAmoutOfItems;
	}
	//Validator..
	public int validateAmountOfItems (int desiredAmount){
		if (desiredAmount <= 0) {
			throw new IllegalArgumentException("Cant use a number less or eqaul to 0 \n nor you can use alpha characters.\n");	
		}
		return desiredAmount;
	}
}