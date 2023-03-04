package com.spark.fastplayer.data.repository

import org.openapitools.client.models.EpgRow
import org.openapitools.client.models.Program
import org.openapitools.client.models.Taxonomy

data class TaxonomyPrograms (val epgRowsList: List<EpgRow>)


data class EPGProgramListGroupedByTaxonomies (val taxonomyPrograms: TaxonomyPrograms)