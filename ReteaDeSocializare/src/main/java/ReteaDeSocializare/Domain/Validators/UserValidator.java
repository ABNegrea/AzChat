package ReteaDeSocializare.Domain.Validators;

import ReteaDeSocializare.Domain.Exceptions.ValidationException;
import ReteaDeSocializare.Domain.User;

public class UserValidator implements Validator<User> {

    /**
     *
     * @param name must not be null and be less than 30characters
     * @return true if it respects the rules, false if not
     */
    static boolean checkLastName(String name) {
        for (int i = 0; i < name.length(); i++)
            if (!Character.isLetter(name.charAt(i)))
                return false;
        return name.length() > 0 && name.length() <= 30;
    }

    static boolean checkFirstName(String name) {
        for (int i = 0; i < name.length(); i++)
            if (!Character.isLetter(name.charAt(i)) && name.charAt(i) != ' ')
                return false;
        return name.length() > 0 && name.length() <= 30;
    }

    @Override
    public void validate(User entity) throws ValidationException {
        String errors = "";
        if (!checkFirstName(entity.getFirstName()))
            errors += "Invalid first name\n";
        if (!checkLastName(entity.getLastName()))
            errors += "Invalid last name\n";
        if (!errors.equals(""))
            throw new ValidationException(errors);
    }
}
