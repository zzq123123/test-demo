package com.leyou.item.annotation;

import com.leyou.item.validator.MyconstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Package: com.leyou.item.annotation
 * Description：
 * Author: wude
 * Date:  2020-11-17 22:14
 * Modified By:
 */
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)//在jvm中 可以被反射获取
@Constraint(validatedBy = MyconstraintValidator.class)
public @interface Mymin {
    String message() default "";

    Class<?>[] groups() default {};//

    Class<? extends Payload>[] payload() default {}; //




}
