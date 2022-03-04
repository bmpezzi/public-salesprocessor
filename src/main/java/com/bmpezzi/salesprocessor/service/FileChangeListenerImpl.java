package com.bmpezzi.salesprocessor.service;

import com.bmpezzi.salesprocessor.pojo.OutputPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Set;

@Service
public class FileChangeListenerImpl implements FileChangeListener {
    private Logger logger = LoggerFactory.getLogger("FileChangeListenerImpl");

    private FileSystemWatcher fileSystemWatcher;

    private Environments environment;

    private FileDigester fileDigester;

    /**
     * Instantiantes a FileChangeListener to wacth for changes at ${HOMEPATH}\data\in
     * @param environment
     * @param fileDigester
     */
    @Autowired
    public FileChangeListenerImpl(Environments environment, FileDigester fileDigester) {
        this.environment = environment;
        this.fileDigester = fileDigester;

        environment.init();

        fileSystemWatcher = new FileSystemWatcher();
        fileSystemWatcher.addSourceDirectory(environment.getInputPath().toFile());
        fileSystemWatcher.addListener(this);
        fileSystemWatcher.start();
    }

    /**
     * Digests the file created.
     * @param type input file type to be digested.
     * @param inputFile input file to be digested.
     */
    private void digestFile(ChangedFile.Type type, Path inputFile) {
        if (ChangedFile.Type.ADD.compareTo(type) == 0) {
            logger.info("Processing: {}\n", inputFile);
            OutputPojo output = fileDigester.extract(inputFile);
            fileDigester.compile(environment.getOutputPath(), inputFile, output);
            fileDigester.dispose(environment.getProcessedPath(), inputFile);
        }
    }

    /**
     * Method Override
     * @param changeSet
     */
    @Override
    public void onChange(Set<ChangedFiles> changeSet) {
        changeSet.forEach(changedFiles ->
                changedFiles.getFiles().forEach(changedFile ->
                        digestFile(changedFile.getType(), changedFile.getFile().toPath())
                )
        );
    }
}
