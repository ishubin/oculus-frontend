package net.mindengine.oculus.frontend.service.exceptions;

public class NotAuthorizedException extends Exception {
	/**
     * 
     */
	private static final long serialVersionUID = -7936812425735339289L;

	public NotAuthorizedException(String msg) {
		super(msg);
	}

	public NotAuthorizedException() {
		super();
	}
}
