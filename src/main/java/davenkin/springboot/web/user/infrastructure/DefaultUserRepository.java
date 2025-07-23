package davenkin.springboot.web.user.infrastructure;

import davenkin.springboot.web.common.infrastructure.MongoBaseRepository;
import davenkin.springboot.web.user.domain.User;
import davenkin.springboot.web.user.domain.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultUserRepository extends MongoBaseRepository<User> implements UserRepository {
}
