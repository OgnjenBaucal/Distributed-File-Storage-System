package storage;
import java.util.ArrayList;
import java.util.List;

import exceptions.HashingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

class FileManifest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileManifest.class.getName());

    private String fileName;
    private List<String> chunkHashes;
    private String fileID = "";

    public FileManifest() {
        this.fileName = "";
        this.chunkHashes = new ArrayList<>();
    }

    public FileManifest(String fileName, List<String> chunks) throws HashingException {
        this.fileName = fileName;
        this.chunkHashes = chunks;
        constructFileID();
    }

    // Getter
    public String getFileName() { return this.fileName; }
    public List<String> getChunkHashes() { 
        List<String> copy = new ArrayList<>();
        for (String hash : chunkHashes)
            copy.add(hash);

        return copy;
    }
    @JsonIgnore
    public String getFileID() throws HashingException { 
        if (this.fileID == "") constructFileID();
        return this.fileID; 
    }

    // Setter
    public void setFileName(String name) { this.fileName = name; }
    public void setChunkHashes(List<String> hashes) { chunkHashes = hashes; }

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
