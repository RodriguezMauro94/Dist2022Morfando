package com.uade.dist.morfando.ui.view.home.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.addChips
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.databinding.FragmentHomeBinding
import com.uade.dist.morfando.ui.view.restaurantList.RestaurantViewMode
import com.uade.dist.morfando.ui.view.restaurantList.RestaurantsAdapter
import com.uade.dist.morfando.ui.viewmodel.home.home.HomeViewModel

class HomeFragment : Fragment(), RestaurantsAdapter.ItemClickListener {
    private var _binding: FragmentHomeBinding? = null
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

        setUpActionBar()
        setUpMenu()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.chips.observe(viewLifecycleOwner) {
            binding.chipsGroup.removeAllViews()
            addChips(it, binding.chipsGroup, homeViewModel)
        }

        homeViewModel.chipClicked.observe(viewLifecycleOwner) {
            // TODO ir al search con el valor correspondiente para la busqueda
            Toast.makeText(context, getString(it), Toast.LENGTH_SHORT).show()
        }

        val restaurants = listOf(
            RestaurantModel("sushi tushi", "japonesa", 2, 3.toLong(), "Palermo", "https://i.imgur.com/GB7lTPH.jpeg"),
            RestaurantModel("burger tushi", "americana", 3, 1.toLong(), "Recoleta", "https://i.imgur.com/OK1u0FO.jpeg"),
            RestaurantModel("La parrilla del tano", "asado", 4, 4.toLong(), "Avellaneda", "https://i.imgur.com/I0jGVwt.jpeg")
        )

        restaurantsNearAdapter = RestaurantsAdapter(this, RestaurantViewMode.HORIZONTAL)
        bindList(binding.homeNearRestaurants, restaurantsNearAdapter)
        homeViewModel.getNearRestaurants()
        homeViewModel.nearRestaurants.observe(viewLifecycleOwner) {
            restaurantsNearAdapter.setRestaurants(it)
        }
        homeViewModel.nearRestaurantsState.observe(viewLifecycleOwner) {
            // TODO capturar loading y mostrar/ocultar skeleton o mostrar un error
        }

        // FIXME deshardcodear
        restaurantsCheapAdapter = RestaurantsAdapter(this, RestaurantViewMode.HORIZONTAL)
        bindList(binding.homeCheapRestaurants, restaurantsCheapAdapter)
        restaurantsCheapAdapter.setRestaurants(restaurants)

        // FIXME deshardcodear
        restaurantsTrendingAdapter = RestaurantsAdapter(this, RestaurantViewMode.HORIZONTAL)
        bindList(binding.homeTrendingRestaurants, restaurantsTrendingAdapter)
        restaurantsTrendingAdapter.setRestaurants(restaurants)

        return root
    }

    private fun setUpActionBar() {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setIcon(R.drawable.logo_home)
        }
    }

    private fun setUpMenu() {
        val menuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                if (menuItem.itemId == R.id.action_account) {
                    //TODO go to account
                    Toast.makeText(context, "Account", Toast.LENGTH_SHORT).show()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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