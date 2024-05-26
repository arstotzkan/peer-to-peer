package peer;

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
        while (true) {
            try {
                // Select a filename that the peer does not have
                String filename = peer.selectRandomFile();
                if (filename == null || downloadedFiles.contains(filename)) {
                    continue; // Skip if no valid file is found or if the file is already downloaded
                }

                // Get users that have fragments of this file
                HashMap<Integer, List<OnlineUser>> usersWithFragments = peer.getUsersWithFragment(filename);

                // Keep track of downloaded fragments
                int totalFragments = usersWithFragments.size();
                byte[][] fragments = new byte[totalFragments][];
                boolean[] fragmentsDownloaded = new boolean[totalFragments];

                // Download fragments
                while (!allFragmentsDownloaded(fragmentsDownloaded)) {
                    for (int i = 0; i < totalFragments; i++) {
                        if (!fragmentsDownloaded[i]) {
                            List<OnlineUser> users = usersWithFragments.get(i);
                            int usersCount = Math.min(4, users.size());

                            for (int j = 0; j < usersCount; j++) {
                                OnlineUser user = users.get(j);
                                FragmentRequestRoutine fragmentRequest = new FragmentRequestRoutine(peer, filename, i, user);
                                fragmentRequest.start();
                            }

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
}
