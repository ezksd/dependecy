import context.Context;


public class Test {

    public static void main(String[] args) {
        Context context = new Context("Person");
        Person test = (Person) context.getBean("person");
        System.out.println(test.name);
        System.out.println(test.number);
    }

}



