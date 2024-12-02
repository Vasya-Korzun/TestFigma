package com.example.testfigma1

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Yellow
import com.example.testfigma1.base.MviIntent
import com.example.testfigma1.base.MviSingleEvent
import com.example.testfigma1.base.MviViewState


data class MainState(
    var selectedSection: Selection,
    val fullListDishes: List<Order>,
    val selectedDishes: List<Order>
) : MviViewState {
    companion object {
        fun initial() = MainState(

            selectedSection = Selection.Calories,

            fullListDishes = listOf(
                Order(
                    "Soup",
                    350.0,
                    "Nutritional value:",
                    Blue,
                    NutritionalValueElement(
                        1200.0, 400.0, 250.0, 500.0
                    )
                ),
                Order(
                    "Lasagna",
                    250.0,
                    "Nutritional value:",
                    Yellow,
                    NutritionalValueElement(
                        1400.0, 450.0, 350.0, 580.0
                    )
                ),
                Order(
                    "Dessert",
                    150.0,
                    "Nutritional value:",
                    Red,
                    NutritionalValueElement(
                        1700.0, 120.0, 200.0, 180.0
                    )
                )
            ),

            selectedDishes = listOf()
        )
    }
}


sealed interface PartialStateChange {

    fun reduce(viewState: MainState): MainState

    data class Add(val index: Int) : PartialStateChange {
        override fun reduce(viewState: MainState): MainState {
            val newList = viewState.selectedDishes + viewState.fullListDishes[index]
            return viewState.copy(
                selectedDishes = newList
            )
        }
    }

    data class Remove(val index: Int) : PartialStateChange {
        override fun reduce(viewState: MainState): MainState {
            var newList = listOf<Order>()
            if (viewState.selectedDishes.isNotEmpty())
                newList = viewState.selectedDishes - viewState.selectedDishes[index]
            return viewState.copy(
                selectedDishes = newList
            )
        }
    }

    data class ChangeSelectSection(val select: Selection) : PartialStateChange {
        override fun reduce(viewState: MainState): MainState {
            println("CHANGE ${select.name}")
            return viewState.copy(
                selectedSection = select
            )
        }
    }

}


sealed class MainIntent : MviIntent {
    data class Add(val index: Int) : MainIntent()
    data class Remove(val index: Int) : MainIntent()
    data class ChangeSelectSection(val select: Selection) :
        MainIntent()
}

sealed class MainEvent : MviSingleEvent

enum class Selection {
    Calories, Tips;
}


data class NutritionalValueElement(
    val kcal: Double,
    val p: Double,
    val f: Double,
    val c: Double
)

data class Order(
    val header: String,
    val serving: Double,
    val nutritionalValue: String,
    val colorElement: Color,
    val nutritionalValueItems: NutritionalValueElement
)






