package storage;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.Config;
import exceptions.StorageException;

public class ChunkStorage {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChunkStorage.class.getName());

    public static void store(String hash, byte[] data) throws StorageException {
        //LOGGER.info("Storing chunk: {}", hash);
        String path = Config.getChunksPath().toString();
        try {
            OutputStream output = new FileOutputStream(path + "\\" + hash);
            output.write(data);
            output.close();
        } 
        catch (FileNotFoundException e) {
            LOGGER.error("Path non-existent or not accesable: {}", path);
            throw new StorageException("Unable to store chunk: Path not found", e);
        } 
        catch (SecurityException e) {
            LOGGER.error("Permission for storing chunk denied: {}", path);
            throw new StorageException("Unable to store chunk: permission denied", e);
        } 
        catch (IOException e) {
            LOGGER.error("IO error while storing chunk: {}", path + "\\" + hash);
            throw new StorageException("Unable to store chunk: storage error", e);
        }
        //LOGGER.info("Chunk stored successfully at: {}", PATH + hash);
    }
    public static byte[] load(String hash) throws StorageException {
        String path = Config.getChunksPath().toString();
        byte[] data = null;
        try (FileInputStream input = new FileInputStream(path + "\\" + hash)) {
            //LOGGER.info("Loading chunk: {}", hash);
            data = input.readAllBytes();
        } 
        catch (FileNotFoundException e) {
            LOGGER.error("Chunk non-existent or incorrect path: {}", path + "\\" + hash);
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
