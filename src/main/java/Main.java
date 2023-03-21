public class Main {
    public static final int PORT = 8989;
    public static void main(String[] args) throws Exception {
        SearchServer server = new SearchServer(PORT);
        server.start();
    }
}