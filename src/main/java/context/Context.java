package context;


import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;

public class Context {
    private HashMap<String, Object> map = new HashMap<>();
    private SimpleStack<String> stack = new SimpleStack<>();

    public Context(String s)  {
        String path = s.replace('.', '/');
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL resource = classloader.getResource(path);
        if (resource == null) {
            path = path + ".class";
            resource = classloader.getResource(path);
            if (resource == null) {
                throw new RuntimeException("file not exitst");
            } else {
                resolve(Paths.get(resource.getPath()));
            }

        } else {
            Path dir = Paths.get(resource.getPath());
            try {
                Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        if (!Files.isDirectory(file) && getExtention(file.toString()).equals("class")) {
                            resolve(file);
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void addBean(String name, Object value) {
        map.put(name, value);
    }

    public Object getBean(String s) {
        if (stack.contains(s)) {
            throw new RuntimeException("circular dependency");
        }
        stack.push(s);
        if (!map.containsKey(s))
            throw new RuntimeException("can't find bean :" + s);
        Object t = map.get(s);
        Object result;
        if (t instanceof BeanWrapper) {
            result = ((BeanWrapper) t).get(this);
        } else if (t instanceof FactoryMethod) {
            result = ((FactoryMethod) t).produce(this);
        } else {
            result = t;
        }
        stack.pop();
        return result;
    }

    private void resolve(Path path) {
        try (InputStream in = Files.newInputStream(path)) {
            ClassReader cr = new ClassReader(in);
            cr.accept(new ClassVi$itor(this), ClassReader.SKIP_CODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getExtention(String fileName) {
        int i = fileName.lastIndexOf('.');
        return i > 0 ? fileName.substring(i + 1) : "";
    }

    static class SimpleStack<E> {
        Floor<E> top;

        void push(E val) {
            top = new Floor<>(val, top);
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
            while (temp != null && !temp.val.equals(e)) {
                temp = temp.next;
            }
            return temp != null;
        }

        static class Floor<E> {
            E val;
            Floor<E> next;

            Floor(E val, Floor<E> next) {
                this.val = val;
                this.next = next;
            }
        }
    }


}
