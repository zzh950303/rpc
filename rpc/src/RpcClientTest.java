
import java.net.InetSocketAddress;

public class RpcClientTest {
    public static void main(String[] args) {
        HelloService helloService= (HelloService) RpcClientPoxy.getRemoteProxyObj(HelloService.class, new InetSocketAddress("localhost",8088));
        System.out.println(helloService.sayHello("xxxxxxx"));
        System.out.println(helloService.sayBye());
    }
}