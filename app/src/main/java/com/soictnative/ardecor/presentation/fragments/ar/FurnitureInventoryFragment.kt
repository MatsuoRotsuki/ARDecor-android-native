package com.soictnative.ardecor.presentation.fragments.ar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.soictnative.ardecor.R
import com.soictnative.ardecor.presentation.adapters.FurnitureInventoryCategoryAdapter
import com.soictnative.ardecor.presentation.adapters.FurnitureInventoryProductAdapter
import com.soictnative.ardecor.databinding.FurnitureInventoryDialogBinding
import com.soictnative.ardecor.util.ScreenState
import com.soictnative.ardecor.presentation.viewmodel.ARViewModel
import com.soictnative.ardecor.presentation.viewmodel.FurnitureInventoryViewModel
import com.unity3d.player.UnityPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FurnitureInventoryFragment: DialogFragment(R.layout.furniture_inventory_dialog) {
    private lateinit var binding: FurnitureInventoryDialogBinding
    private val viewModel by viewModels<FurnitureInventoryViewModel>()
    protected val productAdapter by lazy { FurnitureInventoryProductAdapter() }
    protected val categoryAdapter by lazy { FurnitureInventoryCategoryAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullscreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FurnitureInventoryDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProductRv()
        productAdapter.onClick = { product ->
            val gson = Gson()
            val productJson = gson.toJson(product)
            UnityPlayer.UnitySendMessage("CommandInvoker", "InitializeOnEvent", productJson)
            dismiss()
        }
        setupCategoryRv()
        categoryAdapter.onClick = { category ->
            Toast.makeText(requireContext(), "Lọc ra các sản phẩm là ${category.name}", Toast.LENGTH_SHORT).show()
            viewModel.fetchProductOnCategory(category.id)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.products.collect {
                when (it) {
                    is ScreenState.Loading -> {
                        binding.progressBarFurnitureInventory.visibility = View.VISIBLE
                    }

                    is ScreenState.Success -> {
                        binding.progressBarFurnitureInventory.visibility = View.GONE
                        productAdapter.differ.submitList(it.uiData)
                    }

                    is ScreenState.Error -> {
                        binding.progressBarFurnitureInventory.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.categories.collectLatest {
                when (it) {
                    is ScreenState.Loading -> {
                        binding.progressBarFurnitureInventory.visibility = View.VISIBLE
                    }

                    is ScreenState.Success -> {
                        binding.progressBarFurnitureInventory.visibility = View.GONE
                        categoryAdapter.differ.submitList(it.uiData)
                    }

                    is ScreenState.Error -> {
                        binding.progressBarFurnitureInventory.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun setupCategoryRv() {
        binding.rvARProduct.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun setupProductRv() {
        binding.rvCategoryFurnitureInventory.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }
}