package com.researchspace.figshare.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.test.context.ContextConfiguration;

/**
 * Composed annotation to configure beans for Spring tests
 * @author rspace
 *
 */
@ContextConfiguration(classes={SpringTestConfig.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FigshareSpringTest {

}
