package net.mindengine.oculus.frontend.domain.trm;

import java.util.regex.Pattern;

public class AgentTagRule {
    
    private Pattern pattern;
    private String imageUrl;
    
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

}
