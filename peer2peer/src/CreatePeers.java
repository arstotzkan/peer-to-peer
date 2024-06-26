import peer.Peer;

import java.io.IOException;
import java.util.Scanner;

public class CreatePeers {
    /*Class that initializes a number of Peer objects in this Machine*/
    public static void main(String[] args) throws IOException {
        final String trackerAddress = args[0];
        int numberOfPeers = 6;
        for (int i = 1; i <= numberOfPeers ;i++){
            Peer p = new Peer("peer" + i, "pwd"+i, "src/peer/dir"+i , trackerAddress); //TODO: integrate sharedDirPath
            p.start();

            String registerMessage = p.register();
            System.out.println(p.getUsername() + ": " +registerMessage);

            if (registerMessage.contains("Registration failed")){
                System.out.println(p.getUsername() + ": " + p.logIn());
            }

            System.out.println(p.getUsername() + ": " + p.uploadFileNames() );

        }
    }

}
