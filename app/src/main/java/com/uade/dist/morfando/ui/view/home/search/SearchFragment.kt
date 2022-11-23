package com.uade.dist.morfando.ui.view.home.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.core.getLocation
import com.uade.dist.morfando.core.showToast
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_NAME
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_TOKEN
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.data.model.SearchFilterOptionsModel
import com.uade.dist.morfando.databinding.FragmentSearchBinding
import com.uade.dist.morfando.ui.view.RestaurantDetailsActivity
import com.uade.dist.morfando.ui.view.restaurantList.RestaurantViewMode
import com.uade.dist.morfando.ui.view.restaurantList.RestaurantsAdapter
import com.uade.dist.morfando.ui.viewmodel.home.search.SearchViewModel

class SearchFragment : Fragment(), RestaurantsAdapter.ItemClickListener {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var restaurantsAdapter: RestaurantsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val searchViewModel =
            ViewModelProvider(this)[SearchViewModel::class.java]

        setUpActionBar()

        getLocation(requireContext()) {
            searchViewModel.latitude = it.latitude
            searchViewModel.longitude = it.longitude
        }

        val sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, AppCompatActivity.MODE_PRIVATE)
        val token = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null) ?: ""

        arguments?.getSerializable("options")?.let {
            searchViewModel.filteredOptions.postValue(it as SearchFilterOptionsModel)
        }

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        childFragmentManager.setFragmentResultListener(
            SEARCH_FILTER_OPTIONS_RESULT_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val result = bundle.getSerializable("result")
            searchViewModel.filteredOptions.postValue(result as SearchFilterOptionsModel)
        }

        searchViewModel.filteredOptions.observe(viewLifecycleOwner) {
            searchViewModel.getRestaurants(token)
        }

        searchViewModel.searchText.observe(viewLifecycleOwner) {
            searchViewModel.getRestaurants(token)
        }

        binding.searchText.addTextChangedListener {
            searchViewModel.searchText.postValue(it.toString())
        }

        binding.searchFilter.setOnClickListener {
            val filterBottomSheet = SearchFilterBottomSheetFragment(searchViewModel.filteredOptions.value)
            filterBottomSheet.show(childFragmentManager, filterBottomSheet.tag)
        }

        restaurantsAdapter = RestaurantsAdapter(this, RestaurantViewMode.VERTICAL)
        bindList(binding.searchRestaurants, restaurantsAdapter)
        searchViewModel.searchRestaurants.observe(viewLifecycleOwner) {
            restaurantsAdapter.setRestaurants(it)
            if (it.isEmpty()) {
                binding.empty.visibility = View.VISIBLE
            }
        }

        searchViewModel.requestState.observe(viewLifecycleOwner) {
            when (it) {
                is RequestState.LOADING -> {
                    binding.loading.visibility = View.VISIBLE
                    binding.searchRestaurants.visibility = View.GONE
                }
                is RequestState.SUCCESS -> {
                    binding.loading.visibility = View.GONE
                    binding.searchRestaurants.visibility = View.VISIBLE
                }
                is RequestState.FAILURE -> {
                    binding.loading.visibility = View.GONE
                    binding.searchRestaurants.visibility = View.GONE
                    getString(R.string.generic_error).showToast(requireContext())
                }
            }
        }

        return root
    }

    private fun bindList(recyclerView: RecyclerView, adapter: RestaurantsAdapter) {
        val verticalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = verticalLayoutManager
        recyclerView.adapter = adapter
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

    override fun onItemClick(restaurant: RestaurantModel) {
        val intent = Intent(requireContext(), RestaurantDetailsActivity::class.java)
        intent.putExtra("restaurant", restaurant)
        startActivity(intent)
    }
}