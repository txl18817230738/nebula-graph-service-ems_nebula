package org.univers.ems.exception;

/**
 * @author jie.xi
 */
public class FileParsingException extends Exception {
    public FileParsingException(String message) {
        super(message);
    }
        
    public FileParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}