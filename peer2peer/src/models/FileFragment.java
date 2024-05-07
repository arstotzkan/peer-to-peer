package models;

import java.util.ArrayList;

public class FileFragment {
    String name;
    ArrayList<OnlineUser> usersWithFragment;

    public FileFragment(){
        usersWithFragment = new ArrayList<>();
        name="";
    }

    public FileFragment(String name){
        usersWithFragment = new ArrayList<>();
        this.name=name;
    }

    public ArrayList<OnlineUser> getUsersWithFragment(){
        return usersWithFragment;
    }

    public boolean userHasFile(String username){
        for (OnlineUser ou: usersWithFragment){
            if (ou.getUsername().equals(username)){
                return true;
            }
        }

        return false;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }
}
