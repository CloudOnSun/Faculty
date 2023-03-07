package main.java.server.validators;

import main.java.server.domain.Friendship;
import main.java.server.exceptions.ValidationException;

public class FriendshipValidator extends BaseValidator<Friendship> {
    /**
     * Checks if the friendship has two not null members
     * @param friendship the given frienship
     * @throws ValidationException if the friendship is invalid
     */
    @Override
    public void validate(Friendship friendship) {
        if (friendship.getIdUser1() == null || friendship.getIdUser2() == null ||
            friendship.getIdUser1() <= 0  || friendship.getIdUser2() <=0 ||
            friendship.getIdUser1().equals(friendship.getIdUser2()))
            throw new ValidationException("Invalid friendship");
    }
}
