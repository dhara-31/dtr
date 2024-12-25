package com.si_charginganimation.nilesh_charginganimation.bluetooth;

import android.bluetooth.BluetoothDevice;

public class BLEInFo {
   private String battery;
   private int batteryLevel;
   private BluetoothDevice bluetoothDevice;
   private String deviceName;
   private String lastCharge;
   private int rssi;
   private String serialNumber;
   private String softwareVersion;
   private int status;

   public String getDeviceName() {
       return this.deviceName;
   }

   public void setDeviceName(String str) {
       this.deviceName = str;
   }

   public BluetoothDevice getBluetoothDevice() {
       return this.bluetoothDevice;
   }

   public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
       this.bluetoothDevice = bluetoothDevice;
   }

   public int getBatteryLevel() {
       return this.batteryLevel;
   }

   public void setBatteryLevel(int i) {
       this.batteryLevel = i;
   }

   public String getBattery() {
       return this.battery;
   }

   public void setBattery(String str) {
       this.battery = str;
   }

   public String getLastCharge() {
       return this.lastCharge;
   }

   public void setLastCharge(String str) {
       this.lastCharge = str;
   }

   public int getRssi() {
       return this.rssi;
   }

   public void setRssi(int i) {
       this.rssi = i;
   }

   public String getSerialNumber() {
       return this.serialNumber;
   }

   public void setSerialNumber(String str) {
       this.serialNumber = str;
   }

   public String getSoftwareVersion() {
       return this.softwareVersion;
   }

   public void setSoftwareVersion(String str) {
       this.softwareVersion = str;
   }
}
