package storage;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.StorageException;

public class ChunkStorage {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChunkStorage.class.getName());
    private static final String PATH = "C:\\Users\\ognje\\Documents\\Programiranje\\Projekti\\Distributed File Storage System\\dfss\\storage\\chunks\\";

    public static void store(String hash, byte[] data) throws StorageException {
        //LOGGER.info("Storing chunk: {}", hash);
        try {
            OutputStream output = new FileOutputStream(PATH + hash);
            output.write(data);
            output.close();
        } 
        catch (FileNotFoundException e) {
            LOGGER.error("Path non-existent or not accesable: {}", PATH);
            throw new StorageException("Unable to store chunk: Path not found", e);
        } 
        catch (SecurityException e) {
            LOGGER.error("Permission for storing chunk denied: {}", PATH);
            throw new StorageException("Unable to store chunk: permission denied", e);
        } 
        catch (IOException e) {
            LOGGER.error("IO error while storing chunk: {}", PATH + hash);
            throw new StorageException("Unable to store chunk: storage error", e);
        }
        //LOGGER.info("Chunk stored successfully at: {}", PATH + hash);
    }
    public static byte[] load(String hash) throws StorageException {
        byte[] data = null;
        try (FileInputStream input = new FileInputStream(PATH + hash)) {
            //LOGGER.info("Loading chunk: {}", hash);
            data = input.readAllBytes();
        } 
        catch (FileNotFoundException e) {
            LOGGER.error("Chunk non-existent or incorrect path: {}", PATH + hash);
            throw new StorageException("Unable to find chunk", e);
        } 
        catch (IOException e) {
            LOGGER.error("Failed reading chunk: {}", hash);
            throw new StorageException("Unable to load chunk", e);
        }
        //LOGGER.info("Chunk loaded successfully: {}", hash);
        return data;
    }
}
