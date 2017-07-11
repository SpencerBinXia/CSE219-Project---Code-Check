package cc.workspace;

import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import static djf.ui.AppGUI.CLASS_BORDERED_PANE;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import cc.CodeCheckApp;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import properties_manager.PropertiesManager;
import static cc.CodeCheckProp.CAPTION_PROMPT_TEXT;
import static cc.CodeCheckProp.CURRENT_HEIGHT_PROMPT_TEXT;
import static cc.CodeCheckProp.CURRENT_WIDTH_PROMPT_TEXT;
import static cc.CodeCheckProp.FILE_NAME_COLUMN_TEXT;
import static cc.CodeCheckProp.FILE_NAME_PROMPT_TEXT;
import static cc.CodeCheckProp.ORIGINAL_HEIGHT_PROMPT_TEXT;
import static cc.CodeCheckProp.ORIGINAL_WIDTH_PROMPT_TEXT;
import static cc.CodeCheckProp.PATH_PROMPT_TEXT;
import static cc.CodeCheckProp.UPDATE_BUTTON_TEXT;
import cc.data.Slide;
import cc.data.CodeCheckData;
import static cc.style.CodeCheckStyle.CLASS_EDIT_BUTTON;
import static cc.style.CodeCheckStyle.CLASS_PROMPT_LABEL;
import static cc.style.CodeCheckStyle.CLASS_SLIDES_TABLE;
import static cc.style.CodeCheckStyle.CLASS_UPDATE_BUTTON;
import static cc.CodeCheckProp.HOME_BUTTON_TEXT;
import static cc.CodeCheckProp.PREV_BUTTON_TEXT;
import static cc.CodeCheckProp.NEXT_BUTTON_TEXT;
import static cc.CodeCheckProp.STEP_2_TEXT;
import static cc.CodeCheckProp.STEP_2_DESC_TEXT;
import static cc.style.CodeCheckStyle.CLASS_EDIT_TEXT_FIELD;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/**
 * This class serves as the workspace component for the TA Manager
 * application. It provides all the user interface controls in 
 * the workspace area.
 * 
 * @author Richard McKenna
 */
public class CodeCheckWorkspace extends AppWorkspaceComponent {
    // THIS PROVIDES US WITH ACCESS TO THE APP COMPONENTS
    CodeCheckApp app;

    // THIS PROVIDES RESPONSES TO INTERACTIONS WITH THIS WORKSPACE
    CodeCheckController controller;

    // CHECKS THE CURRENT STEP OF THE GUI.
    int currentStep = 1;
    
// BUTTONS ADDED TO APPGUI TOOLBAR
    Button renameButton;
    Button aboutButton;
    
    // NOTE THAT EVERY CONTROL IS PUT IN A BOX TO HELP WITH ALIGNMENT
    HBox controlStepToolbar;
    Button homeButton;
    Button prevButton;
    Button nextButton;
    
    // FOR THE SLIDES TABLE
    GridPane filePane;
    Label stepLabel;
    Label stepDescLabel;
    Button removeButton;
    Button refreshButton;
    Button viewButton;
    ScrollPane slidesTableScrollPane;
    TableView<Slide> slidesTableView;
    TableColumn<Slide, StringProperty> tableColumn;

    // THE EDIT PANE
    GridPane outputPane;
    Label progressLabel;
    ProgressBar progress;
    ScrollPane outputBoxPane;   
    Button actionButton1;
    Button actionButton2;
    
    // STEP 4 FILE TYPES
    Label typeLabel;
    CheckBox java;
    CheckBox js;
    CheckBox cpp;
    CheckBox cs;
    CheckBox other;
    TextField otherField;
    
    /**
     * The constructor initializes the user interface for the
     * workspace area of the application.
     */
    public CodeCheckWorkspace(CodeCheckApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // LAYOUT THE APP
        initLayout1();
        
        // HOOK UP THE CONTROLLERS
        initControllers();
        
        // AND INIT THE STYLE FOR THE WORKSPACE
        initStyle1();
    }
    
