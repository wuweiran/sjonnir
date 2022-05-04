package clan.midnight.sm;

import java.util.List;
import java.util.ArrayList;

public class StateMachine {
    private final StateTemplate st;
    private State currentState;
    private StateContext sc;

    public StateMachine(StateTemplate stateTemplate) {
        this.st = stateTemplate;
    }

    public void execute() {
        this.st.getStart().run(this);
    }

    public void setStateContext(StateContext sc) {
        this.sc = sc;
    }

    public StateContext getStateContext() {
        return sc;
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public List<Transition> getAvailableTransition(State state) {
        List<Transition> transitions = new ArrayList<>();
        List<Activity> activities = this.st.getActivities();
        activities.forEach(a -> {
            if (a instanceof Transition && ((Transition) a).getFrom().getName().equals(state.getName())) {
                transitions.add((Transition) a);
            }
        });
        return transitions;
    }
}
