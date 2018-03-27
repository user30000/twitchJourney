package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class FunctionalFiniteStateMachineTest extends FunctionalFiniteStateMachine {
    public FunctionalFiniteStateMachineTest() {
        // Start state
        super("state1");
    }

    @Test
    public void run() {
        // state1
        super.run();
        // state2
        super.run();
        // state1
        super.run();
        // state2
        assertEquals("state2", super.getState());
    }

    public String state1() {
        assertEquals("state1", this.getState());

        // Change state1 to state2
        return "state2";
    }

    public String state2() {
        assertEquals("state2", this.getState());

        // Change state2 to state1
        return "state1";
    }
}