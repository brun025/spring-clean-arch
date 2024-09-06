package br.com.api.domain.user;

import br.com.api.domain.validation.ValidationHandler;
import br.com.api.domain.validation.Validator;

import br.com.api.domain.validation.Error;

public class UserValidator extends Validator {

    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 3;
    private final User user;

    public UserValidator(final User aUser, final ValidationHandler aHandler) {
        super(aHandler);
        this.user = aUser;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.user.getName();
        
        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if (name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final int length = name.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }
}
