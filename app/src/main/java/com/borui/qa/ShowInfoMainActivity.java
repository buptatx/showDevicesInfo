package com.borui.qa;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;
import android.widget.Toast;


public class ShowInfoMainActivity extends AppCompatActivity {
    private FragmentStatist msFragement;
    private FragmentCrashInfo mcFragment;
    private FragmentDeviceInfo mdFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction tx = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_statist:
                    if(msFragement == null){
                        msFragement = new FragmentStatist();
                    }
                    tx.replace(R.id.qa_content, msFragement);
                    tx.commit();
                    return true;
                case R.id.navigation_crash:
                    if(mcFragment == null){
                        mcFragment = new FragmentCrashInfo();
                    }
                    tx.replace(R.id.qa_content, mcFragment);
                    tx.commit();
                    return true;
                case R.id.navigation_more:
                    if(mdFragment == null){
                        mdFragment = new FragmentDeviceInfo();
                    }
                    tx.replace(R.id.qa_content, mdFragment);
                    tx.commit();
                    return true;
            }
            return false;
        }
    };

    private boolean shouldAskPermission(){
        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private boolean isPermissionGranted(){
        PackageManager pm = getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "com.borui.qa"));

        if (!permission){
            Toast.makeText(getApplicationContext(), "未授权此权限", Toast.LENGTH_SHORT).show();
        }
        return permission;
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(permsRequestCode, permissions, grantResults);

        switch(permsRequestCode){
            case 200:
                boolean writeAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                if(writeAccepted){
                    Toast.makeText(getApplicationContext(), "读取手机文件权限已授权",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info_main);

        //android6.0 获取权限
        if(shouldAskPermission() && !isPermissionGranted()) {
            String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};
            int permsRequestCode = 200;
            ActivityCompat.requestPermissions(this, perms, permsRequestCode);
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.add(R.id.qa_content, new FragmentStatist(), "statistic");
        tx.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
