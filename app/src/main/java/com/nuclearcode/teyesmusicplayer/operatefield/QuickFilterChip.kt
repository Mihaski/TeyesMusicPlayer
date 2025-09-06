package com.nuclearcode.teyesmusicplayer.operatefield

import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun QuickFilterChip(
    label: String,
    filter: QuickFilter,
    active: Set<QuickFilter>,
    onToggle: (QuickFilter) -> Unit
) {
    FilterChip(
        selected = filter in active,
        onClick = { onToggle(filter) },
        label = { Text(label) }
    )
}