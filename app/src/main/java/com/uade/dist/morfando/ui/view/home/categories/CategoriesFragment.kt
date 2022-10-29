package com.uade.dist.morfando.ui.view.home.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.uade.dist.morfando.R
import com.uade.dist.morfando.databinding.FragmentCategoriesBinding
import com.uade.dist.morfando.ui.view.home.search.SearchFilterOptions

class CategoriesFragment : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setUpActionBar()

        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val categoriesAdapter = CategoriesAdapter(requireContext())
        binding.categoriesGrid.adapter = categoriesAdapter

        binding.categoriesGrid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            requireActivity().findNavController(R.id.nav_host_fragment_activity_home).navigate(
                R.id.navigation_search,
                bundleOf("options" to SearchFilterOptions(cookingType = categories[position].id))
            )
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