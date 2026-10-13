@file:Suppress("ktlint:standard:package-name")

package parfait.core.deeplink.port.`in`

import parfait.core.deeplink.domain.Platform

interface GetDownloadStoreLinkUseCase {
    fun getDownloadLink(command: GetDownloadStoreLinkCommand): DownloadStoreLinkResult
}

data class GetDownloadStoreLinkCommand(
    val userAgent: String?,
    val originalQueryString: String?,
)

data class DownloadStoreLinkResult(
    val platform: Platform,
    val redirectUrl: String?,
    // 카카오톡 인앱 브라우저 요청이면 true. 이때 redirectUrl은 스토어가 아니라 외부 브라우저 전환 URL이다.
    val inAppBrowser: Boolean = false,
)
