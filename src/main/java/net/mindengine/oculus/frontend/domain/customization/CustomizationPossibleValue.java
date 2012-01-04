package net.mindengine.oculus.frontend.domain.customization;

public class CustomizationPossibleValue {
	private Long id;
	private Long customizationId;
	private String possibleValue;

	private Boolean isSet = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomizationId() {
		return customizationId;
	}

	public void setCustomizationId(Long customizationId) {
		this.customizationId = customizationId;
	}

	public String getPossibleValue() {
		return possibleValue;
	}

	public void setPossibleValue(String possibleValue) {
		this.possibleValue = possibleValue;
	}

	public void setIsSet(Boolean isSet) {
		this.isSet = isSet;
	}

	public Boolean getIsSet() {
		return isSet;
	}

}
