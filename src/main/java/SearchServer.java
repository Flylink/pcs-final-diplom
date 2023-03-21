import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class SearchServer {
    private final int port;
    private final BooleanSearchEngine engine;

    public SearchServer(int port) throws IOException {
        this.port = port;
        this.engine = new BooleanSearchEngine(new File("pdfs"));
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Запуск сервера " + port + "...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                ) {
                    String word = in.readLine();
                    if (word.length() == 0) {
                        throw new IllegalStateException("Не корректный ввод");
                    }
                    Gson gson = new Gson();
                    List<PageEntry> result = engine.search(word);
                    out.println(gson.toJson(result));
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}