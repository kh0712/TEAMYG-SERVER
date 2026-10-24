package parfait.http.parfaittext.dto

import parfait.core.parfaittext.port.`in`.UpdateParfaitTextResult

data class UpdateParfaitTextResponse(
    val parfaitTextId: Long,
    val text: String,
    val positionX: Double,
    val positionY: Double,
    val positionZ: Int,
    val scale: Double,
    val rotation: Double,
    val color: String,
    val fontSize: Double,
) {
    companion object {
        fun from(result: UpdateParfaitTextResult): UpdateParfaitTextResponse =
            UpdateParfaitTextResponse(
                parfaitTextId = result.parfaitTextId,
                text = result.text,
                positionX = result.positionX,
                positionY = result.positionY,
                positionZ = result.positionZ,
                scale = result.scale,
                rotation = result.rotation,
                color = result.color,
                fontSize = result.fontSize,
            )
    }
}
