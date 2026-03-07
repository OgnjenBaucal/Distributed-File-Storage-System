package storage;
import java.util.ArrayList;
import java.util.List;

import exceptions.HashingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class FileManifest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileManifest.class.getName());

    private String fileName;
    private String key;
    private List<String> chunkIVs;
    private List<String> chunkHashes;
    private String fileID = "";

    public FileManifest() {
        this.fileName = "";
        this.chunkHashes = new ArrayList<>();
        this.chunkIVs = new ArrayList<>();
    }
    public FileManifest(String fileName, List<String> chunks) throws HashingException {
        this.fileName = fileName;
        this.chunkHashes = chunks;
        constructFileID();
    }

    // Getter
    public String getFileName() { return this.fileName; }
    public String getKey() { return this.key; }
    public List<String> getChunkIVs() { return this.chunkIVs; }
    public List<String> getChunkHashes() { return this.chunkHashes; }
    @JsonIgnore
    public String getFileID() throws HashingException { 
        if (this.fileID.equals("")) constructFileID();
        return this.fileID; 
    }

    // Setter
    public void setFileName(String name) { this.fileName = name; }
    public void setKey(String key) { this.key = key; }
    public void setChunkIVs(List<String> ivs) { this.chunkIVs = ivs; }
    public void setChunkHashes(List<String> hashes) { chunkHashes = hashes; }

    public boolean verifyID(String ID) throws HashingException {
        if (this.fileID.equals("")) constructFileID();
        return this.fileID.equals(ID);
    }

    private void constructFileID() throws HashingException {
        StringBuilder sb = new StringBuilder();
        for (String hash : chunkHashes) 
            sb.append(hash);
    
        try { this.fileID = HashUtil.hashSHA256(sb.toString().getBytes()); } 
        catch (HashingException e) {
            LOGGER.error("Error while instancing the SHA-256 algorithm");
            throw e;
        }
    }
}
