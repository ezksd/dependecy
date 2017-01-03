package metadata;

import context.Context;

@FunctionalInterface
public interface FactoryFunc {
    Object produce(Context context) throws Throwable;
}
