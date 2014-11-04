Rest-Test-Client
================

Rest-Test-Client used to test any rest application.

Quick and dirty way to test your API(rest)

java -jar -DurlsFile={urls txt file path}

File format

content-type | http-method | request url | expected code | Print Responses | {Optional text to search} | {Optional Payload}


Checks for 200 return code and prints the response on console.

application/json|get|http://192.168.1.123:8181/api/location/|200|1

Post the request and checks for 200 return code and prints the response on console and will search "X-Auth-Token" string from response.

application/json|post|http://192.168.1.123:8181/api/location/|200|1|X-Auth-Token


Post the request and checks for 200 return code and prints the response on console and 
will search "X-Auth-Token" string from response. Payload will be sent with the request.

application/json|post|http://192.168.1.123:8181/user/login/|200|1|X-Auth-Token|{"userId":"xyz","pass":"123"}
