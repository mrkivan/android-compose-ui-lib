package com.tnm.android.core.ui.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.tnm.android.core.ui.AppConstants.DARK_MODE_TOPBAR_COLOR
import com.tnm.android.core.ui.view.textView.TvTitleMedium

data class AppToolbarConfig(
    val title: String = "",
    val navigationIcon: ImageVector? = null,
    val navigationIconContentDescription: String? = null,
    val onNavigationClick: (() -> Unit)? = null,
    val actions: List<ToolbarAction> = emptyList()
)

data class ToolbarAction(
    val icon: ImageVector,
    val contentDescription: String?,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(
    toolbarConfig: AppToolbarConfig,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
) {

    // Primary background based on theme
    val backgroundColor =
        if (isDarkMode) DARK_MODE_TOPBAR_COLOR
        else MaterialTheme.colorScheme.primary

    // Text/icon color based on background
    val contentColor =
        if (isDarkMode) MaterialTheme.colorScheme.onSurfaceVariant
        else MaterialTheme.colorScheme.onPrimary


    TopAppBar(
        title = {
            TvTitleMedium(
                text = toolbarConfig.title,
                color = contentColor
            )
        },
        navigationIcon = {
            toolbarConfig.navigationIcon?.let { icon ->
                IconButton(onClick = { toolbarConfig.onNavigationClick?.invoke() }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = toolbarConfig.navigationIconContentDescription,
                        tint = contentColor
                    )
                }
            }
        },
        actions = {
            toolbarConfig.actions.forEach { action ->
                IconButton(onClick = action.onClick) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = action.contentDescription,
                        tint = contentColor
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            titleContentColor = contentColor,
            navigationIconContentColor = contentColor,
            actionIconContentColor = contentColor
        ),
        modifier = modifier
    )
}
