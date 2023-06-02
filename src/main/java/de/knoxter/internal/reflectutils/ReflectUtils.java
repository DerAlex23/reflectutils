package de.knoxter.internal.reflectutils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectUtils {
    public static final String[] DEFAULT_METHOD_NAMES = new String[]{"getClass", "toString", "equals", "hashCode", "wait", "notify", "notifyAll"};

    public static <T> T parseObjectFromMap(Class<T> clazz, Map<String, Object> map) {
        T parsedObj = instantiateClass(clazz);
        Map<String, Method> setters = getMethodNameMap(clazz, "set");
        for (Map.Entry<String, Object> kvp : map.entrySet()) {
            String propName = kvp.getKey();
            if(!setters.containsKey(propName))
                throw new RuntimeException("No setter found for '"+kvp.getKey()+"'");
            Object value = kvp.getValue();
            try {
                setters.get(propName).invoke(parsedObj, value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException("Could not parse '"+kvp.getKey()+"': " + e.getMessage());
            }
        }
        return parsedObj;
    }

    public static Map<String, Method> getMethodNameMapIncludingDefaults(Class<?> clazz, String ... filter) {
        return getMethodNameMap(clazz, true, filter);
    }
    
    public static Map<String, Method> getMethodNameMap(Class<?> clazz, String ... filter) {
        return getMethodNameMap(clazz, false, filter);
    }

    private static Map<String, Method> getMethodNameMap(Class<?> clazz, boolean includeDefaults, String ... filter) {
        Map<String, Method> map = new HashMap<String, Method>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            String name = method.getName();
            if(!includeDefaults && isDefaultMethod(name))
                continue;
            for (String f : filter) {
                if(name.startsWith(f)) {
                    name = methodNameToPropertyName(name, f.length());
                    map.put(name, method);
                    break;
                }
            }
        }
        return map;
    }

    public static boolean isDefaultMethod(String methodName) {
        for (int i = 0; i < DEFAULT_METHOD_NAMES.length; i++) {
            if(DEFAULT_METHOD_NAMES[i].equals(methodName))
                return true;
        }
        return false;
    }

    private static String methodNameToPropertyName(String name, int offset) {
        String propName = name.substring(offset, offset +1).toLowerCase();
        if(name.length() > offset + 1)
            propName += name.substring(offset + 1);
        return propName;
    }

    public static <T> T clone(T obj) {
        Class<T> clazz = (Class<T>) obj.getClass();
        if(clazz.isPrimitive() || clazz == String.class)
            return obj;
        if(clazz.isAssignableFrom(List.class) || clazz.isAssignableFrom(Map.class))
            throw new RuntimeException("To clone lists or maps use apropriate functions");
        T clone = instantiateClass(clazz);
        Map<String, Method> setters = getMethodNameMap(clazz, "set");
        Map<String, Method> getters = getMethodNameMap(clazz, "get", "is");
        for (Map.Entry<String, Method> kvp : setters.entrySet()) {
            try {
                Method getMethod = getters.get(kvp.getKey());
                if(getMethod == null)
                    continue;
                Object value = getMethod.invoke(obj);
                kvp.getValue().invoke(clone, value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException("Could not clone '"+kvp.getKey()+"' object of type '"+clazz.getName()+"': " + e.getMessage());
            }
        }
        return clone;
    }

    public static void copyProperties(Object from, Object to) {
        if(from == null || to == null)
            return;
        Class<?> fromClass = from.getClass();
        Class<?> toClass = to.getClass();
        if(fromClass.isPrimitive() || toClass.isPrimitive())
            return;
        Map<String, Method> fromSetter = getMethodNameMap(fromClass, "set");
        Map<String, Method> fromGetter = getMethodNameMap(fromClass, "get", "is");
        Map<String, Method> toSetter = getMethodNameMap(toClass, "set");
        for (Map.Entry<String, Method> kvp : fromSetter.entrySet()) {
            try {
                Method getMethod = fromGetter.get(kvp.getKey());
                if(getMethod == null)
                    continue;
                Object value = getMethod.invoke(from);
                toSetter.get(kvp.getKey()).invoke(to, value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException("Could not copy '"+kvp.getKey()+"' of type '"+fromClass.getName()+"' to type '"+toClass.getName()+"': " + e.getMessage());
            }
        }
    }

    public static <K,V> Map<K,V> cloneMap(Map<K,V> obj) {
        Map<K,V> clone = new HashMap<K,V>();
        for (Map.Entry<K,V> kvp : obj.entrySet()) {
            K clonedKey = clone(kvp.getKey());
            V clonedValue = clone(kvp.getValue());
            clone.put(clonedKey, clonedValue);
        }
        return clone;
    }

    public static <E> List<E> cloneList(List<E> obj) {
        List<E> clone = new ArrayList<E>();
        for (E e : obj) {
            E clonedElement = clone(e);
            clone.add(clonedElement);
        }
        return clone;
    }

    public static <T> T instantiateGenericClass(Class<T> clazz) {
        T parsedObj;
        try {
            parsedObj = (T)clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Could not instantiate '" + clazz + "': " + e.getMessage());
        }
        return parsedObj;
    }

    public static <T> T instantiateClass(Class<T> clazz) {
        T parsedObj;
        try {
            parsedObj = (T)clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Could not instantiate '" + clazz + "': " + e.getMessage());
        }
        return parsedObj;
    }

    public static <T> T instantiateClass(Class<T> clazz, Object ... params) {
        Class<?>[] paramClasses = new Class<?>[params.length];
        for (int i = 0; i < paramClasses.length; i++)
            paramClasses[i] = params[i].getClass();
        try {
            return (T)clazz.getConstructor(paramClasses).newInstance(params);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("Could not instantiate '" + clazz + "' with " + params.length + " parameters : " + e.getMessage());
        }
    }

    public static Method getMethod(Class<?> clazz, String methodName) {
        try {
            return clazz.getMethod(methodName);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("Could not get method '" + methodName + "' of class " + clazz + ": " + e.getMessage());
        }
    }

    public static List<Class<?>> getGenericTypesOfMethodResult(Method method) {
        ParameterizedType genericType = (ParameterizedType)method.getGenericReturnType();
        Type[] types = genericType.getActualTypeArguments();
        List<Class<?>> list = new ArrayList<Class<?>>();
        for (int i = 0; i < types.length; i++) {
            if(types[i] instanceof Class<?>)
                list.add((Class<?>)types[i]);
        }
        return list;
    }

    public static Class<?> getGenericTypesOfMethodResult(Method method, int genericIndex) {
        if(genericIndex < 0)
            return null;
        List<Class<?>> classes = getGenericTypesOfMethodResult(method);
        if(genericIndex >= classes.size())
            return null;
        return classes.get(genericIndex);
    }
}
