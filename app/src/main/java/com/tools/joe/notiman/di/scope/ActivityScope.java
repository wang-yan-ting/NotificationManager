package com.tools.joe.notiman.di.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by joe_wang on 2016/8/1.
 */
@Scope
@Retention(RUNTIME)
public @interface ActivityScope {
}
