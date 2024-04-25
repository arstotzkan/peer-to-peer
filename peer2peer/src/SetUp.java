public class SetUp {
    /*maybe setting up everything here will be useful for development*/
    public static void main(String[] args) {
        try{
            String[] a = {"127.0.0.1"};
            String[] b = {};
            TrackerInit.main(b);
            CreatePeers.main(a);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}