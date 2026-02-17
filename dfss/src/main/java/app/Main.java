package app;

import exceptions.HashingException;
import exceptions.StorageException;
import storage.StorageService;

public class Main {
    public static void main(String[] args) {
        getTest();
    }
    public static void putTest() {
        String path = "C:\\Users\\ognje\\Desktop\\slika.jpg";
        try {
            String ID = StorageService.put(path);
            System.out.println("FileID: " + ID);
        } catch (HashingException e) {
            System.out.println(e.getMessage());
        } catch (StorageException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void getTest() {
        String fileID = "dedd96b25e36b15c0b046bdb7dcd4b7a6057c6e2b0e32ab4ef92979ad4433253";
        try { StorageService.get(fileID); }
        catch (HashingException e) {
            System.out.println(e.getMessage());
        } catch (StorageException e) {
            System.out.println(e.getMessage());
        }
    }
}