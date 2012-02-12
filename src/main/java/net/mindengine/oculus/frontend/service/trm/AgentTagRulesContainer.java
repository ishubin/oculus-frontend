package net.mindengine.oculus.frontend.service.trm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import net.mindengine.oculus.frontend.domain.trm.AgentTagRule;
import net.mindengine.oculus.frontend.domain.trm.AgentTagWrapper;

/**
 * Defines rules for replacing agent tags text with images
 * 
 * @author ishubin
 * 
 */
public class AgentTagRulesContainer {

    private List<AgentTagRule> rules;

    private File file;
    
    

    public List<AgentTagRule> getRules() {
        return rules;
    }

    public void setRules(List<AgentTagRule> rules) {
        this.rules = rules;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) throws IOException {
        this.file = file;
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream(file);
        props.load(fis);
        fis.close();

        rules = new LinkedList<AgentTagRule>();

        Enumeration<Object> keys = props.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement().toString();
            String value = props.getProperty(key);
            if (key != null && value != null) {
                rules.add(createRule(key, value));
            }
        }
    }

    private AgentTagRule createRule(String pattern, String image) {
        AgentTagRule rule = new AgentTagRule();
        rule.setPattern(Pattern.compile(pattern));
        rule.setImageUrl(image);
        return rule;
    }

}
