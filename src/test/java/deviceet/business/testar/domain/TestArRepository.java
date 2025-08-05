package deviceet.business.testar.domain;

import java.util.List;
import java.util.Optional;

public interface TestArRepository {
    void save(TestAr ar);

    void save(List<TestAr> ars);

    void delete(TestAr ar);

    void delete(List<TestAr> ars);

    TestAr byId(String id);

    TestAr byId(String id, String orgId);

    Optional<TestAr> byIdOptional(String id);

    Optional<TestAr> byIdOptional(String id, String orgId);

    boolean exists(String id, String orgId);
}

