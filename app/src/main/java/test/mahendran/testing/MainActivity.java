package test.mahendran.testing;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;
    private static final String TAG = MainActivity.class.getSimpleName();
    private BluetoothAdapter mBlueToothAdapter;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings mSettings;
    private ArrayList<ScanFilter> mFilters;
    private Handler mHandler;
    private BluetoothGatt mGatt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Initializing Handler
        mHandler = new Handler();

        // Checking Bluetooth is supported in a device.
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not Supported!!", Toast.LENGTH_LONG).show();
            finish();
        }

        BluetoothManager mBlueToothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        // Initializing Bluetooth adapter
        mBlueToothAdapter = mBlueToothManager.getAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();
        // Checking bluetooth is enabled or not. if not asking the user to enable the bluetooth.
        if(mBlueToothAdapter == null || !mBlueToothAdapter.isEnabled()){
            Intent mEnableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(mEnableBluetoothIntent, REQUEST_ENABLE_BT);
        }else{
            // if sdk version is greater than or equal to 21 initializing a ble scanner
            if(Build.VERSION.SDK_INT >= 21){
                mLEScanner = mBlueToothAdapter.getBluetoothLeScanner();
                mSettings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();

                mFilters = new ArrayList<ScanFilter>();
            }
            scanLeDevice(true);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(mBlueToothAdapter != null && mBlueToothAdapter.isEnabled()){
            scanLeDevice(false);
        }
    }

    @Override
    protected void onDestroy(){
        if(mGatt == null){
            return;
        }
        mGatt.close();
        mGatt = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Activity.RESULT_CANCELED){
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void scanLeDevice(boolean enable) {
        if(enable) {
            // Initializing handler to disable scan
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(Build.VERSION.SDK_INT < 21){
                        mBlueToothAdapter.stopLeScan(mLeScanCallback);
                    } else {
                        mLEScanner.stopScan(mScanCallback);
                    }
                }
            }, SCAN_PERIOD);

            if(Build.VERSION.SDK_INT < 21){
                mBlueToothAdapter.startLeScan(mLeScanCallback);
            } else {
                mLEScanner.startScan(mFilters, mSettings, mScanCallback);
            }
        } else {
            if(Build.VERSION.SDK_INT < 21){
                mBlueToothAdapter.stopLeScan(mLeScanCallback);
            } else {
                mLEScanner.stopScan(mScanCallback);
            }
        }
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            //super.onScanResult(callbackType, result);
            Log.i(TAG, String.valueOf(callbackType));
            Log.i(TAG, result.toString());
            BluetoothDevice mBtDevice = result.getDevice();
            connectToDevice(mBtDevice);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            // super.onBatchScanResults(results);
            for(ScanResult scanResult : results){
                Log.i(TAG, scanResult.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            // super.onScanFailed(errorCode);
            Log.e(TAG, "Scan Failed Errorcode : " + errorCode);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, device.toString());
                    connectToDevice(device);
                }
            });
        }
    };

    private void connectToDevice(BluetoothDevice mBtDevice) {
        if(mGatt == null){
            mGatt = mBtDevice.connectGatt(this, false, mGattCallBack);
            scanLeDevice(false);
        }
    }

    private final BluetoothGattCallback mGattCallBack = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            //super.onConnectionStateChange(gatt, status, newState);
            Log.i(TAG, "onConnectionStateChange status:"+status);
            switch (newState){
                case BluetoothProfile.STATE_CONNECTED:
                    Log.e(TAG, "State connected");
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e(TAG, "State disconnected");
                    break;
                default:
                    Log.e(TAG, "State Other");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            // super.onServicesDiscovered(gatt, status);
            List<BluetoothGattService> services = gatt.getServices();
            Log.i(TAG, "onServiceDiscovered " + services.toString());
            gatt.readCharacteristic(services.get(1).getCharacteristics().get(1));
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //super.onCharacteristicRead(gatt, characteristic, status);
            Log.i(TAG, "onCharacteristicRead "+characteristic.toString());
            gatt.discoverServices();
        }
    };
}
