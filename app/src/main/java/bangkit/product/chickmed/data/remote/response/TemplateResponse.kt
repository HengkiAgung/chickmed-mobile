package bangkit.product.chickmed.data.remote.response

import bangkit.product.chickmed.ui.state.UiState
import com.google.gson.annotations.SerializedName

data class TemplateResponse <T: Any> (
    @field:SerializedName("success")
    val success: Boolean,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("data")
    val data: T,
)