package context;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.ASM4;

class MethodVi$itor extends MethodVisitor {
    private static final String beanDesc = Type.getDescriptor(Bean.class);
    private Context context;
    private FactoryMethod factoryMethod;
    private String beanName;
    private BeanType type;
    private int index;

    MethodVi$itor(Context context, FactoryMethod factoryMethod) {
        super(ASM4);
        this.context = context;
        this.factoryMethod = factoryMethod;
    }

    @Override
    public void visitParameter(String name, int access) {
        factoryMethod.setParamNames(index++, name);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return desc.equals(beanDesc) ? new AnnotationVisitor(ASM4) {
            @Override
            public void visit(String name, Object value) {
                if (value instanceof String) {
                    beanName = (String) value;
                }
            }

            @Override
            public void visitEnum(String name, String desc, String value) {
                type = BeanType.valueOf(value);
            }
        } : null;
    }

    @Override
    public void visitEnd() {
        if (beanName != null) {
            if (type == BeanType.PROTOTYPE) {
                context.addBean(beanName, factoryMethod);
            } else {
                context.addBean(beanName, new BeanWrapper(factoryMethod));
            }
        }
    }
}
