package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.StorageException;
import exceptions.NetworkException;

public class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class.getName());

    private static Path APP_DIR;

    private static Path STORAGE_DIR;
    private static Path CHUNKS_DIR;
    private static Path MANIFESTS_DIR;
    private static Path DESTINATION_DIR;

    private static Path NETWORK_DIR;
    //private static Path CLIENT_DIR;
    //private static Path PEERS_DIR;

    public static Path getAppPath() { return APP_DIR; }
    public static Path getStoragePath() { return STORAGE_DIR; }
    public static Path getChunksPath() { return CHUNKS_DIR; }
    public static Path getManifestsPath() { return MANIFESTS_DIR; }
    public static Path getDestinationPath() { return DESTINATION_DIR; }

    public static Path getNetworkPath() { return NETWORK_DIR; }
    //public static Path getClientPath() { return CLIENT_DIR; }
    //public static Path getPeersPath() { return PEERS_DIR; }

    public static void init() throws StorageException {
        LOGGER.info("Configuring folders...");
        try {
            APP_DIR = Paths.get(System.getProperty("user.home"), "disto");
            Files.createDirectories(APP_DIR);

            STORAGE_DIR = APP_DIR.resolve("storage");
            Files.createDirectories(STORAGE_DIR);

            CHUNKS_DIR = STORAGE_DIR.resolve("chunks");
            Files.createDirectories(CHUNKS_DIR);

            MANIFESTS_DIR = STORAGE_DIR.resolve("manifests");
            Files.createDirectories(MANIFESTS_DIR);

            DESTINATION_DIR = STORAGE_DIR.resolve("recovered");
            Files.createDirectories(DESTINATION_DIR);

            NETWORK_DIR = APP_DIR.resolve("network");
            Files.createDirectories(NETWORK_DIR);

        } catch (IOException e) {
            LOGGER.error("Configuration failed");
            throw new StorageException("Failed creating/finding directory", e);
        }
        LOGGER.info("Configuring successfull");
    }
}
