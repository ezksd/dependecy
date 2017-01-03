package metadata;

import org.objectweb.asm.Type;

public class Parameter {
    String name;
    Type type;

    public Parameter(String name, Type type) {
        this.name = name;
        this.type = type;
    }
}
