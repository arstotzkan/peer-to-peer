package peer;

import com.sun.jdi.StringReference;

import java.io.IOException;
import java.net.ServerSocket;

public class Peer extends Thread{
    String name;
    String password;
    ServerSocket server;
    String sharedDirPath;

    public Peer(String name, String password, int port, String sharedDirPath) throws IOException {
        this.name = name;
        this.password = password;
        this.server = new ServerSocket(port);
        this.sharedDirPath = sharedDirPath;
    }
    public void register(){
        /*Send username & password to tracker
        * If username is already user, an error message is
        * returned by the tracker
        *
        * Maybe we can add this to the constructor*/
    }

    public void logIn(){
        /*Send username & password to tracker
        * A tokenId is returned by the tracker*/
    }

    public void logOut(){
        /*Sends message to tracker*/
    }

    public void list(){
        /*Ask tracker for a list of available files*/
    }
    public void details(String fileName){
        /*Ask tracker for details concerning a specific file*/
    }
    public void checkActive(String peerIP, int peerPort){
        /*Check if a specific peer is active*/
    }
    public void simpleDownload(String peerIP, int peerPort){
        /*Find best peer by calling checkActive multiple times for each one
        * and then using the formula 0.75^count_downloads*1.25^count_failures
        * */
    }

    public void notifyTracker(String filename){
        /*Notify tracker that we also have a specific file*/
    }

}
