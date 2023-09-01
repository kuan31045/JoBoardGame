package com.kappstudio.joboardgame.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.factory.VMFactory
import com.kappstudio.joboardgame.databinding.DialogReportBinding
import tech.gujin.toast.ToastUtil

class ReportDialog : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.ThemeOverlay_AppCompat_Dialog_Alert)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val binding = DialogReportBinding.inflate(inflater)

        val viewModel: ReportViewModel by viewModels {
            VMFactory {
                ReportViewModel(
                    ReportDialogArgs.fromBundle(requireArguments()).user,
                )
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.sendOk.observe(viewLifecycleOwner,{
            dismiss()
            ToastUtil.show("檢舉已送出")

            val mAlert = android.app.AlertDialog.Builder(activity)
            mAlert.setTitle("檢舉已送出")
            mAlert.setMessage(getString(R.string.google_play_want_see_this))
            mAlert.setCancelable(false)
            mAlert.setPositiveButton("關閉"){_,_->
            }

            val  mAlertDialog = mAlert.create()
            mAlertDialog.show()
        })

        return binding.root
    }
}