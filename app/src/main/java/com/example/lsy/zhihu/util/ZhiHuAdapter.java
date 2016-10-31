package com.example.lsy.zhihu.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lsy.zhihu.R;
import com.example.lsy.zhihu.bean.Story;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsy on 16-10-29.
 */

public class ZhiHuAdapter extends RecyclerView.Adapter<ZhiHuAdapter.ViewHolder> {
    protected List<Story> storyList;
    protected ItemOnClick itemOnClick=null;

    public ItemOnClick getItemOnClick() {
        return itemOnClick;
    }

    public void setItemOnClick(ItemOnClick itemOnClick) {
        this.itemOnClick = itemOnClick;
    }

    public List<Story> getStoryList() {
        return storyList;
    }

    public void setStoryList(List<Story> storyList) {
        this.storyList=null;
        this.storyList = storyList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(storyList.get(position).getTitle());
        Glide.with(holder.itemView.getContext()).load(storyList.get(position).getImages().get(0))
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return storyList == null ? 0 : storyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.thumbnail_image)
        ImageView imageView;

        @BindView(R.id.zhihu_title)
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemOnClick!=null){
                itemOnClick.onClick(v,getAdapterPosition());
            }
        }
    }
}
