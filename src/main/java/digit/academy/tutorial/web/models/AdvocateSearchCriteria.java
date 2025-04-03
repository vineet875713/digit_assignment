package digit.academy.tutorial.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * AdvocateSearchCriteria
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-02-10T11:17:28.221361812+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvocateSearchCriteria {
    @JsonProperty("id")

    private String id = null;

    @JsonProperty("barRegistrationNumber")

    private String barRegistrationNumber = null;

    @JsonProperty("applicationNumber")

    private String applicationNumber = null;


}
