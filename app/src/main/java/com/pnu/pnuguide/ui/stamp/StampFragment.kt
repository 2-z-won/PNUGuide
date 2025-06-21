package com.pnu.pnuguide.ui.stamp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.recyclerview.widget.GridLayoutManager

import com.pnu.pnuguide.databinding.FragmentStampBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat.getMainExecutor

class StampFragment : Fragment() {

    private var _binding: FragmentStampBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<StampViewModel> {
        AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    private lateinit var imageCapture: ImageCapture

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) startCamera() else Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStampBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerStamps.layoutManager = GridLayoutManager(requireContext(), 4)
        val adapter = StampAdapter()
        binding.recyclerStamps.adapter = adapter

        viewModel.stamps.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.error.observe(viewLifecycleOwner) { failed ->
            if (failed) {
                Toast.makeText(requireContext(), getString(com.pnu.pnuguide.R.string.stamp_failed), Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }

        binding.toolbarStamp.setNavigationOnClickListener { requireActivity().finish() }
        binding.buttonCapture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                requestPermission.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            imageCapture = ImageCapture.Builder().build()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, imageCapture)
                capturePhoto()
            } catch (e: Exception) {
                Log.e("StampFragment", "Use case binding failed", e)
            }
        }, getMainExecutor(requireContext()))
    }

    private fun capturePhoto() {
        val file = File(requireContext().cacheDir, SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis()) + ".jpg")
        val output = ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture.takePicture(output, getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                viewModel.processImage(file)
            }
            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(requireContext(), getString(com.pnu.pnuguide.R.string.stamp_failed), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
