# PlaybackinfoApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getPlaybackinfo**](PlaybackinfoApi.md#getPlaybackinfo) | **GET** /channelPlaybackInfo/{channel-id} | Get user or editorial playlists, with default as the first item


<a name="getPlaybackinfo"></a>
# **getPlaybackinfo**
> ChannelPlaybackInfo getPlaybackinfo(channelId)

Get user or editorial playlists, with default as the first item



### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = PlaybackinfoApi()
val channelId : String = channelId_example // kotlin.String | 
try {
    val result : ChannelPlaybackInfo = apiInstance.getPlaybackinfo(channelId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling PlaybackinfoApi#getPlaybackinfo")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling PlaybackinfoApi#getPlaybackinfo")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **channelId** | **kotlin.String**|  |

### Return type

[**ChannelPlaybackInfo**](ChannelPlaybackInfo.md)

### Authorization


Configure apiKeyAuth:
    ApiClient.apiKey["X-Api-Key"] = ""
    ApiClient.apiKeyPrefix["X-Api-Key"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

