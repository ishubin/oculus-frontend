package net.mindengine.oculus.frontend.domain;

public class Option {
	private String key;
	private String value;

	private boolean checked = false;
	public Option() {

	}

	public Option(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}
	
	public Option(String key, String value, boolean checked) {
        super();
        this.key = key;
        this.value = value;
        this.checked = checked;
    }

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
