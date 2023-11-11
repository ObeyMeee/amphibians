package ua.com.andromeda.amphibians.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ua.com.andromeda.amphibians.R
import ua.com.andromeda.amphibians.network.Amphibian

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmphibiansApp() {
    Scaffold(topBar = { AmphibiansTopAppBar() }) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val amphibiansViewModel: AmphibiansViewModel =
                viewModel(factory = AmphibiansViewModel.Factory)
            HomeScreen(amphibiansViewModel.uiState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmphibiansTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        modifier = modifier,
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    )
}

@Composable
fun HomeScreen(amphibiansUiState: AmphibiansUiState, modifier: Modifier = Modifier) {
    when (amphibiansUiState) {
        is AmphibiansUiState.SUCCESS -> AmphibiansListScreen(amphibiansUiState.amphibians)
        is AmphibiansUiState.LOADING -> LoadingScreen()
        is AmphibiansUiState.ERROR -> ErrorScreen()
    }
}

@Composable
fun AmphibiansListScreen(amphibians: List<Amphibian>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(amphibians, key = { amphibian -> amphibian.name }) {
            AmphibianCard(it)
        }
    }
}

@Composable
private fun AmphibianCard(amphibian: Amphibian, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column {
            Text(text = "${amphibian.name}(${amphibian.type})")
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(amphibian.imgSrc)
                    .crossfade(true)
                    .placeholder(R.drawable.loading_icon)
                    .error(R.drawable.broken_img)
                    .build(),
                contentDescription = amphibian.name
            )
            Text(text = amphibian.description)
        }
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.connection_failed),
            contentDescription = stringResource(R.string.connection_failed)
        )
        Text(text = stringResource(R.string.connection_failed))
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.loading_icon),
        contentDescription = null,
        modifier = modifier
    )
}
