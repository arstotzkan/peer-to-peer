package tracker;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TrackerRequestHandler extends Thread{
    /*WIP, handle requests*/
    DataInputStream in;
    ObjectOutputStream out;
    String sender;
    TrackerMemory memory;
    String message;

    public TrackerRequestHandler(Socket req , TrackerMemory mem) throws IOException {
        this.out = new ObjectOutputStream(req.getOutputStream());
        this.in = new DataInputStream(req.getInputStream());
        this.sender =  req.getRemoteSocketAddress().toString();
        this.memory = mem;
    }

    public void run(){
        try {
            String message = in.readUTF();

            switch (message){
                case "register":
                    this.handleRegister();
                    break;
                case "logIn":
                    this.handleLogIn();
                    break;
                case "logOut":
                    this.handleLogOut();
                    break;
//                case "fileUpdate":
//                    this.handleFileUpdate();
//                    break;
                case "listRequest":
                    this.handleListRequest();
                    break;
                case "detailsRequest":
                    this.handleDetailsRequest();
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleRegister(){
        //add user to memory registered users
        //return confirmation message
    }
    public void handleLogIn(){
        //add user to active users
        //return confirmation message
    }
    public void handleLogOut(){
        //remove user to active users
        //return confirmation message
    }
//    public void handleFileUpdate(){
//        //remove user to active users
//        //return confirmation message
//    }
    public void handleListRequest(){
        //find and return avaiable files from memory
    }
    public void handleDetailsRequest(){
        //get and return details from file
    }
}
