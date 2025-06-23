package com.pnu.pnuguide.ui.course

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pnu.pnuguide.R
import com.pnu.pnuguide.ui.quiz.QuizActivity
import com.pnu.pnuguide.ui.stamp.StampActivity

class SpotDetailDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_spot_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = requireArguments().getString(ARG_TITLE, "")
        val desc = requireArguments().getString(ARG_DESC, "")
        val imageRes = requireArguments().getInt(ARG_IMAGE_RES)
        val imageUrl = requireArguments().getString(ARG_IMAGE_URL)

        val imageView = view.findViewById<ImageView>(R.id.image_detail)
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(imageView)
        } else if (imageRes != 0) {
            imageView.setImageResource(imageRes)
        }

        view.findViewById<TextView>(R.id.text_detail_title).text = title
        view.findViewById<TextView>(R.id.text_detail_desc).text = desc

        view.findViewById<MaterialButton>(R.id.button_camera).setOnClickListener {
            startActivity(Intent(requireContext(), StampActivity::class.java))
            dismiss()
        }
        view.findViewById<MaterialButton>(R.id.button_quiz).setOnClickListener {
            QuizActivity.start(requireContext(), title)
            dismiss()
        }
    }

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_DESC = "arg_desc"
        private const val ARG_IMAGE_RES = "arg_image_res"
        private const val ARG_IMAGE_URL = "arg_image_url"

        fun newInstance(item: CourseItem): SpotDetailDialogFragment {
            val f = SpotDetailDialogFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, item.title)
            args.putString(ARG_DESC, item.duration)
            item.imageRes?.let { args.putInt(ARG_IMAGE_RES, it) }
            item.imageUrl?.let { args.putString(ARG_IMAGE_URL, it) }
            f.arguments = args
            return f
        }
    }
}
