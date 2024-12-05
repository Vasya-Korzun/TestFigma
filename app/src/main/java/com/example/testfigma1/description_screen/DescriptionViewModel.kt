package com.example.testfigma1.description_screen

import androidx.lifecycle.viewModelScope
import com.example.testfigma1.base.AbstractMviViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn

class DescriptionViewModel :
    AbstractMviViewModel<DescriptionIntent, DescriptionState, DescriptionEvent>() {
    override val viewState: StateFlow<DescriptionState>

    init {
        val initialVS = DescriptionState.initial()
        viewState = intentSharedFlow
            .shareWhileSubscribed()
            .toPartialStateChangeFlow()
            .scan(initialVS) { vs, change -> change.reduce(vs) }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                initialVS
            )
    }

    private fun SharedFlow<DescriptionIntent>.toPartialStateChangeFlow(): Flow<PartialStateChange> {

        val initialFlow = filterIsInstance<DescriptionIntent.Insert>().map { intent ->
            PartialStateChange.Insert(intent.index)
        }.shareWhileSubscribed()


        return merge(
            initialFlow,
        )
    }

}