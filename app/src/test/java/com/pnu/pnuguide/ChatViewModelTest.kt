package com.pnu.pnuguide

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.pnu.pnuguide.ui.chat.ChatViewModel
import org.junit.Assert.assertNotNull
import org.junit.Test

class ChatViewModelTest {
    @Test
    fun viewModel_initializes() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val vm = ChatViewModel(app)
        assertNotNull(vm)
    }
}