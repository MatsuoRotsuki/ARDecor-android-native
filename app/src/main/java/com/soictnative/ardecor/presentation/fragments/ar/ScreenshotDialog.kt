package com.soictnative.ardecor.presentation.fragments.ar

import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.soictnative.ardecor.R
import com.soictnative.ardecor.util.Constants.SCREENSHOT_LOCAL_DIRECTORY
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

class ScreenshotDialog: DialogFragment() {

    companion object {
        private const val IMAGE_PATH_KEY = "IMAGE_PATH_KEY"
        fun newInstance(imagePath: String?): ScreenshotDialog {
            val fragment = ScreenshotDialog()
            val args = Bundle()
            args.putString(IMAGE_PATH_KEY, imagePath)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullscreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_screenshot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imagePath = arguments?.getString(IMAGE_PATH_KEY)
        val imageFile = imagePath?.let { File(imagePath) }

        val imageScreenshot = view.findViewById<ImageView>(R.id.imageScreenshot)
        val btnSkipScreenshot = view.findViewById<Button>(R.id.btnSkipScreenshot)
        val btnSaveScreenshot = view.findViewById<Button>(R.id.btnSaveScreenshot)

        if (imageFile != null && imageFile.exists()) {
            val imageBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            imageScreenshot.setImageBitmap(imageBitmap)
        }

        btnSkipScreenshot.setOnClickListener {
            dismiss()
        }

        btnSaveScreenshot.setOnClickListener {
            val publicDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                SCREENSHOT_LOCAL_DIRECTORY
            )
            if (!publicDir.exists()) {
                publicDir.mkdirs()
            }

            val formatter = SimpleDateFormat("yyyyMMdd_HHmmssSSS")
            val timestamp = formatter.format(Date())
            val fileName = "$timestamp.png"

            val publicFile = File(publicDir, fileName)

            val inStream = FileInputStream(imageFile)
            val outStream = FileOutputStream(publicFile)
            val buffer = ByteArray(1024)
            var length: Int
            while (inStream.read(buffer).also { length = it } > 0)
                outStream.write(buffer, 0, length)

            inStream.close()
            outStream.close()

            Toast.makeText(
                requireActivity(),
                "Image saved successfully",
                Toast.LENGTH_SHORT
            ).show()

            MediaScannerConnection.scanFile(
                requireActivity(), arrayOf(publicFile.absolutePath), null, null
            )
            dismiss()
        }
    }
}