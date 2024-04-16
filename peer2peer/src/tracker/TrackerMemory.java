package tracker;

import models.OnlineUser;
import models.UploadedFile;
import models.User;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;



public class TrackerMemory {
    ConcurrentLinkedQueue<User> users = new ConcurrentLinkedQueue<User>();
    ConcurrentLinkedQueue<OnlineUser> loggedInUsers = new ConcurrentLinkedQueue<OnlineUser>();
    ConcurrentLinkedQueue<UploadedFile> fileNames = new ConcurrentLinkedQueue<UploadedFile>();

    ArrayList<String> fileNamesStr = new ArrayList<>(); 
	//ArrayList<String> UserNamesStr = new ArrayList();
	//ArrayList<String> OnlineUserNamesStr = new ArrayList();

    public TrackerMemory() {
        //load methodos ston constructor
        loadFileNames();
    }




    public ConcurrentLinkedQueue<User> getUsers(){
        return users;
    }

    public ConcurrentLinkedQueue<OnlineUser> getLoggedInUsers(){
        return loggedInUsers;
    }

    public ConcurrentLinkedQueue<UploadedFile> getFileNames(){
        return fileNames;
    }

    private void loadFileNames() {
        try (BufferedReader reader = new BufferedReader(new FileReader("fileDownloadList.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // neo antikimeno gia kathe new name
                UploadedFile file = new UploadedFile(line);
                // edo to kanei add
                fileNames.add(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUser(String username){
        for (User u: users){
            if (u.getUsername().equals(username)){
                return u;
            }
        }
        return null;
    }


    public OnlineUser getOnlineUser(String username){
        for (OnlineUser u: loggedInUsers){
            if (u.getUsername().equals(username)){
                return u;
            }
        }
        return null;
    }


    public UploadedFile getUploadedFile(String file1){
        for (UploadedFile f: fileNames){
            if (f.getName().equals(file1)){
                return f;
            }
        }
        return null;
    }

    //returns an ArrayList with the names of all the files in Tracker's Memory 
	public ArrayList<String> getListFileNames(){
        fileNamesStr.clear();//delete previous element in list
		for (UploadedFile f: fileNames){
			String name = f.getName();
			fileNamesStr.add(name);			
        }
        return fileNamesStr;
        
    }
	
	
	
	/*
	//returns an ArrayList with the names of all the users in Tracker's Memory 
	public ArrayList<String> getListUserNames(){
        UserNamesStr.clear();//delete previous element in list
		for (User u: users){
			String name = u.getUsername();
			UserNamesStr.add(name);			
        }
        return UserNamesStr;
        
    }
	*/
	
	/*
	//returns an ArrayList with the names of all the online_users in Tracker's Memory 
	public ArrayList<String> getListOnlineUserNames(){
        OnlineUserNamesStr.clear();//delete previous element in list
		for (OnlineUser o: loggedInUsers){
			String name = o.getUsername();
			OnlineUserNamesStr.add(name);			
        }
        return OnlineUserNamesStr;
        
    }
	*/

    public void addUser(User user1){
        users.add(user1);
    }

    public void addOnlineUser(OnlineUser user1){
        loggedInUsers.add(user1);
    }

    public void addUploadedFile(UploadedFile file1){
        fileNames.add(file1);
    }
    public boolean removeUser(String username){
        for(Iterator<User> it = users.iterator(); it.hasNext(); ) {
            User u = it.next();
            if (u.getUsername().equals(username)){
                it.remove();
                return true;
            }
      }
      return false;
    }
    public boolean removeOnlineUser(String username){
        for(Iterator<OnlineUser> it = loggedInUsers.iterator(); it.hasNext(); ) {
            OnlineUser u = it.next();
            if (u.getUsername().equals(username)){
                it.remove();
                return true;
            }
      }
      return false;
    }
    public boolean removeUploadedFile(String file1){
        for(Iterator<UploadedFile> it = fileNames.iterator(); it.hasNext(); ) {
            UploadedFile f = it.next();
            if (f.getName().equals(file1)){
                it.remove();
                return true;
            }
      }
      return false;
    }
    public void increaseCountDownloads(String username){
        for (OnlineUser u: loggedInUsers){
            if (u.getUsername().equals(username)){
                u.increaseCountDownloads();
            }
        }
    }
    public void increaseCountFailures(String username){
        for (OnlineUser u: loggedInUsers){
            if (u.getUsername().equals(username)){
                u.increaseCountFailures();
            }
        }
    }
}
