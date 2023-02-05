
# Image

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | [**java.util.UUID**](java.util.UUID.md) | Unique resource id |  [optional]
**title** | **kotlin.String** | Title of a content item |  [optional]
**description** | **kotlin.String** | Description of the content |  [optional]
**url** | **kotlin.String** | Relative URL to this resource. Ex /channel/itv1 or /taxonomy/football |  [optional]
**height** | **kotlin.Int** | Height in pixels |  [optional]
**width** | **kotlin.Int** | Width in pixels |  [optional]
**kind** | [**inline**](#Kind) | type of image to determine where to use |  [optional]


<a name="Kind"></a>
## Enum: kind
Name | Value
---- | -----
kind | thumbnail, logo, logo-grey, hero-background-port, hero-background-land



