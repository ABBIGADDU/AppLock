package com.example.user1.applock;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user1.applock.project.model.AppInfoModel;

import java.util.List;

public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.ViewHolder> {

    private Context mContext;
    private List<AppInfoModel> lstResolveInfos;
    private onItemClickListener mOnItemClickListener;

    public AppInfoAdapter(Context context, List<AppInfoModel> pkgAppsList, onItemClickListener onItemClickListener) {
        this.mContext = context;
        this.lstResolveInfos = pkgAppsList;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AppInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v1 = inflater.inflate(R.layout.apps_info_child, viewGroup, false);
        return new ViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(@NonNull AppInfoAdapter.ViewHolder viewHolder, final int position) {
        AppInfoModel rsInfo = lstResolveInfos.get(position);
        viewHolder.ivItemImg.setImageDrawable(rsInfo.getAppIcon());
        viewHolder.tvItemName.setText(rsInfo.getAppName());
        viewHolder.ivItemStatus.setImageDrawable(ContextCompat.getDrawable(mContext, rsInfo.isLocked() ? R.drawable.locked : R.drawable.unlocked));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.itemSelected(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return lstResolveInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivItemImg;
        TextView tvItemName;
        ImageView ivItemStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivItemImg = itemView.findViewById(R.id.iv_Item_image);
            tvItemName = itemView.findViewById(R.id.tv_Item_Name);
            ivItemStatus = itemView.findViewById(R.id.iv_lock_status);
        }
    }

    public interface  onItemClickListener{
        void itemSelected(int position);
    }
}
