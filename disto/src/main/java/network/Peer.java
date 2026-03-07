package network;

public class Peer {
    private String ipAdress;
    private int port;

    public Peer(String ip, int p) {
        this.ipAdress = ip;
        this.port = p;
    }
    public String toString() {
        return ipAdress + ":" + port;
    }
}
