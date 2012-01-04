package net.mindengine.oculus.frontend.domain.customization;

public class UnitCustomizationValue {
	private Long id;
	private Long unitId;
	private Long customizationId;
	private String value;

	public UnitCustomizationValue() {
	}

	public UnitCustomizationValue(Long unitId, Long customizationId, String value) {
		super();
		this.unitId = unitId;
		this.customizationId = customizationId;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public Long getCustomizationId() {
		return customizationId;
	}

	public void setCustomizationId(Long customizationId) {
		this.customizationId = customizationId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
