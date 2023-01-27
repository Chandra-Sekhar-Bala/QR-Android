package codes.chandrasekhar.qrscanner

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Display
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidmads.library.qrgenearator.QRGSaver
import androidx.appcompat.app.AppCompatActivity
import codes.chandrasekhar.qrscanner.databinding.ActivityGenerateQrBinding


class GenerateQR : AppCompatActivity() {
    private lateinit var binding: ActivityGenerateQrBinding
    private var savePath: String = Environment.getExternalStorageDirectory().path + "/QR/"
    private lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateQrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            savePath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/QR/"
        }
    }

    override fun onResume() {
        super.onResume()

        // display measurement
        val displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val point = Point()
        displayManager.getDisplay(Display.DEFAULT_DISPLAY)?.getSize(point)
        val width: Int = point.x
        val height: Int = point.y
        var smallerDimension = width.coerceAtMost(height)
        smallerDimension = smallerDimension * 3 / 4
        // initiate qr generation
        binding.btnGenerate.setOnClickListener {
            val str = binding.generateText.text.toString().trim()
            if (str.isBlank()) {
                toastMessage("Write something first")
            } else {
                // initiating QR generating process
                val qrgEncoder = QRGEncoder(
                    binding.generateText.text.toString().trim(),
                    null,
                    QRGContents.Type.TEXT,
                    smallerDimension
                )
                try {
                    // Getting QR-Code as Bitmap
                    bitmap = qrgEncoder.bitmap
                    // Setting Bitmap to ImageView
                    binding.imgQr.setImageBitmap(bitmap)
                } catch (e: java.lang.Exception) {
                    Log.v(TAG, e.toString())
                }
            }
        }
    }
    private fun saveQR() {
        // Save with location, value, bitmap returned and type of Image(JPG/PNG).
        if (isValid()) {
            try {
                val save = QRGSaver().save(
                    savePath,
                    binding.generateText.text.toString().trim(),
                    bitmap,
                    QRGContents.ImageType.IMAGE_JPEG
                )
                val result = if (save) "Image Saved" else "Image Not Saved"
                toastMessage(result)
                binding.generateText.text = null
            } catch (e: java.lang.Exception) {
                e.message?.let { toastMessage(it) }
            }

        } else {
            toastMessage("Generate QR  first")
        }
    }
    private fun toastMessage(msg: String) {
        Toast.makeText(this@GenerateQR,msg, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save_qr, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sve_qr -> {
                saveQR()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isValid(): Boolean {
        return binding.generateText.text.toString().trim().isNotBlank()
    }

}