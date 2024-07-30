package app.home.homepage.vm

import androidx.lifecycle.ViewModel
import com.home.torrent.main.page.main.model.HomePageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _state: MutableStateFlow<HomePageState> = MutableStateFlow(HomePageState())

    val state = _state.asStateFlow()
}