package com.pnu.pnuguide.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.pnu.pnuguide.databinding.FragmentProfileBinding
import com.pnu.pnuguide.ui.LoginActivity
import com.pnu.pnuguide.ui.profile.EditProfileActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { u ->
            binding.textName.text = u.displayName ?: ""
            binding.textEmail.text = u.email ?: ""
            u.photoUrl?.let { url ->
                Glide.with(this).load(url).into(binding.imageProfile)
            }
        }

        binding.buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.buttonEdit.setOnClickListener {
            // Launch the EditProfileActivity when the user taps the button
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Additional feature ideas:
    // - Navigate to an EditProfileActivity from button_edit to modify name or photo
    // - Provide Change Password option using FirebaseAuth.updatePassword
    // - Link to app SettingsActivity for notifications or theme
    // - Show recent activity such as viewed courses or certification history
}
