package com.nvk.loadmore;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NguoiChoiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<NguoiChoi> nguoiChois;

    private static  final int TYPE_VIEW_ITEM = 0;
    private static  final int TYPE_VIEW_LOADING = 1;

    public NguoiChoiAdapter(Context context, List<NguoiChoi> nguoiChois) {
        this.context = context;
        this.nguoiChois = nguoiChois;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_VIEW_ITEM){
            View view = LayoutInflater.from(context).inflate(R.layout.custom_item_nguoi_choi,parent,false);
            return new NguoiChoiHolder(view,this);
        }else if(viewType == TYPE_VIEW_LOADING){
            View view = LayoutInflater.from(context).inflate(R.layout.custom_item_loading,parent,false);
            return new LoadingHolder(view);
        }
        return  null;


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NguoiChoi nguoiChoi = nguoiChois.get(position);
        if (holder instanceof NguoiChoiHolder){
            NguoiChoiHolder nguoiChoiHolder = (NguoiChoiHolder) holder;
            nguoiChoiHolder.tvTenDangNhap.setText(nguoiChoi.getTenDangNhap());
            nguoiChoiHolder.tvDiemCaoNhat.setText(nguoiChoi.getDiemCaoNhat()+"");
        }else if(holder instanceof LoadingHolder){
            //LoadingHolder loadingHolder = (LoadingHolder) holder;
        }

    }


    @Override
    public int getItemCount() {
        return nguoiChois.size();
    }

    @Override
    public int getItemViewType(int position) {
        return nguoiChois.get(position) == null ? TYPE_VIEW_LOADING : TYPE_VIEW_ITEM;
    }

    class NguoiChoiHolder extends RecyclerView.ViewHolder{
        private TextView tvTenDangNhap,tvDiemCaoNhat;
        private NguoiChoiAdapter adapter;
        public NguoiChoiHolder(@NonNull View itemView,NguoiChoiAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            tvTenDangNhap = itemView.findViewById(R.id.tvTenDangNhap);
            tvDiemCaoNhat = itemView.findViewById(R.id.tvDiemCaoNhat);
        }
    }

    class LoadingHolder extends RecyclerView.ViewHolder{
        private ProgressBar pgbLoading;
        public LoadingHolder(@NonNull View itemView) {
            super(itemView);
            pgbLoading = itemView.findViewById(R.id.pgbLoading);

        }
    }

}