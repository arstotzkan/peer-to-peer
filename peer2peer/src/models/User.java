package models;

public class User {
    
    private String user_name;
    private String password;
    private int count_downloads; 
    private int count_failures;

    public User() {
		user_name = "";
		password = "";
        count_downloads=0;
        count_failures=0;
	}

    public User(String user_name, String password) {
		this.user_name = user_name;
		this.password = password;
        count_downloads=0;
        count_failures=0;
	}

    public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getCount_downloads() {
		return count_downloads;
	}
	public void setCount_downloads(int count_downloads) {
		this.count_downloads = count_downloads;
	}
	public int getCount_failures() {
		return count_failures;
	}
	public void setCount_failures(int count_failures) {
		this.count_failures = count_failures;
	}

    /* method to increase  count_downloads  +1 */
    public void increaseCount_downloads(){
        count_downloads++;
    }

    /*method to increase count_failures +1*/
    public void increaseCount_failures(){
        count_failures++;
    }

}
