package org.huxizhijian.hhcomicviewer2.utils;

import android.os.Environment;

/**
 * 常量
 * Created by wei on 2016/8/20.
 */

public class Constants {
    public static final String HHCOMIC_URL = "http://www.popomh.com/"; //主站网址
    public static final String COMIC_VOL_PAGE = "http://www.huhudm.com/mh/hu"; //漫画章节网址
    public static final String ENCODE_KEY = "tazsicoewrn"; //解码关键字
    public static final String PIC_SERVICE_URL = "http://104.237.55.123:9393/dm"; //图片服务器
    public static final String SEARCH_URL = "http://ssooff.com/"; //搜索网站
    public static final String ACTION_CLASSIFIES = "action_classifies"; //标记为获取分类列表行
    public static final String NO_NETWORK = "没有网络!";

    public static final int DOWNLOADING_NOTIFICATION_ID = 0x512;
    public static final int FINISHED_NOTIFICATION_ID = 0x1024;

    public static final String DEFAULT_DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getPath() + "/HHComic";

    public static final String CLASSIFIES_CONTENT = " <div>\n" +
            "<span><a href='/comic/class_1.html'>萌系</a></span><span><a href='/comic/class_2.html'>搞笑</a></span><span><a href='/comic/class_3.html'>格斗</a></span><span><a href='/comic/class_4.html'>科幻</a></span><span><a href='/comic/class_5.html'>剧情</a></span><span><a href='/comic/class_6.html'>侦探</a></span><span><a href='/comic/class_7.html'>竞技</a></span>" +
            "<span><a href='/comic/class_8.html'>魔法</a></span><span><a href='/comic/class_9.html'>神鬼</a></span><span><a href='/comic/class_10.html'>校园</a></span><span><a href='/comic/class_11.html'>惊栗</a></span><span><a href='/comic/class_12.html'>厨艺</a></span><span><a href='/comic/class_13.html'>伪娘</a></span><span><a href='/comic/class_15.html'>冒险</a></span>" +
            "<span><a href='/comic/class_19.html'>小说</a></span><span><a href='/comic/class_20.html'>港漫</a></span><span><a href='/comic/class_21.html'>耽美</a></span><span><a href='/comic/class_22.html'>经典</a></span><span><a href='/comic/class_23.html'>欧美</a></span><span><a href='/comic/class_24.html'>日文</a></span><span><a href='/comic/class_25.html'>亲情</a></span></div>\n";


    public static final int DOWNLOAD_INIT = 0x0;
    public static final int DOWNLOAD_START = 0x1;
    public static final int DOWNLOAD_DOWNLOADING = 0x2;
    public static final int DOWNLOAD_FINISHED = 0x3;
    public static final int DOWNLOAD_PAUSE = 0x4;
    public static final int DOWNLOAD_ERROR = 0x5;
    public static final int DOWNLOAD_IN_QUEUE = 0x6;
    public static final int MSG_DOWNLOAD = 0x7;
}