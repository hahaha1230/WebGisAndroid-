package com.example.hahaha.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hahaha.Class.MapTools;
import com.example.hahaha.R;

import java.util.List;

/**
 * Created by 佳佳 on 9/23/2018.
 */

public class ToolsAdapter  extends RecyclerView.Adapter<ToolsAdapter.ViewHolder> {

    private List<MapTools> mapTools;
    private OnItemClickListener mOnItemClickListener;
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView toolsImage;
        TextView toolsName;

        public ViewHolder (View view) {
            super(view);
            toolsImage=(ImageView)view.findViewById(R.id.toolsImage);
            toolsName=(TextView)view.findViewById(R.id.toolsName);
        }
    }

    public ToolsAdapter (List<MapTools> toolsList) {
        mapTools=toolsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.tools_item,parent,
                false);
       final ViewHolder holder=new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MapTools mMapTools=mapTools.get(position);
        holder.toolsName.setText(mMapTools.getToolName());
        holder.toolsImage.setImageResource(mMapTools.getToolImageId());


        final View itemView = (RelativeLayout) holder.itemView;

        if ( mOnItemClickListener!= null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClickListener(itemView,position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mapTools.size();
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mOnItemClickListener =clickListener;
    }
    /**
     * 定义点击事件的接口
     */
    public interface  OnItemClickListener {
       void onClickListener(View v,int position);
    }
}
