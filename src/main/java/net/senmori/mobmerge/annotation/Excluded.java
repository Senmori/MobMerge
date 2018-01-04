package net.senmori.mobmerge.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is primarily used for determining which classes should NOT be serialized.
 * <br>
 * This annotation can only be used on {@link ElementType#TYPE} types.<br>
 * All other types can be ignored via the correct setting of {@link com.google.gson.GsonBuilder}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Excluded {

    String reason() default "";
}
