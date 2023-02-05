# EpgApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getEpg**](EpgApi.md#getEpg) | **GET** /epg | Fetch EPG sorted by taxonomies


<a name="getEpg"></a>
# **getEpg**
> kotlin.collections.List&lt;EpgRow&gt; getEpg()

Fetch EPG sorted by taxonomies

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = EpgApi()
try {
    val result : kotlin.collections.List<EpgRow> = apiInstance.getEpg()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling EpgApi#getEpg")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling EpgApi#getEpg")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;EpgRow&gt;**](EpgRow.md)

### Authorization


Configure apiKeyAuth:
    ApiClient.apiKey["X-Api-Key"] = ""
    ApiClient.apiKeyPrefix["X-Api-Key"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

