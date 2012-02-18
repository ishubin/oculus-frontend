package net.mindengine.oculus.frontend.service.trm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mindengine.oculus.frontend.domain.trm.AgentTagRule;
import net.mindengine.oculus.frontend.domain.trm.AgentTagValueWrapper;
import net.mindengine.oculus.frontend.domain.trm.AgentTagWrapper;
import net.mindengine.oculus.frontend.domain.trm.Dimension;
import net.mindengine.oculus.grid.domain.agent.AgentStatus;
import net.mindengine.oculus.grid.domain.agent.AgentTag;

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
        
        //Checking if there is a size of image specified in a rule
        if(image.contains("@")) {
            //Extracting size of image.
            int delimiterPos = image.indexOf("@");
            rule.setImageUrl(image.substring(0, delimiterPos));
            rule.setImageSize(readDimension(image.substring(delimiterPos + 1)));
        }
        else {
            rule.setImageUrl(image);
        }
        return rule;
    }

    private Dimension readDimension(String dimensionString) {
        Dimension dim = new Dimension();
        
        String arr[] = dimensionString.split("x");
        if(arr.length==2) {
            dim.setSize(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
        }
        else {
            throw new IllegalArgumentException("Cannot read dimensions for agents tag rule: '" + dimensionString + "'");
        }
        return dim;
    }
    
    public void wrapAgentTags(AgentStatus[] agents) {
        if(agents!=null) {
            for(AgentStatus agent : agents) {
                AgentTag[] tags = agent.getAgentInformation().getTags();
                if(tags!=null) {
                    AgentTagWrapper[] wrappedTags = new AgentTagWrapper[tags.length];
                    for(int i=0; i<tags.length; i++) {
                        wrappedTags[i] = wrapTag(tags[i]);
                    }
                    agent.getAgentInformation().setTags(wrappedTags);
                }
            }
        }
    }

    private AgentTagWrapper wrapTag(AgentTag tag) {
        AgentTagWrapper tagWrapper = new AgentTagWrapper();
        tagWrapper.setName(tag.getName());
        tagWrapper.setType(tag.getType());
        if(tag.getValue()!=null) {
            tagWrapper.setWrappedValue(wrapTagValue(tag.getName(), tag.getValue()));
        }
        if(tag.getValues()!=null) {
            List<AgentTagValueWrapper> wrappedValues = new LinkedList<AgentTagValueWrapper>();
            for(String value : tag.getValues()) {
                wrappedValues.add(wrapTagValue(tag.getName(), value));
            }
            tagWrapper.setWrappedValues(wrappedValues);
        }
        return tagWrapper;
    }

    private AgentTagValueWrapper wrapTagValue(String name, String value) {
        AgentTagValueWrapper wrappedValue = new AgentTagValueWrapper();
        wrappedValue.setValue(value);
        
        //Fetching image for this tag value
        List<AgentTagRule> rules = getRules();
        if(rules!=null) {
            String path = name + "/" + value;
            for(AgentTagRule rule: rules) {
                Matcher m = rule.getPattern().matcher(path);
                if(m.matches()) {
                    wrappedValue.setIconImage(rule.getImageUrl());
                    wrappedValue.setIconSize(rule.getImageSize());
                    break;
                }
            }
        }
        return wrappedValue;
    }

    /**
     * Fetches all agent tags and sorts them alphabetically.
     * @param agents
     * @return
     */
    public List<AgentTagValueWrapper> fetchAllAgentWrappedTags(AgentStatus[] agents) {
        List<AgentTagValueWrapper> values = new LinkedList<AgentTagValueWrapper>();
        for(AgentStatus agent: agents) {
            AgentTag[] tags = agent.getAgentInformation().getTags();
            if(tags!=null) {
                for(AgentTag tag : tags) {
                    AgentTagWrapper tagWrapper = null;
                    if(tag instanceof AgentTagWrapper) {
                        tagWrapper = (AgentTagWrapper) tag;
                    }
                    else tagWrapper = wrapTag(tag);
                    
                    if(tagWrapper.getType().equals(AgentTag.STRING)) {
                        if(!containsTagValue(values, tagWrapper.getWrappedValue())){
                            values.add(tagWrapper.getWrappedValue());
                        }
                    }
                    else if(tagWrapper.getType().equals(AgentTag.LIST) && tagWrapper.getWrappedValues()!=null) {
                        for(AgentTagValueWrapper wrappedValue : tagWrapper.getWrappedValues()) {
                            if(!containsTagValue(values, wrappedValue)){
                                values.add(wrappedValue);
                            }
                        }
                    }
                 }
            }
        }
        
        Collections.sort(values, new Comparator<AgentTagValueWrapper>() {
            @Override
            public int compare(AgentTagValueWrapper v1, AgentTagValueWrapper v2) {
                String s1 = v1.getValue().trim();
                String s2 = v1.getValue().trim();
                
                return s2.compareToIgnoreCase(s1);
            }
        });
        
        return values;
    }

    private boolean containsTagValue(List<AgentTagValueWrapper> values, AgentTagValueWrapper wrappedValue) {
        for(AgentTagValueWrapper value : values) {
            if(value.getValue().trim().toLowerCase().equals(wrappedValue.getValue().trim().toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
