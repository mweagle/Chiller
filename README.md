# Chiller - An Apache Karaf Quickstarter


In the process of trying to learn about [Apache Karaf](http://karaf.apache.org/) I put together a set of skeleton projects (based on Karaf 2.2.4) that help to bootstrap a custom distribution.  The end goal is a standalone Karaf installation that includes your application bundle(s) in a standalone archive that can be deployed to a Maven-less, Internet-less machine.  Your custom deployment is wrapped up in a single ZIP file with all dependencies embedded.  

There's nothing new here, just a distillation of docs, [mailing list threads](https://cwiki.apache.org/KARAF/mailing-lists.html), Maven wrangling and the like.  You're still need to going to write your application.

The sub-projects are:

* *com.custom.branding* : Resource-only bundle that customizes the Karaf console shell per the [instructions](http://karaf.apache.org/manual/latest-2.2.x/developers-guide/branding-console.html).
* *com.custom.karafbundle* : Simple Java OSGi bundle with the obligatory Hello World
* *com.custom.karafhost* : The "host" installation.  A Karaf installation, including both OSX and Windows shell scripts, will be downloaded and pre-populated with the custom set of bundles.  Eclipse *Run Configurations* that delegate to the downloaded platform shell scripts are included as external files in the project.
* *com.custom.karafservlet* : [PAX Whiteboard](http://team.ops4j.org/wiki/display/paxweb/Whiteboard+Extender) registered servlet at "/hello" which outputs, unoriginally, Hello World.
* *com.custom.scalabundle* : [Scala](http://www.scala-lang.org/) implementation of simple Hello World OSGi bundle.


## Requirements

* [Maven 3.x](http://maven.apache.org/)

For Eclipse Building:

* [Eclipse](http://www.eclipse.org/downloads/)
* [M2Eclipse](http://m2eclipse.sonatype.org/)

For Scala integration: 

* [Scala IDE for Eclipse](http://www.scala-lang.org/node/11986) (_Optional_)

## Building

### Maven

1. From a command prompt in the _com.custom.karafhost_ directory, enter "mvn install"
2. Navigate to the _.../com.custom.karafhost/target/karaf/apache-karaf-2.2.4/bin_ directory and launch your platform-specific _karaf.*_ shell script.

### Eclipse
1. From the *com.custom.karafhost* project context menu, select *Run As* -> *Maven Install*
2. Assuming all goes well, you should see something like: 

```
[INFO] Custom :: Karaf :: Java Bundle .................... SUCCESS [1.988s]
[INFO] Custom :: Karaf :: Scala Bundle ................... SUCCESS [2.935s]
[INFO] Custom :: Karaf :: Java Servlet ................... SUCCESS [0.525s]
[INFO] Custom :: Karaf :: Console Branding ............... SUCCESS [0.459s]
[INFO] Custom :: Karaf :: Distribution ................... SUCCESS [12.737s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 19.324s
[INFO] Finished at: Mon Dec 26 15:39:57 PST 2011
[INFO] Final Memory: 15M/123M
[INFO] ------------------------------------------------------------------------
```

In the *com.custom.karafhost* project, disclose the *run_configurations* folder and select the appropriate _<Platform> - Run.launch_ target to start the Karaf session. 


## Interacting 

Once Karaf has started up, you can enter the Eclipse Console to interact with the running instance.  For instance, _features:list_ will list the available features and their installation state.  Assuming everything built, the tail of the *features:list* should include:

```
…SNIP…
[installed  ] [0.0.1-SNAPSHOT ] karafbundle                   repo-0                 
[installed  ] [0.0.1-SNAPSHOT ] scalabundle                   repo-0                 
[installed  ] [0.0.1-SNAPSHOT ] karafservlet                  repo-0      
```

You can also interact with the included [WebConsole](http://fusesource.com/docs/esb/4.2/getting_started/ESBGetStartedWebConsole.html) by visiting the page at [http://localhost:8080/system/console](http://localhost:8080/system/console).  Login using:

* *user*: karaf
* *password*: karaf

The HTTP port is configurable in the _.../com.custom.karafhost/src/main/resources/karaf/etc/org.ops4j.pax.web.cfg_ file's *org.osgi.service.http.port* property.

To shutdown the Karaf instance, enter the *shutdown* command in the console.

## Adding a New Feature

Karaf introduces a packaging abstraction called a [_Feature_](http://karaf.apache.org/manual/latest-2.2.x/users-guide/provisioning.html).  Features are collections of JARs that represent a single application-level set of functionality.  The bundles in this repo include the following feature mappings:

* karafbundle -> com.custom.karafbundle
* karafservlet -> com.custom.karafservlet
* scalabundle -> com.custom.scalabundle

The feature mappings are defined in _.../com.custom.karafhost/src/main/resources/karaf/system/features.xml_ 

To add your custom feature bundle:

1. Create a new project 
2. Add the new project to the *modules* section in _.../chiller/com.custom.karafhost/pom.xml_
3. Add the feature group to the _.../com.custom.karafhost/src/main/resources/karaf/system/features.xml_ file.  This is where you will define the feature-name, which is used in the next two steps.
4. In the *add-local-features-to-repo* execution id in _.../chiller/com.custom.karafhost/pom.xml_, include your new feature-name in the list of *feature*s to include in the distribution.  If you don't deploy your feature-name to the local-repo, it won't be available at runtime.  
5. If you would like your feature to launch at startup, in _.../com.custom.karafhost/src/main/resources/karaf/etc/org.apache.karaf.features.cfg_, add your new feature-name to the comma-separated *featuresBoot* property.  Note that specifying a non-existent feature-name to launch at boot time doesn't seem to generate an error.
6. Clean and rebuild the parent *com.custom.karafhost* project.  Once the build is launched, use the *features:list* command to verify that your custom feature is both deployed and installed.

### Issues

* I'm not sure how to brand the Karaf WebConsole.  I tried the [Felix approach](http://felix.apache.org/site/branding-the-web-console.html), but was unsuccessful.
* This was primarily a learning experience for Karaf, so I'd appreciate any corrections or improvements.  