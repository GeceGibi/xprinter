package com.example.barcode

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.ComponentName
import android.content.Context
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import net.posprinter.posprinterface.IMyBinder
import net.posprinter.posprinterface.UiExecute
import net.posprinter.service.PosprinterService
import java.lang.Error


/** BarcodePlugin */
class BarcodePlugin : FlutterPlugin, MethodCallHandler, ActivityAware {

    private var TAG = "#SDK#";
    private lateinit var channel: MethodChannel
    private lateinit var context: Context;
    private lateinit var activity: Activity;
    private var binder: IMyBinder? = null

    private val innerPrinterBluetoothMAC: String = "00:11:22:33:44:55"

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity

        Log.w(TAG,"onAttachedToActivity")

        val intent = Intent()
        intent.setClass(context, PosprinterService::class.java)
        context.bindService(intent, connection, BIND_AUTO_CREATE)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        // no-op
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        // no-op
    }

    override fun onDetachedFromActivity() {
        // no-op
    }


    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "barcode")
        channel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }


    private var connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            Log.e(TAG, "IBinder created")
            binder = iBinder as IMyBinder
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            Log.e(TAG, "IBinder disconnected")
        }
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "getPlatformVersion" -> result.success("Android ${android.os.Build.VERSION.RELEASE}")
            "print" -> print(result)
            else -> result.notImplemented()
        }
    }


    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private fun print(result: MethodChannel.Result) {

        Log.e(TAG,"print")

        if (!bluetoothAdapter.isEnabled) {
            //open bluetooth
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(intent, 1, null)
            result.success("bluetooth")
        } else {
            binder?.connectBtPort(innerPrinterBluetoothMAC, object : UiExecute {
                override fun onsucess() {
                    result.success("onsucess")
                    Log.e(TAG, "onsucess")
                }

                override fun onfailed() {
                    result.success("onfailed")
                    Log.e(TAG, "onfailed")
                }
            })
        }
    }


}
