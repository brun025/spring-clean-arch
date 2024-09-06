package br.com.api.domain.user;

import java.time.Instant;
import java.util.Objects;

import br.com.api.domain.AggregateRoot;
import br.com.api.domain.utils.InstantUtils;
import br.com.api.domain.validation.ValidationHandler;

public class User extends AggregateRoot<UserID> implements Cloneable {

    private String name;
    private String email;
    private String password;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    
    private User(
            final UserID anId,
            final String aName,
            final String aEmail,
            final String aPassword,
            final boolean isActive,
            final Instant aCreationDate,
            final Instant aUpdateDate,
            final Instant aDeleteDate
    ) {
        super(anId);
        this.name = aName;
        this.email = aEmail;
        this.password = aPassword;
        this.active = isActive;
        this.createdAt = Objects.requireNonNull(aCreationDate, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(aUpdateDate, "'updatedAt' should not be null");
        this.deletedAt = aDeleteDate;
    }
    
    public static User newUser(final String aName, final String aEmail, final String aPassword, final boolean isActive) {
        final var id = UserID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isActive ? null : now;
        return new User(id, aName, aEmail, aPassword, isActive, now, now, deletedAt);
    }
    
    public static User with(
            final UserID anId,
            final String name,
            final String email,
            final String password,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        return new User(
                anId,
                name,
                email,
                password,
                active,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public static User with(final User aCategory) {
        return with(
                aCategory.getId(),
                aCategory.name,
                aCategory.email,
                aCategory.password,
                aCategory.isActive(),
                aCategory.createdAt,
                aCategory.updatedAt,
                aCategory.deletedAt
        );
    }

	@Override
	public void validate(final ValidationHandler handler) {
        new UserValidator(this, handler).validate();
    }

    public User activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public User deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = InstantUtils.now();
        }

        this.active = false;
        this.updatedAt = InstantUtils.now();
        return this;
    }
    
    public User update(
            final String aName,
            final String aEmail,
            final boolean isActive
    ) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
        this.name = aName;
        this.email = aEmail;
        this.updatedAt = Instant.now();
        return this;
    }
    
    public UserID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public User clone() {
        try {
            return (User) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
