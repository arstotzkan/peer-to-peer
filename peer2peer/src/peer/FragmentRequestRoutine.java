package peer;

import models.OnlineUser;

public class FragmentRequestRoutine extends Thread{

    Peer sender;
    String filename;
    OnlineUser receiver;
    @Override
    public void run() {
        sender.simpleDownload(filename, receiver);
    }
}
