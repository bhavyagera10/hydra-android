package be.ugent.zeus.hydra.common.converter;

import com.squareup.moshi.JsonQualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used to indicate that integers (0 or 1) should be parsed as a boolean.
 *
 * @author Niko Strijbol
 */
@Retention(RetentionPolicy.RUNTIME)
@JsonQualifier
public @interface IntBoolean {
}