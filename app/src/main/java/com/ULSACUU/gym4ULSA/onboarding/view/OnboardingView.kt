package com.ULSACUU.gym4ULSA.routine.onboarding.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ULSACUU.gym4ULSA.onboarding.viewmodel.OnboardingViewModel

@Composable
fun OnboardingView(
    viewModel: OnboardingViewModel,
    onFinish: () -> Unit = {} // callback
) {
    val pages by viewModel.pages.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()

    val pagerState = rememberPagerState(
        initialPage = currentPage,
        pageCount = { pages.size }
    )
    val scope = rememberCoroutineScope()

    // Sync VM <-> Pager
    LaunchedEffect(pagerState.currentPage) {
        viewModel.setPage(pagerState.currentPage)
    }
    LaunchedEffect(currentPage) {
        if (pagerState.currentPage != currentPage) {
            pagerState.scrollToPage(currentPage)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Main Content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            OnboardingPageView(
                pageModel = pages[page],
                isLastPage = viewModel.isLastPage(page)
            )
        }

        // Dots Indicator - positioned at the top
        DotsIndicatorView(
            totalDots = pages.size,
            selectedIndex = currentPage,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp)
        )

        // Bottom indicator (optional - shows subtle hint on last page)
        BottomBarView(
            isLastPage = viewModel.isLastPage(),
            onFinish = {
                onFinish()
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )


    }
}
