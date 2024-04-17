package models;

import java.io.Serializable;

public class User implements Serializable {
    
    private String username;
    private String password;
    private int countDownloads;
    private int countFailures;

    public User() {
		username = "";
		password = "";
        countDownloads =0;
        countFailures =0;
	}

    public User(String user_name, String password) {
		this.username = user_name;
		this.password = password;
        countDownloads =0;
        countFailures =0;
	}

    public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getCountDownloads() {
		return countDownloads;
	}
	public void setCountDownloads(int countDownloads) {
		this.countDownloads = countDownloads;
	}
	public int getCountFailures() {
		return countFailures;
	}
	public void setCountFailures(int countFailures) {
		this.countFailures = countFailures;
	}

    /* method to increase  countDownloads  +1 */
    public void increaseCountDownloads(){
        countDownloads++;
    }

    /*method to increase countFailures +1*/
    public void increaseCountFailures(){
        countFailures++;
    }

}
