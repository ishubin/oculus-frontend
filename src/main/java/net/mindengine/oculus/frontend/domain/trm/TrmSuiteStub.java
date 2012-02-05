package net.mindengine.oculus.frontend.domain.trm;

import java.util.List;

/**
 * This class is used for quick conversion from json format to java
 * @author ishubin
 *
 */
public class TrmSuiteStub {

    private Long testId;
    private List<TrmSuiteStub> tests;
    public List<TrmSuiteStub> getTests() {
        return tests;
    }
    public void setTests(List<TrmSuiteStub> tests) {
        this.tests = tests;
    }
    public Long getTestId() {
        return testId;
    }
    public void setTestId(Long testId) {
        this.testId = testId;
    }
    
}
