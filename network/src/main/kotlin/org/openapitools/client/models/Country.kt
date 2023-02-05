/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package org.openapitools.client.models


import com.squareup.moshi.Json

/**
 * 
 *
 * @param id Unique resource id
 * @param title Title of a content item
 * @param description Description of the content
 * @param url Relative URL to this resource. Ex /channel/itv1 or /taxonomy/football
 * @param countryIsoCode ISO 3166-1 alpha-2 code
 */


data class Country (

    /* Unique resource id */
    @Json(name = "id")
    val id: java.util.UUID? = null,

    /* Title of a content item */
    @Json(name = "title")
    val title: kotlin.String? = null,

    /* Description of the content */
    @Json(name = "description")
    val description: kotlin.String? = null,

    /* Relative URL to this resource. Ex /channel/itv1 or /taxonomy/football */
    @Json(name = "url")
    val url: kotlin.String? = null,

    /* ISO 3166-1 alpha-2 code */
    @Json(name = "countryIsoCode")
    val countryIsoCode: kotlin.String? = null

)

