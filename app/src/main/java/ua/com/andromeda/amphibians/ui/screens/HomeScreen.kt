package ua.com.andromeda.amphibians.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ua.com.andromeda.amphibians.R
import ua.com.andromeda.amphibians.network.Amphibian
import ua.com.andromeda.amphibians.ui.theme.AmphibiansTheme

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
    val modifierFillMaxWidth = modifier.fillMaxWidth()
    when (amphibiansUiState) {
        is AmphibiansUiState.SUCCESS -> AmphibiansListScreen(
            amphibians = amphibiansUiState.amphibians,
            modifier = modifierFillMaxWidth
        )

        is AmphibiansUiState.LOADING -> LoadingScreen(modifier = modifierFillMaxWidth)
        is AmphibiansUiState.ERROR -> ErrorScreen(modifier = modifier)
    }
}

@Composable
fun AmphibiansListScreen(amphibians: List<Amphibian>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp)
    ) {
        items(amphibians, key = { amphibian -> amphibian.name }) {
            AmphibianCard(it)
        }
    }
}

@Composable
private fun AmphibianCard(amphibian: Amphibian, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column {
            Text(
                text = "${amphibian.name}(${amphibian.type})",
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(amphibian.imgSrc)
                    .crossfade(true)
                    .build(),
                contentDescription = amphibian.name,
                placeholder = painterResource(R.drawable.loading_icon),
                error = painterResource(R.drawable.broken_img),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = amphibian.description,
                modifier = Modifier.padding(8.dp)
            )
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
        modifier = modifier.requiredSize(30.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun AmphibianCardPreview() {
    AmphibiansTheme {
        AmphibianCard(amphibian = Amphibian("Ukrainian Toad", "Toad", "What a wonderful toad!", ""))
    }
}
