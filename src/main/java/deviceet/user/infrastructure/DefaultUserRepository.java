package deviceet.user.infrastructure;

import deviceet.common.MongoBaseRepository;
import deviceet.user.domain.User;
import deviceet.user.domain.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultUserRepository extends MongoBaseRepository<User> implements UserRepository {
}
