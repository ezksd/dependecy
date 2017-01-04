package test;

import context.Context;
import junit.framework.Assert;

public class Test {
    @org.junit.Test
    public void test() {
        Context context = new Context("test");
        Person person = (Person) context.getBean("person");
        Assert.assertEquals("hehe", person.name);
        Assert.assertEquals("123456", person.number);
    }
}



