# figshare-client-java
Java bindings to Figshare API

This project uses Spring-REST and Spring Social to provide a Java client to the Figshare API.

It is not complete, but supports basic Article operations and File upload.


### Installing into a Maven repository

From 0.3.0 onwards, this project can be added as a  dependency in your project using [JitPack](https://jitpack.io).

    <dependency>
       <groupId>com.github.rspace-os</groupId>
       <artifactId>figshare-client-java</artifactId>
       <version>v0.4.1</version>
    </dependency>

or Gradle:

    compile 'com.github.rspace-os:figshare-client-java:v0.3.2'
  

Builds and their metadata are available at https://jitpack.io/com/github/rspace-os/figshare-client-java/

Or, you can run:

    ./mvnw clean install -DskipTests=true

to install into a local Maven  repository.

## Building

To compile and run unit tests, check out and run  the following command. This will install the Gradle build tool if you don't already have it.

    ./mvnw clean test
    
To build a jar without  tests:

    ./mvnw clean package -DskipTests=true
    
## Integration tests

Integration tests make real calls to the Figshare API. To run integration tests, you'll need a previously created Figshare account and a private access token that you can obtain from your Figshare account settings page. Add this as a command-line option, replacing 'XXXXX' with your token:

    ./mvnw clean test -DfigshareToken=XXXXX
    
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
