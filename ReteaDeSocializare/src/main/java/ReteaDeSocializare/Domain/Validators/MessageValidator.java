package ReteaDeSocializare.Domain.Validators;

import ReteaDeSocializare.Domain.Exceptions.FriendshipException;
import ReteaDeSocializare.Domain.Exceptions.MessageException;
import ReteaDeSocializare.Domain.Exceptions.ValidationException;
import ReteaDeSocializare.Domain.Friendship;
import ReteaDeSocializare.Domain.Message;
import javafx.scene.text.Text;

public class MessageValidator implements Validator<Message> {

    @Override
    public void validate(Message entity) throws ValidationException {
        if(entity.getSender() == null || entity.getReceiver() == null)
            throw new MessageException("User ID cannot be null!'\n");
        Text text = new Text("");
        if(entity.getMessage().equals(text))
            throw new MessageException("The message cannot be null!'\n");
    }
}