package com.example.healthapp.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.MediaController
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.example.healthapp.R
import org.json.JSONObject
import java.io.InputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class Fragment2 : Fragment() {
    private lateinit var resultTextView: TextView
    private lateinit var exerciseTypeSpinner: Spinner
    private lateinit var videoView: VideoView
    private val REQUEST_CAMERA_PERMISSION = 1
    private val REQUEST_VIDEO_CAPTURE = 2
    private val REQUEST_VIDEO_PICK = 3
    private var videoUri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_2, container, false)
//        val intent = Intent(activity, Tab2AnalyzeActivity::class.java)
//        startActivity(intent)
//        return view

        resultTextView = view.findViewById(R.id.resultTextView)
        videoView = view.findViewById(R.id.videoView)
        val submitButton: Button = view.findViewById(R.id.submitButton)
        val captureButton: Button = view.findViewById(R.id.captureButton)
        val chooseVideoButton: Button = view.findViewById(R.id.chooseVideoButton)
        val textView: TextView = view.findViewById(R.id.textView)
        captureButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CAMERA_PERMISSION)
            } else {
                dispatchTakeVideoIntent()
            }
        }

        chooseVideoButton.setOnClickListener {
            dispatchPickVideoIntent()
        }

        submitButton.setOnClickListener {
            videoUri?.let {
                FileUploadTask().execute(it)
            }
        }
        return view
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakeVideoIntent()
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun dispatchTakeVideoIntent() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(requireContext().packageManager)?.also {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
            }
        }
    }

    private fun dispatchPickVideoIntent() {
        Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI).also { pickVideoIntent ->
            pickVideoIntent.resolveActivity(requireContext().packageManager)?.also {
                startActivityForResult(pickVideoIntent, REQUEST_VIDEO_PICK)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = data?.data
            videoUri?.let { playVideo(it) }
        } else if (requestCode == REQUEST_VIDEO_PICK && resultCode == RESULT_OK) {
            videoUri = data?.data
            videoUri?.let { playVideo(it) }
        }
    }

    private fun playVideo(uri: Uri) {
        videoView.setVideoURI(uri)
        videoView.setMediaController(MediaController(context))
        videoView.requestFocus()
        videoView.start()
    }

    private inner class FileUploadTask : AsyncTask<Any, Void, String>() {

        override fun doInBackground(vararg params: Any?): String {
            val videoUri = params[0] as Uri
            val boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW"
            val urlConnection: HttpURLConnection
            val url = URL("http://172.10.7.128:80/api/classify/")
            try {
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                urlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
                urlConnection.doOutput = true

                urlConnection.connectTimeout = 0 // 무제한 대기
                urlConnection.readTimeout = 0 // 무제한 대기

                val outputStream = urlConnection.outputStream
                val writer = OutputStreamWriter(outputStream, "UTF-8")

                // Write the multipart/form-data content for the file
                writer.write("--$boundary\r\n")
                writer.write("Content-Disposition: form-data; name=\"file\"; filename=\"video.mp4\"\r\n")
                writer.write("Content-Type: video/mp4\r\n")
                writer.write("\r\n")
                writer.flush()

                // Get InputStream from Uri
                val inputStream: InputStream? = requireContext().contentResolver.openInputStream(videoUri)
                val buffer = ByteArray(1024)
                var bytesRead: Int
                inputStream?.use { input ->
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                }
                outputStream.flush()

                writer.write("\r\n")
                writer.write("--$boundary--\r\n")
                writer.flush()
                writer.close()
//                writer.write("\r\n")
//                writer.flush()

                // Get the server response
//                val responseMessage = urlConnection.inputStream.bufferedReader().use { it.readText() }
//                val jsonResponse = JSONObject(responseMessage)
//                return jsonResponse.optString("predict", "No predict provided")

                val responseCode = urlConnection.responseCode
                val responseMessage = urlConnection.inputStream.bufferedReader().use { it.readText() }
                val jsonResponse = JSONObject(responseMessage)
                return jsonResponse.optString("predict", "No predict provided")

            } catch (e: Exception) {
                e.printStackTrace()
                return "Exception: ${e.message}"
            }
        }
//        override fun onPostExecute(result: String) {
//            showResultFragment(result)
//        }
        override fun onPostExecute(result: String) {
            resultTextView.text = result
        }

    }
    private fun showResultFragment(result: String) {
        val fragment = AdviceFragment.newInstance(result)
        val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}