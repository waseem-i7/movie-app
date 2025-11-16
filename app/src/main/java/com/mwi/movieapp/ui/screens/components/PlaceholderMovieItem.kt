package com.mwi.movieapp.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun PlaceholderMovieItem() {
    Column(
        modifier = Modifier
            .padding(top = 17.dp, start = 8.dp, end = 8.dp)
    ) {
        Card {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(172.dp)
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(),
                    )
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .padding(top = 4.dp)
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(),
                )
        )
    }
}