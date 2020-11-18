package com.leyou.item.validator;

import com.leyou.item.annotation.Mymin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Package: com.leyou.item.validator
 * Description：
 * Author: wude
 * Date:  2020-11-17 22:24
 * Modified By:
 */
public class MyconstraintValidator implements ConstraintValidator<Mymin,Object> {
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if ((Integer)o<5) {
            return false;
        }
        return true;
    }
    @Override
    public void initialize(Mymin constraintAnnotation) {
        System.out.println("初始化约束验证器");
    }
}
