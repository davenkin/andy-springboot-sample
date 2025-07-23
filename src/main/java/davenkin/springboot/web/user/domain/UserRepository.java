package davenkin.springboot.web.user.domain;

public interface UserRepository {
    void save(User user);

    User byId(String id);
}
