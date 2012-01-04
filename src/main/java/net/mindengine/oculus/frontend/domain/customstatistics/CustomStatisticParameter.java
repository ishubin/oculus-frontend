package net.mindengine.oculus.frontend.domain.customstatistics;

import java.util.Collection;

public class CustomStatisticParameter {

	private Long id;
	private Long customStatisticId;
	private String name;
	private String description;
	private String shortName;
	
	private Collection<String> possibleValues;
	
	public Long getId() {
    	return id;
    }
	public void setId(Long id) {
    	this.id = id;
    }
	public Long getCustomStatisticId() {
    	return customStatisticId;
    }
	public void setCustomStatisticId(Long customStatisticId) {
    	this.customStatisticId = customStatisticId;
    }
	public String getName() {
    	return name;
    }
	public void setName(String name) {
    	this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public String getShortName() {
        return shortName;
    }
    public void setPossibleValues(Collection<String> possibleValues) {
        this.possibleValues = possibleValues;
    }
    public Collection<String> getPossibleValues() {
        return possibleValues;
    }
}
