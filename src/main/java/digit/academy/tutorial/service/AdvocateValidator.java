package digit.academy.tutorial.service;

import digit.academy.tutorial.util.MdmsUtil;
import digit.academy.tutorial.web.models.Advocate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdvocateValidator {

    private final MdmsUtil mdmsUtil;

    @Value("${validation.pincode.length}")
    private int pincodeLength;

    @Value("${validation.mobile.length}")
    private int mobileLength;

    @Value("${validation.barRegNumber.length.min}")
    private int barRegMinLength;

    @Value("${validation.barRegNumber.length.max}")
    private int barRegMaxLength;


    public void validateCreateRequest(Advocate advocate, RequestInfo requestInfo) {
        if (advocate.getBarRegistrationNumber().length() < barRegMinLength ||
                advocate.getBarRegistrationNumber().length() > barRegMaxLength) {
            throw new IllegalArgumentException("Bar Registration Number length is invalid.");
        }
        if (advocate.getTenantId() == null || advocate.getTenantId().isEmpty()) {
            throw new IllegalArgumentException("Tenant ID cannot be null or empty");
        }

        if (advocate.getApplicationNumber() == null || advocate.getApplicationNumber().isEmpty()) {
            throw new IllegalArgumentException("Application Number cannot be null or empty");
        }

        if (advocate.getAdvocateType() == null || advocate.getAdvocateType().isEmpty()) {
            throw new IllegalArgumentException("Advocate Type cannot be null or empty");
        }

        if (advocate.getBarRegistrationNumber() == null || advocate.getBarRegistrationNumber().isEmpty()) {
            throw new IllegalArgumentException("Bar Registration Number cannot be null or empty");
        }

        // Validate Advocate Type using MDMS
        List<String> masterNames = List.of("UserType");
        Map<String, Map<String, JSONArray>> mdmsData = mdmsUtil.fetchMdmsData(requestInfo, advocate.getTenantId(), "legal-services", masterNames);

        List<String> validTypes = mdmsData.get("legal-services").get("UserType")
                .stream()
                .map(obj -> ((Map<String, String>) obj).get("code"))
                .toList();

        if (!validTypes.contains(advocate.getAdvocateType())) {
            throw new IllegalArgumentException("Invalid Advocate Type");
        }
    }

    public void validateUpdateRequest(Advocate advocate, RequestInfo requestInfo) {
        if (advocate.getId() == null) {
            throw new IllegalArgumentException("Advocate ID is required for update");
        }

        validateCreateRequest(advocate, requestInfo);
    }
}
