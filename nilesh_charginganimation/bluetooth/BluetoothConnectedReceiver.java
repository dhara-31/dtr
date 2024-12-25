package com.si_charginganimation.nilesh_charginganimation.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.si_charginganimation.nilesh_charginganimation.act.MainAct;



public class BluetoothConnectedReceiver extends BroadcastReceiver {

    public static final String ACTION_BLUETOOTH_CONNECTION = "android.devlv.bluetoothbattery.action.BLUETOOTH_CONNECTION_CHANGE";
    private static final String TAG = "BluetoothConnectedRecei";

    @Override
    public void onReceive(Context context, Intent intent) {

        char c;
            String str = intent.getAction() + "";
        int hashCode = str.hashCode();
        if (hashCode != -301431627) {
            if (hashCode == 1821585647 && str.equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                c = 1;
            }
            c = 65535;
        } else {
            if (str.equals("android.bluetooth.device.action.ACL_CONNECTED")) {
                c = 0;
            }
            c = 65535;
        }

        if (c == 0) {


        } else if (c == 1) {
            sendBroadCast(context, "ACTION_ACL_DISCONNECTED");
        }
        final String action = intent.getAction();
        MainAct.setConnected(false);
        if (action.equals( BluetoothDevice.ACTION_ACL_CONNECTED)){
            MainAct.setConnected(false);
        }
        if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)){
            MainAct.setConnected(false);
        }
        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:

                    MainAct.setBlOnOFF(false);
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:

                    MainAct.setBlOnOFF(false);
                    break;
                case BluetoothAdapter.STATE_ON:

                    MainAct.setBlOnOFF(true);
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:

                    MainAct.setBlOnOFF(true);
                    break;
            }
        }
    }

    private void sendBroadCast(Context context, String str) {
        Intent intent = new Intent();
        intent.setAction(ACTION_BLUETOOTH_CONNECTION);
        intent.putExtra("_action", str);
        context.sendBroadcast(intent);
    }

}

