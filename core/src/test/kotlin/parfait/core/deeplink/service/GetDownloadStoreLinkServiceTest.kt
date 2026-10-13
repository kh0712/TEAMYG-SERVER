package parfait.core.deeplink.service

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import parfait.core.deeplink.domain.Platform
import parfait.core.deeplink.port.`in`.GetDownloadStoreLinkCommand

class GetDownloadStoreLinkServiceTest {
    @Test
    fun `Android User-Agent면 Play Store URL에 원본 쿼리스트링을 referrer로 인코딩해 넣는다`() {
        val service =
            GetDownloadStoreLinkService(
                androidPackageName = "com.teamyg.parfait",
                iosAppStoreId = "",
                linkDomain = "parfait-app.store",
            )

        val result =
            service.getDownloadLink(
                GetDownloadStoreLinkCommand(
                    userAgent = "Mozilla/5.0 (Linux; Android 14)",
                    originalQueryString = "link_id=abc&campaign=summer",
                ),
            )

        result.platform shouldBe Platform.ANDROID
        result.redirectUrl shouldBe
            "https://play.google.com/store/apps/details?id=com.teamyg.parfait&referrer=link_id%3Dabc%26campaign%3Dsummer"
    }

    @Test
    fun `Android User-Agent인데 쿼리스트링이 없으면 referrer 없이 기본 URL만 반환한다`() {
        val service =
            GetDownloadStoreLinkService(
                androidPackageName = "com.teamyg.parfait",
                iosAppStoreId = "",
                linkDomain = "parfait-app.store",
            )

        val result =
            service.getDownloadLink(
                GetDownloadStoreLinkCommand(userAgent = "Android", originalQueryString = null),
            )

        result.redirectUrl shouldBe "https://play.google.com/store/apps/details?id=com.teamyg.parfait"
    }

    @Test
    fun `iOS User-Agent이고 App Store ID가 설정되어 있으면 App Store URL을 반환한다`() {
        val service =
            GetDownloadStoreLinkService(
                androidPackageName = "com.teamyg.parfait",
                iosAppStoreId = "1234567890",
                linkDomain = "parfait-app.store",
            )

        val result =
            service.getDownloadLink(
                GetDownloadStoreLinkCommand(
                    userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X)",
                    originalQueryString = "link_id=abc",
                ),
            )

        result.platform shouldBe Platform.IOS
        result.redirectUrl shouldBe "https://apps.apple.com/app/id1234567890"
    }

    @Test
    fun `iOS User-Agent인데 App Store ID가 비어 있으면 redirectUrl이 null이다`() {
        val service =
            GetDownloadStoreLinkService(
                androidPackageName = "com.teamyg.parfait",
                iosAppStoreId = "",
                linkDomain = "parfait-app.store",
            )

        val result =
            service.getDownloadLink(
                GetDownloadStoreLinkCommand(
                    userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X)",
                    originalQueryString = null,
                ),
            )

        result.platform shouldBe Platform.IOS
        result.redirectUrl shouldBe null
    }

    @Test
    fun `데스크톱 User-Agent면 redirectUrl이 null이다`() {
        val service =
            GetDownloadStoreLinkService(
                androidPackageName = "com.teamyg.parfait",
                iosAppStoreId = "1234567890",
                linkDomain = "parfait-app.store",
            )

        val result =
            service.getDownloadLink(
                GetDownloadStoreLinkCommand(
                    userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15)",
                    originalQueryString = "link_id=abc",
                ),
            )

        result.platform shouldBe Platform.OTHER
        result.redirectUrl shouldBe null
    }

    @Test
    fun `카카오톡 인앱 브라우저면 원본 링크를 인코딩한 외부 브라우저 전환 URL을 반환한다`() {
        val service =
            GetDownloadStoreLinkService(
                androidPackageName = "com.teamyg.parfait",
                iosAppStoreId = "",
                linkDomain = "parfait-app.store",
            )

        val result =
            service.getDownloadLink(
                GetDownloadStoreLinkCommand(
                    userAgent = "Mozilla/5.0 (Linux; Android 14) Chrome/120 Mobile Safari/537.36 KAKAOTALK 2511290",
                    originalQueryString = "inviteCode=ABC123",
                ),
            )

        result.inAppBrowser shouldBe true
        result.platform shouldBe Platform.ANDROID
        result.redirectUrl shouldBe
            "kakaotalk://web/openExternal?url=https%3A%2F%2Fparfait-app.store%2Flink%3FinviteCode%3DABC123"
    }

    @Test
    fun `카카오톡 인앱 브라우저인데 쿼리스트링이 없으면 물음표 없이 원본 링크를 인코딩한다`() {
        val service =
            GetDownloadStoreLinkService(
                androidPackageName = "com.teamyg.parfait",
                iosAppStoreId = "",
                linkDomain = "parfait-app.store",
            )

        val result =
            service.getDownloadLink(
                GetDownloadStoreLinkCommand(
                    userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0) KAKAOTALK 10.4.0",
                    originalQueryString = null,
                ),
            )

        result.inAppBrowser shouldBe true
        result.platform shouldBe Platform.IOS
        result.redirectUrl shouldBe "kakaotalk://web/openExternal?url=https%3A%2F%2Fparfait-app.store%2Flink"
    }

    @Test
    fun `카카오톡 UA가 아니면 inAppBrowser가 false이고 기존 스토어 분기를 탄다`() {
        val service =
            GetDownloadStoreLinkService(
                androidPackageName = "com.teamyg.parfait",
                iosAppStoreId = "",
                linkDomain = "parfait-app.store",
            )

        val result =
            service.getDownloadLink(
                GetDownloadStoreLinkCommand(
                    userAgent = "Mozilla/5.0 (Linux; Android 14) Chrome/120",
                    originalQueryString = "inviteCode=ABC123",
                ),
            )

        result.inAppBrowser shouldBe false
        result.redirectUrl shouldBe
            "https://play.google.com/store/apps/details?id=com.teamyg.parfait&referrer=inviteCode%3DABC123"
    }
}
