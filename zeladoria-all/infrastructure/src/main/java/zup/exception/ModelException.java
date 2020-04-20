package zup.exception;

public class ModelException extends Exception {

	private static final long serialVersionUID = 4664456874499611218L;

	public ModelException(String message) {
		super(message);

	}

	public ModelException(Throwable cause) {
		super(cause);
	}

	public ModelException(String message, Throwable cause) {
		super(message, cause);
	}

}