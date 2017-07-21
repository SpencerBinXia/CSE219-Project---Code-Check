package cc.workspace;

import cc.CodeCheckApp;
import cc.data.CodeCheckData;
import cc.data.FileWrapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import static cc.workspace.CodeCheckWorkspace.feedback;

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
        app.getGUI().renameTitle();
        feedback.getChildren().add(new Text("Project renamed to " + app.getGUI().getTitle() + "."));
    }
    
    public void handleAboutButton(){
        Dialog aboutPopup = new Dialog();
        aboutPopup.getDialogPane().getButtonTypes().add(new ButtonType("Got it!", ButtonData.CANCEL_CLOSE));
        aboutPopup.setHeaderText("Code Check organizes Blackboard submissions for your grading convenience!");
        aboutPopup.setTitle("About Code Check");
        aboutPopup.setContentText("Code Check application. Developer: Spencer Xia. Copyright Year: 2017.");
        aboutPopup.show();
    }
    
    public void handleRefreshButton(int step){
        CodeCheckData data = (CodeCheckData)app.getDataComponent();      
        data.resetData();
        if (step == 1){
            handleHome(1);
        }
        else if (step == 5){
            handleNext(step-1);
        }
        else{
            handlePrev(step+1);
        }
    }
    
    public void handleRemoveButton(int step){
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        FileWrapper selectedFile = workspace.slidesTableView.getSelectionModel().getSelectedItem();
        int selectedIndex = workspace.slidesTableView.getSelectionModel().getSelectedIndex();        
        
        // WE ONLY RESPOND IF IT'S A SELECTION
        if (selectedIndex >= 0) {
             Alert alert = new Alert(Alert.AlertType.WARNING, "Are you positive you want to remove this item?", ButtonType.YES, ButtonType.NO);
             alert.showAndWait();
             if (alert.getResult() == ButtonType.NO){
                 alert.close();
             }
             else if (alert.getResult() == ButtonType.YES){
                 CodeCheckData data = (CodeCheckData)app.getDataComponent();
                 File f = selectedFile.getFile();
                 feedback.getChildren().add(new Text(f.getName() + " removed."));
                 if (f.isDirectory() == true){
                     try
                     {
                     FileUtils.deleteDirectory(f);
                     }
                     catch (Exception e)
                     {                  
                     }
                 }
                 else
                 {
                 f.delete();
                 }
                 handleRefreshButton(step);
                 workspace.deselectButtons();
             }
        }        
    }
    
    public void handleViewButton(){
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        FileWrapper selectedFile = workspace.slidesTableView.getSelectionModel().getSelectedItem();
        int selectedIndex = workspace.slidesTableView.getSelectionModel().getSelectedIndex();
        VBox viewBox = new VBox(6);
        
        if (selectedIndex >= 0) {
            String result = "";
            File f = selectedFile.getFile();
            feedback.getChildren().add(new Text("Viewing inner files and directories of " + f.getName() + "."));
            if (f.isDirectory())
            {
                Collection<File> viewDir = FileUtils.listFiles(f, TrueFileFilter.TRUE,TrueFileFilter.INSTANCE);
                for (File innerFile : viewDir){
                    viewBox.getChildren().add(new Text(innerFile.getPath()));
                }
            }
            else
            { 
                try
                {
                ZipFile zipFile = new ZipFile(f);
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements()){
                    ZipEntry entry = entries.nextElement();
                    String zipName = entry.getName();
                    viewBox.getChildren().add(new Text(zipName));
                    //System.out.println(zipName);
                }
                zipFile.close();                    
                }
                 catch (Exception e)
                {
                    feedback.getChildren().add(new Text("Unsuccessful view attempt processed."));
                }
                }

        }
        
        Platform.runLater(new Runnable() {
        @Override
        public void run() {
         //Update your GUI here
        viewBox.setAlignment(Pos.CENTER_LEFT);
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(viewBox);
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setStyle("-fx-background-color: white");
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        viewBox.setStyle("-fx-background-color: white");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //scrollPane.setMinHeight(450);
       // scrollPane.setMaxHeight(450);
        //scrollPane.setMinWidth(300);
       // scrollPane.setMaxWidth(300);  

        Scene scene = new Scene(scrollPane);
         Stage primaryStage = new Stage();
         primaryStage.setScene(scene);
         primaryStage.setTitle("View ZIP File Contents");
         primaryStage.show();
         }
         });
        
            }
    
    public void handleExtractBlackboard() {
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        FileWrapper selectedFile = workspace.slidesTableView.getSelectionModel().getSelectedItem();
        int selectedIndex = workspace.slidesTableView.getSelectionModel().getSelectedIndex();
        
        if (selectedIndex >= 0){
        try
        {
        File f = selectedFile.getFile();
        FileInputStream fInput = new FileInputStream("assignment/blackboard/" + f.getName());
        ZipInputStream zis = new ZipInputStream(fInput);
        ZipEntry ze = zis.getNextEntry();
        byte[] buffer = new byte[2048];
        while (ze != null){
            String filename = ze.getName();
            feedback.getChildren().add(new Text(filename + " extracted to Submissions."));
            File newFile = new File("work/" + app.getGUI().getTitle() + "/Submissions/" + filename);
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0){
                fos.write(buffer, 0, len);
            }
            fos.close();
            zis.closeEntry();
            ze = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        fInput.close();
        }
        catch (Exception e)
        {
            feedback.getChildren().add(new Text("Unsuccessful extraction attempt processed."));
        }
        }
    }
    
    public void handleRenameSubmissions(int step){
        CodeCheckData data = (CodeCheckData)app.getDataComponent();   
        for (int i = 0;i < data.getSubmissions().size();i++){
        File f = data.getSubmissions().get(i).getFile();
        String name = f.getName();
        int underscoreIndex = name.indexOf('_');
        String subName = name.substring(underscoreIndex+1);
        underscoreIndex = subName.indexOf('_');
        int periodIndex = subName.indexOf('.');
        String deleteString = subName.substring(underscoreIndex, periodIndex);
        String realname = subName.replace(deleteString, "");
        feedback.getChildren().add(new Text(name + " renamed to " + realname + "."));
        File newf = new File("work/" + app.getGUI().getTitle() + "/Submissions/" + realname);
        f.renameTo(newf);
        }
        handleRefreshButton(step);
    }
    
    public void handleExtractSubmissions(){
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        FileWrapper selectedFile = workspace.slidesTableView.getSelectionModel().getSelectedItem();
        int selectedIndex = workspace.slidesTableView.getSelectionModel().getSelectedIndex();
        
        if (selectedIndex >= 0){
        try
        {
        File f = selectedFile.getFile();
        int periodIndex = f.getName().indexOf('.');
        String dirName = f.getName().substring(0, periodIndex);
        File studentDir = new File("work/" + app.getGUI().getTitle() + "/Projects/" + dirName);
        studentDir.mkdir();
        ZipFile zipFile = new ZipFile("work/" + app.getGUI().getTitle() + "/Submissions/" + f.getName());
        Enumeration<?> enu = zipFile.entries();
        byte[] buffer = new byte[2048];
        while (enu.hasMoreElements()){
            ZipEntry zipEntry = (ZipEntry) enu.nextElement();
            String filename = zipEntry.getName();
            feedback.getChildren().add(new Text(filename + " extracted to" + dirName + "in Projects."));
            File newFile = new File("work/" + app.getGUI().getTitle() + "/Projects/" + dirName + "/" + filename);
            if (filename.endsWith("/")){
                newFile.mkdirs();
                continue;
            }
            File parent = newFile.getParentFile();
           if (parent != null) {
                parent.mkdirs();
            }
            InputStream is = zipFile.getInputStream(zipEntry);
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = is.read(buffer)) >= 0){
                fos.write(buffer, 0, len);
            }
            is.close();
            fos.close();
        }
        zipFile.close();
        }
        catch (Exception e)
        {
            feedback.getChildren().add(new Text("Unsuccessful extraction attempt processed."));
        }
        }        
    }

  /**  public void handleExtractCode(CheckBox java, CheckBox js, CheckBox cpp, CheckBox cs, CheckBox other, String otherType){
        CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        FileWrapper selectedFile = workspace.slidesTableView.getSelectionModel().getSelectedItem();
        int selectedIndex = workspace.slidesTableView.getSelectionModel().getSelectedIndex();      
        
        if (selectedIndex >= 0){
        File f = selectedFile.getFile();
        File studentDir = new File("work/" + app.getGUI().getTitle() + "/Code/" + f.getName());
        studentDir.mkdir();        
        File[] files = new File("work/" + app.getGUI().getTitle() + "/Projects/" + f.getName()).listFiles();
        ArrayList<File> fileList = showFiles(files);  
        for (int i = 0;i < fileList.size();i++)
        {
            String filename = fileList.get(i).getName();
            int extIndex = filename.indexOf('.');
            String fileExt = filename.substring(extIndex);
            System.out.println("EXTENSION" + fileExt);
            if ((java.isSelected() && fileExt.equals(".java")) || (js.isSelected() && fileExt.equals(".js")) || (cpp.isSelected() && fileExt.equals(".cpp")) 
                || (cpp.isSelected() && fileExt.equals(".h")) || (cpp.isSelected() && fileExt.equals(".c")) || (cs.isSelected() && fileExt.equals(".cs") 
                || (other.isSelected() && fileExt.equals(otherType))))
            {
                System.out.println(fileList.get(i).getName());
                File source = fileList.get(i);
                File dest = new File("work/" + app.getGUI().getTitle() + "/Code/" + f.getName() + "/" + filename);
                try
                {
                 InputStream inStream = null;
                 OutputStream outStream = null;
                        inStream = new FileInputStream(source);
                        outStream = new FileOutputStream(dest);
                        byte[] buffer = new byte[1024];
                        int fileLength;
                        while ((fileLength = inStream.read(buffer)) > 0){                           
                        outStream.write(buffer, 0, fileLength );
                        }
                        inStream.close();
                        outStream.close();
                }
                catch (Exception e)
                {
                }
            }
        }        
    }
    }
    
    private ArrayList<File> showFiles(File[] files) {
    ArrayList<File> fileList = new ArrayList<File>();
    for (File file : files) {
        if (file.isDirectory()) 
        {
            showFiles(file.listFiles()); // Calls same method again.
        } 
        else 
        {
            System.out.println(file);
           fileList.add(file);
        }
    }
    return fileList;
    }**/
    
   public void handleExtractCode(CheckBox java, CheckBox js, CheckBox cpp, CheckBox cs, CheckBox other, String otherType){
         CodeCheckWorkspace workspace = (CodeCheckWorkspace)app.getWorkspaceComponent();
        FileWrapper selectedFile = workspace.slidesTableView.getSelectionModel().getSelectedItem();
        int selectedIndex = workspace.slidesTableView.getSelectionModel().getSelectedIndex();
        

        if (selectedIndex >= 0){
        try
        {
        File f = selectedFile.getFile();
        File studentDir = new File("work/" + app.getGUI().getTitle() + "/Code/" + f.getName());
        studentDir.mkdir();
        ZipFile zipFile = new ZipFile("work/" + app.getGUI().getTitle() + "/Submissions/" + f.getName() + ".zip");
        Enumeration<?> enu = zipFile.entries();
        byte[] buffer = new byte[2048];
        while (enu.hasMoreElements()){
            ZipEntry zipEntry = (ZipEntry) enu.nextElement();
            String filename = zipEntry.getName();
            try
            {
            int extIndex = filename.indexOf('.');
            String fileExt = filename.substring(extIndex);
            if ((java.isSelected() && fileExt.equals(".java")) || (js.isSelected() && fileExt.equals(".js")) || (cpp.isSelected() && fileExt.equals(".cpp")) 
                || (cpp.isSelected() && fileExt.equals(".h")) || (cpp.isSelected() && fileExt.equals(".c")) || (cs.isSelected() && fileExt.equals(".cs") 
                || (other.isSelected() && fileExt.equals(otherType))))
            {
            File newFile = new File("work/" + app.getGUI().getTitle() + "/Code/" + f.getName() + "/" + filename);
            feedback.getChildren().add(new Text(filename + " extracted to " + f.getName() + "in Code."));
            if (filename.endsWith("/")){
                newFile.mkdirs();
                continue;
            }
            File parent = newFile.getParentFile();
           if (parent != null) {
                parent.mkdirs();
            }
            InputStream is = zipFile.getInputStream(zipEntry);
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;

            while ((len = is.read(buffer)) >= 0){
                fos.write(buffer, 0, len);
            }
            is.close();
            fos.close();
            }
            else
            {             
            }
            }
            catch (Exception e)
            {             
            }
        }
        zipFile.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        }          
    }
    
   public void handleCodeCheck(){
   }
   
    public void handleResultsButton(){
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load("https://phpcodechecker.com/");
       
        Platform.runLater(new Runnable() {
        @Override
        public void run() {
         //Update your GUI here
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER_LEFT);

        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
	
        vbox.getChildren().add(hbox);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(browser);
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setStyle("-fx-background-color: white");
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        vbox.getChildren().add(browser);   


        Scene scene = new Scene(vbox);
         Stage primaryStage = new Stage();
         primaryStage.setScene(scene);
         primaryStage.setTitle("Code Check Results");
         primaryStage.show();
         }
         });
    }

    }