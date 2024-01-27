package com.example.kotlin36

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.kotlin36.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListeners()
        openGallery()
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            binding.ivImage.setImageURI(uri)
        }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            Log.e("isGranted", isGranted.toString())
            if (isGranted) {
                shouldShowRequestPermissionRationale(
                    "Разрешение есть"
                )
            } else {
                createDialog()
            }
        }

    private fun setupListeners() = with(binding) {
        btnScip.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("permission", "разрешение есть")
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

            }
        }
    }

    private fun createDialog() {
        AlertDialog.Builder(this)
            .setTitle("Разрешение на чтение данных")
            .setMessage("Перейти настройки")
            .setPositiveButton("да перейти") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton(" нет я хочу остаться ") { _, _ ->
            }
            .show()
    }
    private fun openGallery() {
        binding.ivImage.setOnClickListener {
            getContent.launch("image/*")
        }
    }
}