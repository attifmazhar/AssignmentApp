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


//    override val listAdapter: BaseListAdapter<Movie, out ViewDataBinding> by lazy {
//        PopularMovieAdapter(
//            itemClickListener = {
//                toMovieDetail(it)
//            }
//        )
//    }

//    override val swipeRefreshLayout: SwipeRefreshLayout
//        get() = viewBinding.refreshLayout
//
//    override val recyclerView: RecyclerView
//        get() = viewBinding.recyclerView
//
//    override fun getLayoutManager(): RecyclerView.LayoutManager = GridLayoutManager(context, 2)
//
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.btnScan.setOnClickListener {
            getNavController()?.navigate(ScanDeviceFragmentDirections.toDeviceInfoList())
        }

    }
}
