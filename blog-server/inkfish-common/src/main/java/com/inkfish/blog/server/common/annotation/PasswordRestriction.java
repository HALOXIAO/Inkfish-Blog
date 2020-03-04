package com.inkfish.blog.server.common.annotation;

        import com.inkfish.blog.server.common.annotation.constraint.PasswordRestrictionConstraint;

        import javax.validation.Constraint;
        import javax.validation.Payload;
        import java.lang.annotation.*;

/**
 * @author HALOXIAO
 **/

@Constraint(validatedBy = PasswordRestrictionConstraint.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface PasswordRestriction {

    String message() default "password only can be number and letter";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
