package util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FunctionalFiniteStateMachine {
    private String state;

    protected FunctionalFiniteStateMachine(String state) {
        this.state = state;
    }

    public void run() {
        try {
            Class c = this.getClass();
            Method method;
            method = c.getDeclaredMethod(state);
            assert method.getReturnType().equals(state.getClass());
            this.state = (String)method.invoke(this);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public String getState() {
        return state;
    }
}
