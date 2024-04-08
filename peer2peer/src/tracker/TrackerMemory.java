package tracker;

import models.OnlineUser;
import models.UploadedFile;
import models.User;

import java.util.concurrent.ConcurrentLinkedQueue;


public class TrackerMemory {
    ConcurrentLinkedQueue<User> users = new ConcurrentLinkedQueue<User>();
    ConcurrentLinkedQueue<OnlineUser> loggedInUsers = new ConcurrentLinkedQueue<OnlineUser>();
    ConcurrentLinkedQueue<UploadedFile> fileNames = new ConcurrentLinkedQueue<UploadedFile>();

    /*
    TODO:
        -getter method for each linked queue
        -methods that returns an array list the names of all UploadedFile/Users/OnlineUsers
        -methods that returns details of a specific User/OnlineUser/UploadedFile (input: its name, output: corresdponding object)
        -methods to add a User/OnlineUser/UploadedFile into each of the ConcurrentLinkedQueues (input: object)
        -methods to remove a User/OnlineUser/UploadedFile from each of the ConcurrentLinkedQueues (input: its name)
        -methods to increase countDownloads, countFailures for a specific user (input:name)
    * */
}
