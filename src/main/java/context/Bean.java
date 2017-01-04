package context;

public @interface Bean {
    String value();
    BeanType type() default BeanType.SINGLETON;
}
