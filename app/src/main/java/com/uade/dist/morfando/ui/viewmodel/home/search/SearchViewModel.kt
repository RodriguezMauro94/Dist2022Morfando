package com.uade.dist.morfando.ui.viewmodel.home.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uade.dist.morfando.ui.view.home.search.SearchFilterOptions

class SearchViewModel : ViewModel() {
    val filteredOptions = MutableLiveData<SearchFilterOptions>().apply {
        value = SearchFilterOptions()
    }
}