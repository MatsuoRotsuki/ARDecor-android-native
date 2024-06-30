package com.soictnative.ardecor.presentation.fragments.ar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.soictnative.ardecor.presentation.adapters.FurnitureContextAdapter
import com.soictnative.ardecor.util.Resource
import com.soictnative.ardecor.presentation.viewmodel.ARViewModel
import com.soictnative.ardecor.R
import com.soictnative.ardecor.databinding.FurnitureContextDialogBinding
import com.soictnative.ardecor.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FurnitureContextFragment: BottomSheetDialogFragment(R.layout.furniture_context_dialog) {
    private lateinit var binding: FurnitureContextDialogBinding
    private lateinit var furnitureContextAdapter: FurnitureContextAdapter
    private val viewModel by viewModels<ARViewModel>()

    companion object {
        private const val TAG = "FurnitureContextDialog"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FurnitureContextDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFurnitureContextRv()

        lifecycleScope.launchWhenStarted {
            viewModel.inventoryProducts.collectLatest {
                when (it) {
                    is ScreenState.Loading -> {
                        showLoading()
                    }
                    is ScreenState.Success -> {
                        hideLoading()
                        furnitureContextAdapter.differ.submitList(it.uiData)
                    }
                    is ScreenState.Error -> {
                        hideLoading()
                        Log.e(TAG, it.message)
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showLoading() {
    }

    private fun hideLoading() {
    }

    private fun setupFurnitureContextRv() {
        furnitureContextAdapter = FurnitureContextAdapter()
        binding.rvFurnitureContext.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = furnitureContextAdapter
        }
    }
}