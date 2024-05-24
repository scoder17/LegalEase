package com.example.legalease.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.legalease.model.LawyerData
import com.example.legalease.model.toJsonObject
import com.example.legalease.ui.chatbot.presentation.ChatBotScreen
import com.example.legalease.ui.client.addCaseScreen.AddCaseScreen
import com.example.legalease.ui.client.cases.CasesScreen
import com.example.legalease.ui.client.profile.ClientProfile
import com.example.legalease.ui.client.searchScreen.SearchScreen
import com.example.legalease.ui.client.searchScreen.SearchedLawyerDetailScreen
import com.example.legalease.ui.lawyer.home.LawyerHomeScreen
import com.example.legalease.ui.lawyer.lawyerGetStartedScreen.LawyerGetStartedScreen
import com.example.legalease.ui.lawyer.profile.LawyerProfileScreen
import com.example.legalease.ui.message.ChatListScreen
import com.example.legalease.ui.message.SingleChatScreen
import com.example.legalease.ui.signIn.SignInScreen
import com.example.legalease.ui.signIn.SignInScreenViewModel
import com.example.legalease.ui.signUp.SignUpScreen
import com.example.legalease.ui.signUp.SignUpScreenViewModel
import com.example.legalease.ui.viewModels.AuthViewModel
import com.example.legalease.ui.welcome.WelcomeScreen
import kotlinx.serialization.json.Json

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LegalEaseApp(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    signInScreenViewModel: SignInScreenViewModel,
    signUpScreenViewModel: SignUpScreenViewModel,
    authViewModel: AuthViewModel,
    onBottomBarVisibilityChanged: (Boolean) -> Unit,
    showChatbotIcon: (Boolean) -> Unit
) {
    NavHost(navController = navController, startDestination = WelcomeScreen, modifier = modifier) {
        composable<SignInScreen> {
            onBottomBarVisibilityChanged(false)
            SignInScreen(
                onSignUpClicked = {
                    navController.navigate(SignUpScreen)
                },
                signInViewModel = signInScreenViewModel,
                navigateToHome = {
                    if (signInScreenViewModel.uiState.value.isLawyer) {
                        authViewModel.getLawyer(signInScreenViewModel.currentUser?.uid ?: "")
                    } else {
                        authViewModel.getClient(signInScreenViewModel.currentUser?.uid ?: "")
                    }
                    navController.navigate(HomeScreen)
                }
            )
        }
        composable<SignUpScreen> {
            onBottomBarVisibilityChanged(false)
            SignUpScreen(
                onSignInClicked = {
                    navController.navigate(SignInScreen)
                },
                signUpViewModel = signUpScreenViewModel,
                navController = navController
            )
        }

        composable<ChatBotScreen> {
            onBottomBarVisibilityChanged(false)
            ChatBotScreen()
        }

        composable<HomeScreen> {
            onBottomBarVisibilityChanged(true)
        }
        composable<ChatListScreen> {
            onBottomBarVisibilityChanged(true)
            ChatListScreen(
                vm = authViewModel,
                onChatClicked = { id ->
                    navController.navigate(SingleChatScreen(id = id))
                }
            )

        }
        composable<SingleChatScreen> {
            onBottomBarVisibilityChanged(false)
            val args = it.toRoute<SingleChatScreen>()
            SingleChatScreen(vm = authViewModel, chatId = args.id, onBack = {
                navController.popBackStack()
            }
            )
        }
        composable<SearchScreen> {
            onBottomBarVisibilityChanged(true)
            SearchScreen(onLawyerClicked = {
                navController.navigate(
                    SearchedLayerScreen(
                        it.id
                    )
                )
            })
        }
        composable<ProfileScreen> {
            onBottomBarVisibilityChanged(true)
            LawyerProfileScreen()
        }
        composable<HomeScreen> {
            onBottomBarVisibilityChanged(true)
            showChatbotIcon(true)
            LawyerHomeScreen(signedInViewModel = signInScreenViewModel, onSeeAllClick = { navController.navigate(CaseListScreen) })
        }
        composable<SearchedLayerScreen> {
            val args = it.toRoute<SearchedLayerScreen>()
            onBottomBarVisibilityChanged(false)
            SearchedLawyerDetailScreen(
                lawyerId = args.lawyerId,
                navigateToAddCaseScreen = { lawyerId ->
                    navController.navigate(AddCaseScreen(lawyerId))
                },
                vm = authViewModel,
            )
        }
        composable<LawyerGetStartedScreen> {
            onBottomBarVisibilityChanged(false)
            LawyerGetStartedScreen(
                viewModel = signUpScreenViewModel,
            )
        }
        composable<WelcomeScreen> {
            onBottomBarVisibilityChanged(false)
            showChatbotIcon(false)
            WelcomeScreen { navController.navigate(SignInScreen) }
        }
        composable<AddCaseScreen> {
            val args = it.toRoute<AddCaseScreen>()
            onBottomBarVisibilityChanged(false)
            AddCaseScreen(lawyerId = args.lawyerId)
        }
        composable<ClientProfileScreen> {
            onBottomBarVisibilityChanged(true)
            ClientProfile(viewModel = authViewModel)
        }
        composable<CaseListScreen> {
            onBottomBarVisibilityChanged(true)
            CasesScreen()
        }
    }
}

