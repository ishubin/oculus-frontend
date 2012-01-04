package net.mindengine.oculus.frontend.domain.customstatistics;

import java.util.Date;

public class CustomStatisticChart {

    public static final String TYPE_LINE = "line".intern();
    public static final String TYPE_BAR = "bar".intern();
    
    private Long id;
    private String name;
    private Long customStatisticId;
    private String parameters;
    private String type;
    private Date date;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getCustomStatisticId() {
        return customStatisticId;
    }
    public void setCustomStatisticId(Long customStatisticId) {
        this.customStatisticId = customStatisticId;
    }
    public String getParameters() {
        return parameters;
    }
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {
        return date;
    }
}
