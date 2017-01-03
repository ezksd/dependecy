package context;

import metadata.FactoryMethod;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static context.Utils.isPublic;
import static org.objectweb.asm.Opcodes.ASM4;

public class ClassVi$itor extends ClassVisitor {
    int accFlag;
    String className;
    Context context;
    ClassVi$itor(Context context) {
        super(ASM4);
        this.context = context;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.accFlag = access;
        className = name.replace('/', '.');
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return isPublic(accFlag) && isPublic(access) ? new MethodVi$itor(context, new FactoryMethod(className, name, access, desc)) : null;
    }

}
