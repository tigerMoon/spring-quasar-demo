### Introduction
This is a spring boot use [Quasar](http://docs.paralleluniverse.co/quasar/) `s fiber as httpclient( [Apache Http Client](http://docs.paralleluniverse.co/comsat/) )demo .
to give a quick start with Quasar from spring.

### Run with it

mvn clean install;java -jar target//fiber-demo-cap.jar

http request: `http://localhost:8080/api/client/data`

This project was built by [Capsule](http://www.capsule.io/). if use spring own maven plugin to build fat jar .there is something wrong.
  
### Exploring the Code 

`HttpCLientConfig ` it is a config for spring `RestTemplate` to use [Apache Http Client](http://docs.paralleluniverse.co/comsat/) as HttpClient, 
 
 when `FiberService` invoke `FiberResource`, all the methods are suspendable except `RestTemplate.(...)`, it is from third-part jar .so we can not mak it with `@Suspendable`
 
 we must manually list them in `META-INF/suspendable-supers(for interface) and suspendable(other method)`.
 
 
 