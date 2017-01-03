package metadata;

import org.objectweb.asm.Type;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static context.Utils.isStatic;

public class FactoryMethod {
    private String className;
    private String methodName;
    private int access;
    private String desc;
    private String[] paramNames;
    MethodHandle methodHandle;

    public FactoryMethod(String className, String methodName, int access, String desc) {
        this.className = className;
        this.methodName = methodName;
        this.access = access;
        this.desc = desc;
        int length = Type.getArgumentTypes(desc).length;
        paramNames = new String[length];
    }

    public void setParamNames(int index, String className) {
        paramNames[index] = className;
    }



    public FactoryFunc resolve() {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        Class<?> klass = null;
        MethodHandle handle = null;
        try {
            klass = classloader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        MethodType methodType = MethodType.fromMethodDescriptorString(desc, classloader);
        int length = paramNames.length;
        Object[] parameters = new Object[length];


        try {
            if (methodName.equals("<init>")) {//constructor
                handle = lookup.findConstructor(klass, methodType);
            } else if (isStatic(access)) {   //static factoryMethod
                handle = lookup.findStatic(klass, methodName, methodType);
            } else {
                handle = lookup.findVirtual(klass, methodName, methodType);
            }
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            System.exit(1);
        }

        MethodHandle handle1 = handle;
        return c -> {
            for (int i = 0; i < length; i++) {
                parameters[i] = c.getBean(paramNames[i]);
            }
            return handle1.invokeWithArguments(parameters);
        };
    }
}
