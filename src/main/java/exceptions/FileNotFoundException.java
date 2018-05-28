package exceptions;

/**
 * @author navid
 *         Project-Name: IR-Search-Engine
 *         Date: 5/28/18.
 */
public class FileNotFoundException extends Exception {
    private String message;

    public FileNotFoundException(String message) {
        this.message = message;
    }

    public FileNotFoundException() {
        this("File Not Found in the Given Path!");
    }
}
