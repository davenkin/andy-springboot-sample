package deviceet.testar.domain;

public interface TestArRepository {
    void save(TestAr testAr);

    TestAr byId(String id, String orgId);
}
