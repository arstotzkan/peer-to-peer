package peer;

import models.OnlineUser;

public class FragmentRequestRoutine extends Thread {
    private Peer peer;
    private String filename;
    private int fragmentNumber;
    private OnlineUser receiver;

    public FragmentRequestRoutine(Peer peer, String filename, int fragmentNumber, OnlineUser receiver) {
        this.peer = peer;
        this.filename = filename;
        this.fragmentNumber = fragmentNumber;
        this.receiver = receiver;
    }

    @Override
    public void run() {
        peer.downloadFragment(filename, fragmentNumber, receiver);
    }
}
