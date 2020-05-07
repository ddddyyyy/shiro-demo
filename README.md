# shiro-demo

## Framework used

* SpringBoot: 2.1.12.RELEASE
* JDK: 1.8
* shiro: 1.5.2


## Test case to reproduce the error that could not resovle remeberMe cookie

* login to generate the remeberMe cookie： http://127.0.0.1:8081/login
* delete the JSESSIONID
* test the remeberMe cookie： http://127.0.0.1:8081/test
