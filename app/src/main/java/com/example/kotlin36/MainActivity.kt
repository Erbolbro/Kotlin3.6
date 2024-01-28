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
import com.example.kotlin36.preference.PreferenceHelper

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListeners()
        openGallery()
    }
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            binding?.ivImage?.setImageURI(uri)
        }


    private fun setupListeners() = with(binding) {
        var preferenceHelper = PreferenceHelper(this@MainActivity).isHasPermission
        btnScip.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("permission", "разрешение есть")
            } else {
                if (!preferenceHelper) {
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        1
                    )
                    preferenceHelper = true
                } else {
                    createDialog()
                }
            }
        }
    }

    private fun createDialog() {
        AlertDialog.Builder(this)
            .setTitle("Разрешение на чтение данных")
            .setMessage("Перейти настройки")
            .setPositiveButton("да перейти") { dialog, k ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton(" нет я хочу остаться ") { dialog, k ->
            }
            .show()
    }

    private fun openGallery() {
        binding.ivImage.setOnClickListener {
            getContent.launch("image/*")
        }
    }
}