package tracker;

import models.OnlineUser;
import models.UploadedFile;
import models.User;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;


public class TrackerMemory {
    ConcurrentLinkedQueue<User> users = new ConcurrentLinkedQueue<User>();
    ConcurrentLinkedQueue<OnlineUser> loggedInUsers = new ConcurrentLinkedQueue<OnlineUser>();
    ConcurrentLinkedQueue<UploadedFile> fileNames = new ConcurrentLinkedQueue<UploadedFile>();

    

    public ConcurrentLinkedQueue<User> getUsers(){
        return users;
    }

    public ConcurrentLinkedQueue<OnlineUser> getLoggedInUsers(){
        return loggedInUsers;
    }

    public ConcurrentLinkedQueue<UploadedFile> getFileNames(){
        return fileNames;
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
