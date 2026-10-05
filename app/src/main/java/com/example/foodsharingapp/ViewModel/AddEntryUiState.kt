package com.example.foodsharingapp.ViewModel

// for photo, description, tags(up to 3), location, save
// UI state
data class AddEntry(
    val photoUrl: String? = null,
    val photoSource: PhotoSource? = null,
    val photoError: String? = null,

    val description: String = "",
    val descriptionError: String? = null,

    val tags: List<String> = emptyList(),
    val tagInput: String = "",
    val tagError: String? = null,

    val latitude: Double? = null,
    val longitude: Double? = null,
    val placeName: String? = null,
    val isFetchingLocation: Boolean = false,
    val locationError: String? = null,

    val isSaving: Boolean = false,
    val saveError: String? = null,
    val savedSuccessfully: Boolean = false
) {
    val canSave: Boolean
        get() = photoUrl != null
                && description.isNotBlank()
                && tags.size <= 3
                && !isSaving
}

enum class PhotoSource { CAMERA, GALLERY }
