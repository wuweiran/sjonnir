package clan.midnight.sm;

import java.util.ArrayList;
import java.util.List;

public class StateTemplate {
    List<Activity> activities = new ArrayList<>();
    private Start start;
    private End end;

    public StateTemplate addStart(Start start) {
        if (this.start != null) {
            throw new StateMachineException("Duplicated START states");
        }

        this.start = start;
        activities.add(start);
        return this;
    }

    public StateTemplate addEnd(End end) {
        if (this.end != null) {
            throw new StateMachineException("Duplicated END states");
        }

        this.end = end;
        activities.add(end);
        return this;
    }

    public StateTemplate addState(State state) {
        if (state instanceof Start) {
            return addStart((Start) state);
        }

        if (state instanceof End) {
            return addEnd((End) state);
        }

        activities.add(state);
        return this;
    }

    public StateTemplate addTransition(Transition transition) {
        activities.add(transition);
        return this;
    }

    public Start getStart() {
        return start;
    }

    public List<Activity> getActivities() {
        return activities;
    }
}
