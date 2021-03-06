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

package org.huxizhijian.hhcomicviewer.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.huxizhijian.hhcomicviewer.R;
import org.huxizhijian.hhcomicviewer.adapter.entity.ComicTabList;
import org.huxizhijian.hhcomicviewer.model.Comic;
import org.huxizhijian.hhcomicviewer.ui.entry.ComicDetailsActivity;
import org.huxizhijian.sdk.imageloader.ImageLoaderOptions;
import org.huxizhijian.sdk.imageloader.listener.ImageLoaderManager;

/**
 * Created by wei on 2017/1/13.
 */

public class RecommendDetailsAdapter extends RecyclerView.Adapter<RecommendDetailsAdapter.NewViewHolder> {

    private Context mContext;
    private ComicTabList mTabList;
    private LayoutInflater mInflater;

    private ImageLoaderManager mImageLoader = ImageLoaderOptions.getImageLoaderManager();

    public RecommendDetailsAdapter(Context context, ComicTabList tabList) {
        mContext = context;
        mTabList = tabList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public NewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewViewHolder(mInflater.inflate(R.layout.item_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final NewViewHolder vh, int position) {
        final Comic comic = mTabList.getComics().get(position);
        vh.title.setText(comic.getTitle());
        vh.desc.setText(comic.getAuthor());
        vh.info.setText(comic.getComicStatus());

        mImageLoader.displayThumbnail(mContext, comic.getThumbnailUrl(), vh.iv,
                R.mipmap.blank, R.mipmap.blank, 165, 220);

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ComicDetailsActivity.class);
                intent.putExtra("cid", comic.getCid());
                intent.putExtra("thumbnailUrl", comic.getThumbnailUrl());
                intent.putExtra("title", comic.getTitle());

                if (vh.iv.getDrawable() != null) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        //如果是android5.0及以上，开启shareElement动画
                        String transitionName = mContext.getString(R.string.image_transition_name);

                        ActivityOptions transitionActivityOptions = ActivityOptions
                                .makeSceneTransitionAnimation((Activity) mContext, vh.iv, transitionName);
                        mContext.startActivity(intent, transitionActivityOptions.toBundle());
                    } else {
                        mContext.startActivity(intent);
                    }
                } else {
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTabList.getComics().size();
    }

    class NewViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView title, desc, info;

        NewViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.imageView_item);
            title = (TextView) itemView.findViewById(R.id.tv_title_item);
            desc = (TextView) itemView.findViewById(R.id.tv_description_item);
            info = (TextView) itemView.findViewById(R.id.tv_read_info_item);
        }
    }
}
