package com.railly_linker.springboot_mvc_project_template.data_sources.network_retrofit2.request_apis

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

// (한 주소에 대한 API 요청명세)
// 사용법은 아래 기본 사용 샘플을 참고하여 추상함수를 작성하여 사용
interface AccountsGoogleComRequestApi {
    // [Google Oauth2 AccessToken 요청]
    @POST("/o/oauth2/token")
    @FormUrlEncoded
    fun oOauth2Token(
        // OAuth2 로그인으로 발급받은 코드
        @Field("code") code: String,
        // OAuth2 ClientId
        @Field("client_id") clientId: String,
        // OAuth2 clientSecret
        @Field("client_secret") clientSecret: String,
        // 무조건 "authorization_code" 입력
        @Field("grant_type") grantType: String,
        // OAuth2 로그인할때 사용한 Redirect Uri
        @Field("redirect_uri") redirectUri: String
    ): Call<OOauth2TokenOutputVO?>

    data class OOauth2TokenOutputVO(
        @SerializedName("access_token")
        @Expose
        val accessToken: String?,
        @SerializedName("expires_in")
        @Expose
        val expiresIn: Long?,
        @SerializedName("scope")
        @Expose
        val scope: String?,
        @SerializedName("token_type")
        @Expose
        val tokenType: String?
    )
}