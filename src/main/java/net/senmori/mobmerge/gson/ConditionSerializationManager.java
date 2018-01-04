package net.senmori.mobmerge.gson;

import java.io.File;

/**
 * This class handles the serialization/deserialization of conditions
 */
public final class ConditionSerializationManager {
    private final File baseDirectory;

    public ConditionSerializationManager(File baseDir) {
        this.baseDirectory = baseDir;
    }

    /**
     * Get the base directory this serialization manager will create files in.
     * @return
     */
    public File getBaseDirectory() {
        return baseDirectory;
    }
}
