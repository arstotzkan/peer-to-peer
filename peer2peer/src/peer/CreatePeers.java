package peer;

import java.io.IOException;
import java.util.Scanner;

public class CreatePeers {
    /*Class that initializes a number of Peer objects in this Machine*/
    public static void main(String[] args) throws IOException {
        int numberOfPeers = -1;
        Scanner scanner = new Scanner(System.in);
        while (numberOfPeers < 1 || numberOfPeers > 1000) {
            System.out.println("Set number of peers (1-1000) :");
            numberOfPeers = scanner.nextInt(); //taking number of workers from args
        }

        for (int i = 0; i < numberOfPeers ;i++){
            Peer p = new Peer("peer" + i, "pwd"+i, "" ); //TODO: integrate sharedDirPath
            p.register();
            p.logIn();
        }
    }

}
