package util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FunctionalFiniteStateMachine {
    public static class Do {
        public enum Action{
            NOTHING,
            SWAP,
            SWAP_AND_DO
        }

        private final Action action;
        private final String state;

        private Do(Action action) {
            this.action = action;
            this.state = "";
        }

        private Do(Action action, String state) {
            this.action = action;
            this.state = state;
        }


        public static Do nothing() {
            return new Do(Action.NOTHING);
        }

        public static Do swap_to(String state) {
            return new Do(Action.SWAP, state);
        }

        public static Do swap_and_do(String state) {
            return new Do(Action.SWAP_AND_DO, state);
        }
    }

    private String state;
    private Do doing;

    protected FunctionalFiniteStateMachine(String state) {
        this.state = state;
        this.doing = Do.nothing();
    }

    public void step() {
        try {
            action();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void action() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Class c = this.getClass();
        Method method;
        method = c.getDeclaredMethod(state);
        assert method.getReturnType().equals(doing.getClass());
        this.doing = (Do)method.invoke(this);
        switch (this.doing.action) {
            case NOTHING:
                break;
            case SWAP:
                this.state = this.doing.state;
                break;
            case SWAP_AND_DO:
                this.state = this.doing.state;
                this.action();
                break;
        }
    }

    public String getState() {
        return state;
    }
}
