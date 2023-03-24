package ReteaDeSocializare.Repository.InFileRepository;
import ReteaDeSocializare.Domain.Friendship;

import ReteaDeSocializare.Domain.Validators.Validator;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class FriendshipFileRepository extends AbstractFileRepository<UUID, Friendship> {
    public FriendshipFileRepository(String fileName, Validator<Friendship> validator){
        super(fileName, validator);
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return
                entity.getId() + ";" +
                        entity.getId1() + ";" +
                        entity.getId2() + ";" +
                        entity.getTime();

    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        Friendship ret = new Friendship(UUID.fromString(attributes.get(0)), UUID.fromString(attributes.get(1)), UUID.fromString(attributes.get(2)), LocalDateTime.parse(attributes.get(3)));
        return ret;
    }


}
