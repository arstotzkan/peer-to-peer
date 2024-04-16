package models;

import java.util.ArrayList;

public class UploadedFile {
    

     String name;
     ArrayList<OnlineUser> usersWithFile;

     public UploadedFile(){
        usersWithFile = new ArrayList<>();
        name="";
     }

     public UploadedFile(String name){
        usersWithFile = new ArrayList<>();
        this.name=name;
     }

     public ArrayList<OnlineUser> getUsersWithFile(){
        return usersWithFile;
     }

     public void setName(String name){
        this.name=name;
     }

     public String getName(){
        return name;
     }
}
