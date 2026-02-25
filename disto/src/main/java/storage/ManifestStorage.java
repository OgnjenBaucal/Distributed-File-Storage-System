package storage;
import exceptions.StorageException;
import app.Config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManifestStorage {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManifestStorage.class.getName());

    public static void store(FileManifest file, String fileID) throws StorageException {
        String path = Config.getManifestsPath() + "\\" + fileID + ".json";
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter()
                  .writeValue(new File(path), file);
        }
        catch (Exception e) {
            LOGGER.error("Failed storing file manifest into json: {}", fileID);
            throw new StorageException("Unable to store file manifest", e);
        }
    }
    public static FileManifest get(String fileID) throws StorageException {
        String path = Config.getManifestsPath() + "\\" + fileID + ".json";
        ObjectMapper mapper = new ObjectMapper();
        try {
            FileManifest file = mapper.readValue(new File(path), FileManifest.class);
            return file;
        } catch (Exception e) {
            LOGGER.error("Failed loading file manifest: {}", fileID);
            throw new StorageException("Unable to load file manifest", e);
        }
    }
    public static boolean exists(String fileID) {
        Path path = Config.getManifestsPath().resolve(fileID + ".json");
        return Files.exists(path);
    }
}
