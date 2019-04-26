import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;

public class ServerTask implements Runnable {
    private Socket socket = null;
    private HashMap<String, Class> serviceRegistry;

    public ServerTask(Socket socket, HashMap<String, Class> serviceRegistry) {
        this.socket = socket;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            String serviceName = inputStream.readUTF();
            String serviceMethod = inputStream.readUTF();
            Class<?>[] parameterTypes = (Class<?>[]) inputStream.readObject();
            Object[] arguments = (Object[]) inputStream.readObject();

            Class serviceClass = serviceRegistry.get(serviceName);
            if (serviceClass == null) {
                throw new ClassNotFoundException(serviceName + " not found");
            }
            Method method = serviceClass.getMethod(serviceMethod, parameterTypes);
            Object object = method.invoke(serviceClass.newInstance(), arguments);
            //
            // 3.将执行结果反序列化，通过socket发送给客户端
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(object);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
