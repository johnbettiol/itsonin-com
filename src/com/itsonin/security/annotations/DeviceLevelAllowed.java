package com.itsonin.security.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.itsonin.enums.DeviceLevel;

/**
 * @author nkislitsin
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)

public @interface DeviceLevelAllowed {
    public DeviceLevel[] value();
}
