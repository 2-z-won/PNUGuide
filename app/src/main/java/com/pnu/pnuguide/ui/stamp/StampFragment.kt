package com.pnu.pnuguide.ui.stamp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.pnu.pnuguide.ui.quiz.QuizActivity

import com.pnu.pnuguide.databinding.FragmentStampBinding

class StampFragment : Fragment() {

    private var _binding: FragmentStampBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<StampViewModel> {
        AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) startCamera()
            else Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }

    private val takePicture = registerForActivityResult(TakePicturePreview()) { bitmap ->
        if (bitmap != null) viewModel.processBitmap(bitmap)
        else Toast.makeText(requireContext(), getString(com.pnu.pnuguide.R.string.stamp_failed), Toast.LENGTH_SHORT).show()
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
        binding.buttonStartCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                requestPermission.launch(Manifest.permission.CAMERA)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { failed ->
            if (failed) {
                Toast.makeText(requireContext(), getString(com.pnu.pnuguide.R.string.stamp_failed), Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }

        viewModel.label.observe(viewLifecycleOwner) { code ->
            code?.let {
                QuizActivity.start(requireContext(), it)
                viewModel.clearLabel()
            }
        }


        // Return to the previous screen (typically Home) when back icon is pressed
        binding.toolbarStamp.setNavigationOnClickListener {
            activity?.finish()
        }
    }

    private fun startCamera() {
        takePicture.launch(null)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

