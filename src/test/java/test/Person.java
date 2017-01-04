package test;
import context.Bean;

public class Person {
    public String name;
    public String number;
    public Person() {

    }

    @Bean("person")
    public Person(String name, String number) {
        this.name = name;
        this.number = number;
    }

    @Bean("name")
    public String fun1() {
        return "hehe";
    }

    @Bean("number")
    public String fun2() {
        return "123456";
    }
}
