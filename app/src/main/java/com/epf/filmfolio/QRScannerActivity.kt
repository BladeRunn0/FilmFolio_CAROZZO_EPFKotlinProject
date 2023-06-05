package com.epf.filmfolio

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class QRScannerActivity: AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var qrScanner: ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscan)

        if (ContextCompat.checkSelfPermission(this@QRScannerActivity, android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this@QRScannerActivity, arrayOf(android.Manifest.permission.CAMERA), 1)
        }


        val contentFrame = findViewById<ViewGroup>(R.id.content_frame)
        qrScanner = ZXingScannerView(this)
        contentFrame.addView(qrScanner)
    }

    override fun onResume() {
        super.onResume()
        qrScanner?.setResultHandler(this)
        qrScanner?.startCamera()
    }

    override fun onPause() {
        super.onPause()
        qrScanner?.stopCamera()
    }

    override fun handleResult(rawResult: Result) {
        Log.d("QRCode1", rawResult.text)
        Log.d("QRCode2", rawResult.barcodeFormat.toString())

        val intent = Intent(this, FilmDetailActivity::class.java)
        intent.putExtra("Film", rawResult.text.toInt())
        startActivity(intent)

        val handler = Handler()
        handler.postDelayed(
            { qrScanner!!.resumeCameraPreview(this@QRScannerActivity) },
            1000
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this@QRScannerActivity, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@QRScannerActivity, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

}