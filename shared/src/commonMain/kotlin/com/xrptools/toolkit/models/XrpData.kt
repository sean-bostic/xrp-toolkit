package com.xrptools.toolkit.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinGeckoResponse(
    @SerialName("market_data") val marketData: MarketData
)

@Serializable
data class MarketData(
    @SerialName("current_price") val currentPrice: Map<String, Double>,
    @SerialName("price_change_percentage_24h") val priceChangePercentage24h: Double,
    @SerialName("market_cap") val marketCap: Map<String, Long>,
    @SerialName("total_volume") val totalVolume: Map<String, Long>,
    @SerialName("high_24h") val high24h: Map<String, Double>,
    @SerialName("low_24h") val low24h: Map<String, Double>
)

data class XrpSummary(
    val priceUsd: Double,
    val priceChangePercentage24h: Double,
    val marketCapUsd: Long,
    val volume24hUsd: Long,
    val high24hUsd: Double,
    val low24hUsd: Double
)