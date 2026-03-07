package app;

import storage.*;
import network.*;
import exceptions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Disto {
    private static final Logger LOGGER = LoggerFactory.getLogger(Disto.class.getName());
    private static Server server;
    private static Client client;

    public static void start() throws NetworkException {

    }
    public static void stop() {

    }
    public static String store(String path) throws StorageException, HashingException, NetworkException, EncryptionException {
        String[] tmp = path.split("\\\\");
        String fileName = tmp[tmp.length-1];

        LOGGER.info("Uploading {}", fileName);

        // Splitting into chunks
        List<byte[]> chunks = Chunker.split(path);
        
        // Hashing the chunks
        List<String> chunkHashes = new ArrayList<>();
        for (byte[] chunk : chunks) 
            chunkHashes.add(HashUtil.hashSHA256(chunk));
        
        // Making File Manifest
        FileManifest fileManifest = new FileManifest(fileName, chunkHashes);
        String fileID = fileManifest.getFileID();

        // Check if fileID exists
        if (ManifestStorage.exists(fileID)) {
            LOGGER.warn("File already exists: {}", fileID);
            return fileID;
        }

        // Encrypt chunks
        LOGGER.info("Encrypting chunks...");
        SecretKey key = AESUtil.generateKey();
        List<GCMParameterSpec> chunkIVs = new ArrayList<>();
        for (int i = 0; i < chunks.size(); i++)
            chunkIVs.add(AESUtil.generateIV());
        
        List<byte[]> encryptedChunks = AESUtil.encryptChunks(chunks, key, chunkIVs);
        LOGGER.info("Chunks encrypted successfully");
        
        // Store the chunks
        LOGGER.info("Storing chunks...");
        for (int i = 0; i < encryptedChunks.size(); i++) 
            ChunkStorage.store(chunkHashes.get(i), encryptedChunks.get(i));

        LOGGER.info("Chunks stored successfully");
        
        // Store the file manifest
        LOGGER.info("Storing file manifest...");
        fileManifest.setKey(AESUtil.keyToString(key));
        List<String> chunkStringIVs = new ArrayList<>();
        for (GCMParameterSpec iv : chunkIVs)
            chunkStringIVs.add(AESUtil.ivToString(iv));
        fileManifest.setChunkIVs(chunkStringIVs);

        ManifestStorage.store(fileManifest, fileID);
        LOGGER.info("File stored successfully, fileID: {}", fileID);

        return fileID;
    }
    public static void get(String fileID) throws StorageException, HashingException, NetworkException, EncryptionException {
        LOGGER.info("Gettting file: {}", fileID);

        if (!ManifestStorage.exists(fileID)) {
            LOGGER.error("File ID not found: {}", fileID);
            throw new StorageException("FileID not found");
        }

        LOGGER.info("File ID found");
        FileManifest fileManifest = ManifestStorage.get(fileID);
        LOGGER.info("File manifest loaded");

        if (!fileManifest.verifyID(fileID)) {
            LOGGER.error("Data corrupted: File Manifest ID doesn't match File ID");
            throw new StorageException("File corrupted");
        }

        LOGGER.info("Loading chunks...");
        List<byte[]> chunks = new ArrayList<>();
        List<String> chunkHashes = fileManifest.getChunkHashes();
        for (String hash : chunkHashes)
            chunks.add(ChunkStorage.load(hash));
        
        LOGGER.info("Chunks loaded successfully");

        LOGGER.info("Decrypting chunks");
        SecretKey key = AESUtil.stringToKey(fileManifest.getKey());
        List<GCMParameterSpec> chunkIVs = new ArrayList<>();
        for (String iv : fileManifest.getChunkIVs())
            chunkIVs.add(AESUtil.stringToIv(iv));

        List<byte[]> decryptedChunks = AESUtil.decryptChunks(chunks, key, chunkIVs);
        for (int i = 0; i < decryptedChunks.size(); i++) {
            if (!chunkHashes.get(i).equals(HashUtil.hashSHA256(decryptedChunks.get(i)))) {
                LOGGER.error("Data corrupted: Chunks corrupted");
                throw new StorageException("Chunks corrupted");
            }
        }

        LOGGER.info("Reconstructing file...");
        try (OutputStream output = new FileOutputStream(new File(Config.getDestinationPath() + "\\" + fileManifest.getFileName()))) {
            for (byte[] chunk : decryptedChunks) {
                output.write(chunk);
            }
        } catch (IOException e) {
            LOGGER.error("Failed file reconstruction");
            throw new StorageException("Unable to reconstruct file", e);
        }
        LOGGER.info("File reconstructed: {}", fileManifest.getFileName());
    }

    private static void storeChunk() throws StorageException {

    }
    private static byte[] getChunk(String hash) throws StorageException {
        return null;
    }
}
