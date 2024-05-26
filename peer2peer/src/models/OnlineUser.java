package models;

import java.util.HashSet;
import java.util.Set;

public class OnlineUser extends User {
	private String tokenID;
	private String address;
	private int port;
	private Set<Integer> fragments;

	public OnlineUser(String username, String password, String tokenID, String address, int port) {
		super(username, password);
		this.tokenID = tokenID;
		this.address = address;
		this.port = port;
		this.fragments = new HashSet<>();
	}

	public OnlineUser() {
		super("", "");
		this.tokenID = "";
		this.address = "";
		this.port = 0;
		this.fragments = new HashSet<>();
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

	public void setPort(int port) {
		this.port = port;
	}

	public Set<Integer> getFragments() {
		return fragments;
	}

	public void setFragments(Set<Integer> fragments) {
		this.fragments = fragments;
	}

	public boolean hasFragment(int fragmentNumber) {
		return fragments.contains(fragmentNumber);
	}

	@Override
	public String toString() {
		return getUsername();
	}
}
