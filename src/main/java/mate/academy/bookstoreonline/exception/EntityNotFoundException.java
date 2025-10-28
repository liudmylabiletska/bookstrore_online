package mate.academy.bookstoreonline.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
//
//    public static Supplier<EntityNotFoundException> supplier(String message) {
//        return () -> new EntityNotFoundException(message);
//    }
}
