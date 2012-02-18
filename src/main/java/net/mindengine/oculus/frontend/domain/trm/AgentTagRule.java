package net.mindengine.oculus.frontend.domain.trm;

import java.util.regex.Pattern;

public class AgentTagRule {
    
    private Pattern pattern;
    private String imageUrl;
    
    //If specified as null then image size will not be applied, meaning that image will be shown as is.
    private Dimension imageSize;
    
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public Pattern getPattern() {
        return pattern;
    }
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
    public Dimension getImageSize() {
        return imageSize;
    }
    public void setImageSize(Dimension imageSize) {
        this.imageSize = imageSize;
    }

}
