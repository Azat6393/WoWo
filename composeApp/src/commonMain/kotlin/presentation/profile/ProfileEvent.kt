package presentation.profile

sealed class ProfileEvent {
    data object GetUserInfo: ProfileEvent()
    data object GetUserStatistics: ProfileEvent()
    data object SaveNickname: ProfileEvent()
    data class  OnNickNameChanged(val nickname: String): ProfileEvent()
}