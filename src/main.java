import server.TelnetServer;

public class main {
    private static TelnetServer telnetServer;

    public static void main(String[] args) {
        telnetServer = TelnetServer.getInstance();
        if (telnetServer != null) {
            telnetServer.runServer();
        } else {
            System.err.println("Connect fail!!!");
        }
    }
}
