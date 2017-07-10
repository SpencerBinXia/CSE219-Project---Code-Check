package cc.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import cc.CodeCheckApp;
import cc.data.CodeCheckData;
import cc.data.Slide;

/**
 * This class serves as the file component for the Slideshow Creator App.
 * It provides all saving and loading services for the application.
 * 
 * @author Richard McKenna
 */
public class CodeCheckFiles implements AppFileComponent {
    // THIS IS THE APP ITSELF
    CodeCheckApp app;
    
    // THESE ARE USED FOR IDENTIFYING JSON TYPES
    static final String JSON_SLIDES = "slides";
    static final String JSON_SLIDE = "slide";
    static final String JSON_FILE_NAME = "file_name";
    static final String JSON_PATH = "path";
    static final String JSON_CAPTION = "caption";
    static final String JSON_ORIGINAL_WIDTH = "original_width";
    static final String JSON_ORIGINAL_HEIGHT = "original_height";
    static final String JSON_CURRENT_WIDTH = "current_width";
    static final String JSON_CURRENT_HEIGHT = "current_height";
    
    public CodeCheckFiles(CodeCheckApp initApp) {
        app = initApp;
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);

	// CLEAR THE OLD DATA OUT
	CodeCheckData dataManager = (CodeCheckData)data;
        dataManager.resetData();

        // NOW LOAD ALL THE DATA FROM THE json OBJECT
        JsonArray jsonSlidesArray = json.getJsonArray(JSON_SLIDES);
        for (int i = 0; i < jsonSlidesArray.size(); i++) {
            JsonObject jsonSlide = jsonSlidesArray.getJsonObject(i);
            String fileName = jsonSlide.getString(JSON_FILE_NAME);
            String path = jsonSlide.getString(JSON_PATH);
            String caption = jsonSlide.getString(JSON_CAPTION);
            int originalWidth = jsonSlide.getInt(JSON_ORIGINAL_WIDTH);
            int originalHeight = jsonSlide.getInt(JSON_ORIGINAL_HEIGHT);
            int currentWidth = jsonSlide.getInt(JSON_CURRENT_WIDTH);
            int currentHeight = jsonSlide.getInt(JSON_CURRENT_HEIGHT);
            dataManager.addSlide(fileName, path, caption, originalWidth, originalHeight, currentWidth, currentHeight);
        }
    }
      
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	CodeCheckData dataManager = (CodeCheckData)data;

	// NOW BUILD THE SLIDES JSON OBJECTS TO SAVE
	JsonArrayBuilder slidesArrayBuilder = Json.createArrayBuilder();
	ObservableList<Slide> slides = dataManager.getSlides();
	for (Slide slide : slides) {	    
	    JsonObject slideJson = Json.createObjectBuilder()
		    .add(JSON_FILE_NAME, slide.getFileName())
                    .add(JSON_PATH, slide.getPath())
                    .add(JSON_CAPTION, slide.getCaption())
                    .add(JSON_ORIGINAL_WIDTH, slide.getOriginalWidth())
                    .add(JSON_ORIGINAL_HEIGHT, slide.getOriginalHeight())
                    .add(JSON_CURRENT_WIDTH, slide.getCurrentWidth())
                    .add(JSON_CURRENT_HEIGHT, slide.getCurrentHeight()).build();
	    slidesArrayBuilder.add(slideJson);
	}
	JsonArray slidesArray = slidesArrayBuilder.build();
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_SLIDES, slidesArray)
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    // IMPORTING/EXPORTING DATA IS USED WHEN WE READ/WRITE DATA IN AN
    // ADDITIONAL FORMAT USEFUL FOR ANOTHER PURPOSE, LIKE ANOTHER APPLICATION

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}