package ew.sr.x1c.quilt.meow.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventBus {

    private final Map<Class<?>, Map<Byte, Map<Object, Method[]>>> byListenerAndPriority;
    private final Map<Class<?>, EventHandlerMethod[]> byEventBake;
    private final Lock lock;
    private final Logger logger;

    public EventBus() {
        this(null);
    }

    public EventBus(Logger logger) {
        byListenerAndPriority = new HashMap<>();
        byEventBake = new ConcurrentHashMap<>();
        lock = new ReentrantLock();

        this.logger = logger == null ? Logger.getLogger(Logger.GLOBAL_LOGGER_NAME) : logger;
    }

    public void post(Object event) {
        EventHandlerMethod[] handler = byEventBake.get(event.getClass());
        if (handler != null) {
            for (EventHandlerMethod method : handler) {
                try {
                    method.invoke(event);
                } catch (IllegalAccessException ex) {
                    throw new Error("方法無法存取 : " + event, ex);
                } catch (IllegalArgumentException ex) {
                    throw new Error("方法拒絕該目標 / 參數 : " + event, ex);
                } catch (InvocationTargetException ex) {
                    logger.log(Level.WARNING, MessageFormat.format("調度事件 {0} 於監聽器 {1} 時發生錯誤", event, method.getListener()), ex.getCause());
                }
            }
        }
    }

    private Map<Class<?>, Map<Byte, Set<Method>>> findHandler(Object listener) {
        Map<Class<?>, Map<Byte, Set<Method>>> handler = new HashMap<>();
        for (Method method : listener.getClass().getDeclaredMethods()) {
            EventHandler annotation = method.getAnnotation(EventHandler.class);
            if (annotation != null) {
                Class<?>[] parameter = method.getParameterTypes();
                if (parameter.length != 1) {
                    logger.log(Level.INFO, "方法 {0} 於類別 {1} 標註 {2} 沒有單一參數", new Object[]{
                        method, listener.getClass(), annotation
                    });
                    continue;
                }
                Map<Byte, Set<Method>> priorityMap = handler.get(parameter[0]);
                if (priorityMap == null) {
                    priorityMap = new HashMap<>();
                    handler.put(parameter[0], priorityMap);
                }
                Set<Method> priority = priorityMap.get(annotation.priority());
                if (priority == null) {
                    priority = new HashSet<>();
                    priorityMap.put(annotation.priority(), priority);
                }
                priority.add(method);
            }
        }
        return handler;
    }

    public void register(Object listener) {
        Map<Class<?>, Map<Byte, Set<Method>>> handler = findHandler(listener);
        lock.lock();
        try {
            for (Map.Entry<Class<?>, Map<Byte, Set<Method>>> handlerEntry : handler.entrySet()) {
                Map<Byte, Map<Object, Method[]>> priorityMap = byListenerAndPriority.get(handlerEntry.getKey());
                if (priorityMap == null) {
                    priorityMap = new HashMap<>();
                    byListenerAndPriority.put(handlerEntry.getKey(), priorityMap);
                }
                for (Map.Entry<Byte, Set<Method>> entry : handlerEntry.getValue().entrySet()) {
                    Map<Object, Method[]> currentPriorityMap = priorityMap.get(entry.getKey());
                    if (currentPriorityMap == null) {
                        currentPriorityMap = new HashMap<>();
                        priorityMap.put(entry.getKey(), currentPriorityMap);
                    }
                    Method[] bake = new Method[entry.getValue().size()];
                    currentPriorityMap.put(listener, entry.getValue().toArray(bake));
                }
                bakeHandler(handlerEntry.getKey());
            }
        } finally {
            lock.unlock();
        }
    }

    public void unregister(Object listener) {
        Map<Class<?>, Map<Byte, Set<Method>>> handler = findHandler(listener);
        lock.lock();
        try {
            for (Map.Entry<Class<?>, Map<Byte, Set<Method>>> entry : handler.entrySet()) {
                Map<Byte, Map<Object, Method[]>> priorityMap = byListenerAndPriority.get(entry.getKey());
                if (priorityMap != null) {
                    for (Byte priority : entry.getValue().keySet()) {
                        Map<Object, Method[]> currentPriority = priorityMap.get(priority);
                        if (currentPriority != null) {
                            currentPriority.remove(listener);
                            if (currentPriority.isEmpty()) {
                                priorityMap.remove(priority);
                            }
                        }
                    }
                    if (priorityMap.isEmpty()) {
                        byListenerAndPriority.remove(entry.getKey());
                    }
                }
                bakeHandler(entry.getKey());
            }
        } finally {
            lock.unlock();
        }
    }

    private void bakeHandler(Class<?> eventClass) {
        Map<Byte, Map<Object, Method[]>> handlerByPriority = byListenerAndPriority.get(eventClass);
        if (handlerByPriority != null) {
            List<EventHandlerMethod> handlerList = new ArrayList<>(handlerByPriority.size() * 2);
            byte value = Byte.MIN_VALUE;
            do {
                Map<Object, Method[]> handlerByListener = handlerByPriority.get(value);
                if (handlerByListener != null) {
                    handlerByListener.entrySet().forEach((listenerHandler) -> {
                        for (Method method : listenerHandler.getValue()) {
                            EventHandlerMethod ehm = new EventHandlerMethod(listenerHandler.getKey(), method);
                            handlerList.add(ehm);
                        }
                    });
                }
            } while (value++ < Byte.MAX_VALUE);
            byEventBake.put(eventClass, handlerList.toArray(new EventHandlerMethod[handlerList.size()]));
        } else {
            byEventBake.remove(eventClass);
        }
    }
}
