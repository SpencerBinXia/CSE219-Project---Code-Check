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
    ObservableList<FileWrapper> projects;
    ObservableList<FileWrapper> code;

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
        projects = FXCollections.observableArrayList();
        code = FXCollections.observableArrayList();
    }
    
    // ACCESSOR METHOD
    public ObservableList<FileWrapper> getBlackboard() {
        return blackboard;
    }
    
    public ObservableList<FileWrapper> getSubmissions() {
        return submissions;
    }
    
    public ObservableList<FileWrapper> getProjects() {
        return projects;
    }    
    
    public ObservableList<FileWrapper> getCode(){
        return code;
    }
    /**
     * Called each time new work is created or loaded, it resets all data
     * and data structures such that they can be used for new values.
     */
    @Override
    public void resetData() {
        blackboard.clear();
        submissions.clear();
        projects.clear();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        File blackDir = new File(props.getProperty(APP_PATH_WORK));
        File[] listOfBlack = blackDir.listFiles();
        
        for (int i = 0; i < listOfBlack.length; i++) {
        if (listOfBlack[i].isFile()) {
            FileWrapper newFile = new FileWrapper(listOfBlack[i], listOfBlack[i].getName(), listOfBlack[i].getPath());
            blackboard.add(newFile);
        }
        }
        
        if (app.getGUI().getTitle() != null){
        File subDir = new File("work/" + app.getGUI().getTitle() + "/Submissions/");
        File[] listOfSub = subDir.listFiles();     
        
        for (int i = 0; i < listOfSub.length; i++) {
        if (listOfSub[i].isFile()) {
            FileWrapper newFile = new FileWrapper(listOfSub[i], listOfSub[i].getName(), listOfSub[i].getPath());
            submissions.add(newFile);
        }
        }
        
        File projDir = new File("work/" + app.getGUI().getTitle() + "/Projects/");
        File[] listOfProj = projDir.listFiles();
        
        for (int i = 0; i < listOfProj.length; i++) {
        if (listOfProj[i].isDirectory()) {
            FileWrapper newFile = new FileWrapper(listOfProj[i], listOfProj[i].getName(), listOfProj[i].getPath());
            projects.add(newFile);
        }
        }
        
        File codeDir = new File("work/" + app.getGUI().getTitle() + "/Code/");
        File[] listOfCode = codeDir.listFiles(); 
        
        for (int i = 0; i < listOfCode.length; i++) {
        if (listOfCode[i].isDirectory()) {
            FileWrapper newFile = new FileWrapper(listOfCode[i], listOfCode[i].getName(), listOfCode[i].getPath());
            code.add(newFile);
        }
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