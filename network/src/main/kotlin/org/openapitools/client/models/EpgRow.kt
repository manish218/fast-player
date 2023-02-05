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

import org.openapitools.client.models.Program

import com.squareup.moshi.Json

/**
 * 
 *
 * @param programs List of programs belonging to one channel and first taxonomy in the nested fields of the program
 */


data class EpgRow (

    /* List of programs belonging to one channel and first taxonomy in the nested fields of the program */
    @Json(name = "programs")
    val programs: kotlin.collections.List<Program>? = null

)

