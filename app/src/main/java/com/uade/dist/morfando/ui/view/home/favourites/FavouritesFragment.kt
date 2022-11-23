package com.uade.dist.morfando.ui.view.home.favourites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.core.showToast
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_NAME
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_TOKEN
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.databinding.FragmentFavouritesBinding
import com.uade.dist.morfando.ui.view.RestaurantDetailsActivity
import com.uade.dist.morfando.ui.view.restaurantList.RestaurantViewMode
import com.uade.dist.morfando.ui.view.restaurantList.RestaurantsAdapter
import com.uade.dist.morfando.ui.viewmodel.home.favourites.FavouritesViewModel
import io.supercharge.shimmerlayout.ShimmerLayout

class FavouritesFragment : Fragment(), RestaurantsAdapter.ItemClickListener {
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var restaurantsAdapter: RestaurantsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val favouritesViewModel =
            ViewModelProvider(this)[FavouritesViewModel::class.java]

        setUpActionBar()

        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, AppCompatActivity.MODE_PRIVATE)
        val token = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null) ?: ""

        restaurantsAdapter = RestaurantsAdapter(this, RestaurantViewMode.VERTICAL)
        bindList(binding.favouritesRestaurants, restaurantsAdapter)
        favouritesViewModel.getRestaurants(token)
        favouritesViewModel.favouritesRestaurants.observe(viewLifecycleOwner) {
            restaurantsAdapter.setRestaurants(it)
        }

        val shimmer = binding.root.findViewById<ShimmerLayout>(R.id.skeleton)
        favouritesViewModel.requestState.observe(viewLifecycleOwner) {
            when (it) {
                is RequestState.LOADING -> {
                    shimmer.visibility = View.VISIBLE
                    binding.favouritesRestaurants.visibility = View.GONE
                    binding.noneFavourites.visibility = View.GONE
                    shimmer.startShimmerAnimation()
                }
                is RequestState.SUCCESS -> {
                    shimmer.stopShimmerAnimation()
                    shimmer.visibility = View.GONE
                    binding.favouritesRestaurants.visibility = View.VISIBLE
                    if (binding.favouritesRestaurants.isEmpty()) {
                        binding.noneFavourites.visibility = View.VISIBLE
                    }
                }
                is RequestState.FAILURE -> {
                    shimmer.stopShimmerAnimation()
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