package tracker;

import models.OnlineUser;
import models.UploadedFile;
import models.User;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class TrackerRequestHandler extends Thread{
    /*WIP, handle requests*/
    ObjectInputStream in;
    ObjectOutputStream out;
    String sender;
    TrackerMemory memory;

    public TrackerRequestHandler(Socket req , TrackerMemory mem) throws IOException {
        this.out = new ObjectOutputStream(req.getOutputStream());
        this.in = new ObjectInputStream(req.getInputStream());
        this.sender =  req.getRemoteSocketAddress().toString();
        this.memory = mem;
    }

    public void run(){
        try {
            HashMap<String,String> request = (HashMap<String,String>) in.readObject();
            String message = request.get("type");

            switch (message){
                case "register":
                    this.handleRegister(request);
                    break;
                case "logIn":
                    this.handleLogIn(request);
                    break;
                case "logOut":
                    this.handleLogOut(request);
                    break;
                case "listRequest":
                    this.handleListRequest(request);
                    break;
                case "detailsRequest":
                    this.handleDetailsRequest(request);
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleRegister(HashMap<String,String> request) throws IOException {

        //TODO: HANDLE USERNAME ALREADY EXISTS
        User newUser = new User(request.get("username"), request.get("password") );
        this.memory.addUser(newUser);

        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Succesfully registered");
        out.writeObject(response);
    }
    public void handleLogIn(HashMap<String,String> request) throws IOException {
        //TODO: function to generate tokenID

        //TODO: HANDLE USER NOT REGISTERED
        OnlineUser loggedIn = new OnlineUser(request.get("username"),request.get("password"), "", this.sender);
        this.memory.addOnlineUser(loggedIn);

        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Succesfully logged in");
        out.writeObject(response);
    }
    public void handleLogOut(HashMap<String,String> request) throws IOException {
        //TODO: HANDLE USER NOT LOGGED IN

        String username = request.get("username");
        this.memory.removeOnlineUser(username);

        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Succesfully logged out");
        out.writeObject(response);
    }

    public void handleListRequest(HashMap<String,String> request) throws IOException {
        this.memory.getFileNames();
        //TODO: TAKE ONLY NAMES
        HashMap<String, ArrayList<String>> response = new HashMap<>();
        response.put("F", new ArrayList<String>());
        out.writeObject(response);
    }
    public void handleDetailsRequest(HashMap<String,String> request) throws IOException {
        //get and return details from file
        String filename = request.get("filename");
        UploadedFile file = this.memory.getUploadedFile(filename);
        HashMap<String, UploadedFile> response = new HashMap<>();
        response.put("details", file);
        out.writeObject(response);
    }
}
