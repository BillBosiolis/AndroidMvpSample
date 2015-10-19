package com.example.androidmvpsample.di.scope;

import java.lang.annotation.Retention;
import javax.inject.Scope;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Bill on 17/10/2015.
 */
@Scope
@Retention(RUNTIME)
public @interface PerActivity {
}
