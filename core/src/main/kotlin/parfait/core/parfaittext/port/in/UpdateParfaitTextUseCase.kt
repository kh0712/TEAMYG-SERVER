@file:Suppress("ktlint:standard:package-name")

package parfait.core.parfaittext.port.`in`

interface UpdateParfaitTextUseCase {
    fun update(command: UpdateParfaitTextCommand): UpdateParfaitTextResult
}

data class UpdateParfaitTextCommand(
    val memberId: Long,
    val groupId: Long,
    val parfaitId: Long,
    val parfaitTextId: Long,
    val text: String? = null,
    val positionX: Double? = null,
    val positionY: Double? = null,
    val positionZ: Int? = null,
    val scale: Double? = null,
    val rotation: Double? = null,
    val color: String? = null,
    val fontSize: Double? = null,
)

data class UpdateParfaitTextResult(
    val parfaitTextId: Long,
    val text: String,
    val positionX: Double,
    val positionY: Double,
    val positionZ: Int,
    val scale: Double,
    val rotation: Double,
    val color: String,
    val fontSize: Double,
)
