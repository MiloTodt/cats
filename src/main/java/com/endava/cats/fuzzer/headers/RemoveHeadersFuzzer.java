package com.endava.cats.fuzzer.headers;

import com.endava.cats.fuzzer.Fuzzer;
import com.endava.cats.io.ServiceCaller;
import com.endava.cats.io.ServiceData;
import com.endava.cats.model.CatsHeader;
import com.endava.cats.model.CatsResponse;
import com.endava.cats.model.FuzzingData;
import com.endava.cats.report.TestCaseListener;
import com.endava.cats.util.CatsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RemoveHeadersFuzzer implements Fuzzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveHeadersFuzzer.class);

    private final ServiceCaller serviceCaller;
    private final TestCaseListener testCaseListener;
    private final CatsUtil catsUtil;

    @Autowired
    public RemoveHeadersFuzzer(ServiceCaller sc, TestCaseListener lr, CatsUtil cu) {
        this.serviceCaller = sc;
        this.testCaseListener = lr;
        this.catsUtil = cu;
    }

    public void fuzz(FuzzingData data) {
        if (data.getHeaders().isEmpty()) {
            LOGGER.info("No headers to fuzz");
            return;
        }

        Set<Set<CatsHeader>> headersCombination = catsUtil.powerSet(data.getHeaders());
        Set<CatsHeader> mandatoryHeaders = data.getHeaders().stream().filter(CatsHeader::isRequired).collect(Collectors.toSet());

        for (Set<CatsHeader> headersSubset : headersCombination) {
            testCaseListener.createAndExecuteTest(LOGGER, this, () -> process(data, headersSubset, mandatoryHeaders));
        }
    }

    private void process(FuzzingData data, Set<CatsHeader> headersSubset, Set<CatsHeader> requiredHeaders) {
        testCaseListener.addScenario(LOGGER, "Scenario: send only the following headers: {} plus any authentication headers.", headersSubset);
        boolean anyMandatoryHeaderRemoved = this.isAnyMandatoryHeaderRemoved(headersSubset, requiredHeaders);

        testCaseListener.addExpectedResult(LOGGER, "Expected result: should return [{}] response code as mandatory headers [{}] removed", catsUtil.getExpectedWordingBasedOnRequiredFields(anyMandatoryHeaderRemoved));

        CatsResponse response = serviceCaller.call(data.getMethod(), ServiceData.builder().relativePath(data.getPath()).headers(headersSubset)
                .payload(data.getPayload()).addUserHeaders(false).queryParams(data.getQueryParams()).build());
        testCaseListener.reportResult(LOGGER, data, response, catsUtil.getResultCodeBasedOnRequiredFieldsRemoved(anyMandatoryHeaderRemoved));
    }

    private boolean isAnyMandatoryHeaderRemoved(Set<CatsHeader> headersSubset, Set<CatsHeader> requiredHeaders) {
        Set<CatsHeader> intersection = new HashSet<>(requiredHeaders);
        intersection.retainAll(headersSubset);
        return intersection.size() != requiredHeaders.size();
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String description() {
        return "iterate through each header and remove different combinations of them";
    }
}
