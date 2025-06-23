package com.pnu.pnuguide.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.pnu.pnuguide.R
import android.widget.TextView
import android.widget.ProgressBar
import android.net.Uri
import com.pnu.pnuguide.data.QuizProgress
import com.pnu.pnuguide.ui.course.CourseActivity
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.io.FileOutputStream

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<MaterialButton>(R.id.btn_download_map_top).setOnClickListener {
            openCampusMapPdf()
        }
        view.findViewById<MaterialButton>(R.id.btn_view_favorites).setOnClickListener {
            startActivity(Intent(requireContext(), CourseActivity::class.java))
        }
        val user = FirebaseAuth.getInstance().currentUser
        val welcome = view.findViewById<TextView>(R.id.tv_welcome)
        val email = user?.email
        val prefix = email?.substringBefore("@") ?: ""
        welcome.text = "방문을 환영합니다! ${prefix}님"


        view.findViewById<MaterialButton>(R.id.btn_apply_tour).setOnClickListener {
            openWebUrl("https://www.pusan.ac.kr/kor/CMS/CampusTour/information.do?mCode=MN137")
        }

        view.findViewById<MaterialButton>(R.id.btn_open_youtube).setOnClickListener {
            openWebUrl("https://www.youtube.com/channel/UC2C8QLSMp3QMFS96xQc7WRA")
        }

        val count = QuizProgress.getCount(requireContext())
        val progressText = view.findViewById<TextView>(R.id.tv_stamp_progress)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_stamp)
        progressText.text = "Stamps Collected: $count/25"
        progressBar.max = 25
        progressBar.progress = count
    }

    override fun onResume() {
        super.onResume()
        view?.let { v ->
            val user = FirebaseAuth.getInstance().currentUser
            val prefix = user?.email?.substringBefore("@") ?: ""
            v.findViewById<TextView>(R.id.tv_welcome)?.text = "방문을 환영합니다! ${prefix}님"
        }
    }

    private fun openCampusMapPdf() {
        val inputStream = resources.openRawResource(R.raw.campus_map)
        val file = File(requireContext().getExternalFilesDir(null), "campus_map.pdf")
        if (!file.exists()) {
            FileOutputStream(file).use { output ->
                inputStream.copyTo(output)
            }
        }
        val uri: Uri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(intent)
    }

    private fun openWebUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
