package net.mindengine.oculus.frontend.service.exceptions;

public class UnexistentResource extends Exception {
	/**
     * 
     */
	private static final long serialVersionUID = 8730842826230590641L;

	public UnexistentResource(String resource) {
		super(resource);
	}

	public UnexistentResource(Class<?> clazz, Long id) {
		super(clazz.getSimpleName() + " with id = '" + id + "' doesn't exist");
	}
	
	public UnexistentResource(String name, Long id) {
        super(name + " with id = '" + id + "' doesn't exist");
    }

}
