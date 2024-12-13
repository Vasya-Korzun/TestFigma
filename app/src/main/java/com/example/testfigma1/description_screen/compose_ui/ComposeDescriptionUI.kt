package com.example.testfigma1.description_screen.compose_ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testfigma1.description_screen.DescriptionIntent
import com.example.testfigma1.description_screen.DescriptionState


@Composable
fun ScreenDescription(
    innerPadding: PaddingValues,
    viewState: DescriptionState,
    dispatch: (DescriptionIntent) -> Unit,
    onButton1Click: () -> Unit,
    onButton2Click: () -> Unit,
    text: String,
) {

    Text(
        text = text,
        style = TextStyle(
            color = White,
            fontSize = 30.sp,
            fontWeight = FontWeight(600)
        )
    )

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = { onButton1Click() },
                shape = RoundedCornerShape(20.dp),

                ) {
                Text("Desc", fontSize = 20.sp)
            }
        }
        Box(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = { onButton2Click() },
                shape = RoundedCornerShape(20.dp),

                ) {
                Text("Back", fontSize = 20.sp)
            }
        }

    }
}


@Preview
@Composable
fun PreviewScreenDescription() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Green
    ) { innerPaddings ->
        ScreenDescription(
            innerPaddings,
            viewState = DescriptionState.initial(),
            dispatch = {},
            onButton1Click = {},
            onButton2Click = {},
            text = "Item"
        )
    }
}


