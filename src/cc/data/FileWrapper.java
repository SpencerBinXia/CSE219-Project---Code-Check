package cc.data;

import java.io.File;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This data class represents a single slide in a slide show. Note that
 * it has information regarding the path to the file, the caption for
 * the slide, and sizing information.
 */
public class FileWrapper {
    private File file;
    private StringProperty fileNameProperty;
    private StringProperty pathProperty;
    
    public FileWrapper(  File file, String initFileName,
                    String initPath) 
    {
        this.file = file;
        fileNameProperty = new SimpleStringProperty(initFileName);
        pathProperty = new SimpleStringProperty(initPath);
    }
    
    // ACCESSORS AND MUTATORS
    
    public File getFile(){
        return file;
    }
    public StringProperty getFileNameProperty() {
        return fileNameProperty;
    }
    public String getFileName() {
        return fileNameProperty.getValue();
    }
    public void setFileName(String initFileName) {
        fileNameProperty.setValue(initFileName);
    }
    
    public StringProperty getPathProperty() {
        return pathProperty;
    }
    public String getPath() {
        return pathProperty.getValue();
    }
    public void setPath(String initPath) {
        pathProperty.setValue(initPath);
    }
    
}
