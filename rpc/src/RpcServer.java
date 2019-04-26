import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class RpcServer {
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private int port;
    private String ip;

    public RpcServer(String ip, int port) {
        this.port = port;
        this.ip = ip;
    }

    private static final HashMap<String, Class> serviceRegistry = new HashMap<String, Class>();

    public void register(Class serviceInterface, Class impl) {
        serviceRegistry.put(serviceInterface.getName(), impl);
    }

    //
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket();

        serverSocket.bind(new InetSocketAddress(ip, port));

        // 1.监听客户端的TCP连接，接到TCP连接后将其封装成task，由线程池执行
        while (true) {
            Socket socket = serverSocket.accept();
            executor.execute(new ServerTask(socket, serviceRegistry));
        }
    }
}