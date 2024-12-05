package com.example.testfigma1.description_screen

import com.example.testfigma1.base.MviIntent
import com.example.testfigma1.base.MviSingleEvent
import com.example.testfigma1.base.MviViewState
import com.example.testfigma1.main_screen.Order

data class DescriptionState(
    val dishDescription: Order?
) : MviViewState {
    companion object {
        fun initial() = DescriptionState(
            dishDescription = null
        )
    }
}

sealed interface PartialStateChange {

    fun reduce(viewState: DescriptionState): DescriptionState

    data class Insert(val index: Int) : PartialStateChange {
        override fun reduce(viewState: DescriptionState): DescriptionState {
            return viewState.copy(
                dishDescription = null
            )
        }
    }

}

sealed class DescriptionIntent : MviIntent {
    data class Insert(val index: Int) : DescriptionIntent()
}

sealed class DescriptionEvent : MviSingleEvent




