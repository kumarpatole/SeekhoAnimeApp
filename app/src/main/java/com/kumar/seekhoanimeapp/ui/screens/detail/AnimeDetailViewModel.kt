package com.kumar.seekhoanimeapp.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kumar.seekhoanimeapp.data.local.entity.AnimeEntity
import com.kumar.seekhoanimeapp.data.repository.AnimeRepository
import com.kumar.seekhoanimeapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    private val repository: AnimeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    init {
        savedStateHandle.get<Int>("animeId")?.let { id ->

            repository.getAnimeDetails(id)
                .onEach {
                    _state.value = DetailState(anime = it.data)
                }
                .launchIn(viewModelScope)

            viewModelScope.launch {
                repository.refreshAnimeDetails(id)
            }
        }
    }


    private fun getAnimeDetails(id: Int) {
        repository.getAnimeDetails(id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = DetailState(anime = result.data)
                }
                is Resource.Error -> {
                    _state.value = DetailState(
                        error = result.message ?: "An unexpected error occurred",
                        anime = result.data
                    )
                }
                is Resource.Loading -> {
                    _state.value = DetailState(isLoading = true, anime = result.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.ToggleFavorite -> {
                viewModelScope.launch {
                    _state.value.anime?.let {
//                        repository.toggleFavoriteStatus(it)
                        repository.toggleFavorite(it)
                    }
                }
            }
        }
    }
}

data class DetailState(
    val isLoading: Boolean = false,
    val anime: AnimeEntity? = null,
    val error: String? = null
)

sealed class DetailEvent {
    object ToggleFavorite : DetailEvent()
}
