# Http-Multi-Server-And-Proxy-Server
Socket Programming – HTTP Server and Proxy Server In this project, you are required to implement a multi-threaded HTTP server that returns a  document determined by the requested URI. You are also requested to implement a proxy server  with some specific properties. You are also required to use ApacheBench program for testing your servers.  1) Implementing a multithreaded web server  You are required to implement an HTTP server that achieve the following requirements: a) Your server should be capable of providing concurrency via multi-threading. b) Your server program should take single argument which specifies the port number. c) Your server should return an HTML document according to the requested URI. The size of  the document is determined by the requested URI (any size between 100 and 20,000 bytes). Your  server should remove the leading slash ‘/’ from the URI and extract the resulting string as an  integer (in decimal) that specify the size of the document to be sent to the client. For example,  if the request line is “GET /1500 HTTP/1.0”, your server should send back an HTML file  that contains exactly 1,500 bytes of text (When the client save the document on the local disk, the  file size should be 1,500 bytes). The returned HTML file should have a proper HTML format  with &lt;HTML>, &lt;HEAD> and &lt;BODY> tags, but the content can be anything. If the  requested URI asks for a size less than 100, or for a size greater than 20,000, or if the URL is not  an integer, your server should not return any document, but instead it should return a "Bad  Request" message with error code 400. If the method in request message is not GET, server  would return “Not Implemented” (501) for valid HTTP methods other than GET, or “Bad  Request” (400) for invalid methods.


2) Implementing a proxy server
You are required to implement a proxy server that achieves the following requirements:
a) Your proxy server will not be able to cache HTTP objects. It just relays the request to the 
Web server implemented in the first step and send back the result to the client
b) Proxy Server should use port 8888. Any client should be able to send a GET message to the 
proxy server as described in 1-c. But the proxy server would not generate any file, it would just 
direct GET message to the Web server. The result received from the web server would then 
passed to the client.
c) Your proxy server only directs the requests to your web server. It doesn’t direct any request to 
another web server. (+3 pts bonus if your proxy directs to any web server)
d) In this project, proxy server has a restriction. If the requested file size is greater than 9,999 (in 
other words, if the URI is greater than 9,999) it would not pass the request to the web server. 
Rather it sends “Request-URI Too Long” message with error code 414.
e) If the Web Server is not running currently, your proxy server would return a “Not Found” 
error message with status code 404.
