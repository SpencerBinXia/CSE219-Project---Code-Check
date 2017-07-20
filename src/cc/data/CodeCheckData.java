package cc.data;

import javafx.collections.ObservableList;
import djf.components.AppDataComponent;
import javafx.collections.FXCollections;
import cc.data.FileWrapper;
import static cc.CodeCheckProp.APP_PATH_WORK;
import static cc.CodeCheckProp.APP_PATH_SUB;
import cc.CodeCheckApp;
import java.io.File;
import properties_manager.PropertiesManager;

/**
 * This is the data component for CodeCheckApp. It has all the data needed
 * to be set by the user via the User Interface and file I/O can set and get
 * all the data from this object
 * 
 * @author Richard McKenna
 */
public class CodeCheckData implements AppDataComponent {

    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    CodeCheckApp app;

    // NOTE THAT THIS DATA STRUCTURE WILL DIRECTLY STORE THE
    // DATA IN THE ROWS OF THE TABLE VIEW
    ObservableList<FileWrapper> blackboard;
    ObservableList<FileWrapper> submissions;
    FileWrapper[] rawBlack;
    FileWrapper[] rawSub;

    /**
     * This constructor will setup the required data structures for use.
     * 
     * @param initApp The application this data manager belongs to. 
     */
    public CodeCheckData(CodeCheckApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        
        // MAKE THE SLIDES MODEL
        blackboard = FXCollections.observableArrayList();
        submissions = FXCollections.observableArrayList();
    }
    
    // ACCESSOR METHOD
    public ObservableList<FileWrapper> getBlackboard() {
        return blackboard;
    }
    
    public ObservableList<FileWrapper> getSubmissions() {
        return submissions;
    }
    /**
     * Called each time new work is created or loaded, it resets all data
     * and data structures such that they can be used for new values.
     */
    @Override
    public void resetData() {
        blackboard.clear();
        submissions.clear();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        File blackDir = new File(props.getProperty(APP_PATH_WORK));
        File[] listOfBlack = blackDir.listFiles();
        
        for (int i = 0; i < listOfBlack.length; i++) {
        if (listOfBlack[i].isFile()) {
            FileWrapper newFile = new FileWrapper(listOfBlack[i], listOfBlack[i].getName(), listOfBlack[i].getPath());
            blackboard.add(newFile);
        }
        }
        
        File subDir = new File(props.getProperty(APP_PATH_SUB));
        File[] listOfSub = subDir.listFiles();     
        
        for (int i = 0; i < listOfSub.length; i++) {
        if (listOfSub[i].isFile()) {
            FileWrapper newFile = new FileWrapper(listOfSub[i], listOfSub[i].getName(), listOfSub[i].getPath());
            submissions.add(newFile);
        }
        }       
    }

    // FOR ADDING A SLIDE WHEN THERE ISN'T A CUSTOM SIZE
    /**
    public void addSlide(String fileName, String path, String caption, int originalWidth, int originalHeight) {
        Slide slideToAdd = new Slide(fileName, path, caption, originalWidth, originalHeight);
        slides.add(slideToAdd);
    }

    // FOR ADDING A SLIDE WITH A CUSTOM SIZE
    public void addSlide(String fileName, String path, String caption, int originalWidth, int originalHeight, int currentWidth, int currentHeight) {
        Slide slideToAdd = new Slide(fileName, path, caption, originalWidth, originalHeight);
        slideToAdd.setCurrentWidth(currentWidth);
        slideToAdd.setCurrentHeight(currentHeight);
        slides.add(slideToAdd);
    }
    * **/
}