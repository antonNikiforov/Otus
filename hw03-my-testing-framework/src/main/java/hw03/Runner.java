package hw03;

import myAnnotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Runner {
    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, InvocationTargetException {
        Class<?> testClass = Framework.class;

        runOn(testClass);
    }

    private static void runOn(Class<?> testClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException {
        Map<String, Method> beforeAll = makeMapAnnotationsMethods(testClass, BeforeAll.class);
        Map<String, Method> beforeEach = makeMapAnnotationsMethods(testClass, BeforeEach.class);
        Map<String, Method> test = makeMapAnnotationsMethods(testClass, Test.class);
        Map<String, Method> afterEach = makeMapAnnotationsMethods(testClass, AfterEach.class);
        Map<String, Method> afterAll = makeMapAnnotationsMethods(testClass, AfterAll.class);
        try {
            beforeAll.get("interface myAnnotations.BeforeAll static void hw03.Framework.beforeAll()").invoke(null);
            for (Method t : test.values()) {
                Constructor<?> constructor = testClass.getConstructor();
                Object obj = constructor.newInstance();
                for (Method before : beforeEach.values())
                    before.invoke(obj);
                t.invoke(obj);
                for (Method after : afterEach.values())
                    after.invoke(obj);
            }
            afterAll.get("interface myAnnotations.AfterAll static void hw03.Framework.afterAll()").invoke(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Method> makeMapAnnotationsMethods(Class<?> testClass, Class<? extends Annotation> annotations) {
        Map<String, Method> test = new HashMap<>();
        for (Method i : testClass.getDeclaredMethods()) {
            if (i.isAnnotationPresent(annotations))
                test.put(annotations + " " + i, i);
        }
        return test;
    }

}


