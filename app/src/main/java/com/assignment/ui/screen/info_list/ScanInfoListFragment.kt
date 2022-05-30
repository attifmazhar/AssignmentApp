package com.assignment.ui.scan

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.assignment.BR
import com.assignment.R
import com.assignment.databinding.FragmentDeviceInfoListBinding
import com.assignment.ui.adapter.ScanInfoListAdapter
import com.assignment.ui.base.BaseFragment
import com.assignment.utils.BatteryInfo
import com.assignment.utils.DeviceInfoUtils
import com.assignment.utils.DeviceInformation
import com.assignment.utils.FileUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScanInfoListFragment :
    BaseFragment<ScanInfoListViewModel, FragmentDeviceInfoListBinding>() {

    lateinit var deviceInformation: DeviceInformation
    lateinit var batteryInfo: BatteryInfo

    override val viewModel: ScanInfoListViewModel by viewModels()
    override val layoutId = R.layout.fragment_device_info_list
    override fun getBindingVariable() = BR.viewModel

    @Inject
    lateinit var adapter: ScanInfoListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewBinding.list.adapter = adapter
        adapter.setItemClickListener {
            getNavController()?.navigate(ScanInfoListFragmentDirections.toDeviceInfoDetail(it))
        }

        deviceInformation = DeviceInformation(context, activity)
        batteryInfo = BatteryInfo(context)



        this.context?.let {
            FileUtils.writeStringAsFile(it, DeviceInfoUtils(deviceInformation, batteryInfo).getAllDeviceInfo(), "device_info.txt" )

//            val allData = FileUtils.readFileAsString(it,"device_info.txt")
//            println(allData)

        }


    }
}
