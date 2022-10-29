package com.uade.dist.morfando.ui.view.home.search

import android.os.Bundle
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

        /*val textView: TextView = binding.textSearch
        searchViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/

        childFragmentManager.setFragmentResultListener(
            SEARCH_FILTER_OPTIONS_RESULT_KEY,
            viewLifecycleOwner
        ) { key, bundle ->
            // TODO hacer llamada a la base
            val result = bundle.getSerializable("bundleKey")
            Toast.makeText(requireContext(), "Resultado de filtrado", Toast.LENGTH_LONG).show()
        }



        binding.searchFilter.setOnClickListener {
           /* SearchFilterBottomSheetFragment.newInstance(Bundle()).apply {
                show(parentFragmentManager, tag)
            }*/
            val filterBottomSheet = SearchFilterBottomSheetFragment()
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