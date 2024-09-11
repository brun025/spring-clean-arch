package br.com.api.domain.user;

import br.com.api.domain.validation.ValidationHandler;
import br.com.api.domain.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import br.com.api.domain.validation.Error;

public class UserValidator extends Validator {

    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 3;
    public static final int PASSWORD_MAX_LENGTH = 20;
    public static final int PASSWORD_MIN_LENGTH = 5;
    private final User user;

    public UserValidator(final User aUser, final ValidationHandler aHandler) {
        super(aHandler);
        this.user = aUser;
    }

    @Override
    public void validate() {
        checkNameConstraints();
        checkEmailConstraints();
        checkPasswordConstraints();
    }

	private void checkNameConstraints() {
        final var name = this.user.getName();
        
        if (StringUtils.equals(name, null)){
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
	
	private void checkEmailConstraints() {
        final var email = this.user.getEmail();
        final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        
        if (StringUtils.equals(email, null)){
            this.validationHandler().append(new Error("'email' should not be null"));
            return;
        }
        
        Pattern pattern = Pattern.compile(REGEX_EMAIL, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        
        if (!matcher.matches()) {
        	this.validationHandler().append(new Error("Invalid E-mail"));
        }
    }
	
	private void checkPasswordConstraints() {
        final var password = this.user.getPassword();
        
        if (StringUtils.equals(password, null)){
            this.validationHandler().append(new Error("'password' should not be null"));
            return;
        }

        if (password.isBlank()) {
            this.validationHandler().append(new Error("'password' should not be empty"));
            return;
        }

        final int length = password.trim().length();
        if (length > PASSWORD_MAX_LENGTH || length < PASSWORD_MIN_LENGTH) {
            this.validationHandler().append(new Error("'password' must be between 5 and 20 characters"));
        }
    }
}
