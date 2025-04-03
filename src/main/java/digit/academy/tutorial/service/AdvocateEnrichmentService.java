package digit.academy.tutorial.service;

import digit.academy.tutorial.util.IdgenUtil;
import digit.academy.tutorial.web.models.Advocate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdvocateEnrichmentService {

    private final IdgenUtil idgenUtil;

    public void enrichCreateRequest(Advocate advocate, RequestInfo requestInfo) {
        // Generate a unique Application Number if not provided
        if (advocate.getApplicationNumber() == null) {
            List<String> ids = idgenUtil.getIdList(requestInfo, advocate.getTenantId(), "advocate.applicationnumber", "ADVOC_<SEQ>_<YEAR>", 1);
            advocate.setApplicationNumber(ids.get(0));
        }

        // Assign a new UUID if it's not provided
        if (advocate.getId() == null) {
            advocate.setId(UUID.randomUUID());
        }

        // Set audit details
        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy(requestInfo.getUserInfo().getUuid());
        auditDetails.setCreatedTime(System.currentTimeMillis());
        auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getUuid());
        auditDetails.setLastModifiedTime(System.currentTimeMillis());
        advocate.setAuditDetails(auditDetails);
    }

    public void enrichUpdateRequest(Advocate advocate, RequestInfo requestInfo) {
        if (advocate.getAuditDetails() != null) {
            advocate.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUuid());
            advocate.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
        }
    }
}
