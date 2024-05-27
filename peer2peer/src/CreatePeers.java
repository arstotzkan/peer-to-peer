import peer.Peer;

import java.io.IOException;
import java.util.Scanner;

public class CreatePeers {
    /*Class that initializes a number of Peer objects in this Machine*/
    public static void main(String[] args) throws IOException {
        final String trackerAddress = args[0];
        int numberOfPeers = 10;
        Peer[] peers = new Peer[10];

        for (int i = 0; i < numberOfPeers ;i++){
            peers[i] = new Peer("peer" + i, "pwd"+ i, "src/peer/dir"+ i , trackerAddress); //TODO: integrate sharedDirPath
            peers[i].start();

            String registerMessage = peers[i].register();
            System.out.println(peers[i].getUsername() + ": " +registerMessage);

            if (registerMessage.contains("Registration failed")){
                System.out.println(peers[i].getUsername() + ": " + peers[i].logIn());
            }

            System.out.println(peers[i].getUsername() + ": " + peers[i].uploadFileNames() );

        }

        for (int i = 0; i < numberOfPeers; i++){

        }
    }

}
