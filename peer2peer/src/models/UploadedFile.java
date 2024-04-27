package models;

import java.io.Serializable;
import java.util.ArrayList;

public class UploadedFile implements Serializable {
    

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

     public boolean userHasFile(String username){
         for (OnlineUser ou: usersWithFile){
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
