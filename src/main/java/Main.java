public class Main {

    public static void main(String[] args) {
        int clients = 100;
        int taxiDrivers = 10;
        MessageCreationType type = MessageCreationType.FROM_XML;

        Test test = new Test(clients, taxiDrivers, type);
        test.start();
    }
}
