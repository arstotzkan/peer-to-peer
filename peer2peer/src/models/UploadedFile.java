package models;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UploadedFile implements Serializable {
    

     String name;
     ArrayList<FileFragment> fragments;

    ArrayList<OnlineUser> seeders;

     public UploadedFile(){
         fragments = new ArrayList<>();
         seeders = new ArrayList<>();
        name="";
     }

     public UploadedFile(String name){
         fragments = new ArrayList<>();
        this.name=name;

         for (int i = 0; i < 10; i++){
             FileFragment f = new FileFragment(name +".part." + i);
             fragments.add(f);
         }
     }

    public ArrayList<FileFragment> getFragments(){
        return fragments;
    }
    public ArrayList<OnlineUser> getSeeders(){
        return seeders;
    }

    public ArrayList<OnlineUser> getUsersWithFragment(){
         HashSet<OnlineUser> users = new HashSet<OnlineUser>();

         for (FileFragment f : fragments){
            for (OnlineUser u: f.getUsersWithFragment()){
                users.add(u);
            }
         }

         return new ArrayList<>(users);
    }

     public void setName(String name){
        this.name=name;
     }

     public String getName(){
        return name;
     }

     public boolean userHasFragment(String username) {
         for (OnlineUser ou : getUsersWithFragment()) {
             if (ou.getUsername().equals(username)) {
                 return true;
             }
         }
         return false;
     }

    public FileFragment getFragment(String fragmentName){
        for (FileFragment ff: fragments){
            if (ff.getName().equals(fragmentName)){
                return ff;
            }
        }
        return null;
    }
    public boolean userIsSeeder(String username) {
        for (OnlineUser ou : seeders) {
            if (ou.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
