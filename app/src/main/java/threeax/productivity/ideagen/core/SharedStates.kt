package threeax.productivity.ideagen.core

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class BottomSheetStateModal : ViewModel() {
    var showBottomSheet by mutableStateOf(false)

    var activityId by mutableStateOf(0)

    var activityIndex by mutableStateOf(0)

}

class completionRatioViewModel : ViewModel() {
    var completionRatio by mutableStateOf(0f)
}