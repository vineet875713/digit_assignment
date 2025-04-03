package digit.academy.tutorial.repository;

import digit.academy.tutorial.web.models.AdvocateClerk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AdvocateClerkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AdvocateClerkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(AdvocateClerk advocateClerk) {
        String sql = "INSERT INTO advocate_clerk (id, tenant_id, application_number, advocate_id, is_active) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, advocateClerk.getId(), advocateClerk.getTenantId(),
                advocateClerk.getApplicationNumber(), advocateClerk.getAdvocateId(), advocateClerk.getIsActive());
    }
}
