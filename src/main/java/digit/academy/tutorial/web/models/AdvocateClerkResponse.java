package digit.academy.tutorial.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvocateClerkResponse {

    @JsonProperty("responseInfo")
    @Valid
    @NotNull
    private ResponseInfo responseInfo;

    @JsonProperty("advocateClerks")
    @Valid
    @NotNull
    private List<AdvocateClerk> advocateClerks;
}
