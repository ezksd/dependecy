package context;

class BeanWrapper {
    private Object value;
    private FactoryMethod method;

    BeanWrapper(FactoryMethod method) {
        this.method = method;
    }

    Object get(Context context) {
        if (value == null) {
            value = method.produce(context);
        }
        return value;
    }
}
