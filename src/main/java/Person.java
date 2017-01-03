import metadata.Bean;

public class Person {
    String name;
    String number;

    @Bean("person")
    public Person(String name, String number) {
        this.name = name;
        this.number = number;
    }

    @Bean("name")
    public static String fun1() {
        return "hehe";
    }

    @Bean("number")
    public static String fun2() {
        return "1234567";
    }
}
