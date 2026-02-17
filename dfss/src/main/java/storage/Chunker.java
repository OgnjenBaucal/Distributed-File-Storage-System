package storage;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.StorageException;

public class Chunker {
    private static final Logger LOGGER = LoggerFactory.getLogger(Chunker.class.getName());
    private static final int CHUNK_SIZE = 1048576; // 1 MB

    public static List<byte[]> split(String filePath) throws StorageException {
        List<byte[]> chunksList = new ArrayList<>();
        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(filePath))) {
            LOGGER.info("Splitting file: {}", filePath);

            byte[] chunk = new byte[CHUNK_SIZE];
            int data = input.read(chunk, 0, CHUNK_SIZE);
            while (data != -1) {
                chunksList.add(Arrays.copyOf(chunk, data));
                data = input.read(chunk, 0, CHUNK_SIZE);
            }
        } 
        catch (FileNotFoundException e) {
            LOGGER.error("File not found: {}", filePath);
            throw new StorageException("File not found", e);
        } 
        catch (IOException e) {
            LOGGER.error("Error during splitting file: {}", filePath);
            throw new StorageException("Error during spliting file: " + filePath, e);
        }
        LOGGER.debug("File split into {} chunks.", chunksList.size());
        LOGGER.info("File split succesfully.");
        return chunksList;
    }
}
