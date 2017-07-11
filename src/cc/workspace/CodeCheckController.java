package cc.workspace;

import djf.ui.AppMessageDialogSingleton;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import properties_manager.PropertiesManager;
import cc.CodeCheckApp;
import static cc.CodeCheckProp.APP_PATH_WORK;
import static cc.CodeCheckProp.INVALID_IMAGE_PATH_MESSAGE;
import static cc.CodeCheckProp.INVALID_IMAGE_PATH_TITLE;
import cc.data.CodeCheckData;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;

/**
 * This class provides responses to all workspace interactions, meaning
 * interactions with the application controls not including the file
 * toolbar.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public class CodeCheckController {
    // THE APP PROVIDES ACCESS TO OTHER COMPONENTS AS NEEDED
    CodeCheckApp app;
    
    //Tracks the current step that the application is on.
    
    /**
     * Constructor, note that the app must already be constructed.
     */
    public CodeCheckController(CodeCheckApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
    }
    
    public int handleHome(int step){
       CodeCheckWorkspace gui = (CodeCheckWorkspace)app.getWorkspaceComponent();
       step = 1;
       gui.setStep1();
       return step;
    }

    public int handlePrev(int step){
        CodeCheckWorkspace gui = (CodeCheckWorkspace)app.getWorkspaceComponent();
        step--;
        if (step == 1){
            gui.setStep1();
        }
        else if (step == 2){
            gui.setStep2();            
        }
        else if (step == 3){
            gui.setStep3();
        }
        else if (step == 4){
            gui.setStep4();
        }
        return step;
    }
    
    public int handleNext(int step){
        CodeCheckWorkspace gui = (CodeCheckWorkspace)app.getWorkspaceComponent();
        step++;
        if (step == 2){
            gui.setStep2();
        }
        else if (step == 3){
            gui.setStep3();
        }
        else if (step == 4){
            gui.setStep4();
        }
        else if (step == 5){
            gui.setStep5();
        }
        return step;
    }
    
    public void handleRenameButton(){
        app.getGUI().setTitle();
    }
    
    public void handleAboutButton(){
        Dialog aboutPopup = new Dialog();
        aboutPopup.getDialogPane().getButtonTypes().add(new ButtonType("Got it!", ButtonData.CANCEL_CLOSE));
        aboutPopup.setHeaderText("Code Check organizes Blackboard submissions for your grading convenience!");
        aboutPopup.setTitle("About Code Check");
        aboutPopup.setContentText("Code Check application. Developer: Spencer Xia. Copyright Year: 2017.");
        aboutPopup.show();
    }
    
    // CONTROLLER METHOD THAT HANDLES ADDING A DIRECTORY OF IMAGES
    public void handleAddAllImagesInDirectory() {
        try {
            // ASK THE USER TO SELECT A DIRECTORY
            DirectoryChooser dirChooser = new DirectoryChooser();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            dirChooser.setInitialDirectory(new File(props.getProperty(APP_PATH_WORK)));
            File dir = dirChooser.showDialog(app.getGUI().getWindow());
            if (dir != null) {
                File[] files = dir.listFiles();
                for (File f : files) {
                    String fileName = f.getName();
                    if (fileName.toLowerCase().endsWith(".png") ||
                            fileName.toLowerCase().endsWith(".jpg") ||
                            fileName.toLowerCase().endsWith(".gif")) {
                        String path = f.getPath();
                        String caption = "";
                        Image slideShowImage = loadImage(path);
                        int originalWidth = (int)slideShowImage.getWidth();
                        int originalHeight = (int)slideShowImage.getHeight();
                        CodeCheckData data = (CodeCheckData)app.getDataComponent();
                        data.addSlide(fileName, path, caption, originalWidth, originalHeight);
                    }
                }
            }
        }
        catch(MalformedURLException murle) {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String title = props.getProperty(INVALID_IMAGE_PATH_TITLE);
            String message = props.getProperty(INVALID_IMAGE_PATH_MESSAGE);
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(title, message);
        }
    }
    
    // THIS HELPER METHOD LOADS AN IMAGE SO WE CAN SEE IT'S SIZE
    private Image loadImage(String imagePath) throws MalformedURLException {
	File file = new File(imagePath);
	URL fileURL = file.toURI().toURL();
	Image image = new Image(fileURL.toExternalForm());
	return image;
    }
}