package com.assignment.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.assignment.BR


abstract class BaseFragment<VM : ViewModel, VB : ViewDataBinding> : Fragment() {

    protected abstract val viewModel: VM
    protected lateinit var viewBinding: VB
    protected abstract fun getBindingVariable(): Int

    @get:LayoutRes
    protected abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewBinding.apply {
            setVariable(BR.viewModel, viewModel)
            viewBinding.lifecycleOwner = viewLifecycleOwner
            root.isClickable = true
            executePendingBindings()
        }

        return viewBinding.root
    }

    fun Fragment.getNavController(): NavController? {
        return try {
            NavHostFragment.findNavController(this)
        } catch (e: IllegalStateException) {
            null
        }
    }


}