package ReteaDeSocializare.Domain.Validators;

import ReteaDeSocializare.Domain.Exceptions.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
