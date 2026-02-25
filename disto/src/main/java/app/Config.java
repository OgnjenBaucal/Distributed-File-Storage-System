package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.StorageException;

public class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class.getName());

    private static Path APP_DIR;
    private static Path STORAGE_DIR;
    private static Path CHUNKS_DIR;
    private static Path MANIFESTS_DIR;
    private static Path DESTINATION_DIR;

    public static Path getAppPath() { return APP_DIR; }
    public static Path getStoragePath() { return STORAGE_DIR; }
    public static Path getChunksPath() { return CHUNKS_DIR; }
    public static Path getManifestsPath() { return MANIFESTS_DIR; }
    public static Path getDestinationPath() { return DESTINATION_DIR; }

    public static void init() throws StorageException {
        //String os = System.getProperty("os.name").toLowerCase();
        //if (os.contains("win")) {
        //    APP_DIR = Paths.get(System.getenv("APPDATA"), "distro");
        //} else {
        //    APP_DIR = Paths.get(System.getProperty("user.home"), "distro");
        //}

        LOGGER.info("Configuring folders...");
        try {
            APP_DIR = Paths.get(System.getProperty("user.home"), "distro");
            Files.createDirectories(APP_DIR);

            STORAGE_DIR = APP_DIR.resolve("storage");
            Files.createDirectories(STORAGE_DIR);

            CHUNKS_DIR = STORAGE_DIR.resolve("chunks");
            Files.createDirectories(CHUNKS_DIR);

            MANIFESTS_DIR = STORAGE_DIR.resolve("manifests");
            Files.createDirectories(MANIFESTS_DIR);

            DESTINATION_DIR = STORAGE_DIR.resolve("recovered");
            Files.createDirectories(DESTINATION_DIR);

        } catch (IOException e) {
            LOGGER.error("Configuration failed");
            throw new StorageException("Failed creating/finding directory", e);
        }
        LOGGER.info("Configuring successfull");

        
    }
}
