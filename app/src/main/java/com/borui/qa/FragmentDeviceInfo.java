package com.borui.qa;

import android.app.Fragment;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by zhangpeng on 2017/12/21.
 */

public class FragmentDeviceInfo extends Fragment {
    private TelephonyManager tm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_devicesinfo, container, false);
         tm = (TelephonyManager) getActivity().getApplication().getSystemService(TELEPHONY_SERVICE);
        return view;
    }

    private void updateImei(){
        TextView textLabel = (TextView)getView().findViewById(R.id.vimei);
        textLabel.setText(tm.getDeviceId());
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i("DevicesFragmentResume", "onResume");
        updateImei();
    }
}
