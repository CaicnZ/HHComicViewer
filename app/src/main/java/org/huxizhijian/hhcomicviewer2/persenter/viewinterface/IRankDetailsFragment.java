package org.huxizhijian.hhcomicviewer2.persenter.viewinterface;

import org.huxizhijian.hhcomicviewer2.model.Comic;

import java.util.List;

/**
 * Created by wei on 2017/1/20.
 */

public interface IRankDetailsFragment {

    void onSuccess(List<Comic> comics);

    void onException(Throwable e);

    void onFailure(int errorCode, String errorMsg);

}
