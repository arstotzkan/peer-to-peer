package models;

public class OnlineUser extends User{
    
    private String token_id;
    private String ip_address;
    private int port;
    
    public OnlineUser(String user_name, String password, String token_id, String ip_address, int port) {
		super(user_name, password);
		this.token_id = token_id;
		this.ip_address = ip_address;
		this.port = port;
	}

    public String getToken_id() {
		return token_id;
	}
	public void setToken_id(String token_id) {
		this.token_id = token_id;
	}
	public String getIp_address() {
		return ip_address;
	}
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

}
