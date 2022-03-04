package com.bmpezzi.salesprocessor.service;

import com.bmpezzi.salesprocessor.pojo.OutputPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Input file digester: extracts and parses data from input file and disposes it finishing the operation.
 *
 * @author bmpezzi
 */
@Service
public class FileDigester {
    private Logger logger = LoggerFactory.getLogger("FileDigester");

    private static final String DAT_EXTENSION = ".dat";
    private static final String DONE_DAT_EXTENSION = ".done.dat";
    private static final int FIRST_INDEX = 0;
    private static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

    @Autowired
    private EntityParser entityParser;

    /**
     * Extracts data from input file
     * @param inputFile input file with data to be extracted.
     * @return OutputPojo raw data.
     */
    OutputPojo extract(Path inputFile){
        OutputPojo output = null;
        try {
            if (null != inputFile  && inputFile.toString().endsWith(".dat")) {
                logger.info("Extracting data from: {}", inputFile);
                List<String> lines = Files.readAllLines(inputFile);
                entityParser.start();
                for (String line: lines) {
                    entityParser.parse(line);
                }
                output = entityParser.buildOutput();
            } else {
                logger.error("Aborting extraction: {} is not a .DAT file\n", inputFile);
            }
        } catch (IOException ioe) {
            logger.error("extract", ioe);
        }
        return output;
    }

    /**
     * Compiles output raw data to an output file.
     * @param outputPath OutputPojo path directory.
     * @param inputFile Input file.
     * @param output OutputPojo raw data.
     */
    void compile(Path outputPath, Path inputFile, OutputPojo output){
        if(null != outputPath && null != inputFile && null != output){
            logger.info("Compiling output: {}", output);
            File outputFile = createDoneFile(outputPath, inputFile);
            try(PrintWriter writer = new PrintWriter(outputFile)) {
                writer.println(output.getTotalOfCustomersMessage());
                writer.println(output.getTotalOfSalesmenMessage());
                writer.println(output.getMostExpensiveSaleIdMessage());
                writer.println(output.getWorstSalesmanNameMessage());
            } catch (FileNotFoundException fnfe) {
                logger.error(fnfe.getMessage());
            }
        }
    }

    /**
     * Dispose input file to PROCESSED path.
     * @param processedPath Processed path.
     * @param inputFile Input file.
     */
    void dispose(Path processedPath, Path inputFile){
        try {
            if(null != inputFile) {
                File oldInputFile = inputFile.toFile();
                File newFile = createFile(
                        processedPath, inputFile.getFileName().toString());
                inputFile.toFile().renameTo(newFile);
                Files.deleteIfExists(oldInputFile.toPath());
                logger.info("Disposing file: {}", newFile);
            }
        } catch (IOException e) {
            logger.error("dispose", e);
        }
    }

    /**
     * Create done file: the output response from file extraction.
     * @param outputPath OutputPojo path directory.
     * @param inputFile Input file.
     * @return Done file.
     */
    private File createDoneFile(Path outputPath, Path inputFile){
        String outputFileName = inputFile.getFileName().toString();
        int lastIndex = outputFileName.lastIndexOf(DAT_EXTENSION);

        StringBuilder fileName =
                new StringBuilder(outputFileName.substring(FIRST_INDEX, lastIndex))
                        .append(DONE_DAT_EXTENSION);
        return createFile(outputPath, fileName.toString());
    }

    /**
     * Create file denoted by a destination path and a given name.
     * @param destinationPath Destination path.
     * @param fileName File name
     * @return A new file.
     */
    private File createFile(Path destinationPath, String fileName){
        StringBuilder fullFileName =
                new StringBuilder(destinationPath.toAbsolutePath().toString())
                        .append(FILE_SEPARATOR)
                        .append(fileName);
        logger.info("New file created: {}", fullFileName);
        return new File(fullFileName.toString());
    }
}
