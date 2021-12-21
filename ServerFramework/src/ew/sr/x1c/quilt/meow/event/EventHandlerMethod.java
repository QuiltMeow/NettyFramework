package ew.sr.x1c.quilt.meow.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class EventHandlerMethod {

    @Getter
    private Object listener;

    @Getter
    private Method method;

    public void invoke(Object event) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        method.invoke(listener, event);
    }
}
