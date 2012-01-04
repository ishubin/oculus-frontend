package net.mindengine.oculus.frontend.service.exceptions;

public class PermissionDeniedException extends Exception {
	/**
     * 
     */
	private static final long serialVersionUID = 101439229614942355L;

	public PermissionDeniedException(String msg) {
		super(msg);
	}

	public PermissionDeniedException() {
		super();
	}

}
