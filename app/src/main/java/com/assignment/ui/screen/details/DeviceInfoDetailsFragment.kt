package com.assignment.ui.scan

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.assignment.BR
import com.assignment.R
import com.assignment.databinding.FragmentDeviceInfoDetailBinding
import com.assignment.ui.base.BaseFragment
import com.assignment.utils.BatteryInfo
import com.assignment.utils.DeviceInfoUtils
import com.assignment.utils.DeviceInformation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeviceInfoDetailsFragment :
    BaseFragment<DeviceInfoDetailsViewModel, FragmentDeviceInfoDetailBinding>() {
    lateinit var deviceInformation: DeviceInformation
    lateinit var batteryInfo: BatteryInfo

    override val viewModel: DeviceInfoDetailsViewModel by viewModels()
    override val layoutId = R.layout.fragment_device_info_detail
    override fun getBindingVariable() = BR.viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deviceInformation = DeviceInformation(context, activity)
        batteryInfo = BatteryInfo(context)

        if (arguments != null) {
            val infoType = DeviceInfoDetailsFragmentArgs.fromBundle(requireArguments()).infoType
            viewModel.initData(infoType, DeviceInfoUtils(deviceInformation, batteryInfo))
        }

    }
}
