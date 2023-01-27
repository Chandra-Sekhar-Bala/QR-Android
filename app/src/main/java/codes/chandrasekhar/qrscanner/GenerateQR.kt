package codes.chandrasekhar.qrscanner

import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.WindowManager
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AppCompatActivity
import codes.chandrasekhar.qrscanner.databinding.ActivityGenerateQrBinding


class GenerateQR : AppCompatActivity() {
    private lateinit var binding : ActivityGenerateQrBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateQrBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        val manager = getSystemService(WINDOW_SERVICE) as WindowManager
        val display: Display = manager.getDefaultDisplay()
        val point = Point()
        display.getSize(point)
        val width: Int = point.x
        val height: Int = point.y
        var smallerDimension = width.coerceAtMost(height)
        smallerDimension = smallerDimension * 3 / 4
//         Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        binding.btnGenerate.setOnClickListener {
            val qrgEncoder = QRGEncoder(
                binding.generateText.text.toString().trim(),
                null,
                QRGContents.Type.TEXT,
                smallerDimension
            )
//            qrgEncoder.colorBlack = Color.RED
//            qrgEncoder.colorWhite = Color.BLUE
            try {
                // Getting QR-Code as Bitmap
                val bitmap = qrgEncoder.bitmap
                // Setting Bitmap to ImageView
                binding.imgQr.setImageBitmap(bitmap)
            } catch (e: java.lang.Exception) {
                Log.v(TAG, e.toString())
            }
        }
    }
}