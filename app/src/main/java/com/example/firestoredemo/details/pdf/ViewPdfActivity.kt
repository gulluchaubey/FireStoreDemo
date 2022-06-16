package com.example.firestoredemo.details.pdf

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.firestoredemo.databinding.ActivityViewPdfBinding
import com.example.firestoredemo.utils.getRootDirPath
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.github.barteksc.pdfviewer.util.FitPolicy
import java.io.File


class ViewPdfActivity : AppCompatActivity() {

    private lateinit var binding:ActivityViewPdfBinding
    var url = "https://firebasestorage.googleapis.com/v0/b/iprep-cad1e.appspot.com/o/Digital%20Books%2FSyllabus%20Books%2FNCERT%20Books%2FNCERT%20Books%20in%20English%2FMathematics%2FClass%201%2FIndex.pdf?alt=media&token=7e2b76ea-952c-4480-81ab-cda960ddc027"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewPdfBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        PRDownloader.initialize(applicationContext)

        binding.progressBar.visibility = View.VISIBLE
        val fileName = "myFile.pdf"
        downloadPdfFromInternet(
            url,
            getRootDirPath(this),
            fileName
        )
    }

    private fun downloadPdfFromInternet(url: String, dirPath: String, fileName: String) {
        PRDownloader.download(
            url,
            dirPath,
            fileName
        ).build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    Toast.makeText(this@ViewPdfActivity, "downloadComplete", Toast.LENGTH_LONG)
                        .show()
                    val downloadedFile = File(dirPath, fileName)
                    binding.progressBar.visibility = View.GONE
                    showPdfFromFile(downloadedFile)
                }

                override fun onError(error: com.downloader.Error?) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@ViewPdfActivity, "Error in downloading file : $error", Toast.LENGTH_LONG).show()
                }
            })
    }
    private fun showPdfFromFile(file: File) {
        binding.pdfView.fromFile(file)
            .password(null)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .onPageError { page, _ ->
                Toast.makeText(
                    this@ViewPdfActivity,
                    "Error at page: $page", Toast.LENGTH_LONG
                ).show()
            }
            .load()
    }
}