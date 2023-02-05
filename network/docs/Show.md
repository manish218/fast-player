
# Show

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | [**java.util.UUID**](java.util.UUID.md) | Unique resource id |  [optional]
**title** | **kotlin.String** | Title of a content item |  [optional]
**description** | **kotlin.String** | Description of the content |  [optional]
**url** | **kotlin.String** | Relative URL to this resource. Ex /channel/itv1 or /taxonomy/football |  [optional]
**showId** | **kotlin.String** | Alpha numeric number to identify a show. It is unique accross a territory |  [optional]
**images** | [**kotlin.collections.List&lt;Image&gt;**](Image.md) |  |  [optional]
**country** | **kotlin.String** | See Country.isoCountryCode. One channel can only belong to one country |  [optional]
**language** | **kotlin.collections.List&lt;kotlin.String&gt;** |  |  [optional]
**isFavourite** | **kotlin.Boolean** | If user marked this as a favourite |  [optional]
**taxonomies** | [**kotlin.collections.List&lt;Taxonomy&gt;**](Taxonomy.md) |  |  [optional]



