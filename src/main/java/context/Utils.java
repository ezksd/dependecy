package context;

import static org.objectweb.asm.Opcodes.*;
public class Utils {
    public static boolean isPublic(int acc) {
        return (acc & ACC_PUBLIC) != 0;
    }

    public static boolean isStatic(int acc) {
        return (acc & ACC_STATIC) != 0;
    }
}
