package clan.midnight.sm;

import java.util.ArrayList;
import java.util.List;

public class Transition implements Activity {
    private String name;
    private State from;
    private State to;
    private final List<Condition> conditions = new ArrayList<>();
    private final List<Action> actions = new ArrayList<>();

    public Transition() {}

    public Transition(State from, State to) {
        this.from = from;
        this.to = to;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void addCondition(Condition condition) {
        conditions.add(condition);
    }

    public State getFrom() {
        return from;
    }

    public State getTo() {
        return to;
    }

    @Override
    public void run(StateMachine sm) {
        StateContext sc = sm.getStateContext();
        for (Action action : actions) {
            action.execute(sc);
        }
        if (to != null) {
            to.run(sm);
        }
    }

    public boolean evaluateCondition(StateContext sc) {
        for (Condition condition : conditions) {
            if (condition.isPermitted(sc)) {
                return false;
            }
        }

        return true;
    }
}
