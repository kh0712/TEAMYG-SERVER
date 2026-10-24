package parfait.http.parfaittext.dto

import parfait.core.parfaittext.port.`in`.UpdateParfaitTextCommand

data class UpdateParfaitTextRequest(
    val text: String? = null,
    val positionX: Double? = null,
    val positionY: Double? = null,
    val positionZ: Int? = null,
    val scale: Double? = null,
    val rotation: Double? = null,
    val color: String? = null,
    val fontSize: Double? = null,
) {
    fun toCommand(
        memberId: Long,
        groupId: Long,
        parfaitId: Long,
        parfaitTextId: Long,
    ): UpdateParfaitTextCommand =
        UpdateParfaitTextCommand(
            memberId = memberId,
            groupId = groupId,
            parfaitId = parfaitId,
            parfaitTextId = parfaitTextId,
            text = text,
            positionX = positionX,
            positionY = positionY,
            positionZ = positionZ,
            scale = scale,
            rotation = rotation,
            color = color,
            fontSize = fontSize,
        )
}
