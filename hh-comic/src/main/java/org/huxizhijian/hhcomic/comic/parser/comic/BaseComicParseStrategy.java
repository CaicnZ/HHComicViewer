package org.huxizhijian.hhcomic.comic.parser.comic;

import okhttp3.Request;

/**
 * @author huxizhijian
 * @date 2017/10/9
 */
public abstract class BaseComicParseStrategy implements ComicParseStrategy {

    protected Request getRequestGetAndWithUrl(String url) {
        return new Request.Builder().url(url).get().build();
    }

}
