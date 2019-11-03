package com.nvk.loadmore;

import android.content.Context;
import android.net.Network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.HashMap;

public class NguoiChoiLoader extends AsyncTaskLoader<String> {
    private int page;
    private int limit;



    public NguoiChoiLoader(@NonNull Context context,int page,int limit) {
        super(context);
        this.page = page;
        this.limit = limit;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        HashMap<String,String> queryParams = new HashMap<>();
        queryParams.put("page",Integer.toString(this.page));
        queryParams.put("limit",Integer.toString(this.limit));
        String json = "";
        try{
            json = NetWork.connect("nguoi_choi","GET",queryParams);
        }catch (Exception err){

        }
        return json ;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
