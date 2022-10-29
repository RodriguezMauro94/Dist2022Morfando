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
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.addCheckedChips
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
                bundleOf("result" to options)
            )
            dismiss()
        }

        addOpenNowChips()
        addPriceRangeChips()
        addRatingRangeChips()
        addCookingTypeChips()

        binding.searchFiltersOpenNowGroup.setOnCheckedStateChangeListener { _, _ ->
            options.openNow = true
        }

        binding.searchFiltersCookingTypeGroup.setOnCheckedStateChangeListener { group, _ ->
            val checkedValue = group.findViewById<Chip>(group.checkedChipId).tag as String
            options.cookingType = checkedValue
        }

        binding.searchFiltersPriceRangeGroup.setOnCheckedStateChangeListener { group, _ ->
            val checkedValue = group.findViewById<Chip>(group.checkedChipId).tag as String
            options.priceRange = checkedValue.toInt()
        }

        binding.searchFiltersRatingRangeGroup.setOnCheckedStateChangeListener { group, _ ->
            val checkedValue = group.findViewById<Chip>(group.checkedChipId).tag as String
            options.ratingRange = checkedValue.toInt()
        }

        binding.searchFiltersDistanceSlider.addOnChangeListener { _, value, _ ->
            binding.searchFiltersDistanceValue.text = "${value.toInt()} Km."
            options.distance = value.toInt()
        }
    }

    private fun addOpenNowChips() {
        addChips(
            mapOf(
                Pair("open_now", R.string.search_filters_open_now)
            ),
            binding.searchFiltersOpenNowGroup
        )
    }

    private fun addPriceRangeChips() {
        addChips(
            mapOf(
                Pair("1", R.string.price_range_first),
                Pair("2", R.string.price_range_second),
                Pair("3", R.string.price_range_third),
                Pair("4", R.string.price_range_fourth)
            ),
            binding.searchFiltersPriceRangeGroup
        )
    }

    private fun addRatingRangeChips() {
        addChips(
            mapOf(
                Pair("1", R.string.rating_range_one),
                Pair("2", R.string.rating_range_two),
                Pair("3", R.string.rating_range_three),
                Pair("4", R.string.rating_range_four)
            ),
            binding.searchFiltersRatingRangeGroup
        )
    }

    private fun addCookingTypeChips() {
        addChips(
            mapOf(
                Pair("meat", R.string.cooking_type_meat),
                Pair("chicken", R.string.cooking_type_chicken),
                Pair("pasta", R.string.cooking_type_pasta),
                Pair("pizza", R.string.cooking_type_pizza),
                Pair("salad", R.string.cooking_type_salad),
                Pair("sushi", R.string.cooking_type_sushi),
                Pair("empanada", R.string.cooking_type_empanada),
                Pair("tacos", R.string.cooking_type_tacos)
            ),
            binding.searchFiltersCookingTypeGroup
        )
    }

    private fun addChips(
        chips: Map<String, Int>,
        chipsGroup: ChipGroup
    ) {
        chipsGroup.addCheckedChips(requireContext(), chips)
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