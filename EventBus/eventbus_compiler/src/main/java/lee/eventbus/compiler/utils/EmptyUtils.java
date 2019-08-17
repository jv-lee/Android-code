package lee.eventbus.compiler.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @author jv.lee
 * @date 2019-08-17
 * @description
 */
public class EmptyUtils {

    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
