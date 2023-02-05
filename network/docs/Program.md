
# Program

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | [**java.util.UUID**](java.util.UUID.md) | Unique resource id |  [optional]
**title** | **kotlin.String** | Title of a content item |  [optional]
**description** | **kotlin.String** | Description of the content |  [optional]
**url** | **kotlin.String** | Relative URL to this resource. Ex /channel/itv1 or /taxonomy/football |  [optional]
**channel** | [**Channel**](Channel.md) |  |  [optional]
**show** | [**Show**](Show.md) |  |  [optional]
**country** | [**kotlin.Any**](.md) | See Country.isoCountryCode. One channel can only belong to one country |  [optional]
**language** | **kotlin.collections.List&lt;kotlin.String&gt;** |  |  [optional]
**images** | [**kotlin.collections.List&lt;Image&gt;**](Image.md) |  |  [optional]
**scheduleStart** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | Program start date and time in UTC |  [optional]
**scheduleEnd** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) | Program end date and time in UTC |  [optional]
**seasonNumber** | **kotlin.Int** |  |  [optional]
**episodeNumber** | **kotlin.Int** |  |  [optional]
**isFavourite** | **kotlin.Boolean** | If user marked this as a favourite |  [optional]
**taxonomies** | [**kotlin.collections.List&lt;Taxonomy&gt;**](Taxonomy.md) |  |  [optional]



