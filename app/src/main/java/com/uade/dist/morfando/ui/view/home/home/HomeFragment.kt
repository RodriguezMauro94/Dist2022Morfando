package com.uade.dist.morfando.ui.view.home.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.*
import com.uade.dist.morfando.data.local.SHARED_IS_OWNER
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_NAME
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_TOKEN
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.databinding.FragmentHomeBinding
import com.uade.dist.morfando.ui.view.MyProfileActivity
import com.uade.dist.morfando.ui.view.RestaurantDetailsActivity
import com.uade.dist.morfando.ui.view.restaurantList.RestaurantViewMode
import com.uade.dist.morfando.ui.view.restaurantList.RestaurantsAdapter
import com.uade.dist.morfando.ui.viewmodel.home.home.HomeViewModel

class HomeFragment : Fragment(), RestaurantsAdapter.ItemClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var restaurantsNearAdapter: RestaurantsAdapter
    private lateinit var myRestaurantsAdapter: RestaurantsAdapter
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

        getLocation(requireActivity()) {
            homeViewModel.latitude = it.latitude
            homeViewModel.longitude = it.longitude
        }

        val sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, AppCompatActivity.MODE_PRIVATE)
        val token = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null) ?: ""
        val isOwner = sharedPreferences.getBoolean(SHARED_IS_OWNER, false)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.getPersonalData(sharedPreferences, token)

        homeViewModel.chips.observe(viewLifecycleOwner) {
            binding.chipsGroup.removeAllViews()
            addChips(it, binding.chipsGroup, homeViewModel)
        }

        homeViewModel.chipClicked.observe(viewLifecycleOwner) {
            it.option.latitude = homeViewModel.latitude
            it.option.longitude = homeViewModel.longitude
            requireActivity().findNavController(R.id.nav_host_fragment_activity_home).navigate(
                R.id.navigation_search,
                bundleOf("options" to it.option)
            )
        }

        if (isOwner) {
            binding.homeMyRestaurantsTitle.visibility = View.VISIBLE
            binding.homeMyRestaurantsGroup.visibility = View.VISIBLE

            myRestaurantsAdapter = RestaurantsAdapter(this, RestaurantViewMode.HORIZONTAL)
            bindList(binding.homeMyRestaurants, myRestaurantsAdapter)
            homeViewModel.getMyRestaurants(token)
            homeViewModel.myRestaurants.observe(viewLifecycleOwner) {
                myRestaurantsAdapter.setRestaurants(it)
                if (it.isEmpty()) {
                    binding.emptyMyRestaurants.visibility = View.VISIBLE
                }
            }
            homeViewModel.myRestaurantsState.observe(viewLifecycleOwner) {
                when (it) {
                    is RequestState.LOADING -> {
                        binding.loadingMyRestaurants.visibility = View.VISIBLE
                        binding.homeMyRestaurants.visibility = View.GONE
                    }
                    is RequestState.SUCCESS -> {
                        binding.loadingMyRestaurants.visibility = View.GONE
                        binding.homeMyRestaurants.visibility = View.VISIBLE
                    }
                    is RequestState.FAILURE -> {
                        binding.loadingMyRestaurants.visibility = View.GONE
                        binding.homeMyRestaurants.visibility = View.GONE
                        getString(R.string.generic_error).showToast(requireContext())
                    }
                }
            }
        }

        restaurantsNearAdapter = RestaurantsAdapter(this, RestaurantViewMode.HORIZONTAL)
        bindList(binding.homeNearRestaurants, restaurantsNearAdapter)
        homeViewModel.getNearRestaurants(token)
        homeViewModel.nearRestaurants.observe(viewLifecycleOwner) {
            restaurantsNearAdapter.setRestaurants(it)
        }
        homeViewModel.nearRestaurantsState.observe(viewLifecycleOwner) {
            when (it) {
                is RequestState.LOADING -> {
                    binding.loadingNear.visibility = View.VISIBLE
                    binding.homeNearRestaurants.visibility = View.GONE
                }
                is RequestState.SUCCESS -> {
                    binding.loadingNear.visibility = View.GONE
                    binding.homeNearRestaurants.visibility = View.VISIBLE
                }
                is RequestState.FAILURE -> {
                    binding.loadingNear.visibility = View.GONE
                    binding.homeNearRestaurants.visibility = View.GONE
                    getString(R.string.generic_error).showToast(requireContext())
                }
            }
        }

        restaurantsCheapAdapter = RestaurantsAdapter(this, RestaurantViewMode.HORIZONTAL)
        bindList(binding.homeCheapRestaurants, restaurantsCheapAdapter)
        homeViewModel.getCheapRestaurants(token)
        homeViewModel.cheapRestaurants.observe(viewLifecycleOwner) {
            restaurantsCheapAdapter.setRestaurants(it)
        }
        homeViewModel.cheapRestaurantsState.observe(viewLifecycleOwner) {
            when (it) {
                is RequestState.LOADING -> {
                    binding.loadingCheap.visibility = View.VISIBLE
                    binding.homeCheapRestaurants.visibility = View.GONE
                }
                is RequestState.SUCCESS -> {
                    binding.loadingCheap.visibility = View.GONE
                    binding.homeCheapRestaurants.visibility = View.VISIBLE
                }
                is RequestState.FAILURE -> {
                    binding.loadingCheap.visibility = View.GONE
                    binding.homeCheapRestaurants.visibility = View.GONE
                    getString(R.string.generic_error).showToast(requireContext())
                }
            }
        }

        restaurantsTrendingAdapter = RestaurantsAdapter(this, RestaurantViewMode.HORIZONTAL)
        bindList(binding.homeTrendingRestaurants, restaurantsTrendingAdapter)
        homeViewModel.getTrendingRestaurants(token)
        homeViewModel.trendingRestaurants.observe(viewLifecycleOwner) {
            restaurantsTrendingAdapter.setRestaurants(it)
        }
        homeViewModel.trendingRestaurantsState.observe(viewLifecycleOwner) {
            when (it) {
                is RequestState.LOADING -> {
                    binding.loadingTrending.visibility = View.VISIBLE
                    binding.homeTrendingRestaurants.visibility = View.GONE
                }
                is RequestState.SUCCESS -> {
                    binding.loadingTrending.visibility = View.GONE
                    binding.homeTrendingRestaurants.visibility = View.VISIBLE
                }
                is RequestState.FAILURE -> {
                    binding.loadingTrending.visibility = View.GONE
                    binding.homeTrendingRestaurants.visibility = View.GONE
                    getString(R.string.generic_error).showToast(requireContext())
                }
            }
        }

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
                if (menuItem.itemId == R.id.action_account) {
                    startActivity(Intent(requireActivity(), MyProfileActivity::class.java))
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
        chips: Map<String, ChipSearchOptionsModel>,
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
        val intent = Intent(requireContext(), RestaurantDetailsActivity::class.java)
        intent.putExtra("restaurant", restaurant)
        startActivity(intent)
    }
}