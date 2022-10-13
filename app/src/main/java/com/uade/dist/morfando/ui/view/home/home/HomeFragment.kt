package com.uade.dist.morfando.ui.view.home.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.ChipGroup
import com.uade.dist.morfando.core.addChips
import com.uade.dist.morfando.databinding.FragmentHomeBinding
import com.uade.dist.morfando.ui.viewmodel.home.home.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        homeViewModel.chips.observe(viewLifecycleOwner) {
            binding.chipsGroup.removeAllViews()
            addChips(it, binding.chipsGroup, homeViewModel)
        }

        homeViewModel.chipClicked.observe(viewLifecycleOwner) {
            //TODO ir al search con el valor correspondiente para la busqueda
            Toast.makeText(context, getString(it), Toast.LENGTH_SHORT).show()
        }

        return root
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
}