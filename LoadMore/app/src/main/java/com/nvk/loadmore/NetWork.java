package com.nvk.loadmore;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NetWork {
    public static final String BASE ="http://192.168.1.14:8000/public/api/";
    public static String connect(String uri, String method, HashMap<String,String> params){
        Uri.Builder builder = Uri.parse(BASE+uri).buildUpon();
        //thêm dòng này
        //loop như foreach
        // key: page , value: page(int)-> 1
        // key: limit, value: limit(int)-> 25
        // loop 2 lần
        // Lần 1
        //bulder: http://.../nguoi_choi?page=1
        //lần 2
        //bulder: http://.../nguoi_choi?page=1&limit=25
        for (Map.Entry<String,String> pa :params.entrySet()){
            builder.appendQueryParameter(pa.getKey(),pa.getValue());
        }
        //uriBuilt = http://192.168.1.14:8000/public/api/nguoi_choi?page=1&limit=25
        Uri uriBuilt = builder.build();
        String json= null;
        HttpURLConnection connection=null;
        try {
            //uri phải giống với trên URL của trình duyệt
            URL url = new URL(uriBuilt.toString());
            connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            Log.d("AAAAAA",connection+"");
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            StringBuilder saveJson = new StringBuilder();
            while ((line = reader.readLine()) != null){
                saveJson.append(line);
                saveJson.append("\n");
            }
            json = saveJson.toString();
            Log.d("AAAAAA",json+"");
            inputStream.close();
            reader.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return  json;
    }

}
