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
import cc.data.FileWrapper;
import cc.data.CodeCheckData;
import static cc.style.CodeCheckStyle.CLASS_EDIT_BUTTON;
import static cc.style.CodeCheckStyle.CLASS_PROMPT_LABEL;
import static cc.style.CodeCheckStyle.CLASS_SLIDES_TABLE;
import static cc.style.CodeCheckStyle.CLASS_UPDATE_BUTTON;
import static cc.style.CodeCheckStyle.CLASS_NORMAL_PANE;
import static cc.CodeCheckProp.HOME_BUTTON_TEXT;
import static cc.CodeCheckProp.PREV_BUTTON_TEXT;
import static cc.CodeCheckProp.NEXT_BUTTON_TEXT;
import static cc.CodeCheckProp.STEP_2_TEXT;
import static cc.CodeCheckProp.STEP_2_DESC_TEXT;
import static cc.style.CodeCheckStyle.CLASS_EDIT_TEXT_FIELD;
import static cc.style.CodeCheckStyle.CLASS_DESC_LABEL;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

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
    FlowPane filePane;
    Label stepLabel;
    Label stepDescLabel;
    Button removeButton;
    Button refreshButton;
    Button viewButton;
    ScrollPane slidesTableScrollPane;
    TableView<FileWrapper> slidesTableView;
    TableColumn<FileWrapper, StringProperty> tableColumn;

    // THE EDIT PANE
    
    TextFlow feedback;
    String outputText;
    StringProperty outputTextProperty;
    FlowPane outputPane;
    Label progressLabel;
    ProgressBar progress;
    ProgressIndicator indicator;
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
    String otherType;
    
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
        feedback = new TextFlow();
        outputPane = new FlowPane();
        filePane = new FlowPane();
        stepLabel = new Label(props.getProperty(FILE_NAME_PROMPT_TEXT));
        stepDescLabel = new Label(props.getProperty(PATH_PROMPT_TEXT));
        removeButton = new Button(props.getProperty(CAPTION_PROMPT_TEXT));
        refreshButton = new Button(props.getProperty(ORIGINAL_WIDTH_PROMPT_TEXT));
        viewButton = new Button(props.getProperty(ORIGINAL_HEIGHT_PROMPT_TEXT));
        progressLabel = new Label(props.getProperty(CURRENT_WIDTH_PROMPT_TEXT));
        progress = new ProgressBar();
        indicator = new ProgressIndicator(0);
        outputBoxPane = new ScrollPane(feedback);
        actionButton1 = new Button(props.getProperty(CURRENT_HEIGHT_PROMPT_TEXT));
        actionButton2 = new Button(props.getProperty(UPDATE_BUTTON_TEXT));
        java = new CheckBox(".java");
        js = new CheckBox(".js");
        cpp = new CheckBox(".c, .h, .cpp");
        cs = new CheckBox(".cs");
        other = new CheckBox();
        otherField = new TextField();
        otherField.setText(otherType);
        slidesTableScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        outputBoxPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        
        // ARRANGE THE TABLE
        slidesTableView.getColumns().add(tableColumn);
        tableColumn.prefWidthProperty().bind(slidesTableView.widthProperty().divide(1));
        // HOOK UP THE TABLE TO THE DATA
        tableColumn.setCellValueFactory(
        new PropertyValueFactory<FileWrapper,StringProperty>("fileName")
        );
        
        // THEM ORGANIZE THEM
        controlStepToolbar.getChildren().add(homeButton);
        controlStepToolbar.getChildren().add(prevButton);
        controlStepToolbar.getChildren().add(nextButton);
        controlStepToolbar.setAlignment(Pos.CENTER_RIGHT);
        slidesTableScrollPane.setContent(slidesTableView);
        filePane.setMinWidth(704);
        filePane.setMaxWidth(704);
        filePane.setHgap(60);
        outputPane.setMinWidth(576);
        outputPane.setMaxWidth(576);
        slidesTableScrollPane.setMinHeight(300);
        slidesTableScrollPane.setMaxHeight(300);
        slidesTableScrollPane.setMinWidth(670);
        slidesTableScrollPane.setMaxWidth(670);
        GridPane checkmarks = new GridPane();
        HBox otherRow = new HBox();
        otherRow.getChildren().addAll(other, otherField);
        checkmarks.setHgap(150);
        checkmarks.add(java, 0, 0);
        checkmarks.add(js, 0, 1);
        checkmarks.add(cpp, 1, 0);
        checkmarks.add(cs, 1, 1);
        checkmarks.add(otherRow,0, 2);
        filePane.getChildren().addAll(stepLabel, stepDescLabel, slidesTableScrollPane, removeButton, refreshButton, viewButton, checkmarks);
        outputBoxPane.setMinWidth(530);
        outputBoxPane.setMaxWidth(530);
        outputBoxPane.setMinHeight(350);
        outputBoxPane.setMaxHeight(350);
        feedback.setMinWidth(530);
        feedback.setMaxWidth(530);
        feedback.setMinHeight(350);
        feedback.setMaxHeight(350);
        outputPane.setHgap(100);
        outputPane.getChildren().addAll(progressLabel, progress, actionButton1, actionButton2, outputBoxPane);
        
        // DISABLE THE DISPLAY TEXT FIELDS
        java.setVisible(false);
        js.setVisible(false);
        cpp.setVisible(false);
        cs.setVisible(false);
        other.setVisible(false);
        otherField.setVisible(false);
        actionButton2.setVisible(false);
        actionButton2.setDisable(true);
        homeButton.setDisable(true);
        prevButton.setDisable(true);
        java.setSelected(true);
        
        // AND THEN PUT EVERYTHING INSIDE THE WORKSPACE
        controlStepToolbar.setMinWidth(450);
        controlStepToolbar.setMaxWidth(450);
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
        
        //INITIALIZE TABLE
    }

    public void setStep1(){
        CodeCheckData data = (CodeCheckData)app.getDataComponent();   
        ObservableList<FileWrapper> model = data.getBlackboard();
        slidesTableView.getItems().clear();   
        slidesTableView.getItems().addAll(model);
        slidesTableView.getColumns().get(0).setVisible(false);
        slidesTableView.getColumns().get(0).setVisible(true);
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        stepLabel.setText(props.getProperty(FILE_NAME_PROMPT_TEXT));
        stepDescLabel.setText(props.getProperty(PATH_PROMPT_TEXT));
        progressLabel.setText(props.getProperty(CURRENT_WIDTH_PROMPT_TEXT));
        actionButton1.setText(props.getProperty(CURRENT_HEIGHT_PROMPT_TEXT));
        tableColumn.setText(props.getProperty(FILE_NAME_COLUMN_TEXT));
        homeButton.setDisable(true);
        prevButton.setDisable(true);
        nextButton.setDisable(false);
        removeButton.setDisable(true);
        viewButton.setDisable(true);
        actionButton1.setDisable(true);
        java.setVisible(false);
        js.setVisible(false);
        cpp.setVisible(false);
        cs.setVisible(false);
        other.setVisible(false);
        otherField.setVisible(false);
        actionButton2.setVisible(false);       
    }
    
    public void setStep2(){
        CodeCheckData data = (CodeCheckData)app.getDataComponent();
        ObservableList<FileWrapper> model = data.getSubmissions();
        data.resetData();
        slidesTableView.getItems().clear();      
        slidesTableView.getItems().addAll(model);
        slidesTableView.getColumns().get(0).setVisible(false);
        slidesTableView.getColumns().get(0).setVisible(true);
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        stepLabel.setText(props.getProperty(STEP_2_TEXT));
        stepDescLabel.setText(props.getProperty(STEP_2_DESC_TEXT));
        tableColumn.setText("Student Submissions");
        progressLabel.setText("Rename Progress");
        actionButton1.setText("Rename");
        homeButton.setDisable(false);
        prevButton.setDisable(false);
        removeButton.setDisable(true);
        viewButton.setDisable(true);
        actionButton1.setDisable(true);
        actionButton2.setVisible(false);        
    }
    
    public void setStep3(){
        CodeCheckData data = (CodeCheckData)app.getDataComponent();  
        ObservableList<FileWrapper> model = data.getSubmissions();
        data.resetData();
        slidesTableView.getItems().clear();      
        slidesTableView.getItems().addAll(model);
        slidesTableView.getColumns().get(0).setVisible(false);
        slidesTableView.getColumns().get(0).setVisible(true);      
        stepLabel.setText("Step 3: Unzip Student Submissions");
        stepDescLabel.setText("Select student submissions and click Unzip.");
        progressLabel.setText("Unzip Progress");
        tableColumn.setText("Student ZIP FIles");
        actionButton1.setText("Unzip");
        removeButton.setDisable(true);
        viewButton.setDisable(true);
        actionButton1.setDisable(true);
        java.setVisible(false);
        js.setVisible(false);
        cpp.setVisible(false);
        cs.setVisible(false);
        other.setVisible(false);
        otherField.setVisible(false);
        actionButton2.setVisible(false);       
    }
    
    public void setStep4(){
        CodeCheckData data = (CodeCheckData)app.getDataComponent();  
        ObservableList<FileWrapper> model = data.getProjects();
        data.resetData();
        slidesTableView.getItems().clear();      
        slidesTableView.getItems().addAll(model);
        slidesTableView.getColumns().get(0).setVisible(false);
        slidesTableView.getColumns().get(0).setVisible(true);  
        stepLabel.setText("Step 4: Extract Source Code");
        stepDescLabel.setText("Select students and click Extract Code.");
        progressLabel.setText("Code Progress");
        actionButton1.setText("Extract Code");
        actionButton2.setText("");
        tableColumn.setText("Student Work Directories");
        nextButton.setDisable(false);
        removeButton.setDisable(true);
        viewButton.setDisable(true);
        actionButton1.setDisable(true);
        java.setVisible(true);
        js.setVisible(true);
        cpp.setVisible(true);
        cs.setVisible(true);
        other.setVisible(true);
        otherField.setVisible(true);
        actionButton2.setVisible(false);       
    }
    
    public void setStep5(){
        CodeCheckData data = (CodeCheckData)app.getDataComponent();  
        ObservableList<FileWrapper> model = data.getCode();
        data.resetData();
        slidesTableView.getItems().clear();      
        slidesTableView.getItems().addAll(model);
        slidesTableView.getColumns().get(0).setVisible(false);
        slidesTableView.getColumns().get(0).setVisible(true);          
        stepLabel.setText("Step 5: Code Check");
        stepDescLabel.setText("Select students and click Code Check.");
        progressLabel.setText("Check Progress");
        actionButton1.setText("Code Check");
        actionButton2.setText("View Results");
        tableColumn.setText("Student Work");
        removeButton.setDisable(true);
        viewButton.setDisable(true);
        actionButton1.setDisable(true);
        nextButton.setDisable(true);
        java.setVisible(false);
        js.setVisible(false);
        cpp.setVisible(false);
        cs.setVisible(false);
        other.setVisible(false);
        otherField.setVisible(false);
        actionButton2.setVisible(true);
    }
    
    public void updateButtons()
    {
        removeButton.setDisable(false);
        viewButton.setDisable(false);
        actionButton1.setDisable(false);
    }
    
    public void deselectButtons()
    {
        removeButton.setDisable(true);
        viewButton.setDisable(true);
        actionButton1.setDisable(true);
    }
    
    private void initControllers() {
        // NOW LET'S SETUP THE EVENT HANDLING
        controller = new CodeCheckController(app);
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
       
        refreshButton.setOnAction(e->{
            controller.handleRefreshButton(currentStep);
            deselectButtons();
        });
        
        actionButton2.setOnAction(e->{
            controller.handleResultsButton();
        });
        
        slidesTableView.getSelectionModel().selectedItemProperty().addListener(x->{  
            updateButtons();
            viewButton.setOnAction(v->{
               controller.handleViewButton(); 
            });
            removeButton.setOnAction(r->{
                controller.handleRemoveButton(currentStep);
            });
            actionButton1.setOnAction(e->{
            if (currentStep == 1){
                controller.handleExtractBlackboard();
            }
            if (currentStep == 2){
                controller.handleRenameSubmissions(currentStep);
            }
            if (currentStep == 3){
                controller.handleExtractSubmissions();
            }
            if (currentStep == 4){
                controller.handleExtractCode(java, js, cpp, cs, other, otherType);
            }
            if (currentStep == 5){
                       Hyperlink link = new Hyperlink();
                         link.setText("https://phpcodechecker.com/");
                        Text plagiarism = new Text("Code Check complete. Student Plagiarism Check Results can be found at:");
                        handleCodeCheck();
                        feedback.getChildren().addAll(plagiarism, link);
                         link.setOnAction(l->{
                            controller.handleResultsButton();
                        });
            }
        });
        });        
    }
    
    // WE'LL PROVIDE AN ACCESSOR METHOD FOR EACH VISIBLE COMPONENT
    // IN CASE A CONTROLLER OR STYLE CLASS NEEDS TO CHANGE IT
    
    public void initStyle1() {

        controlStepToolbar.getStyleClass().add(CLASS_BORDERED_PANE);
        renameButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        aboutButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        homeButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        prevButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        nextButton.getStyleClass().add(CLASS_EDIT_BUTTON);

        // THE SLIDES TABLE
        slidesTableView.getStyleClass().add(CLASS_SLIDES_TABLE);
        for (TableColumn tc : slidesTableView.getColumns())
            tc.getStyleClass().add(CLASS_SLIDES_TABLE);
        
        filePane.getStyleClass().add(CLASS_NORMAL_PANE);
        outputPane.getStyleClass().add(CLASS_NORMAL_PANE);
        stepLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        stepDescLabel.getStyleClass().add(CLASS_DESC_LABEL);
        progressLabel.getStyleClass().add(CLASS_DESC_LABEL);
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
        slidesTableView.getItems().removeAll();
        slidesTableView.getItems().clear();              
        currentStep = 1;
        setStep2();
        setStep1();
    }
    
    @Override
    public void reloadWorkspace(AppDataComponent dataComponent) {
        slidesTableView.getItems().removeAll();
        slidesTableView.getItems().clear();           
        currentStep = 1;        
        setStep2();
        setStep1();
    }
    
    public void handleCodeCheck(){
                            double max = 200;
                           double perc;
                        for (int i = 0; i < 200; i++) {
                            System.out.println(i);
                            perc = i/max;
                                  
                            progress.setProgress(perc);
                            indicator.setProgress(perc);
                                }
                            }


}


