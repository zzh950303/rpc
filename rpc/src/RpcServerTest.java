import java.io.IOException;

public class RpcServerTest {
    public static void main(String[] args) {
        try {
            RpcServer rpcServer=new RpcServer("localhost",8088);
            rpcServer.register(HelloService.class, HelloServiceImpl.class);
            rpcServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}