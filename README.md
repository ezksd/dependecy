#基于ASM的依赖注入实现

通过方法参数获取依赖，编译需要加上参数`-parameters`。

#如何使用
为方法、构造器加上`@Bean(value,type)`注解，value为Bean名称，type参见`BeanType`默认为`SINGLETON`

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

`Context`参数为包全限定名或者类全限定名