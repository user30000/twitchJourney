package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class FunctionalFiniteStateMachineTest extends FunctionalFiniteStateMachine {
    private boolean step1 = true;
    private boolean step2 = true;
    public FunctionalFiniteStateMachineTest() {
        // Start state
        super("state1");
    }

    @Test
    public void step() {
        assertEquals("state1", super.getState());
        super.step();
        assertEquals("state2", super.getState());
        super.step();
        assertEquals("state1", super.getState());
        super.step();
        assertEquals("state3", super.getState());
        super.step();
        assertEquals("state3", super.getState());
        super.step();
        assertEquals("state3", super.getState());
    }

    public Do state1() {
        if(step1) {
            return Do.swap_to("state2");
        } else {
            System.out.println(1);
            return Do.swap_to("state3");
        }
    }

    public Do state2() {
        step1 = false;
        return Do.swap_to("state1");
    }

    public Do state3() {
        if (step2) {
            step2 = false;
            return Do.nothing();
        } else {
            return Do.swap_and_do("state4");
        }
    }

    public Do state4() {
        return Do.swap_and_do("state1");
    }
}