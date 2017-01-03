package context;


import metadata.Bean;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
public class Context {
    HashMap<String, Object> map = new HashMap<>();

    public void addBean(String name, Object value) {
        map.put(name, value);
    }

    public Object getBean(String s) {
        Object t = map.get(s);
        return t instanceof BeanWrapper ? ((BeanWrapper) t).get(this) : null;
    }

    @Bean("hehe")
    public Context(String s) {
        String path = s.replace('.', '/') + ".class";
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try(InputStream in=classloader.getResourceAsStream(path)){
            resolve(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void resolve(InputStream in) throws IOException {
        ClassReader cr = new ClassReader(in);
        cr.accept(new ClassVi$itor(this),ClassReader.SKIP_CODE);
    }


    public static void main(String[] args){
        Context context = new Context("context.Context");
    }

}
