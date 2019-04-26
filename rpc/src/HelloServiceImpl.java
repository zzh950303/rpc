public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String string) {
        System.out.println(string+"2");
        return string;
    }

    @Override
    public String sayBye() {
        System.out.println("bye bye2");
        return "bye bye";
    }
}
