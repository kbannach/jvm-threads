# jvm-threads
The goal is to show a vulnerability of `SimpleDateFormat` class in a multithreaded use-case.
In this application I try to concurrently generate a list of strings representing dates, each for next 100 days from now, and check if the results are OK.
### Usage
```
mvn test
```
### Results
Using one SimpleDateFormat object concurently results in undefined behaviour (sometimes parsing a date results in `"null"`, sometimes it results in a string representing a date not in range of 100 days, and sometimes it doubles existing strings).
### How to solve it?
This problem may be solved easly by using `LocalDateFormat` class to obtain one object instance per thread.
