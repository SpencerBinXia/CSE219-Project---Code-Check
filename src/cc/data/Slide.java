package cc.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This data class represents a single slide in a slide show. Note that
 * it has information regarding the path to the file, the caption for
 * the slide, and sizing information.
 */
public class Slide {
    private StringProperty fileNameProperty;
    private StringProperty pathProperty;
    private StringProperty captionProperty;
    private IntegerProperty originalWidthProperty;
    private IntegerProperty originalHeightProperty;
    private IntegerProperty currentWidthProperty;
    private IntegerProperty currentHeightProperty;
    
    public Slide(   String initFileName,
                    String initPath, 
                    String initCaption,
                    int initOriginalWidth,
                    int initOriginalHeight) {
        fileNameProperty = new SimpleStringProperty(initFileName);
        pathProperty = new SimpleStringProperty(initPath);
        captionProperty = new SimpleStringProperty(initCaption);
        originalWidthProperty = new SimpleIntegerProperty(initOriginalWidth);
        originalHeightProperty = new SimpleIntegerProperty(initOriginalHeight);
        currentWidthProperty = new SimpleIntegerProperty(initOriginalWidth);
        currentHeightProperty = new SimpleIntegerProperty(initOriginalHeight);
    }
    
    // ACCESSORS AND MUTATORS
    
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
    
    public StringProperty getCaptionProperty() {
        return captionProperty;
    }
    public String getCaption() {
        return captionProperty.getValue();
    }
    public void setCaption(String initCaption) {
        captionProperty.setValue(initCaption);
    }
    
    public IntegerProperty getOriginalWidthProperty() {
        return originalWidthProperty;
    }
    public Integer getOriginalWidth() {
        return originalWidthProperty.getValue();
    }
    public void setOriginalWidth(Integer initOriginalWidth) {
        originalWidthProperty.setValue(initOriginalWidth);
    }
    
    public IntegerProperty getOriginalHeightProperty() {
        return originalHeightProperty;
    }
    public Integer getOriginalHeight() {
        return originalHeightProperty.getValue();
    }
    public void setOriginalHeight(Integer initOriginalHeight) {
        originalHeightProperty.setValue(initOriginalHeight);
    }
    
    public IntegerProperty getCurrentWidthProperty() {
        return currentWidthProperty;
    }
    public Integer getCurrentWidth() {
        return currentWidthProperty.getValue();
    }
    public void setCurrentWidth(Integer initCurrentWidth) {
        currentWidthProperty.setValue(initCurrentWidth);
    }
    
    public IntegerProperty getCurrentHeightProperty() {
        return currentHeightProperty;
    }
    public Integer getCurrentHeight() {
        return currentHeightProperty.getValue();
    }
    public void setCurrentHeight(Integer initCurrentHeight) {
        currentHeightProperty.setValue(initCurrentHeight);
    }
}
