package com.uade.dist.morfando.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.core.showToast
import com.uade.dist.morfando.databinding.ActivityPersonalDataBinding
import com.uade.dist.morfando.ui.viewmodel.PersonalDataViewModel

class PersonalDataActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPersonalDataBinding
    private val personalDataViewModel: PersonalDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        personalDataViewModel.getPersonalData()

        binding.save.setOnClickListener {
            personalDataViewModel.updatePersonalData()
        }

        personalDataViewModel.getRequestState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    getString(R.string.loading).showToast(this)
                }
                is RequestState.SUCCESS -> {
                    updateFields()
                }
                is RequestState.FAILURE -> {
                    getString(R.string.generic_error).showToast(this)
                }
            }
        }

        personalDataViewModel.postRequestState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    getString(R.string.loading).showToast(this)
                }
                is RequestState.SUCCESS -> {
                    getString(R.string.personal_data_updated).showToast(this)
                }
                is RequestState.FAILURE -> {
                    getString(R.string.generic_error).showToast(this)
                }
            }
        }

        binding.nameValue.doAfterTextChanged {
            personalDataViewModel.personalData.value?.apply {
                name = it.toString()
            }
        }

        //TODO foto
    }

    private fun updateFields() {
        personalDataViewModel.personalData.value?.apply {
            binding.nameValue.setText(name)
            //TODO foto
        }
    }
}