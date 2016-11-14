package com.tools.joe.notiman.di.ui.ConfigActivity;

import com.tools.joe.notiman.di.AppComponent;
import com.tools.joe.notiman.di.scope.ActivityScope;
import com.tools.joe.notiman.ui.ConfigActivity;

import dagger.Component;

/**
 * Created by joe_wang on 2016/8/1.
 */
@ActivityScope
@Component (dependencies = AppComponent.class, modules = ConfigActivityModule.class)
public interface ConfigActivityComponent {
    void inject(ConfigActivity activity);
}
