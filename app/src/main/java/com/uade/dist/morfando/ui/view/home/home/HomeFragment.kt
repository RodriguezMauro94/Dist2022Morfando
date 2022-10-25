package com.uade.dist.morfando.ui.view.home.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.uade.dist.morfando.core.addChips
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.databinding.FragmentHomeBinding
import com.uade.dist.morfando.ui.view.restaurantList.RestaurantsAdapter
import com.uade.dist.morfando.ui.viewmodel.home.home.HomeViewModel

class HomeFragment : Fragment(), RestaurantsAdapter.ItemClickListener {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var restaurantsNearAdapter: RestaurantsAdapter
    private lateinit var restaurantsCheapAdapter: RestaurantsAdapter
    private lateinit var restaurantsTrendingAdapter: RestaurantsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.chips.observe(viewLifecycleOwner) {
            binding.chipsGroup.removeAllViews()
            addChips(it, binding.chipsGroup, homeViewModel)
        }

        homeViewModel.chipClicked.observe(viewLifecycleOwner) {
            //TODO ir al search con el valor correspondiente para la busqueda
            Toast.makeText(context, getString(it), Toast.LENGTH_SHORT).show()
        }

        val restaurants = listOf(
            RestaurantModel("sushi tushi", "japonesa", "$$-$$$", 3.toLong(), "Palermo", "https://i.imgur.com/GB7lTPH.jpeg"),
            RestaurantModel("burger tushi", "americana", "$$$-$$$$", 1.toLong(), "Recoleta", "https://i.imgur.com/OK1u0FO.jpeg"),
            RestaurantModel("La parrilla del tano", "asado", "$-$$", 4.toLong(), "Avellaneda", "https://i.imgur.com/I0jGVwt.jpeg")
        )

        restaurantsNearAdapter = RestaurantsAdapter(
            this
        )
        bindList(binding.homeNearRestaurants, restaurantsNearAdapter)
        homeViewModel.getNearRestaurants()
        homeViewModel.nearRestaurants.observe(viewLifecycleOwner) {
            restaurantsNearAdapter.setRestaurants(it)
        }

        restaurantsCheapAdapter = RestaurantsAdapter(this)
        bindList(binding.homeCheapRestaurants, restaurantsCheapAdapter)
        restaurantsCheapAdapter.setRestaurants(restaurants)

        restaurantsTrendingAdapter = RestaurantsAdapter(this)
        bindList(binding.homeTrendingRestaurants, restaurantsTrendingAdapter)
        restaurantsTrendingAdapter.setRestaurants(restaurants)

        return root
    }

    private fun bindList(recyclerView: RecyclerView, adapter: RestaurantsAdapter) {
        val horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = horizontalLayoutManager
        recyclerView.adapter = adapter
    }

    private fun addChips(
        chips: Map<String, Int>,
        chipsGroup: ChipGroup,
        homeViewModel: HomeViewModel
    ) {
        chipsGroup.addChips(requireContext(), chips) { chipTapped ->
            homeViewModel.chipTapped(chipTapped)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(restaurant: RestaurantModel) {
        // TODO ir a VIP del restaurant
        Toast.makeText(requireContext(), "clickeado: " + restaurant.name, Toast.LENGTH_LONG).show()
    }
}