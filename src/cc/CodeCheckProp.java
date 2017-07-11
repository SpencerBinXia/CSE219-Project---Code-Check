package cc;

/**
 * This enum provides a list of all the user interface
 * text that needs to be loaded from the XML properties
 * file. By simply changing the XML file we could initialize
 * this application such that all UI controls are provided
 * in another language.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public enum CodeCheckProp {
    // FOR SIMPLE OK/CANCEL DIALOG BOXES
    OK_PROMPT,
    CANCEL_PROMPT,
    
    // THESE ARE FOR TEXT PARTICULAR TO THE APP'S WORKSPACE CONTROLS
    HOME_BUTTON_TEXT,
    PREV_BUTTON_TEXT,
    NEXT_BUTTON_TEXT,
    STEP_2_TEXT,
    STEP_2_DESC_TEXT,
    PROGRESS_2_TEXT,
    RENAME_FILES_BUTTON_TEXT,
    SLIDES_HEADER_TEXT,
    ADD_ALL_IMAGES_BUTTON_TEXT,
    ADD_IMAGE_BUTTON_TEXT,
    REMOVE_IMAGE_BUTTON_TEXT,
    FILE_NAME_COLUMN_TEXT,
    CAPTION_COLUMN_TEXT,
    ORIGINAL_WIDTH_COLUMN_TEXT,
    ORIGINAL_HEIGHT_COLUMN_TEXT,
    CURRENT_WIDTH_COLUMN_TEXT,
    CURRENT_HEIGHT_COLUMN_TEXT,
    FILE_NAME_PROMPT_TEXT,
    PATH_PROMPT_TEXT,
    CAPTION_PROMPT_TEXT,
    ORIGINAL_WIDTH_PROMPT_TEXT,
    ORIGINAL_HEIGHT_PROMPT_TEXT,
    CURRENT_WIDTH_PROMPT_TEXT,
    CURRENT_HEIGHT_PROMPT_TEXT,
    UPDATE_BUTTON_TEXT,
    
    // FOR OTHER INTERACTIONS
    APP_PATH_WORK,
    INVALID_IMAGE_PATH_TITLE,
    INVALID_IMAGE_PATH_MESSAGE
}
