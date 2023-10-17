package com.kappstudio.joboardgame.ui.album.photo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.databinding.FragmentPhotoBinding

class PhotoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentPhotoBinding.inflate(inflater)

        val photos = PhotoFragmentArgs.fromBundle(requireArguments()).photos
        val adapter = PhotoPagerAdapter(
            childFragmentManager,
            photos
        )

        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = PhotoFragmentArgs.fromBundle(requireArguments()).position

        binding.tvReport.setOnClickListener {
            val mAlert = android.app.AlertDialog.Builder(activity)
            mAlert.setTitle(getString(R.string.report_ok))
            mAlert.setMessage(getString(R.string.google_play_want_see_this4))
            mAlert.setCancelable(true)
            mAlert.setPositiveButton(getString(R.string.ok)) { _, _ ->
            }

            val mAlertDialog = mAlert.create()
            mAlertDialog.show()
        }

        return binding.root
    }
}