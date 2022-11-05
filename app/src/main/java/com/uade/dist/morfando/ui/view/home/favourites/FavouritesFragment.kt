package com.uade.dist.morfando.ui.view.home.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.core.showToast
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.databinding.FragmentFavouritesBinding
import com.uade.dist.morfando.ui.view.restaurantList.RestaurantViewMode
import com.uade.dist.morfando.ui.view.restaurantList.RestaurantsAdapter
import com.uade.dist.morfando.ui.viewmodel.home.favourites.FavouritesViewModel

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

        restaurantsAdapter = RestaurantsAdapter(this, RestaurantViewMode.VERTICAL)
        bindList(binding.favouritesRestaurants, restaurantsAdapter)
        favouritesViewModel.getRestaurants()
        favouritesViewModel.favouritesRestaurants.observe(viewLifecycleOwner) {
            restaurantsAdapter.setRestaurants(it)
        }
        favouritesViewModel.requestState.observe(viewLifecycleOwner) {
            when (it) {
                is RequestState.LOADING -> {
                    // TODO mostrar skeleton
                }
                is RequestState.SUCCESS -> {
                    // TODO ocultar skeleton
                }
                is RequestState.FAILURE -> {
                    // TODO ocultar skeleton
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
        // TODO ir a VIP del restaurant
        Toast.makeText(requireContext(), "clickeado: " + restaurant.name, Toast.LENGTH_SHORT).show()
    }
}