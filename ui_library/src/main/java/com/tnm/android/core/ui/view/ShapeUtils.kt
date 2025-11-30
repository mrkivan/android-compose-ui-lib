package com.tnm.android.core.ui.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SpacerHeightSmall() = Spacer(modifier = Modifier.height(4.dp))

@Composable
fun SpacerHeightMedium() = Spacer(modifier = Modifier.height(8.dp))

@Composable
fun SpacerHeightLarge() = Spacer(modifier = Modifier.height(16.dp))

@Composable
fun SpacerWidthSmall() = Spacer(modifier = Modifier.width(4.dp))

@Composable
fun SpacerWidthMedium() = Spacer(modifier = Modifier.width(8.dp))

@Composable
fun SpacerWidthLarge() = Spacer(modifier = Modifier.width(16.dp))