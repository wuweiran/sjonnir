package clan.midnight.sm;

public interface StateAction<T, R> extends Action {
    @Override
    void execute(StateContext sc);

    default R execute(T args) {
        return null;
    }
}
