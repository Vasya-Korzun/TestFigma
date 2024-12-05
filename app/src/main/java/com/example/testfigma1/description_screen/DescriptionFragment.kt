package com.example.testfigma1.description_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext


class DescriptionFragment : Fragment() {

    val viewModel: DescriptionViewModel by viewModels()

    private var composeView: ComposeView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onDestroyView() {
        composeView = null
        super.onDestroyView()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        composeView?.setContent {
            val intentChannel = remember { Channel<DescriptionIntent>(Channel.UNLIMITED) }

            val dispatch = remember {
                { intent: DescriptionIntent ->
                    intentChannel.trySend(intent).getOrThrow()
                }
            }
            LaunchedEffect(Unit) {
                withContext(Dispatchers.Main.immediate) {
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
                ScreenDescription(
                    innerPadding,
                    viewState = viewState,
                    dispatch = dispatch
                )
            }
        }
    }

}


//TODO ---------------------------------DescriptionFragment.kt--------------------------------------

@Composable
fun ScreenDescription(
    innerPadding: PaddingValues,
    viewState: DescriptionState,
    dispatch: (DescriptionIntent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
    ) {

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
        ScreenDescription(innerPaddings, viewState = DescriptionState.initial(), dispatch = {})
    }
}
//TODO ---------------------------------DescriptionFragment.kt--------------------------------------

