# figshare-client-java
Java bindings to Figshare API

This project uses Spring-REST and Spring Social to provide a Java client to the Figshare API.

It is not complete, but supports basic Article operations and File upload.

This library is also now available in MavenCentral if you want to just add it as a dependency:
    
    <dependency>
      <groupId>com.researchspace</groupId>
      <artifactId>figshare-client-java</artifactId>
      <version>0.0.1</version>
    </dependency>
    
or in Gradle:

    compile 'com.researchspace:figshare-client-java:0.0.1'


## Building

To compile and run unit tests, check out and run  the following command. This will install the gradle build tool if you don't already have it.

    ./gradlew clean test
    
To build without integration tests:

    ./gradlew clean build -x integrationTest
    
## Integration tests

Integration tests make real calls to the Figshare API. To run integration tests, you'll need a previously created Figshare account and a private access token that you can obtain from your Figshare account settings page. Add this as a command-line option, replacing 'XXXXX' with your token:

    ./gradlew clean integrationTest -DfigshareToken=XXXXX
    
Alternatively you can add the line:

    systemProp.figshareToken=XXXXXXX
    
 to the file gradle.properties in your GRADLE\_HOME folder (by default this is USER\_HOME/.gradle) and run using command:
 
    ./gradlew clean integrationTest    
    
### Using the library

The library can be used either with a personal token or by using the OAuth2 mechanism to acquire an access token.

Additionally you can use this library in a Spring application, or independently. 

If a personal token is used, this will take precedence over any OAuth2 access token


### Use case 1 - personal token:

#### In a Spring application


Simply supply a personal token as a system property and autowire the Figshare API into your application:

```java
 @Autowired
 private Figshare figshare;
```

To set up Figshare bean, if using Java configuration, use the configuration as is from SpringTestConfig. E.g.

```java
    
    @Autowired Environment env;
	
	@Bean
	FigshareTemplate FigshareTemplate (){
	  FigshareTemplate ft =  new FigshareTemplate();
	  ft.setPersonalToken(env.getProperty("figshareToken"));
	  return ft;
	}
``` 

#### In a non-Spring application
    
You'll have to set up the FigshareTemplate manually:

```java 
	 
	  String token = "myToken";
	  FigshareTemplate ft =  new FigshareTemplate();
	  ft.setPersonalToken(token);
	  return ft;
``` 

### Usage

Here is some code which creates a new article, uploads a file, and returns a link to the article on the Figshare website.

```java

		// get a file from somewhere.
        File anyFile = new File("Somefile.xls");
        
        // Objects to post are generally created using Builder pattern:
        ArticlePostBuilder articleBuilder = ArticlePost.builder();
		articleBuilder.title("title")
		   .description("Some description")
		   .author(new Author("Fred Bloggs", null))
		   .author(new Author(null, "x@y.com"));
		   
		// you can also iterate over Categories to find the ID matching your category
		articleBuilder.category(Category.UNCATEGORIZED.getId().intValue());
		
		// tag required for publishing to work, if needed.
		articleBuilder.tags(Arrays.asList(new String []{"from_java"}));
		ArticlePost toPost = articleBuilder.build();
		
		// now let's submit it:
		Location article = figshare.createArticle(toPost);
	    PrivateArticleLink privateLink = figshare.createPrivateArticleLink(article.getId());
	    Location fileId = figshare.uploadFile(article.getId(), anyFile);
		String feedbackMsg = String.format("Deposit succeeded - private article link is %s.", privateLink.getWeblink());
			

```
