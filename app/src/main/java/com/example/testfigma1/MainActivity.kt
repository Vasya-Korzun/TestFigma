package com.example.testfigma1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testfigma1.ui.theme.GrayText
import com.example.testfigma1.ui.theme.GreenBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val intentChannel = remember { Channel<MainIntent>(Channel.UNLIMITED) }

            val dispatch = remember {
                { intent: MainIntent ->
                    intentChannel.trySend(intent).getOrThrow()
                }
            }

            LaunchedEffect(Unit) {
                withContext(Dispatchers.Main.immediate) {   //TODO read!!!!!!
                    intentChannel
                        .consumeAsFlow()
                        .onEach(viewModel::processIntent)
                        .collect()
                }
            }

            val viewState by viewModel.viewState.collectAsStateWithLifecycle()

            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                containerColor = Green
            ) { innerPadding ->
                Screen(
                    innerPadding,
                    viewState = viewState,
                    dispatch = dispatch
                )
            }
        }
    }
}


data class Order(val colorElement: Color)

@Composable
fun Screen(
    innerPadding: PaddingValues,
    viewState: MainState,
    dispatch: (MainIntent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
    ) {
        item {
            SelectElement(
                selection = viewState.selectedSection,
                onClick = { section ->
                    dispatch(MainIntent.ChangeSelectSection(section))
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "The dishes in your Order",
                style = TextStyle(
                    color = White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(600)
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        itemsIndexed(viewState.fullListDishes) { index, dish ->
            TheDishesInYourOrder(
                modifierColor = dish.colorElement,
                onClick = { dispatch(MainIntent.Add(index)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Text(
                text = "Add all dishes to FatSecret",
                style = TextStyle(
                    color = White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(600)
                ),
            )
        }
        item {
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "Selected dishes",
                style = TextStyle(
                    color = White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(600)
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        itemsIndexed(viewState.selectedDishes) { index, dish ->
            SelectDishes(
                modifierColor = dish.colorElement,
                onClick = { dispatch(MainIntent.Remove(index)) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Add to FatSecret",
                style = TextStyle(
                    color = White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(600)
                )
            )
        }
    }
}


//@Composable
//fun SelectElement(viewState: MainState, dispatch: (MainIntent) -> Unit) { //TODO изменить

@Composable
fun SelectElement(selection: Selection, onClick: (s: Selection) -> Unit) { //TODO изменить

    Row(
        modifier = Modifier
            .padding()
            .padding(2.dp)
            .fillMaxWidth()
            .height(44.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (selection == Selection.Calories) Gray else Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                )
                .weight(0.5f)
                .fillMaxHeight()
//                .clickable { dispatch(MainIntent.ChangeSelectSection(Selection.Calories)) },
                .clickable { onClick(Selection.Calories) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Calories",
                style = TextStyle(
                    color = if (selection == Selection.Calories) White else Gray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight(600)
                ),
            )
        }
        Box(
            modifier = Modifier
                .background(
                    color = if (selection == Selection.Tip) Gray else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .weight(0.5f)
                .fillMaxHeight()
//                .clickable { dispatch(MainIntent.ChangeSelectSection(Selection.Tip)) },
                .clickable { onClick(Selection.Tip) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Tips",
                style = TextStyle(
                    color = if (selection == Selection.Tip) White else Gray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight(600)
                ),
            )
        }
    }

}


@Composable
fun TheDishesInYourOrder(
    modifierColor: Color,
    onClick: () -> Unit
) {

    Box {
        Column(
            modifier = Modifier
                .background(
                    color = Black,
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxWidth()
                .height(134.dp)
                .padding(16.dp)
        ) {

            Column {

                Text(
                    text = "Header", style = TextStyle(
                        color = White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight(600)
                    )
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "XXX g - XXX serving", style = TextStyle(
                        color = GrayText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400)
                    )
                )
            }

            Spacer(Modifier.height(12.dp))

            Column(modifier = Modifier.height(50.dp)) {

                Text(
                    text = "Nutritional value",
                    style = TextStyle(
                        color = White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight(600)
                    ),
                )

                Spacer(Modifier.height(9.dp))


                Row {
                    Box(
                        modifier = Modifier
                            .background(
                                color = modifierColor,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .height(24.dp)
                            .width(69.dp),
                        contentAlignment = Alignment.Center

                    ) {
                        Text(
                            text = "XXX ccal",
                            style = TextStyle(
                                color = GrayText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight(600)
                            ),
                        )
                    }

                    Spacer(modifier = Modifier.padding(4.dp))

                    Box(
                        modifier = Modifier
                            .background(
                                color = Gray,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .height(24.dp)
                            .width(69.dp),
                        contentAlignment = Alignment.Center

                    ) {
                        Text(
                            text = "P:XX g",
                            style = TextStyle(
                                color = GrayText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight(600)
                            ),
                        )
                    }

                    Spacer(modifier = Modifier.padding(4.dp))

                    Box(
                        modifier = Modifier
                            .background(
                                color = Gray,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .height(24.dp)
                            .width(69.dp),
                        contentAlignment = Alignment.Center

                    ) {
                        Text(
                            text = "F:XX g",
                            style = TextStyle(
                                color = GrayText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight(600)
                            ),
                        )
                    }

                    Spacer(modifier = Modifier.padding(4.dp))

                    Box(
                        modifier = Modifier
                            .background(
                                color = Gray,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .height(24.dp)
                            .width(69.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "C:XX g",
                            style = TextStyle(
                                color = GrayText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight(600)
                            ),
                        )
                    }
                }


            }

        }

        Image(
            painter = painterResource(R.drawable.icon_add),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clickable {
                    onClick()
                }

        )
    }
}


@Composable
fun SelectDishes(
    modifierColor: Color,
    onClick: () -> Unit
) {
    Box {
        Column(
            modifier = Modifier
                .background(
                    color = Black,
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxWidth()
                .height(96.dp)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .height(24.dp)
                    .width(104.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .width(39.dp)
                        .background(color = GreenBackground, shape = RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Full", style = TextStyle(
                            color = Green,
                            fontSize = 12.sp,
                            fontWeight = FontWeight(600)
                        )
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Box(
                    modifier = Modifier
                        .height(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Header", style = TextStyle(
                            color = White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(600),
                        )
                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Box(
                    modifier = Modifier
                        .background(
                            color = Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .height(24.dp)
                        .width(69.dp),
                    contentAlignment = Alignment.Center

                ) {
                    Text(
                        text = "P:XX g",
                        style = TextStyle(
                            color = GrayText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight(600)
                        ),
                    )
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Box(
                    modifier = Modifier
                        .background(
                            color = Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .height(24.dp)
                        .width(69.dp),
                    contentAlignment = Alignment.Center

                ) {
                    Text(
                        text = "F:XX g",
                        style = TextStyle(
                            color = GrayText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight(600)
                        ),
                    )
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Box(
                    modifier = Modifier
                        .background(
                            color = Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .height(24.dp)
                        .width(69.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "C:XX g",
                        style = TextStyle(
                            color = GrayText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight(600)
                        ),
                    )
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Box(
                    modifier = Modifier
                        .background(
                            color = modifierColor,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .height(24.dp)
                        .width(69.dp),
                    contentAlignment = Alignment.Center

                ) {
                    Text(
                        text = "XXX ccal",
                        style = TextStyle(
                            color = GrayText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight(600)
                        ),
                    )
                }

            }


        }


        Image(
            painter = painterResource(R.drawable.icon_remove),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clickable {
                    onClick()
                }
        )
    }
}


@Preview
@Composable
fun PreviewScreen() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Green
    ) { innerPaddings ->
        Screen(innerPaddings, viewState = MainState.initial(), dispatch = {})
    }
}
