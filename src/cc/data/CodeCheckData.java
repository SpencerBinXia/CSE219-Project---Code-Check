package cc.data;

import javafx.collections.ObservableList;
import djf.components.AppDataComponent;
import javafx.collections.FXCollections;
import cc.CodeCheckApp;

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
    ObservableList<Slide> slides;

    /**
     * This constructor will setup the required data structures for use.
     * 
     * @param initApp The application this data manager belongs to. 
     */
    public CodeCheckData(CodeCheckApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        
        // MAKE THE SLIDES MODEL
        slides = FXCollections.observableArrayList();
    }
    
    // ACCESSOR METHOD
    public ObservableList<Slide> getSlides() {
        return slides;
    }
    
    /**
     * Called each time new work is created or loaded, it resets all data
     * and data structures such that they can be used for new values.
     */
    @Override
    public void resetData() {

    }

    // FOR ADDING A SLIDE WHEN THERE ISN'T A CUSTOM SIZE
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
}