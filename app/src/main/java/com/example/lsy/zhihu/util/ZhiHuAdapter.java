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
    protected static final int TYPE_OF_NEW=1;
    protected static final int TYPE_OF_DATA=2;

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

    public void addStoryList(List<Story> list){
        if(storyList!=null){
            storyList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(storyList.get(position).getIsNew()){
            return TYPE_OF_NEW;
        }else{
            return TYPE_OF_DATA;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType==TYPE_OF_NEW){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
            return new ViewHolder(view,viewType);
        }else{
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_date,parent,false);
            return new ViewHolder(view,viewType);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(getItemViewType(position)==TYPE_OF_DATA){
            holder.dateText.setText(storyList.get(position).getSpaceText());
            return;
        }
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

        TextView dateText;

        public ViewHolder(View itemView,int viewType) {
            super(itemView);
            if(viewType==TYPE_OF_DATA) {
                dateText= (TextView) itemView.findViewById(R.id.new_date);
                return;
            }
            ButterKnife.bind(this, itemView);

//            imageView= (ImageView) itemView.findViewById(R.id.thumbnail_image);
//            textView= (TextView) itemView.findViewById(R.id.zhihu_title);

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
