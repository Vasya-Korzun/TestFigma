package com.example.testfigma1.main_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.example.testfigma1.R
import com.example.testfigma1.main_screen.compose_ui.ScreenMain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext


class MainFragment : Fragment(R.layout.fragment_main) {

    val viewModel: MainViewModel by viewModels()

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
                containerColor = Blue
            ) { innerPadding ->
                ScreenMain(
                    innerPadding,
                    viewState = viewState,
                    dispatch = dispatch,
                    onButtonClick = { order ->
                        findNavController()
                            .navigate(
                                MainFragmentDirections.actionMainFragmentToDescriptionFragment(
                                    order
                                )
                            )
                    }
                )
            }
        }
    }
}
