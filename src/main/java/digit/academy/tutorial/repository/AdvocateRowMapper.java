package digit.academy.tutorial.repository;

import digit.academy.tutorial.web.models.Advocate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AdvocateRowMapper implements RowMapper<Advocate> {
    @Override
    public Advocate mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Advocate.builder()
                .id(UUID.fromString(rs.getString("id")))
                .tenantId(rs.getString("tenant_id"))
                .applicationNumber(rs.getString("application_number"))
                .barRegistrationNumber(rs.getString("bar_registration_number"))
                .advocateType(rs.getString("advocate_type"))
                .organisationID(rs.getString("organisation_id") != null ? UUID.fromString(rs.getString("organisation_id")) : null)
                .individualId(rs.getString("individual_id"))
                .isActive(rs.getBoolean("is_active"))
                .additionalDetails(rs.getObject("additional_details"))
                .build();
    }
}

