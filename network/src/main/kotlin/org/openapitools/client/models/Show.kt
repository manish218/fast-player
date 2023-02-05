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

import org.openapitools.client.models.Image
import org.openapitools.client.models.Taxonomy

import com.squareup.moshi.Json

/**
 * 
 *
 * @param id Unique resource id
 * @param title Title of a content item
 * @param description Description of the content
 * @param url Relative URL to this resource. Ex /channel/itv1 or /taxonomy/football
 * @param showId Alpha numeric number to identify a show. It is unique accross a territory
 * @param images 
 * @param country See Country.isoCountryCode. One channel can only belong to one country
 * @param language 
 * @param isFavourite If user marked this as a favourite
 * @param taxonomies 
 */


data class Show (

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

    /* Alpha numeric number to identify a show. It is unique accross a territory */
    @Json(name = "showId")
    val showId: kotlin.String? = null,

    @Json(name = "images")
    val images: kotlin.collections.List<Image>? = null,

    /* See Country.isoCountryCode. One channel can only belong to one country */
    @Json(name = "country")
    val country: kotlin.String? = null,

    @Json(name = "language")
    val language: kotlin.collections.List<kotlin.String>? = null,

    /* If user marked this as a favourite */
    @Json(name = "isFavourite")
    val isFavourite: kotlin.Boolean? = false,

    @Json(name = "taxonomies")
    val taxonomies: kotlin.collections.List<Taxonomy>? = null

)

