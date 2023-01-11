package com.example.circularai_basic

import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.circularai_basic.UsbService.UsbBinder
import com.example.circularai_basic.databinding.ActivityMainBinding
import java.lang.ref.WeakReference
import java.util.*

private val commandQueue: Queue<String> = LinkedList()
private var usbService: UsbService? = null
private val TAG = "APP_ACTIVITY"

class MainActivity : AppCompatActivity() {

    private var mHandler: MyHandler? = null
    private val usbConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(arg0: ComponentName, arg1: IBinder) {
            usbService = (arg1 as UsbBinder).service
            usbService!!.setHandler(mHandler)
            Log.i(TAG, "Service Started")
            //usbService!!.write("\$H\n".toByteArray())
            //commandQueue.add("G90 G0 X230\n")
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            usbService = null
        }
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mHandler = MyHandler(this)

        //commandQueue.clear()

        val btn0 = binding.btn0
        val btn1 = binding.btn1
        val btn2 = binding.btn2
        val btn3 = binding.btn3
        val btn4 = binding.btn4
        val btn5 = binding.btn5

        btn1.setOnClickListener {

            if(commandQueue.isNotEmpty()){
                Log.i(TAG, "WAIT! Current Action in Progress.")
                return@setOnClickListener
            }
            if (usbService != null) { // if UsbService was correctly binded, Send data
            //usbService!!.write("\$$\n".toByteArray())
            usbService!!.write("\$H\n".toByteArray())
            commandQueue.add("G90 G0 X0\n")
            commandQueue.add("G90 G0 Y21\n")
            commandQueue.add("G90 G0 Y0\n")
            commandQueue.add("G90 G0 X230\n")
            }
        }

        btn2.setOnClickListener {

            if(commandQueue.isNotEmpty()){
                Log.i(TAG, "WAIT! Current Action in Progress.")
                return@setOnClickListener
            }
            if (usbService != null) { // if UsbService was correctly binded, Send data
                //usbService!!.write("\$$\n".toByteArray())
                usbService!!.write("\$H\n".toByteArray())
                commandQueue.add("G90 G0 X230\n")
                commandQueue.add("G90 G0 Y21\n")
                commandQueue.add("G90 G0 Y0\n")
                commandQueue.add("G90 G0 X230\n")
            }
        }

        btn3.setOnClickListener {

            if(commandQueue.isNotEmpty()){
                Log.i(TAG, "WAIT! Current Action in Progress.")
                return@setOnClickListener
            }
            if (usbService != null) { // if UsbService was correctly binded, Send data
                //usbService!!.write("\$$\n".toByteArray())
                usbService!!.write("\$H\n".toByteArray())
                commandQueue.add("G90 G0 X460\n")
                commandQueue.add("G90 G0 Y21\n")
                commandQueue.add("G90 G0 Y0\n")
                commandQueue.add("G90 G0 X230\n")
            }
        }

        btn4.setOnClickListener {

            if(commandQueue.isNotEmpty()){
                Log.i(TAG, "WAIT! Current Action in Progress.")
                return@setOnClickListener
            }
            if (usbService != null) { // if UsbService was correctly binded, Send data
                //usbService!!.write("\$$\n".toByteArray())
                usbService!!.write("\$H\n".toByteArray())
                commandQueue.add("G90 G0 X690\n")
                commandQueue.add("G90 G0 Y21\n")
                commandQueue.add("G90 G0 Y0\n")
                commandQueue.add("G90 G0 X230\n")
            }
        }
        btn5.setOnClickListener {

            if(commandQueue.isNotEmpty()){
                Log.i(TAG, "WAIT! Current Action in Progress.")
                return@setOnClickListener
            }
            if (usbService != null) { // if UsbService was correctly binded, Send data
                //usbService!!.write("\$$\n".toByteArray())
                usbService!!.write("\$H\n".toByteArray())
                commandQueue.add("G90 G0 X920\n")
                commandQueue.add("G90 G0 Y21\n")
                commandQueue.add("G90 G0 Y0\n")
                commandQueue.add("G90 G0 X230\n")
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        Log.i(TAG, "ON Resume Called")

        setFilters() // Start listening notifications from UsbService
        startService(
            UsbService::class.java,
            usbConnection,
            null
        ) // Start UsbService(if it was not started before) and Bind it
    }

    public override fun onPause() {
        super.onPause()
        unregisterReceiver(mUsbReceiver)
        unbindService(usbConnection)
    }

    private fun startService(
        service: Class<*>,
        serviceConnection: ServiceConnection,
        extras: Bundle?
    ) {
        if (!UsbService.SERVICE_CONNECTED) {
            val startService = Intent(this, service)
            if (extras != null && !extras.isEmpty) {
                val keys = extras.keySet()
                for (key in keys) {
                    val extra = extras.getString(key)
                    startService.putExtra(key, extra)
                }
            }
            startService(startService)
        }
        val bindingIntent = Intent(this, service)
        bindService(bindingIntent, serviceConnection, BIND_AUTO_CREATE)
    }

    private fun setFilters() {
        val filter = IntentFilter()
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED)
        filter.addAction(UsbService.ACTION_NO_USB)
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED)
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED)
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED)
        registerReceiver(mUsbReceiver, filter)
    }

    /*
 * Notifications from UsbService will be received here.
 */
    private val mUsbReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                UsbService.ACTION_USB_PERMISSION_GRANTED -> Toast.makeText(
                    context,
                    "USB Ready",
                    Toast.LENGTH_SHORT
                ).show()
                UsbService.ACTION_USB_PERMISSION_NOT_GRANTED -> Toast.makeText(
                    context,
                    "USB Permission not granted",
                    Toast.LENGTH_SHORT
                ).show()
                UsbService.ACTION_NO_USB -> Toast.makeText(
                    context,
                    "No USB connected",
                    Toast.LENGTH_SHORT
                ).show()
                UsbService.ACTION_USB_DISCONNECTED -> Toast.makeText(
                    context,
                    "USB disconnected",
                    Toast.LENGTH_SHORT
                ).show()
                UsbService.ACTION_USB_NOT_SUPPORTED -> Toast.makeText(
                    context,
                    "USB device not supported",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    private class MyHandler(activity: MainActivity) : Handler() {
        private val mActivity: WeakReference<MainActivity>
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                UsbService.MESSAGE_FROM_SERIAL_PORT -> {
                    val data = msg.obj as String
                    Log.i(TAG, data)
                    if (data.contains("ok")){
                        val to_send = commandQueue.poll()
                        if(to_send != null){
                            usbService!!.write(to_send.toByteArray())
                        }
                    }
                }
                UsbService.CTS_CHANGE -> Toast.makeText(
                    mActivity.get(),
                    "CTS_CHANGE",
                    Toast.LENGTH_LONG
                ).show()
                UsbService.DSR_CHANGE -> Toast.makeText(
                    mActivity.get(),
                    "DSR_CHANGE",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        init {
            mActivity = WeakReference(activity)
        }
    }
}