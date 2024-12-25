package com.si_charginganimation.nilesh_charginganimation.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;


 
import java.util.ArrayList;


public class BLEConnectHelper {
    private static final String TAG = "BluetoothConnectHelper";
    private BluetoothDevice bluetoothDevice;
    private BluetoothGattCallback mBluetoothGatCallback = new BluetoothGattCallback() {
        private BLEInFo bleInFo;
        private ArrayList<BluetoothGattCharacteristic> characteristicArrayList = new ArrayList<>();

        @Override
        public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i2) {
            super.onConnectionStateChange(bluetoothGatt, i, i2);
            if (i2 == 2) {
                BLEConnectHelper.this.mBluetoothGatt.discoverServices();
                return;
            }
            BLEInFo bLEInFo = new BLEInFo();
            bLEInFo.setBluetoothDevice(bluetoothGatt.getDevice());
            if (BLEConnectHelper.this.mCallback != null) {
                BLEConnectHelper.this.mCallback.onFailure(bLEInFo);
            }
            BLEConnectHelper.this.close();
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
            super.onServicesDiscovered(bluetoothGatt, i);
            if (i == 0) {
                this.bleInFo = new BLEInFo();
                bluetoothGatt.readRemoteRssi();
                for (BluetoothGattService bluetoothGattService : bluetoothGatt.getServices()) {
                    for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService.getCharacteristics()) {
                        if (bluetoothGattCharacteristic.getUuid().equals( BLEConstant.CHARACTERISTIC_BATTERY)) {
                            this.characteristicArrayList.add(bluetoothGattCharacteristic);
                        } else if (bluetoothGattCharacteristic.getUuid().equals(BLEConstant.CHARACTERISTIC_SOFTWARE_VS)) {
                            this.characteristicArrayList.add(bluetoothGattCharacteristic);
                        } else if (bluetoothGattCharacteristic.getUuid().equals(BLEConstant.CHARACTERISTIC_DEVICE_NAME)) {
                            this.characteristicArrayList.add(bluetoothGattCharacteristic);
                        } else if (bluetoothGattCharacteristic.getUuid().equals(BLEConstant.CHARACTERISTIC_SERIAL)) {
                            this.characteristicArrayList.add(bluetoothGattCharacteristic);
                        }
                    }
                }
                requestCharacteristics(bluetoothGatt);
            }
        }

        public void requestCharacteristics(BluetoothGatt bluetoothGatt) {
            ArrayList<BluetoothGattCharacteristic> arrayList = this.characteristicArrayList;
            bluetoothGatt.readCharacteristic(arrayList.get(arrayList.size() - 1));
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            if (i == 0) {
                this.bleInFo.setBluetoothDevice(bluetoothGatt.getDevice());
                if (bluetoothGattCharacteristic.getUuid().equals(BLEConstant.CHARACTERISTIC_BATTERY)) {
                    byte[] value = bluetoothGattCharacteristic.getValue();
                    BLEInFo bLEInFo = this.bleInFo;
                    bLEInFo.setBattery(((int) value[1]) + "%");
                    this.bleInFo.setRssi(BLEConnectHelper.this.getRssi());
                    this.bleInFo.setBatteryLevel(value[1]);
                    BLEInFo bLEInFo2 = this.bleInFo;
                    bLEInFo2.setLastCharge(((int) value[15]) + ":" + ((int) value[16]) + ", " + ((int) value[14]) + " " + BLEConnectHelper.this.convertMonth(value[13]));
                } else if (bluetoothGattCharacteristic.getUuid().equals(BLEConstant.CHARACTERISTIC_SERIAL)) {
                    this.bleInFo.setSerialNumber(bluetoothGattCharacteristic.getStringValue(1));
                } else if (bluetoothGattCharacteristic.getUuid().equals(BLEConstant.CHARACTERISTIC_SOFTWARE_VS)) {
                    this.bleInFo.setSoftwareVersion(bluetoothGattCharacteristic.getStringValue(1));
                } else if (bluetoothGattCharacteristic.getUuid().equals(BLEConstant.CHARACTERISTIC_DEVICE_NAME)) {
                    this.bleInFo.setDeviceName(bluetoothGattCharacteristic.getStringValue(0));
                }
                BLEConnectHelper.this.setRssi(0);
                ArrayList<BluetoothGattCharacteristic> arrayList = this.characteristicArrayList;
                arrayList.remove(arrayList.get(arrayList.size() - 1));
                if (this.characteristicArrayList.size() > 0) {
                    requestCharacteristics(bluetoothGatt);
                } else if (BLEConnectHelper.this.mCallback != null) {
                    BLEConnectHelper.this.mCallback.onSuccess(this.bleInFo);
                }
            }
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt bluetoothGatt, int i, int i2) {
            super.onReadRemoteRssi(bluetoothGatt, i, i2);
            BLEConnectHelper.this.setRssi(i);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            super.onCharacteristicChanged(bluetoothGatt, bluetoothGattCharacteristic);
        }
    };
    private BluetoothGatt mBluetoothGatt;
    private BluetoothDataCallback mCallback;
    private Context mContext;
    private int rssi;


    public interface BluetoothDataCallback {
        void onFailure(BLEInFo bLEInFo);

        void onSuccess(BLEInFo bLEInFo);
    }


    public String convertMonth(int i) {
        switch (i) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return "null";
        }
    }

    public BLEConnectHelper(Context context, BluetoothDataCallback bluetoothDataCallback) {
        this.mContext = context;
        this.mCallback = bluetoothDataCallback;
    }

    public void connect(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
        this.mBluetoothGatt = bluetoothDevice.connectGatt(this.mContext, false, this.mBluetoothGatCallback);
    }


    public void setRssi(int i) {
        this.rssi = i;
    }

    public int getRssi() {
        return this.rssi;
    }

    public void close() {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt != null) {
            bluetoothGatt.close();
            this.mBluetoothGatt = null;
        }
    }

    private void getBattery(BluetoothGatt bluetoothGatt) {
        bluetoothGatt.readCharacteristic(bluetoothGatt.getService(BLEConstant.MORE_CONFIGURATION_SERVICE).getCharacteristic(BLEConstant.CHARACTERISTIC_BATTERY));
    }

    private void getBLEName(BluetoothGatt bluetoothGatt) {
        bluetoothGatt.readCharacteristic(bluetoothGatt.getService(BLEConstant.GENERIC_ACCESS_SERVICE).getCharacteristic(BLEConstant.CHARACTERISTIC_DEVICE_NAME));
    }

    public void getInformation(BluetoothGatt bluetoothGatt) {

        BluetoothGattService service = bluetoothGatt.getService(BLEConstant.DEVICE_INFORMATION_SERVICE);
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(BLEConstant.CHARACTERISTIC_SERIAL);
        BluetoothGattCharacteristic characteristic2 = service.getCharacteristic(BLEConstant.CHARACTERISTIC_SOFTWARE_VS);
        bluetoothGatt.readCharacteristic(characteristic);
        bluetoothGatt.readCharacteristic(characteristic2);
    }
}
