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
    String senderAddress;
    int senderPort;
    TrackerMemory memory;

    public TrackerRequestHandler(Socket req , TrackerMemory mem) throws IOException {
        this.out = new ObjectOutputStream(req.getOutputStream());
        this.in = new ObjectInputStream(req.getInputStream());
        this.senderAddress =  req.getInetAddress().getHostAddress();
        this.senderPort = req.getPort();
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
                case "uploadFileName":
                    this.handleUploadRequest(request);
                    break;

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleRegister(HashMap<String,String> request) throws IOException {

        // elegxos oti an yparxei idi o xristis
        String username = request.get("username");
        if (this.memory.getUser(username) != null) {

            HashMap<String, String> response = new HashMap<>();
            response.put("message", "User with this username already exists");
            out.writeObject(response);
            return;
        }

        User newUser = new User(request.get("username"), request.get("password") );
        this.memory.addUser(newUser);

        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Succesfully registered");
        out.writeObject(response);
    }
    public void handleLogIn(HashMap<String,String> request) throws IOException {
        //TODO: function to generate tokenID

        //elegxo an yparxei o xristis
        String username = request.get("username");
        if (this.memory.getUser(username) == null) {

            HashMap<String, String> response = new HashMap<>();
            response.put("message", "User does not exist");
            out.writeObject(response);
            return;
        }

        OnlineUser loggedIn = new OnlineUser(request.get("username"),request.get("password"), "", this.senderAddress, this.senderPort);
        this.memory.addOnlineUser(loggedIn);

        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Succesfully logged in");
        out.writeObject(response);
    }
    public void handleLogOut(HashMap<String,String> request) throws IOException {
       // elegxei an einai login o xristis
        String username = request.get("username");
        if (this.memory.getOnlineUser(username) == null) {

            HashMap<String, String> response = new HashMap<>();
            response.put("message", "User is not logged in");
            out.writeObject(response);
            return;
        }

        this.memory.removeOnlineUser(username);
        //TODO: delete usedfrom all files
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Succesfully logged out");
        out.writeObject(response);
    }

    public void handleListRequest(HashMap<String,String> request) throws IOException {
        this.memory.getFileNames();
        HashMap<String, ArrayList<String>> response = new HashMap<>();
        response.put("fileList", this.memory.getListFileNames());
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

    public void handleUploadRequest(HashMap <String, String> request) throws IOException{
        String filename = request.get("filename");
        String username = request.get("username");

        UploadedFile f = this.memory.getUploadedFile(filename);
        OnlineUser user = this.memory.getOnlineUser(username);


        HashMap<String, String> response = new HashMap<>();

        if (f == null || user == null){
            response.put("message", "Failure");
        }else{
            f.getUsersWithFile().add(user); //TODO: check if already uploaded
            response.put("message", "Success");
        }

        out.writeObject(response);

    }
}
