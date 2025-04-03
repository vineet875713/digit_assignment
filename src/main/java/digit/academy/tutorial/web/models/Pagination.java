package digit.academy.tutorial.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * Pagination details
 */
@Schema(description = "Pagination details")
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-02-10T11:17:28.221361812+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pagination {
    @JsonProperty("limit")

    @DecimalMax("100")
    private Double limit = 10d;

    @JsonProperty("offSet")

    private Double offSet = 0d;

    @JsonProperty("totalCount")

    private Double totalCount = null;

    @JsonProperty("sortBy")

    private String sortBy = null;

    @JsonProperty("order")

    private AllOfPaginationOrder order = null;


}
