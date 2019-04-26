import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RpcClientPoxy {

    public static Object getRemoteProxyObj(final Class<?> serviceInterface, final InetSocketAddress addr) {
        return Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class[]{serviceInterface}, (o, method, args) -> {
            Socket socket = null;
            ObjectOutputStream outputStream = null;
            ObjectInputStream inputStream = null;
            Object object = null;
            try {
                socket = new Socket();
                socket.connect(addr);

                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeUTF(serviceInterface.getName());
                outputStream.writeUTF(method.getName());
                outputStream.writeObject(method.getParameterTypes());
                outputStream.writeObject(args);

                inputStream = new ObjectInputStream(socket.getInputStream());
                object = inputStream.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (socket != null) socket.close();
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
            }
            return object;
        });
    }
}