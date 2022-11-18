package com.jobinterviewapp.presentation.components

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.BottomAppBarDefaults.containerColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jobinterviewapp.presentation.BottomNavItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class NoRippleInteractionSource : MutableInteractionSource {

    override val interactions: Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction) = true
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar(
        modifier = modifier
            .shadow(4.dp),
    ) {
        items.forEach { item ->
            val selected = item.screen.route == backStackEntry.value?.destination?.route
                    || item.screen.subRoutes?.contains(
                backStackEntry.value?.destination?.route?.split('/')?.first()) ?: false
            NavigationBarItem(
                interactionSource = NoRippleInteractionSource(),
                selected = selected,
                onClick = {
                    if(backStackEntry.value?.destination?.route != item.screen.route) {
                        navController.navigate(item.screen.route) {
                            popUpTo(item.screen.route) {
                                inclusive = true
                            }
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = if(selected) item.iconFilledId else item.iconOutlinedId),
                        contentDescription = null,
                    )
                },
                label = {
                    Text(
                        text = item.screen.screenName.asString(),
                        fontSize = 13.sp,
                    )
                }
            )
        }
    }
}