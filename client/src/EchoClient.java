import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoClient {
    private final int port;
    private final String host;

    public EchoClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public static EchoClient connectTo(int port) {
        var localhost = "127.0.0.1";
        return new EchoClient(port, localhost);
    }

    public void run() {
        System.out.println("напиши 'bye чтобы выйти\n\n\n");
        try (var socket = new Socket(host, port)) {
            var scanner = new Scanner(System.in, "UTF-8");
            var out = socket.getOutputStream();
            var input = socket.getInputStream();
            var isr = new InputStreamReader(input);
            var writer = new PrintWriter(out);
            try (scanner; writer; var sc = new Scanner(isr)) {
                while (true) {
                    String message = scanner.nextLine();
                    writer.write(message);
                    writer.write(System.lineSeparator());
                    writer.flush();
                    var answer = sc.nextLine().strip();
                    System.out.printf("Answer: %s%n", answer);
                    if ("bye".equalsIgnoreCase(message)) {
                        return;
                    }
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Connection dropped");
        } catch (IOException e) {
            System.out.printf("Can't connect to %s:%s!%n", host, port);
            e.printStackTrace();
        }
    }
}