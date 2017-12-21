package com.borui.qa;

import android.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Build;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.http.util.EncodingUtils;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

/**
 * Created by zhangpeng on 2017/12/7.
 */

public class FragmentStatist extends Fragment {
    private String phone_model = Build.MODEL;

    public static int getFileLineCount(String filename) {
        int cnt = 0;
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new FileReader(filename));
            @SuppressWarnings("unused")
            String lineRead = "";
            while ((lineRead = reader.readLine()) != null) {
            }
            cnt = reader.getLineNumber();
        } catch (Exception ex) {
            cnt = -1;
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return cnt;
    }

    //读SD中的文件
    public String readFileSdcardFile(String fileName) throws IOException {
        String res = "";
        try{
            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();

            if (length > 0){
                byte [] buffer = new byte[length];
                fin.read(buffer);
                res = EncodingUtils.getString(buffer, "UTF-8");

                int lines = getFileLineCount(fileName);
                res += "\ntotal lines: " + lines;
            }else{
                res = fileName + "文件无内容";
            }

            fin.close();
        }
        catch(Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            res = "\r\n" + sw.toString() + "\r\n";
        }

        return res;
    }

    //展示手机型号
    protected void updateSdLabel(){
        TextView textLabel = (TextView)getView().findViewById(R.id.sdlabel);
        textLabel.setText(phone_model + ":打点数据内容");
    }

    //获取当前的数据统计文件名
    protected String getStatisticsFilename() {
        String prefix =  "/sdcard/Safety/Desktop/lejian/statistics/";
        String filename = "";
        String sdate = "";

        Calendar todayStart = Calendar.getInstance();
        todayStart.setTimeInMillis(System.currentTimeMillis());
        sdate = todayStart.get(Calendar.YEAR) + "-"
                + (todayStart.get(Calendar.MONTH) + 1) + "-"
                + todayStart.get(Calendar.DAY_OF_MONTH);

        filename = prefix + sdate + ".txt";
        return filename;
    }

    //设置数据
    protected void showStatisticsData(){
        String statictisFile = getStatisticsFilename();
        try {
            String fileContent = readFileSdcardFile(statictisFile);
            TextView textContent = (TextView)getView().findViewById(R.id.sdcontent);
            textContent.setText(fileContent);
        }catch (Exception e){
            Log.e("qa-showData", "get content failed");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        TextView textContent = (TextView)view.findViewById(R.id.sdcontent);
        textContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i("FragmentResume", "onResume");
        updateSdLabel();
        showStatisticsData();
    }
}
