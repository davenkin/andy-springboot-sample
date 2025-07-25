package deviceet.user.domain;

public interface UserRepository {
    void save(User user);

    User byId(String id);
}
