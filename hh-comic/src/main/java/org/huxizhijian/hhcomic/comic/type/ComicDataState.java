/*
 * Copyright 2017 huxizhijian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.huxizhijian.hhcomic.comic.type;

/**
 * Comic的是否保存为历史、收藏、下载的状态表示类
 *
 * @author huxizhijian 2017/9/21
 */
public enum ComicDataState {
    /**
     * 历史状态
     */
    HISTORY,
    /**
     * 收藏状态
     */
    FAVORITE,
    /**
     * 下载状态
     */
    DOWNLOADED
}
