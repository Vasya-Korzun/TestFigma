package com.example.testfigma1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testfigma1.ui.theme.GrayText
import com.example.testfigma1.ui.theme.GreenBackground

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                containerColor = Green
            ) { innerPadding ->
                Screen(innerPadding)
            }
        }
    }
}


@Composable
fun Screen(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
//            .verticalScroll(
//                rememberScrollState(),
//            )
    ) {
        SelectElement()
        TheDishesInYourOrderScreen()
        selectDishesScreen()
    }
}


@Composable
fun selectDishes() {
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

            Row() {
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
                            color = Blue,
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
            painter = painterResource(R.drawable.icon_add),
            contentDescription = "",
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}

@Composable
fun selectDishesScreen() {
    Spacer(modifier = Modifier.height(48.dp))        //??????????????????????????????????????
    Text(
        text = "Selected dishes",
        style = TextStyle(
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight(600)
        ),
    )
    Spacer(modifier = Modifier.padding(16.dp))

    LazyColumn(
//        userScrollEnabled = false
    ) {
        items(3) { item ->
            selectDishes()
        }
    }
    Spacer(modifier = Modifier.padding(16.dp))
    Text(
        text = "Add to FatSecret",
        style = TextStyle(
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight(600)
        ),
    )

}


data class Order(val colorElement: Color)

@Composable
fun TheDishesInYourOrderScreen() {
    val headers = mutableListOf(Order(Blue), Order(Color.Yellow), Order(Color.Red))

    Text(
        text = "The dishes in your Order",
        style = TextStyle(
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight(600)
        ),
    )
    Spacer(modifier = Modifier.height(16.dp))
    LazyColumn(
//        userScrollEnabled = false
    ) {
        items(headers) { item ->
            TheDishesInYourOrder(item.colorElement)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Add all dishes to FatSecret",
        style = TextStyle(
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight(600)
        ),
    )

}


@Composable
fun SelectElement() {

    val selectedElement = remember { mutableStateOf(false) }

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
                    color = if (!selectedElement.value) Gray else Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                )
                .weight(0.5f)
                .fillMaxHeight()
                .clickable { selectedElement.value = false },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Calories",
                style = TextStyle(
                    color = if (!selectedElement.value) White else Gray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight(600)
                ),
            )
        }
        Box(
            modifier = Modifier
                .background(
                    color = if (selectedElement.value) Gray else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .weight(0.5f)
                .fillMaxHeight()
                .clickable { selectedElement.value = true },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Tips",
                style = TextStyle(
                    color = if (selectedElement.value) White else Gray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight(600)
                ),
            )
        }
    }

}


@Composable
fun TheDishesInYourOrder(modifierColor: Color) {

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

            Column() {

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


                Row() {
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
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}


@Preview
@Composable
fun PreviewScreen() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) { innerPaddings ->
        Screen(innerPaddings)
    }
}
