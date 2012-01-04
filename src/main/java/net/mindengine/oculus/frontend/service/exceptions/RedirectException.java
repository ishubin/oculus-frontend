package net.mindengine.oculus.frontend.service.exceptions;

public class RedirectException extends RuntimeException{

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

	private String redirectUrl;
	
	public RedirectException(String redirectUrl) {
	    super();
	    this.redirectUrl = redirectUrl;
    }

	public void setRedirectUrl(String redirectUrl) {
	    this.redirectUrl = redirectUrl;
    }

	public String getRedirectUrl() {
	    return redirectUrl;
    }
}
