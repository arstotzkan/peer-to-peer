package models;

public class OnlineUser extends User{
    
    private String tokenID;
    private String address;

    public OnlineUser(String username, String password, String tokenID, String address) {
		super(username, password);
		this.tokenID = tokenID;
		this.address = address;
	}

	public OnlineUser(){
		super("", "");
		this.tokenID = "";
		this.address = "";
	}

    public String getTokenID() {
		return tokenID;
	}
	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

}
