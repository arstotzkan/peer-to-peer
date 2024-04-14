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

    /*μέθοδος που δέχεται το username και ψάχνει στη λίστα των χρηστών και αν 
     * βρει το χρήστη τον επιστρέφει αν όχι επιστρέφει null
     */
    public User getUser(String username){
        for (User u: users){
            if (u.getUser_name().equals(username)){
                return u;
            }
        }
        return null;
    }

    /*μέθοδος που δέχεται το username και ψάχνει στη λίστα των online χρηστών και αν 
     * βρει το χρήστη τον επιστρέφει αν όχι επιστρέφει null
     */
    public OnlineUser getOnlineUser(String username){
        for (OnlineUser u: loggedInUsers){
            if (u.getUser_name().equals(username)){
                return u;
            }
        }
        return null;
    }

    /*μέθοδος που δέχεται το όνομα αρχείου και ψάχνει στη λίστα των αρχείων και αν 
     * το βρει το επιστρέφει αν όχι επιστρέφει null
     */
    public UploadedFile getUploadedFile(String file1){
        for (UploadedFile f: fileNames){
            if (f.getName().equals(file1)){
                return f;
            }
        }
        return null;
    }

    /*μέθοδος για προσθήκη χρήστη στην ουρά */
    public void addUser(User user1){
        users.add(user1);
    }

    /*μέθοδος για προσθήκη online χρήστη στην ουρά */
    public void addOnlineUser(OnlineUser user1){
        loggedInUsers.add(user1);
    }

    /*μέθοδος για προσθήκη αρχείου στην ουρά */
    public void addUploadedFile(UploadedFile file1){
        fileNames.add(file1);
    }
    /*μέθοδος για αφαίρεση χρήστη στην ουρά */
    public boolean removeUser(String username){
        for(Iterator<User> it = users.iterator(); it.hasNext(); ) {
            User u = it.next();
            if (u.getUser_name().equals(username)){
                it.remove();
                return true;
            }
      }
      return false;
    }
    /*μέθοδος για αφαίρεση online χρήστη στην ουρά */
    public boolean removeOnlineUser(String username){
        for(Iterator<OnlineUser> it = loggedInUsers.iterator(); it.hasNext(); ) {
            OnlineUser u = it.next();
            if (u.getUser_name().equals(username)){
                it.remove();
                return true;
            }
      }
      return false;
    }
    /*μέθοδος για αφαίρεση αρχείου στην ουρά */
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
    /*μέθοδος για αύξηση του count downloads για συγκεκριμένο χρήστη */
    public void increaseCountDownloads(String username){
        for (OnlineUser u: loggedInUsers){
            if (u.getUser_name().equals(username)){
                u.increaseCount_downloads();
            }
        }
    }
    /*μέθοδος για αύξηση του count failures για συγκεκριμένο χρήστη */
    public void increaseCountFailures(String username){
        for (OnlineUser u: loggedInUsers){
            if (u.getUser_name().equals(username)){
                u.increaseCount_failures();;
            }
        }
    }
}
