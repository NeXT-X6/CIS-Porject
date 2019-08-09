package test.common;

public enum ErrorMessage {
	NULL("NULL")
    ;
    
	private ErrorMessage(String message) {
		this.errorMessage = message;
	}

    public String getErrorMessage() {
        return errorMessage;
    }
    
	private String errorMessage;    
}
