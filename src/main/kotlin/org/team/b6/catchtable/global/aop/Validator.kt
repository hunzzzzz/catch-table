package org.team.b6.catchtable.global.aop

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class Validator : ConstraintValidator<ValidCategory, Any> {
    private lateinit var enumValues: Array<out Enum<*>>

    override fun initialize(annotation: ValidCategory) {
        enumValues = annotation.enumClass.java.enumConstants
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext?) =
        if (value == null) true else enumValues.any { it.name == value.toString() }
}