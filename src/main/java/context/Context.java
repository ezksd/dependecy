package context;


import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Context {
    private HashMap<String, Object> map = new HashMap<>();
    private SimpleStack<String> stack = new SimpleStack<>();

    public void addBean(String name, Object value) {
        map.put(name, value);
    }


    public Object getBean(String s) {
        if (stack.contains(s)) {
            throw new RuntimeException("circular dependency");
        }
        stack.push(s);
        if(!map.containsKey(s))
            throw new RuntimeException("can't find bean :" + s);
        Object t = map.get(s);
        Object result;
        if (t instanceof BeanWrapper) {
            result = ((BeanWrapper) t).get(this);
        } else {
            result = t;
        }
        stack.pop();
        return result;
    }

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

    static class SimpleStack<E>{
        static class Floor<E>{
            E val;
            Floor<E> next;

            Floor(E val, Floor<E> next) {
                this.val = val;
                this.next = next;
            }
        }

        Floor<E> top;

        void push(E val) {
            top = new Floor<E>(val, top);
        }

        E pop() {
            if (top == null) {
                throw new RuntimeException("empty stack");
            } else {
                E t = top.val;
                top = top.next;
                return t;
            }
        }

        boolean contains(E e) {
            Floor<E> temp = top;
            while (temp != null) {
                if (temp.val.equals(e)) {
                    return true;
                } else {
                    temp = temp.next;
                }
            }
            return false;
        }
    }


}
