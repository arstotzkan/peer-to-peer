package peer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PeerInit {
    static Scanner scanner = new Scanner(System.in);
    static Peer peer;

    static String sharedDir;
    public static void main(String[] args) {
        sharedDir = args[0];
        System.out.println("Hello world!");
    }

    private static void logInOrRegisterScreen() throws IOException {
        String choice = "";
        while (!choice.equals("0") && !choice.equals("1") && !choice.equals("2") ) {
            System.out.println("1)Log in\n2)Register\n0)Go back");
            choice = scanner.nextLine();
        }

        if (choice.equals("1")){
            logInScreen();
        } else if (choice.equals("2")) {
            registerScreen();
        }
    }

    private static void logInScreen() throws IOException {
        System.out.println("\n\nUsername: ");
        String username = scanner.nextLine();
        System.out.println("\nPassword: ");
        String password = scanner.nextLine();

        peer = new Peer(username,password, sharedDir );
        System.out.println(peer.logIn());
        mainScreen();
    }
    private static void registerScreen() throws IOException {
        System.out.println("\n\nUsername: ");
        String username = scanner.nextLine();
        System.out.println("\nPassword: ");
        String password = scanner.nextLine();

        peer = new Peer(username,password, sharedDir );
        System.out.println("\n" + peer.register());
        mainScreen();
    }

    private static void mainScreen(){
        ArrayList<String> fileList = peer.list();

        if (fileList.size() > 0){
            System.out.println("\n\n" + fileList);
            System.out.println("\nSelect filename to get details from: ");
            String filename = scanner.nextLine();

            fileDetailsScreen(filename);
        } else {
            mainScreen();
        }
    }
    private static void fileDetailsScreen(String filename){
        String details = peer.details(filename);

        if (!details.contains("Error")){
            System.out.println("\n\n" + details);
            System.out.println("\nSelect peer to get file from: ");
            String peerUsername = scanner.nextLine();
            downloadFileScreen(filename,peerUsername);
        } else {
            mainScreen();
        }
    }

    private static void downloadFileScreen(String filename, String peerUsername){
        String downloadMessage = peer.simpleDownload(filename, peerUsername);
        System.out.println(downloadMessage);
        fileDetailsScreen(filename);

    }
}
