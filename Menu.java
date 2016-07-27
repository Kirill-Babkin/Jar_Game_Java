public class Menu {
	private String mMenuMessage;
	private int mOption;
	
	public Menu(String menuMessage){
		mMenuMessage = menuMessage;
		mOption = 0;
	}
	
	public String getMenuMessage() {
		return mMenuMessage;
	}
	public void setOption(int option){
		mOption = option;
	}
	public int getOption(){
		return mOption;
	}
}