package com.example.healthapp.activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Intent
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.healthapp.R
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SubmitActivity : AppCompatActivity() {

    private lateinit var resultTextView: TextView
    private val REQUEST_CAMERA_PERMISSION = 1
    private val REQUEST_VIDEO_CAPTURE = 2
    private var videoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit)

        resultTextView = findViewById(R.id.resultTextView)
        val submitButton: Button = findViewById(R.id.submitButton)
        val captureButton: Button = findViewById(R.id.captureButton)

        captureButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CAMERA_PERMISSION)
            } else {
                dispatchTakeVideoIntent()
            }
        }

        submitButton.setOnClickListener {
            videoUri?.let {
                FileUploadTask().execute(it)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakeVideoIntent()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun dispatchTakeVideoIntent() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = data?.data
        }
    }


    private inner class FileUploadTask : AsyncTask<Uri, Void, String>() {

        override fun doInBackground(vararg params: Uri?): String {
            val videoUri = params[0]
            val boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW"
            val urlConnection: HttpURLConnection
            val url = URL("http://172.10.7.128:80/api/upload/")
            try {
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                urlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
                urlConnection.doOutput = true

                val outputStream = urlConnection.outputStream
                val writer = OutputStreamWriter(outputStream, "UTF-8")

                // Write the multipart/form-data content
                writer.write("--$boundary\r\n")
                writer.write("Content-Disposition: form-data; name=\"file\"; filename=\"video.mp4\"\r\n")
                writer.write("Content-Type: video/mp4\r\n")
                writer.write("\r\n")
                writer.flush()

                // Get InputStream from Uri
                val inputStream: InputStream? = contentResolver.openInputStream(videoUri!!)
                val buffer = ByteArray(1024)
                var bytesRead: Int
                inputStream?.use { input ->
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                }

                writer.write("\r\n")
                writer.write("--$boundary--\r\n")
                writer.flush()
                writer.close()

                // Get the server response
                val responseCode = urlConnection.responseCode
                val responseMessage = urlConnection.inputStream.bufferedReader().use { it.readText() }
                return "Response Code: $responseCode\n$responseMessage"

            } catch (e: Exception) {
                e.printStackTrace()
                return "Exception: ${e.message}"
            }
        }

        override fun onPostExecute(result: String) {
            resultTextView.text = result
        }

    }
}