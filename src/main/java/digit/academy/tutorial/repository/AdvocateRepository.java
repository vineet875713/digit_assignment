package digit.academy.tutorial.repository;

import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateSearchCriteria;
import org.egov.common.contract.request.RequestInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdvocateRepository {
    Optional<Advocate> findById(UUID id);

    Optional<Advocate> findByApplicationNumber(String applicationNumber);

    List<Advocate> search(AdvocateSearchCriteria criteria);

    void save(Advocate advocate, RequestInfo requestInfo);

    void update(Advocate advocate, RequestInfo requestInfo);

    List<Advocate> getPendingApplications();
}


