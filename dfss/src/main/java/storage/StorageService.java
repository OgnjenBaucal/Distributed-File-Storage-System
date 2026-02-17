package storage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.OutputStream;

import exceptions.HashingException;
import exceptions.StorageException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageService  {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class.getName());
    private static final String DESTINATION_PATH = "C:\\Users\\ognje\\Documents\\Programiranje\\Projekti\\Distributed File Storage System\\dfss\\storage\\recovered\\";

    public static String put(String filePath) throws StorageException, HashingException {
        String[] tmp = filePath.split("\\\\");
        String fileName = tmp[tmp.length-1];

        LOGGER.info("Uploading {}", fileName);

        // Splitting into chunks
        List<byte[]> chunks = Chunker.split(filePath);
        
        // Hashing the chunks
        List<String> hashedChunks = new ArrayList<>();
        for (byte[] chunk : chunks) 
            hashedChunks.add(HashUtil.hashSHA256(chunk));
        
        // Making File Manifest
        FileManifest file = new FileManifest(fileName, hashedChunks);
        String fileID = file.getFileID();

        // Check if fileID exists
        if (ManifestStorage.exists(fileID)) {
            LOGGER.warn("File already exists: {}", fileID);
            return fileID;
        }

        // Store the chunks
        LOGGER.info("Storing chunks...");
        for (int i = 0; i < chunks.size(); i++) 
            ChunkStorage.store(hashedChunks.get(i), chunks.get(i));

        LOGGER.info("Chunks stored successfully");
        
        LOGGER.info("Storing file manifest...");
        // Store the file manifest
        ManifestStorage.store(file, fileID);
        LOGGER.info("File stored successfully, fileID: {}", fileID);

        return fileID;
    }

    public static void get(String fileID) throws StorageException, HashingException {
        LOGGER.info("Gettting file: {}", fileID);

        if (!ManifestStorage.exists(fileID)) {
            LOGGER.error("File ID not found: {}", fileID);
            throw new StorageException("FileID not found");
        }

        LOGGER.info("File ID found");
        FileManifest fileManifest = ManifestStorage.get(fileID);
        LOGGER.info("File manifest loaded");

        if (!fileID.equals(fileManifest.getFileID())) {
            LOGGER.error("Data corrupted: File Manifest ID doesn't match File ID");
            throw new StorageException("Files corrupted");
        }

        LOGGER.info("Loading chunks...");
        List<byte[]> chunks = new ArrayList<>();
        for (String hash : fileManifest.getChunkHashes()) {
            chunks.add(ChunkStorage.load(hash));
        }
        LOGGER.info("Chunks loaded successfully");

        LOGGER.info("Reconstructing file...");
        try (OutputStream output = new FileOutputStream(new File(DESTINATION_PATH + fileManifest.getFileName()))) {
            for (byte[] chunk : chunks) {
                output.write(chunk);
            }
        } catch (IOException e) {
            LOGGER.error("Failed file reconstruction");
            throw new StorageException("Unable to reconstruct file", e);
        }
        LOGGER.info("File reconstructed: {}", fileManifest.getFileName());
    }
}
