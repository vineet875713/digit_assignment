package digit.academy.tutorial.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvocateVerificationRequest {

    @JsonProperty("requestInfo")
    @Valid
    private RequestInfo requestInfo;

    @JsonProperty("applicationNumber")
    @NotNull
    private String applicationNumber;

    @JsonProperty("action")  // "APPROVE" or "REJECT"
    @NotNull
    private String action;

    @JsonProperty("rejectionReason")  // Only required if rejecting
    private String rejectionReason;
}
