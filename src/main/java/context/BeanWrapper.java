package context;

import metadata.FactoryFunc;

public class BeanWrapper {
    Object value;
    FactoryFunc func;
    public BeanWrapper(FactoryFunc func) {
        this.func = func;
    }

    Object get(Context context) {
        if (value == null) {
            try {
                value = func.produce(context);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return value;
    }
}
