package models;

public class OnlineUser extends User{
    
    private String tokenID;
    private String address;

	private int port;

    public OnlineUser(String username, String password, String tokenID, String address, int port) {
		super(username, password);
		this.tokenID = tokenID;
		this.address = address;
		this.port = port;
	}

	public OnlineUser(){
		super("", "");
		this.tokenID = "";
		this.address = "";
		this.port = 0;
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

	public int getPort() {
		return port;
	}
}
