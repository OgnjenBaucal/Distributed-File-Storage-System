package app;

import exceptions.HashingException;
import exceptions.StorageException;
import storage.StorageService;

public class Main {
    public static void main(String[] args) {
        try { Config.init(); } 
        catch (StorageException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (args.length == 0) listCommands();
        else if (args.length == 1) {
            if (args[0].equals("help")) listCommands();
        }
        else if (args.length == 2) {
            if (args[0].equals("put")) put(args[1]);
            else if (args[0].equals("get")) get(args[1]);
        }
        else listCommands();
    }

    public static void listCommands() {
        System.out.println("\nYou didn't specify any existing command, these are the available commands: ");
        System.out.println("help                | Lists all available commands");
        System.out.println("put <filePath>      | Puts the file at the specified path into the storage system");
        System.out.println("get <fileID>        | Retrieves the file with the specified ID into the recovered folder");
        // System.out.println("peers               | Lists all peers");
        // System.out.println("port                | Retrieves the current port being used");
        // System.out.println("port <newPort>      | Changes the port");
        // System.out.println("add <ip:port>       | Adds peer with specified ip and port to the network");
        // System.out.println("remove <ip:port>    | Removes peer with specified ip and port from the network");
    }

    public static void put(String path) {
        try {
            String ID = StorageService.put(path);
            System.out.println("FileID: " + ID);
        } catch (HashingException e) {
            System.out.println(e.getMessage());
        } catch (StorageException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("File uploaded successfully");
    }

    public static void get(String fileID) {
        try { StorageService.get(fileID); }
        catch (HashingException e) {
            System.out.println(e.getMessage());
        } catch (StorageException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("File recovered successfully");
    }
}