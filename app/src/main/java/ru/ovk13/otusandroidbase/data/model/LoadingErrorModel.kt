package ru.ovk13.otusandroidbase.data.model

class LoadingErrorModel(
    val message: String,
    val type: Int
) {
    companion object {
        const val FULL_RELOAD = 0
        const val LOAD_PAGE = 1
    }
}