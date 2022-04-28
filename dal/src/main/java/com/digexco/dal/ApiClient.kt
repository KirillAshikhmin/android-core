package com.digexco.dal

import android.annotation.SuppressLint
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.ConnectionSpec
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException

object ApiClient {

    fun getHttpClient(ignoreSsl: Boolean = false): OkHttpClient.Builder {
        return try {

            val builder = OkHttpClient.Builder()

            if (ignoreSsl) {
                val trustManager = object : X509TrustManager {
                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                }

                // Create a trust manager that does not validate certificate chains
                val trustAllCerts = arrayOf<TrustManager>(trustManager)

                // Install the all-trusting trust manager
                val sslContext: SSLContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())
                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory


                /*val spec: ConnectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .cipherSuites(
                        TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                    )
                    .build()
                    val spec = Collections.singletonList(specs)*/
                val spec = listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)

                builder
                    .hostnameVerifier { _, _ -> true }
                    .connectionSpecs(spec)
                    .sslSocketFactory(sslSocketFactory, trustManager)
            }

            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @ExperimentalSerializationApi
    fun <S> createService(serverUrl: String, client: OkHttpClient, serviceClass: Class<S>): S {
        val contentType = "application/json".toMediaType()
        val url = if (serverUrl.endsWith('/')) serverUrl else "$serverUrl/"
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(client)
            .build()
            .create(serviceClass)
    }

}

