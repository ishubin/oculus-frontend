package net.mindengine.oculus.frontend.domain.trm;


/**
 * Used by frontend in order to wrap tag values and replace them with images according to agent-tags-rules
 * @author ishubin
 *
 */
public class AgentTagValueWrapper {

    private String value;
    private String iconImage;
    private Dimension iconSize;
    public String getIconImage() {
        return iconImage;
    }
    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public Dimension getIconSize() {
        return iconSize;
    }
    public void setIconSize(Dimension iconSize) {
        this.iconSize = iconSize;
    }
}
