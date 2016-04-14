There are a couple of different ways you can get Arid POJOs.
# With Maven 2 #
If you are using Maven2 you can simply define the following dependencies.
```
<repositories>
   <repository>
  <id>pia-repository</id>
  <url>http://www.pojosinaction.com/repository</url>
  </repository>
<repositories>

<dependency>
   <groupId>net.chrisrichardson</groupId>
   <artifactId>arid-framework</artifactId>
   <version>1.0</version>
</dependency>

<dependency>
   <groupId>net.chrisrichardson</groupId>
   <artifactId>arid-dao</artifactId>
   <version>1.0</version>
</dependency>
```
## Download the JARs ##
Alternatively, you can download the latest [distribution](http://code.google.com/p/aridpojos/downloads/list) and use the jars in the lib directory. However, these jars are not updated regularly