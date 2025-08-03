package deviceet.testar.infrastructure;

import deviceet.common.infrastructure.AbstractMongoRepository;
import deviceet.testar.domain.TestAr;
import deviceet.testar.domain.TestArRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MongoTestArRepository extends AbstractMongoRepository<TestAr> implements TestArRepository {
}
