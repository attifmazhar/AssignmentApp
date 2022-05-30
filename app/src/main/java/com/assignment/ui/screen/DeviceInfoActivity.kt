package com.assignment.ui.screen

import androidx.activity.viewModels
import com.assignment.BR
import com.assignment.R
import com.assignment.databinding.ActivityMainBinding
import com.assignment.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeviceInfoActivity :
    BaseActivity<DeviceInfoViewModel, ActivityMainBinding>(R.layout.activity_main) {

    override val viewModel: DeviceInfoViewModel by viewModels()

    override fun getBindingVariable() = BR.viewModel

}