    public void initLayout1() {
        // WE'LL USE THIS TO GET UI TEXT
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // FIRST MAKE ALL THE COMPONENTS
        renameButton = app.getGUI().initChildButton(app.getGUI().getFileToolbar(),  "Rename",  "Rename the current Code Check",   false);
        aboutButton = app.getGUI().initChildButton(app.getGUI().getFileToolbar(),  "About",  "About the Code Check Application",   false);       
        controlStepToolbar = new HBox();
        homeButton = new Button(props.getProperty(HOME_BUTTON_TEXT));
        prevButton = new Button(props.getProperty(PREV_BUTTON_TEXT));
        nextButton = new Button(props.getProperty(NEXT_BUTTON_TEXT));
        slidesTableScrollPane = new ScrollPane();
        slidesTableView = new TableView();
        tableColumn = new TableColumn(props.getProperty(FILE_NAME_COLUMN_TEXT));
        outputPane = new GridPane();
        filePane = new GridPane();
        stepLabel = new Label(props.getProperty(FILE_NAME_PROMPT_TEXT));
        stepDescLabel = new Label(props.getProperty(PATH_PROMPT_TEXT));
        removeButton = new Button(props.getProperty(CAPTION_PROMPT_TEXT));
        refreshButton = new Button(props.getProperty(ORIGINAL_WIDTH_PROMPT_TEXT));
        viewButton = new Button(props.getProperty(ORIGINAL_HEIGHT_PROMPT_TEXT));
        progressLabel = new Label(props.getProperty(CURRENT_WIDTH_PROMPT_TEXT));
        progress = new ProgressBar();
        outputBoxPane = new ScrollPane();
        actionButton1 = new Button(props.getProperty(CURRENT_HEIGHT_PROMPT_TEXT));
        actionButton2 = new Button(props.getProperty(UPDATE_BUTTON_TEXT));
        java = new CheckBox(".java");
        js = new CheckBox(".js");
        cpp = new CheckBox(".c, .h, .cpp");
        cs = new CheckBox(".cs");
        other = new CheckBox();
        otherField = new TextField();
        slidesTableScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        outputBoxPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        
        // ARRANGE THE TABLE
        slidesTableView.getColumns().add(tableColumn);
        tableColumn.prefWidthProperty().bind(slidesTableView.widthProperty().divide(1));
        // HOOK UP THE TABLE TO THE DATA
        CodeCheckData data = (CodeCheckData)app.getDataComponent();
        ObservableList<Slide> model = data.getSlides();
        slidesTableView.setItems(model);
        
        // THEM ORGANIZE THEM
        controlStepToolbar.getChildren().add(homeButton);
        controlStepToolbar.getChildren().add(prevButton);
        controlStepToolbar.getChildren().add(nextButton);
        slidesTableScrollPane.setContent(slidesTableView);
        filePane.add(stepLabel, 0, 0);
        filePane.add(stepDescLabel, 0, 1);
        filePane.add(slidesTableScrollPane, 0, 2);
        filePane.add(removeButton, 0, 4);
        filePane.add(refreshButton, 1, 4);
        filePane.add(viewButton, 2, 4);
        filePane.add(java, 0, 5);
        filePane.add(js, 1, 5);
        filePane.add(cpp, 0, 6);
        filePane.add(cs, 1, 6);
        filePane.add(other, 0, 7);
        filePane.add(otherField, 1, 7);
        outputPane.add(progressLabel, 0, 0);
        outputPane.add(progress, 1, 0);
        outputPane.add(actionButton1, 0, 1);
        outputPane.add(actionButton2, 1, 1);
        outputPane.add(outputBoxPane, 0, 3);
        
        // DISABLE THE DISPLAY TEXT FIELDS
        java.setVisible(false);
        js.setVisible(false);
        cpp.setVisible(false);
        cs.setVisible(false);
        other.setVisible(false);
        otherField.setVisible(false);
        actionButton2.setVisible(false);
        homeButton.setDisable(true);
        prevButton.setDisable(true);
        
        // AND THEN PUT EVERYTHING INSIDE THE WORKSPACE
        app.getGUI().getTopToolbarPane().getChildren().add(controlStepToolbar);
        BorderPane workspaceBorderPane = new BorderPane();
        workspaceBorderPane.setLeft(filePane);
        slidesTableScrollPane.setFitToWidth(true);
        slidesTableScrollPane.setFitToHeight(true);
        outputBoxPane.setFitToWidth(true);
        outputBoxPane.setFitToHeight(true);
        workspaceBorderPane.setCenter(outputPane);
        
        // AND SET THIS AS THE WORKSPACE PANE
        workspace = workspaceBorderPane;
    }

    public void setStep1(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        stepLabel.setText(props.getProperty(FILE_NAME_PROMPT_TEXT));
        stepDescLabel.setText(props.getProperty(PATH_PROMPT_TEXT));
        progressLabel.setText(props.getProperty(CURRENT_WIDTH_PROMPT_TEXT));
        actionButton1.setText(props.getProperty(CURRENT_HEIGHT_PROMPT_TEXT));
        homeButton.setDisable(true);
        prevButton.setDisable(true);
        nextButton.setDisable(false);
        java.setVisible(false);
        js.setVisible(false);
        cpp.setVisible(false);
        cs.setVisible(false);
        other.setVisible(false);
        otherField.setVisible(false);
        actionButton2.setVisible(false);       
    }
    
