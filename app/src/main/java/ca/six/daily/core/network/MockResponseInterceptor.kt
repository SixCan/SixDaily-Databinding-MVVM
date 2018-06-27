package ca.six.daily.core.network

import okhttp3.*

class MockResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (HttpEngine.isMock) {
            val jsonContent = HttpEngine.mockJson
            val responseBody = ResponseBody.create(MediaType.parse("application/x-www-form-urlencoded"), jsonContent)
            return Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("ok")
                    .body(responseBody)
                    .build()
        } else {
            return chain.proceed(request)
        }
    }
}

