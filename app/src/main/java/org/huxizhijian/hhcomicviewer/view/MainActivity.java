package org.huxizhijian.hhcomicviewer.view;

import org.huxizhijian.hhcomic.viewmodel.HomeViewModel;
import org.huxizhijian.hhcomicviewer.R;
import org.huxizhijian.hhcomicviewer.view.base.ComicActivity;

/**
 * @author huxizhijian
 * @date 2018/11/20
 */
public class MainActivity extends ComicActivity<HomeViewModel> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
