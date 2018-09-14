To anyone stumbling across this...  This was a startup idea I had back in the past where I wanted an OpenStreetMap style competitor to tripadvisor and google reviews.

I got lazy and didn't follow it up.


#itsonin.com Technical Specification
##Type Definitions
Type|Internal Type|Example
---|---|---
Long|Java Long|
token|String|uuid
Date|Java Date|Formatter: "yyyy-MM-dd'T'HH:mm:ss"
DeviceType|String|APPLICATION, BROWSER
DeviceLevel|String|NORMAL, SUPER
##Request Headers
Header|Value
---|---
Content-Type|application/json

##Response Codes
##Entities 
###Device
Field|Type|Usage|Info/Examples
---|---|---|---
deviceId|Long|Sequential Device Id|
type|DeviceType|What kind of device|Web = BROWSER, Mobile Apps=APPLICATION
token|String|Unique Device Token (for Auth)|
level|DeviceLevel|What access level|Default = NORMAL
created|Date|Device Creation Time|
lastLogin|Date|Device Last connectionTime|
##Api Routes
Route||Data
---|---|---
POST: api/device/create|Session|Not Needed
 |Request|{"type":"BROWSER"}
 |Response|{"deviceId":1,"type":"BROWSER","token":"12ee7607-e22b-44ea-9966-e4d3a78e128d","level":"NORMAL","created":"2014-03-08T12:51:10.862+0000","lastLogin":"2014-03-08T12:51:10.468+0000"}
 |Notes|For testing you can include "level":"SUPER" in request. **Problem:** We should probably auto authenticate on device creation
 | |
GET: api/device/(deviceId)/authenticate/(token)|Session|Not Needed
 | Request |  eg: api/device/3/authenticate/41c656bb-d101-4aa5-ae98-7f111f038e1c
 | Response | {"deviceId":3,"type":"BROWSER","token":"41c656bb-d101-4aa5-ae98-7f111f038e1c","level":"NORMAL","created":"2014-03-08T12:51:10.917+0000","lastLogin":"2014-03-08T12:51:10.915+0000"}
 | Notes | Calling authenticate will return a session cookie (**keep your cookies!**) to be used for all further requests in the response header. The response of authenticate is needed to update local device info (level etc).
 | |
GET: api/device/(deviceId)/delete|Session|Required
 | Request | eg: api/device/5/delete
 | Response | {"status":"success","message":"Device deleted successfully"}
 | Notes | A SUPER device can delete any other device
 
