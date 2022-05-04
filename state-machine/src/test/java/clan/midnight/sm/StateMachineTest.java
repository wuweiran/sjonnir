package clan.midnight.sm;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class StateMachineTest {
    @Test
    void sampleTest() {
        StateTemplate st = new StateTemplate();
        Start start = new Start();
        State a = new State("stateA");
        State b = new State("stateB");
        State c = new State("stateC");
        State d = new State("stateD");
        State e = new State("stateE");
        End end = new End();
        Transition start2A = new Transition(start, a);
        Transition a2B = new Transition(a, b);
        Transition b2C = new Transition(a, c);
        Transition b2E = new Transition(b, e);
        Transition c2D = new Transition(c, d);
        Transition e2D = new Transition(e, d);
        Transition e2End = new Transition(e, end);
        st.addStart(start).addState(a).addState(b).addState(c).addState(d).addState(e).addEnd(end)
                .addTransition(start2A).addTransition(a2B).addTransition(b2C).addTransition(b2E)
                .addTransition(c2D).addTransition(e2D).addTransition(e2End);
        b.addPreStateAction(sc -> {
            sc.put("a", 2);
            sc.put("b", 2);
        });
        e.addPostStateAction(sc -> sc.put("e", true));
        b2C.addCondition(sc -> Objects.equals(1, sc.get("a")) && Objects.equals(1, sc.get("b")));
        b2E.addCondition(sc -> Objects.equals(2, sc.get("a")) && Objects.equals(2, sc.get("b")));
        e2D.addAction(sc -> sc.put("d", false));

        StateMachine sm = new StateMachine(st);
        StateContext sc = new StateContext() {
            private final HashMap<String, Object> map = new HashMap<>();

            @Override
            public Object get(String key) {
                return map.get(key);
            }

            @Override
            public void put(String key, Object value) {
                map.put(key, value);
            }

            @Override
            public void remove(String key) {
                map.remove(key);
            }
        };
        sm.setStateContext(sc);
        sm.execute();

        assertEquals(sc.get("a"), 2);
        assertEquals(sc.get("b"), 2);
        assertEquals(sc.get("e"), true);
        assertEquals(sc.get("d"), false);
    }
}