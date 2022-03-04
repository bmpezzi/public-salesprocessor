package com.bmpezzi.salesprocessor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Enviroment data holder: process enviroment setup, making all resources available for file processing.
 *
 * @author bmpezzi
 */
@Service
public class Environments {
    private Logger logger = LoggerFactory.getLogger("Environment");

    private static final String HOMEPATH = "user.home";
    private static final String DATA_IN = "/data/in";
    private static final String DATA_OUT = "/data/out";
    private static final String DATA_PROCESSED = "/data/processed";

    private String base;
    private Path inputPath;
    private Path outputPath;
    private Path processedPath;

    /**
     * Initializes the following directories:
     * - INPUT
     * - OUTPUT
     * - PROCESSED
     */
    public void init() {
        try {
            initInputPath();
            initOutputPath();
            initProcessedPath();
        } catch (Exception e) {
            exit(e.getMessage());
        }
    }

    /**
     * Initializes INPUT directory.
     */
    private void initInputPath() {
        base = System.getProperty(HOMEPATH);
        if (null == base) {
            exit("Undefined environment variable 'user.home'");
        }
        String in = base + DATA_IN;
        inputPath = Paths.get(in);

        if (!inputPath.toFile().exists()) {
            exit("Does not exist: " + in);
        } else if (!inputPath.toFile().isDirectory()) {
            exit("Not a directory: " + in);
        } else if (!inputPath.toFile().canRead()) {
            exit("Could not read: " + in);
        }
        logger.info("Input Path: {}", inputPath);
    }

    /**
     * Initializes OUTPUT directory.
     */
    private void initOutputPath() {
        String out = base + DATA_OUT;
        outputPath = createDirectory(out);
        logger.info("Output Path: {}", outputPath);
    }

    /**
     * Initializes PROCESSED directory.
     */
    private void initProcessedPath() {
        String processed = base + DATA_PROCESSED;
        processedPath = createDirectory(processed);
        logger.info("Processed Path: {}", processedPath);
    }

    /**
     * Creates a directory for a given path.
     *
     * @param path Path that will create a directory.
     * @return A directory that corresponds to a given path.
     */
    private Path createDirectory(String path) {
        Path directory = Paths.get(path);
        if (!directory.toFile().exists()) {
            if (!directory.toFile().mkdirs()) {
                throw new RuntimeException("Could not create: " + path);
            }
        } else if (!directory.toFile().isDirectory()) {
            throw new RuntimeException("Not a directory: " + path);
        }
        return directory;
    }

    /**
     * Log message before exit
     * @param message Error message.
     */
    private void exit(String message) {
        logger.error(message);
        System.exit(1);
    }

    /**
     * Getter for attribute inputPath
     * @return
     */
    public Path getInputPath() {
        return inputPath;
    }

    /**
     * Getter for attribute outputPath
     * @return
     */
    public Path getOutputPath() {
        return outputPath;
    }

    /**
     * Getter for attribute processedPath
     * @return
     */
    public Path getProcessedPath() {
        return processedPath;
    }
}
