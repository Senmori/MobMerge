package net.senmori.mobmerge.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface EntityCondition {

    /**
     * A short description of what the condition tests for.<br>
     * This is used for displaying conditions in-game.
     */
    String description() default "";

    /**
     * A default condition is one which will always run, regardless of an entity's type.
     * <br>
     * Because default conditions are run on every entity, they should return {@code true} if the condition<br>
     * does not apply to said entity.
     */
    boolean defaultCondition() default false;
}
