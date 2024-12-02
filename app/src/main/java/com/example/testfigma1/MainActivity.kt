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
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
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
                order = dish,
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
                order = dish,
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


@Composable
fun SelectElement(selection: Selection, onClick: (section: Selection) -> Unit) {

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
                    color = if (selection == Selection.Tips) Gray else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .weight(0.5f)
                .fillMaxHeight()
                .clickable { onClick(Selection.Tips) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Tips",
                style = TextStyle(
                    color = if (selection == Selection.Tips) White else Gray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight(600)
                ),
            )
        }
    }

}


@Composable
fun TheDishesInYourOrder(
    order: Order,
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
                    text = order.header, style = TextStyle(
                        color = White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight(600)
                    )
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = order.serving.toString(), style = TextStyle(
                        color = GrayText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400)
                    )
                )
            }

            Spacer(Modifier.height(12.dp))

            Column(modifier = Modifier.height(50.dp)) {

                Text(
                    text = order.nutritionalValue,
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
                                color = when {
                                    order.nutritionalValueItems.kcal < 1300.0 -> Blue
                                    order.nutritionalValueItems.kcal >= 1300.0 && order.nutritionalValueItems.kcal < 1500.0 -> Yellow
                                    else -> Red
                                },
                                shape = RoundedCornerShape(4.dp)
                            )
                            .height(24.dp)
                            .width(69.dp),
                        contentAlignment = Alignment.Center

                    ) {
                        Text(
                            text = order.nutritionalValueItems.kcal.toString(),
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
                            text = order.nutritionalValueItems.p.toString(),
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
                            text = order.nutritionalValueItems.f.toString(),
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
                            text = order.nutritionalValueItems.c.toString(),
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
    order: Order,
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
                    .width(124.dp)
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
                        text = order.header, style = TextStyle(
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
                        text = order.nutritionalValueItems.p.toString(),
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
                        text = order.nutritionalValueItems.f.toString(),
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
                        text = order.nutritionalValueItems.c.toString(),
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
                            color = order.colorElement,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .height(24.dp)
                        .width(69.dp),
                    contentAlignment = Alignment.Center

                ) {
                    Text(
                        text = order.nutritionalValueItems.kcal.toString(),
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
