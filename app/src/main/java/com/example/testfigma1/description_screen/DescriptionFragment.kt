package com.example.testfigma1.description_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.example.testfigma1.R
import com.example.testfigma1.description_screen.compose_ui.ScreenDescription
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DescriptionFragment : Fragment(R.layout.fragment_description) {

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


            val snackBarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

            Scaffold(
                snackbarHost = { SnackbarHost(snackBarHostState) },
                modifier = Modifier
                    .fillMaxSize(),
                containerColor = Green
            ) { innerPadding ->
                ScreenDescription(
                    innerPadding,
                    viewState = viewState,
                    dispatch = dispatch,
                    onButton1Click = {
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                "Hello GFS",
                                withDismissAction = true,
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                    },
                    onButton2Click = {
                        findNavController().popBackStack()
                    },
                    text = DescriptionFragmentArgs.fromBundle(requireArguments()).dishDescription.description,
                )
            }
        }
    }

}
