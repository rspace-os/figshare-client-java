This document record significant changes to the project

### 0.6.0 2024-01-12

- compile with java 17
- switch to using parent pom from rspace-os-parent project
- remove `FigshareCategory.UNCATEGORIZED` static example category, as there is no such category in latest Figshare API. 
For testing, use newly added `FigshareCategory.SOFTWARE_TESTING`. 
 

### 0.5.0 2022-06-08

- add public/private URLs to ArticleLink

### 0.4.1 2022-03-01

- bump dependency versions. Spring 5.2 -> 5.3.16

### 0.4.0 2022-01-11

- enable static lists of licenses and categories
- add some IgnoreUnknownProperty annotations for resilience

### 0.3.2 
- add 'warnings' attribute to locations model
