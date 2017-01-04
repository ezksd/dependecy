package context;

import org.objectweb.asm.Type;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static org.objectweb.asm.Opcodes.ACC_STATIC;

class FactoryMethod {
    private String className;
    private String methodName;
    private int access;
    private String desc;
    private String[] paramNames;
    private MethodHandle handle;

    FactoryMethod(String className, String methodName, int access, String desc) {
        this.className = className;
        this.methodName = methodName;
        this.access = access;
        this.desc = desc;
        int length = Type.getArgumentTypes(desc).length;
        paramNames = new String[length];
    }

    void setParamNames(int index, String className) {
        paramNames[index] = className;
    }

    private void resolve() {
        Class<?> klass = null;
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try {
            klass = classloader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        MethodType methodType = MethodType.fromMethodDescriptorString(desc, classloader);
        try {
            if (methodName.equals("<init>")) {
                handle = lookup.findConstructor(klass, methodType).asSpreader(Object[].class, paramNames.length);
            } else if (isStatic(access)) {
                handle = lookup.findStatic(klass, methodName, methodType).asSpreader(Object[].class, paramNames.length);
            } else {
                MethodHandle constructor = null;
                try {
                    constructor = lookup.findConstructor(klass, MethodType.methodType(void.class));
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("need a default constructor", e.getCause());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("constructor should be public", e.getCause());
                }
                Object o = null;
                try {
                    o = constructor.invoke();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                handle = lookup.findVirtual(klass, methodName, methodType).asSpreader(Object[].class, paramNames.length).bindTo(o);
            }
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    Object produce(Context context) {
        if (handle == null)
            resolve();

        int length = paramNames.length;
        Object[] parameters = new Object[length];

        for (int i = 0; i < length; i++) {
            parameters[i] = context.getBean(paramNames[i]);
        }

        try {
            return handle.invoke(parameters);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    private boolean isStatic(int acc) {
        return (acc & ACC_STATIC) != 0;
    }

}
