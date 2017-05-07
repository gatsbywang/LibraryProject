package com.library.widget.titlebar;

/**
 * Builder设置模式，
 * Created by 花歹 on 2017/5/2.
 * Email:   gatsbywang@126.com
 */

public interface INavigationBar {

    /**
     * 绑定资源Id
     *
     * @return
     */
    public int bindLayoutId();


    /**
     * 构建NavigationBar
     */
    public void applyView();
}
