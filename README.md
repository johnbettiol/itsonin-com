#itsonin.com Technical Specification
##Device 
###Entity
Field|Type|Usage|Info/Examples
---|---|---|---
deviceId|Long|Sequential Device Id|
type|DeviceType|What kind of device|APPLICATION, BROWSER
token|String|Unique Device Token (for Auth)|
level|DeviceLevel|What access level|NORMAL, SUPER
created|Date|Device Creation Time|
lastLogin|Date|Device Last connectionTime|
###Api Routes
Route||Data
---|---|---
api/device/create|Session|Not Needed
 |Request|{"type":"BROWSER"}
 |Response|{"deviceId":1,"type":"BROWSER","token":"12ee7607-e22b-44ea-9966-e4d3a78e128d","level":"NORMAL","created":"2014-03-08T12:51:10.862+0000","lastLogin":"2014-03-08T12:51:10.468+0000"}
 |Notes|For testing you can include "level":"SUPER" in request. **Problem:** We should probably auto authenticate on device creation
 | |
api/device/(deviceId)/authenticate/(token)|Session|Not Needed
 | Request |  URI: api/device/3/authenticate/41c656bb-d101-4aa5-ae98-7f111f038e1c
 | Response | {"deviceId":3,"type":"BROWSER","token":"41c656bb-d101-4aa5-ae98-7f111f038e1c","level":"NORMAL","created":"2014-03-08T12:51:10.917+0000","lastLogin":"2014-03-08T12:51:10.915+0000"}
 | Notes | Calling authenticate will return a session cookie  to be used for all further requests in the response header. The response of authenticate is needed to update local device info (level etc).
 
 