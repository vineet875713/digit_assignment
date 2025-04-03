package digit.academy.tutorial.web.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * AllOfPaginationOrder
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2025-02-10T11:17:28.221361812+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllOfPaginationOrder {

    private Order order;

    public String getValue() {
        return order != null ? order.toString() : null;
    }
}

