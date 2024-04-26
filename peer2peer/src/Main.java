import peer.Peer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static String trackerAddress = "localhost";
    static Peer peer;

    static String sharedDir;
    public static void main(String[] args) throws IOException {
        sharedDir = args[0];
        trackerAddress = args[1];
        logInOrRegisterScreen();
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
        System.out.println("\nUsername: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();

        peer = new Peer(username,password, sharedDir,trackerAddress );
        peer.start();

        String loginMessage = peer.logIn();
        System.out.println(loginMessage + "\n");

        if (loginMessage.contains("Login successful"))
            mainScreen();
        else
            logInOrRegisterScreen();
    }
    private static void registerScreen() throws IOException {
        System.out.println("\nUsername: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();

        peer = new Peer(username,password, sharedDir,trackerAddress);
        peer.start();
        String registerMessage = peer.register();
        System.out.println(registerMessage + "\n");

        if (registerMessage.contains("Registration successful"))
            mainScreen();
        else
            logInOrRegisterScreen();
    }

    private static void mainScreen() throws IOException {
        ArrayList<String> fileList = peer.list();
        System.out.println("\n\n" + "File list: \n=========");

        if (fileList.size() > 0){
            for (String f : fileList)
                System.out.println(f);

            System.out.println("\nSelect filename to download (or press 0 to log out): ");
            String filename = scanner.nextLine();

            if (!filename.equals("0"))
                downloadFileScreen(filename);
            else{
                System.out.println(peer.logOut());
                logInOrRegisterScreen();
            }
        } else {
            mainScreen();
        }
    }

    private static void downloadFileScreen(String filename) throws IOException {
        String downloadMessage = peer.downloadFile(filename);
        System.out.println("\n\n" + downloadMessage);
        String choice = "";
        while (!choice.equals("0") && !choice.equals("1")) {
            System.out.println("\n1)Go to file screen\n0)Log out");
            choice = scanner.nextLine();

            if (choice.equals("1")){
                mainScreen();
            } else if (choice.equals("0")) {
                System.out.println(peer.logOut());
                logInOrRegisterScreen();
            }
        }
    }
}
