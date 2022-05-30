package com.assignment.ui.scan

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.assignment.BR
import com.assignment.R
import com.assignment.databinding.FragmentScanBinding
import com.assignment.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanDeviceFragment :
    BaseFragment<ScanDeviceViewModel, FragmentScanBinding>() {

    override val viewModel: ScanDeviceViewModel by viewModels()
    override val layoutId = R.layout.fragment_scan
    override fun getBindingVariable() = BR.viewModel

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.btnScan.setOnClickListener {
            getNavController()?.navigate(ScanDeviceFragmentDirections.toDeviceInfoList())
        }

    }
}