    public void setStep2(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        stepLabel.setText(props.getProperty(STEP_2_TEXT));
        stepDescLabel.setText(props.getProperty(STEP_2_DESC_TEXT));
        progressLabel.setText("Rename Progress");
        actionButton1.setText("Rename");
        homeButton.setDisable(false);
        prevButton.setDisable(false);
        actionButton2.setVisible(false);        
    }
    
    public void setStep3(){
        stepLabel.setText("Step 3: Unzip Student Submissions");
        stepDescLabel.setText("Select student submissions and click Unzip.");
        progressLabel.setText("Unzip Progress");
        actionButton1.setText("Unzip");
        java.setVisible(false);
        js.setVisible(false);
        cpp.setVisible(false);
        cs.setVisible(false);
        other.setVisible(false);
        otherField.setVisible(false);
        actionButton2.setVisible(false);       
    }
    
    public void setStep4(){
        stepLabel.setText("Step 4: Extract Source Code");
        stepDescLabel.setText("Select students and click Extract Code");
        progressLabel.setText("Code Progress");
        actionButton1.setText("Extract Code");
        nextButton.setDisable(false);
        java.setVisible(true);
        js.setVisible(true);
        cpp.setVisible(true);
        cs.setVisible(true);
        other.setVisible(true);
        otherField.setVisible(true);
        actionButton2.setVisible(false);       
    }
    
    public void setStep5(){
        stepLabel.setText("Step 5: Code Check");
        stepDescLabel.setText("Select students and click Code Check");
        progressLabel.setText("Check Progress");
        actionButton1.setText("Code Check");
        nextButton.setDisable(true);
        java.setVisible(false);
        js.setVisible(false);
        cpp.setVisible(false);
        cs.setVisible(false);
        other.setVisible(false);
        otherField.setVisible(false);
        actionButton2.setVisible(true);
    }
    
    private void initControllers() {
        // NOW LET'S SETUP THE EVENT HANDLING
        controller = new CodeCheckController(app);

       /** addAllImagesInDirectoryButton.setOnAction(e->{
            controller.handleAddAllImagesInDirectory();
        });**/
       
       renameButton.setOnAction(e->{
           controller.handleRenameButton();
       });
       
       aboutButton.setOnAction(e->{
           controller.handleAboutButton();
       });
       
       homeButton.setOnAction(e->{
           currentStep = controller.handleHome(currentStep);
       });
       
       prevButton.setOnAction(e->{
           currentStep = controller.handlePrev(currentStep);
       });
       
       nextButton.setOnAction(e->{
           currentStep = controller.handleNext(currentStep);
       });
       

    }
    
    
    // WE'LL PROVIDE AN ACCESSOR METHOD FOR EACH VISIBLE COMPONENT
    // IN CASE A CONTROLLER OR STYLE CLASS NEEDS TO CHANGE IT
    
    public void initStyle1() {

        controlStepToolbar.getStyleClass().add(CLASS_BORDERED_PANE);
        homeButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        prevButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        nextButton.getStyleClass().add(CLASS_EDIT_BUTTON);

        // THE SLIDES TABLE
        slidesTableView.getStyleClass().add(CLASS_SLIDES_TABLE);
        for (TableColumn tc : slidesTableView.getColumns())
            tc.getStyleClass().add(CLASS_SLIDES_TABLE);
        
        filePane.getStyleClass().add(CLASS_BORDERED_PANE);
        outputPane.getStyleClass().add(CLASS_BORDERED_PANE);
        stepLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        stepDescLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        progressLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        outputBoxPane.getStyleClass().add(CLASS_SLIDES_TABLE);
        removeButton.getStyleClass().add(CLASS_UPDATE_BUTTON);
        refreshButton.getStyleClass().add(CLASS_UPDATE_BUTTON);
        viewButton.getStyleClass().add(CLASS_UPDATE_BUTTON);
        actionButton1.getStyleClass().add(CLASS_UPDATE_BUTTON);
        actionButton2.getStyleClass().add(CLASS_UPDATE_BUTTON);
        otherField.getStyleClass().add(CLASS_EDIT_TEXT_FIELD);

    }

    @Override
    public void resetWorkspace() {

    }
    
    @Override
    public void reloadWorkspace(AppDataComponent dataComponent) {

    }

}
