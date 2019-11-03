package com.nvk.loadmore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<String> {
    private RecyclerView rcvNguoiChoi;
    private static final List<NguoiChoi> nguoiChois = new ArrayList<>();;
    private NguoiChoiAdapter adapter;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;
    private int totalPage;
    private static final int PAGE_SIZE = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radiation();
        //startLoader();
        loadData(null);
        setApdater();
        checkScroll();

    }

    private void checkScroll() {
        rcvNguoiChoi.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) rcvNguoiChoi.getLayoutManager();
                //item hiển thị trên 1 màng hình: 11
                int visibleItemCount = layoutManager.getChildCount();
                //tổng 1 page đầu tiên 25
                int totalItemCount = layoutManager.getItemCount();
                //first 0
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (!isLoading && !isLastPage){
                    //Khi Scroll đến cuối
                    //ItemCount: 12
                    //first: 13
                    //total: 25
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE){
                        isLoading=true;
                        currentPage++;
                        nguoiChois.add(null);
                        // tính từ 0 cập nhật lại cái mới insert
                        adapter.notifyItemInserted(nguoiChois.size()-1);

                        Bundle data = new Bundle();
                        data.putInt("page",currentPage);
                        data.putInt("limit",PAGE_SIZE);
                        loadData(data);
                    }

                }


            }
        });

    }

    private void loadData(Bundle data) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null){
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        //Kiểm tra connected
        if (networkInfo != null && networkInfo.isConnected()){
            startLoader(data);
        }else{
            taoThongBao("Không Thể Kết nối Internet").show();
        }
    }

    private AlertDialog taoThongBao(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(s).setTitle("Lỗi");
        builder.setPositiveButton("Đồng Ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        return  builder.create();
    }

    private void setApdater() {
        adapter = new NguoiChoiAdapter(this,nguoiChois);
        rcvNguoiChoi.setLayoutManager(new LinearLayoutManager(this));
        rcvNguoiChoi.setAdapter(adapter);
    }

    private void startLoader(Bundle data) {
        //lần 1 khi khởi tạo không vào restart
        //lần 2 gọi restart
        if (getSupportLoaderManager().getLoader(0) != null){
            getSupportLoaderManager().restartLoader(0,data,this);
        }
        //khởi tạo loader
        getSupportLoaderManager().initLoader(0,data,this);

    }

    private void radiation() {
        rcvNguoiChoi = findViewById(R.id.rcvNguoiChoi);

    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        int page = 1;
        int limit =25;
        //lần đầu sẽ không vào đây
        if (args != null){
            page = args.getInt("page");
            limit = args.getInt("limit");
        }

        return new NguoiChoiLoader(this,page,limit);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        JSONObject objData = null;
        try {
            if (data == null){
                taoThongBao("Không thể kết nối server").show();
                return;
            }
            objData = new JSONObject(data);
            int total = objData.getInt("total");
            totalPage = total / PAGE_SIZE;
            if (total % PAGE_SIZE != 0){
                totalPage++;
            }
            JSONArray arrData = objData.getJSONArray("data");
            if (nguoiChois.size() > 0){
                nguoiChois.remove(nguoiChois.size() -1);
                int scrollPosition = nguoiChois.size();
                adapter.notifyItemRemoved(scrollPosition);

            }


            for (int i = 0; i < arrData.length(); i++) {
                JSONObject objItemData = arrData.getJSONObject(i);
                int id = objItemData.getInt("id");
                String tenDangNhap = objItemData.getString("ten_dang_nhap");
                int diemCaoNhat = objItemData.getInt("diem_cao_nhat");
                NguoiChoi nguoiChoi = new NguoiChoi();
                nguoiChoi.setId(id);
                nguoiChoi.setTenDangNhap(tenDangNhap);
                nguoiChoi.setDiemCaoNhat(diemCaoNhat);
                nguoiChois.add(nguoiChoi);
            }
            adapter.notifyDataSetChanged();

            isLoading = false;
            isLastPage = (currentPage == totalPage);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("AAAAAA",nguoiChois.get(0).getTenDangNhap());

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
