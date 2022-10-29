package com.uade.dist.morfando.ui.view.home.search

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.uade.dist.morfando.R
import com.uade.dist.morfando.databinding.BottomSheetSearchFilterBinding
import java.io.Serializable

class SearchFilterBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: BottomSheetSearchFilterBinding? = null
    private val binding get() = _binding!!
    private val options = SearchFilterOptions()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSearchFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchFiltersAccept.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                SEARCH_FILTER_OPTIONS_RESULT_KEY,
                // TODO llenar objeto de info
                bundleOf("result" to SearchFilterOptions())
            )
            dismiss()
        }

        binding.searchFiltersDistanceSlider.addOnChangeListener { _, value, _ ->
            binding.searchFiltersDistanceValue.text = "${value.toInt()} Km."
            options.distance = value.toInt()
        }


    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val rootView = View.inflate(context, R.layout.bottom_sheet_search_filter, null)
        dialog.setContentView(rootView)

        val bottomSheet = dialog.window?.findViewById(R.id.design_bottom_sheet) as FrameLayout
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.skipCollapsed = true
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}

const val SEARCH_FILTER_OPTIONS_RESULT_KEY = "searchFilterOptionsResult"

data class SearchFilterOptions(
    var openNow: Boolean? = false,
    var priceRange: Int? = 1,
    var ratingRange: Int? = 1,
    var cookingType: String? = null,
    var distance: Int? = 5
): Serializable