package com.tools.joe.notiman.di.ui.NotiListActivity;

import com.tools.joe.notiman.di.AppComponent;
import com.tools.joe.notiman.di.scope.ActivityScope;
import com.tools.joe.notiman.ui.NotiListActivity;

import dagger.Component;

/**
 * Created by joe_wang on 2016/8/5.
 */
@ActivityScope
@Component(dependencies = AppComponent.class,
        modules = NotiListActivityModule.class)
public interface NotiListActivityComponent {
    void inject(NotiListActivity activity);
}
