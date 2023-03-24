package ReteaDeSocializare.Domain.Validators;

import ReteaDeSocializare.Domain.Exceptions.FriendshipException;
import ReteaDeSocializare.Domain.Exceptions.ValidationException;
import ReteaDeSocializare.Domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {

    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(entity.getId1() == null || entity.getId2() == null)
            throw new FriendshipException("User ID cannot be null!\n");
        if(entity.getId1() == entity.getId2())
            throw new FriendshipException("User can't be friend with himself!\n");
    }
}
