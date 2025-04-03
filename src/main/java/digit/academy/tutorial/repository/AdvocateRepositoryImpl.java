package digit.academy.tutorial.repository;

import digit.academy.tutorial.util.IdgenUtil;
import digit.academy.tutorial.util.MdmsUtil;
import digit.academy.tutorial.util.WorkflowUtil;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class AdvocateRepositoryImpl implements AdvocateRepository {

    private final JdbcTemplate jdbcTemplate;
    private final IdgenUtil idgenUtil;
    private final MdmsUtil mdmsUtil;
    private final WorkflowUtil workflowUtil;

    @Autowired
    public AdvocateRepositoryImpl(JdbcTemplate jdbcTemplate, IdgenUtil idgenUtil,
                                  MdmsUtil mdmsUtil, WorkflowUtil workflowUtil) {
        this.jdbcTemplate = jdbcTemplate;
        this.idgenUtil = idgenUtil;
        this.mdmsUtil = mdmsUtil;
        this.workflowUtil = workflowUtil;
    }

    @Override
    public Optional<Advocate> findById(UUID id) {
        String sql = "SELECT * FROM advocate WHERE id = ?";
        try {
            Advocate advocate = jdbcTemplate.queryForObject(sql, new AdvocateRowMapper(), id);
            return Optional.ofNullable(advocate);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Advocate> findByApplicationNumber(String applicationNumber) {
        String sql = "SELECT * FROM advocate WHERE application_number = ?";
        try {
            Advocate advocate = jdbcTemplate.queryForObject(sql, new AdvocateRowMapper(), applicationNumber);
            return Optional.ofNullable(advocate);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Advocate> search(AdvocateSearchCriteria criteria) {
        StringBuilder sql = new StringBuilder("SELECT * FROM advocate WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (criteria.getId() != null) {
            sql.append(" AND id = ?");
            params.add(UUID.fromString(criteria.getId()));
        }

        if (criteria.getApplicationNumber() != null) {
            sql.append(" AND application_number = ?");
            params.add(criteria.getApplicationNumber());
        }

        if (criteria.getBarRegistrationNumber() != null) {
            sql.append(" AND LOWER(bar_registration_number) ILIKE ?");
            params.add("%" + criteria.getBarRegistrationNumber().toLowerCase() + "%");
        }

        // Sorting Pending Applications (Oldest First)
        sql.append(" ORDER BY created_time ASC");

        return jdbcTemplate.query(sql.toString(), new AdvocateRowMapper(), params.toArray());
    }

    @Override
    public void save(Advocate advocate, RequestInfo requestInfo) {
        if (advocate.getApplicationNumber() == null) {
            List<String> ids = idgenUtil.getIdList(requestInfo, advocate.getTenantId(),
                    "advocate.applicationnumber", "ADVOC_<SEQ>_<YEAR>", 1);
            advocate.setApplicationNumber(ids.get(0));
        }

        String sql = """
                    INSERT INTO advocate (id, tenant_id, application_number, bar_registration_number, advocate_type, 
                                          organisation_id, individual_id, is_active, additional_details)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql, advocate.getId(), advocate.getTenantId(), advocate.getApplicationNumber(),
                advocate.getBarRegistrationNumber(), advocate.getAdvocateType(), advocate.getOrganisationID(),
                advocate.getIndividualId(), advocate.getIsActive(), advocate.getAdditionalDetails());

        workflowUtil.updateWorkflowStatus(requestInfo, advocate.getTenantId(),
                advocate.getApplicationNumber(), "AdvocateRegistration",
                advocate.getWorkflow(), "legal-services");
    }

    @Override
    public void update(Advocate advocate, RequestInfo requestInfo) {
        String sql = """
                    UPDATE advocate SET application_number = ?, bar_registration_number = ?, advocate_type = ?, 
                                        organisation_id = ?, individual_id = ?, is_active = ?, additional_details = ?
                    WHERE id = ?
                """;
        jdbcTemplate.update(sql, advocate.getApplicationNumber(), advocate.getBarRegistrationNumber(),
                advocate.getAdvocateType(), advocate.getOrganisationID(), advocate.getIndividualId(),
                advocate.getIsActive(), advocate.getAdditionalDetails(), advocate.getId());

        workflowUtil.updateWorkflowStatus(requestInfo, advocate.getTenantId(),
                advocate.getApplicationNumber(), "AdvocateRegistration",
                advocate.getWorkflow(), "legal-services");
    }

    @Override
    public List<Advocate> getPendingApplications() {
        String sql = "SELECT * FROM advocate WHERE workflow_status = 'User Registration Requested' ORDER BY created_time ASC";
        return jdbcTemplate.query(sql, new AdvocateRowMapper());
    }

}
