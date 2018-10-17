package zqx.rj.com.lovecar.utils.gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * author：  HyZhan
 * created： 2018/10/14 12:36
 * desc：    TODO
 */

public class ParameterizedTypeBuilder implements ParameterizedType {

    private final Class raw;
    private final Type[] args;

    public ParameterizedTypeBuilder(Class raw, Type[] args) {
        this.raw = raw;
        this.args = args != null ? args : new Type[0];
    }

    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    @Override
    public Type getRawType() {
        return raw;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
