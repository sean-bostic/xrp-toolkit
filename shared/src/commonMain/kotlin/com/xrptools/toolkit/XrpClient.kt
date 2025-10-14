package com.xrptools.toolkit

import com.xrptools.toolkit.models.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class XrpClient {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun getXrpData(): Result<XrpSummary> {
        return try {
            val response = httpClient.get("https://api.coingecko.com/api/v3/coins/ripple") {
                parameter("localization", false)
                parameter("tickers", false)
                parameter("market_data", true)
                parameter("community_data", false)
                parameter("developer_data", false)
                parameter("sparkline", false)
            }

            val data: CoinGeckoResponse = response.body()
            val marketData = data.marketData

            Result.success(XrpSummary(
                priceUsd = marketData.currentPrice["usd"] ?: 0.0,
                priceChangePercentage24h = marketData.priceChangePercentage24h,
                marketCapUsd = marketData.marketCap["usd"] ?: 0L,
                volume24hUsd = marketData.totalVolume["usd"] ?: 0L,
                high24hUsd = marketData.high24h["usd"] ?: 0.0,
                low24hUsd = marketData.low24h["usd"] ?: 0.0
            ))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun close() {
        httpClient.close()
    }
}