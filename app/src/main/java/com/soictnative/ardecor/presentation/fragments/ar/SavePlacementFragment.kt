package com.soictnative.ardecor.presentation.fragments.ar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.soictnative.ardecor.R
import com.soictnative.ardecor.databinding.FragmentSavePlacementBinding

class SavePlacementFragment: DialogFragment() {
    private lateinit var binding: FragmentSavePlacementBinding

    companion object {
        const val PLACEMENT_JSON_KEY = "PLACEMENT_JSON_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullscreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavePlacementBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        
    }
}