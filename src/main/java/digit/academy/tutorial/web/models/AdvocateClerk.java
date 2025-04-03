package digit.academy.tutorial.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.models.AuditDetails;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvocateClerk {
    @JsonProperty("id")
    @Valid
    private UUID id = null;

    @JsonProperty("tenantId")
    @NotNull
    @Size(min = 2, max = 128)
    private String tenantId = null;

    @JsonProperty("applicationNumber")
    @Size(min = 2, max = 64)
    private String applicationNumber = null;

    @JsonProperty("advocateId")
    @NotNull
    private UUID advocateId = null;

    @JsonProperty("isActive")
    private Boolean isActive = true;

    @JsonProperty("auditDetails")
    @Valid
    private AuditDetails auditDetails = null;
}
