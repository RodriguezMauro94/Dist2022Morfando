package com.uade.dist.morfando.ui.view.home.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.uade.dist.morfando.databinding.FragmentSearchBinding
import com.uade.dist.morfando.ui.viewmodel.home.search.SearchViewModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val searchViewModel =
            ViewModelProvider(this)[SearchViewModel::class.java]

        setUpActionBar()

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        childFragmentManager.setFragmentResultListener(
            SEARCH_FILTER_OPTIONS_RESULT_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val result = bundle.getSerializable("result")
            searchViewModel.filteredOptions.postValue(result as SearchFilterOptions)
        }

        searchViewModel.filteredOptions.observe(viewLifecycleOwner) {
            // TODO hacer llamada a la base
        }

        binding.searchFilter.setOnClickListener {
            val filterBottomSheet = SearchFilterBottomSheetFragment(searchViewModel.filteredOptions.value)
            filterBottomSheet.show(childFragmentManager, filterBottomSheet.tag)
        }

        return root
    }

    private fun setUpActionBar() {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayShowHomeEnabled(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}