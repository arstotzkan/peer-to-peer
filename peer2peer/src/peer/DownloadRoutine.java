package peer;

import models.FileFragment;
import models.OnlineUser;
import models.UploadedFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DownloadRoutine extends Thread {
    private Peer peer;
    private Set<String> downloadedFiles;

    public DownloadRoutine(Peer peer) {
        this.peer = peer;
        this.downloadedFiles = new HashSet<>();
    }

    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                // Select a filename that the peer does not have
                String filename = peer.selectRandomFile();
                if (filename == null || downloadedFiles.contains(filename)) {
                    continue; // Skip if no valid file is found or if the file is already downloaded
                }

                System.out.println(peer.getUsername() +" will download" + filename);
                // Get users that have fragments of this file
                UploadedFile file = peer.details(filename);

                ArrayList<ArrayList<OnlineUser>> usersWithFragments = createFragmentToUserMap(file);
                // Keep track of downloaded fragments
                int totalFragments = usersWithFragments.size();
                byte[][] fragments = new byte[totalFragments][];
                boolean[] fragmentsDownloaded = new boolean[totalFragments];

                // Download fragments
                while (!allFragmentsDownloaded(fragmentsDownloaded)) {
                    for (int i = 0; i < totalFragments; i++) {
                        if (!fragmentsDownloaded[i]) {
                            List<OnlineUser> users = usersWithFragments.get(i);
                            //System.out.println("Peer:" + peer.getUsername() + "Users with " + filename +  " " + i + ": " + users);
                            int usersCount = Math.min(4, users.size());

                            for (int j = 0; j < usersCount; j++) {
                                OnlineUser user = users.get(j);
                                FragmentRequestRoutine fragmentRequest = new FragmentRequestRoutine(peer, filename, i, user);
                                fragmentRequest.start();
                            }

                            fragmentsDownloaded[i] = true;
                            // Wait for 500ms before requesting the next set of fragments
                            Thread.sleep(500);
                        }
                    }
                }

                // Assemble the file from its fragments
                peer.assembleFile(filename, fragments);

                // Mark the file as downloaded
                downloadedFiles.add(filename);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean allFragmentsDownloaded(boolean[] fragmentsDownloaded) {
        for (boolean downloaded : fragmentsDownloaded) {
            if (!downloaded) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<ArrayList<OnlineUser>> createFragmentToUserMap(UploadedFile uf){
        ArrayList<ArrayList<OnlineUser>> list = new ArrayList<>(10);

        for (int j = 0; j < 10; j++)
            list.add(new ArrayList<OnlineUser>());

        for (int i = 0; i < 10; i++){
            FileFragment ff = uf.getFragments().get(i);
            System.out.println(peer.getUsername() + ": " + ff.getName() + " "+ ff.getUsersWithFragment());
            for (OnlineUser u: ff.getUsersWithFragment()){
                list.get(i).add(u);
            }
        }

        return list;
    }
}
