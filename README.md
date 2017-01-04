#基于`ASM`的依赖注入实现

通过方法参数获取依赖，编译需要加上参数`-parameters`。

#如何使用
为方法、构造器加上`@Bean(value,type)`注解，value为Bean名称，type参见`BeanType`默认为`SINGLETON`，如：
```java
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

```
`Context`参数为包全限定名或者类全限定名

```java
package test;
import context.Context;

public class Test {
    public static void main(String[] args){
        Context context = new Context("test");
        Person person = (Person) context.getBean("person");
        System.out.println(person.name);
        System.out.println(person.number);
    }
}
```

