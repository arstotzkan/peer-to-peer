package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UploadedFile implements Serializable {
    private String name;
    private int fragmentCount;
    private ArrayList<OnlineUser> usersWithFile;

    public UploadedFile() {
        usersWithFile = new ArrayList<>();
        name = "";
    }

    public UploadedFile(String name) {
        usersWithFile = new ArrayList<>();
        this.name = name;
    }

    public UploadedFile(String name, int fragmentCount, ArrayList<OnlineUser> usersWithFile) {
        this.name = name;
        this.fragmentCount = fragmentCount;
        this.usersWithFile = usersWithFile;
    }

    public ArrayList<OnlineUser> getUsersWithFile() {
        return usersWithFile;
    }

    public boolean userHasFile(String username) {
        for (OnlineUser ou : usersWithFile) {
            if (ou.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getFragmentCount() {
        return fragmentCount;
    }

    public void setFragmentCount(int fragmentCount) {
        this.fragmentCount = fragmentCount;
    }
}
