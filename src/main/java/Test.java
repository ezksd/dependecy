import metadata.Bean;
import context.Context;
import org.objectweb.asm.Opcodes;


public class Test implements Opcodes {

    @Bean("test")
    public static Test fun() {
        return new Test("123");
    }

    public Test(String s) {

    }

    static void fun1(Object... objects) {
        System.out.println(objects.length);
    }

    public static void main(String[] args) {
        Context context = new Context("Person");
        Person test = (Person) context.getBean("person");
        System.out.println(test.name);
        System.out.println(test.number);
    }

}



