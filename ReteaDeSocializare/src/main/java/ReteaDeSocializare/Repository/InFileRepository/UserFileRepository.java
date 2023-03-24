package ReteaDeSocializare.Repository.InFileRepository;

import ReteaDeSocializare.Domain.User;
import ReteaDeSocializare.Domain.Validators.Validator;

import java.util.List;
import java.util.UUID;

public class UserFileRepository extends AbstractFileRepository<UUID, User> {

    public UserFileRepository(String fileName, Validator<User> validator) {
        super(fileName, validator);
    }


    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId()+";"+entity.getLastName()+";"+entity.getFirstName();
    }
    @Override
    public User extractEntity(List<String> attributes) {
        User user = new User(UUID.fromString(attributes.get(0)), attributes.get(2), attributes.get(1));
        return user;
    }


}
