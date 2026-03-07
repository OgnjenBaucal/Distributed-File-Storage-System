package app;

import java.util.Scanner;
import exceptions.*;

public class Main {
    public static void main(String args[]) {
        boolean running = true;
        try { 
            Config.init(); 
            Disto.start();
        } catch (StorageException | NetworkException e) {
            System.out.println(e.getMessage());
            running = false;
        }

        Scanner input = new Scanner(System.in);
        while(running) {
            System.out.print("> ");
            String command = input.nextLine();
            args = command.split(" ");

            if (args.length == 0) listCommands();
            else if (args.length == 1) {
                if (args[0].equals("help")) listCommands();
                else if (args[0].equals("exit")) running = false;
                else if (args[0].equals("peers")) printPeers();
                else if (args[0].equals("port")) getPort();
                else listCommands();
            }
            else if (args.length == 2) {
                if (args[0].equals("store")) put(args[1]);
                else if (args[0].equals("get")) get(args[1]);
                else if (args[0].equals("port")) changePort(args[1]);
                else if (args[0].equals("add")) addPeer(args[1]);
                else if (args[0].equals("remove")) removePeer(args[1]);
                else listCommands();
            }
            else listCommands();
            System.out.println();
        }
        System.out.println("Shutting down...");
        input.close();
        Disto.stop();
    }

    public static void listCommands() {
        System.out.println();
        System.out.println("You didn't specify any existing command, these are the available commands: ");
        System.out.println("> store <filePath>    | Stores the file at the specified path into the storage system");
        System.out.println("> get <fileID>        | Retrieves the file with the specified ID into the recovered folder");
        System.out.println("> peers               | Lists all peers");
        System.out.println("> port                | Retrieves the current port being used");
        System.out.println("> port <newPort>      | Changes the port");
        System.out.println("> add <ip:port>       | Adds peer with specified ip and port to the network");
        System.out.println("> remove <ip:port>    | Removes peer with specified ip and port from the network");
        System.out.println("> help                | Lists all available commands");
        System.out.println("> exit                | Shut down");
    }
    public static void put(String path) {
        try {
            String ID = Disto.store(path);
            System.out.println("File stored successfully");
            System.out.println("FileID: " + ID);
        } catch (HashingException e) {
            System.out.println(e.getMessage());
        } catch (StorageException e) {
            System.out.println(e.getMessage());
        } catch (NetworkException e) {
            System.out.println(e.getMessage());
        } catch (EncryptionException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void get(String fileID) {
        try { 
            Disto.get(fileID); 
            System.out.println("File recovered successfully");
        }
        catch (HashingException e) {
            System.out.println(e.getMessage());
        } catch (StorageException e) {
            System.out.println(e.getMessage());
        } catch (NetworkException e) {
            System.out.println(e.getMessage());
        } catch (EncryptionException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void printPeers() {

    }
    public static void getPort() {

    }
    public static void changePort(String newPort) {

    }
    public static void addPeer(String peer) {

    }
    public static void removePeer(String peer) {

    }
}