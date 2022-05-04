package clan.midnight.sm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class State implements Activity {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String name;
    private StateStatus status;
    private final List<StateAction<?, ?>> preStateActions = new ArrayList<>();
    private final List<StateAction<?, ?>> postStateActions = new ArrayList<>();

    public State() {
        this("");
    }

    public State(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run(StateMachine sm) {
        sm.setCurrentState(this);
        status = StateStatus.NOT_STARTED;
        runPreStateActions(sm);
        status = StateStatus.STARTED;
        runPostStateActions(sm);
        status = StateStatus.ENDED;
        List<Transition> transitions = getAvailableTransitions(sm);
        if (transitions.isEmpty()) {
            return;
        }
        Transition nextTransition = selectTransition(transitions, sm.getStateContext());
        if (nextTransition != null) {
            nextTransition.run(sm);
        }
    }

    private void runPreStateActions(StateMachine sm) {
        for (StateAction<?, ?> sa : preStateActions) {
            try {
                sa.execute(sm.getStateContext());
            } catch (Exception e) {
                logger.error("PreStateAction {} execute error!", sa.getClass(), e);
            }
        }
    }

    private void runPostStateActions(StateMachine sm) {
        for (StateAction<?, ?> sa : postStateActions) {
            try {
                sa.execute(sm.getStateContext());
            } catch (Exception e) {
                logger.error("PostStateAction {} execute error!", sa.getClass(), e);
            }
        }
    }

    private List<Transition> getAvailableTransitions(StateMachine sm) {
        if (this instanceof End) {
            return Collections.emptyList();
        }

        return sm.getAvailableTransition(this);
    }

    public Transition selectTransition(List<Transition> transitions, StateContext sc) {
        if (transitions.size() == 1) return transitions.get(0);
        for (Transition transition : transitions) {
            if (transition.evaluateCondition(sc)) {
                return transition;
            }
        }
        return null;
    }

    public void addPreStateAction(StateAction<?, ?> preStateAction) {
        preStateActions.add(preStateAction);
    }

    public void addPostStateAction(StateAction<?, ?> postStateAction) {
        postStateActions.add(postStateAction);
    }
}
