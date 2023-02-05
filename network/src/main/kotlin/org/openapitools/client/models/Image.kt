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
 * @param height Height in pixels
 * @param width Width in pixels
 * @param kind type of image to determine where to use
 */


data class Image (

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

    /* Height in pixels */
    @Json(name = "height")
    val height: kotlin.Int? = null,

    /* Width in pixels */
    @Json(name = "width")
    val width: kotlin.Int? = null,

    /* type of image to determine where to use */
    @Json(name = "kind")
    val kind: Image.Kind? = null

) {

    /**
     * type of image to determine where to use
     *
     * Values: thumbnail,logo,logoMinusGrey,heroMinusBackgroundMinusPort,heroMinusBackgroundMinusLand
     */
    enum class Kind(val value: kotlin.String) {
        @Json(name = "thumbnail") thumbnail("thumbnail"),
        @Json(name = "logo") logo("logo"),
        @Json(name = "logo-grey") logoMinusGrey("logo-grey"),
        @Json(name = "hero-background-port") heroMinusBackgroundMinusPort("hero-background-port"),
        @Json(name = "hero-background-land") heroMinusBackgroundMinusLand("hero-background-land");
    }
}
