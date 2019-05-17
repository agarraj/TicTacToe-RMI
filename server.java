import static java.rmi.Naming.rebind;

public class server {
    public static void main(String[] args) throws Exception{
        sysImplementation obj = new sysImplementation();
        rebind("tictac",obj);
        System.out.println("Server Started");
    }
}



//
//public class server {
//    public static void main(String[] args) throws Exception {
//        implementation obj = new implementation();
//        rebind("ADD",obj);
//        System.out.println("Server started");
//    }
//}
