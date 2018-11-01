[![Build Status](https://travis-ci.org/bootique-examples/bootique-jetty-websocket-demo.svg)](https://travis-ci.org/bootique-examples/bootique-jetty-websocket-demo)

# bootique-jetty-websocket-demo

An example of the jetty websocket based on part of [example](https://github.com/kslisenko/streaming) from VoxxedDays 2018 conference.

## Prerequisites

* Java 1.8 or newer.
* Apache Maven.

## Build the Demo

Here is how to build it:

	git clone git@github.com:bootique-examples/bootique-jetty-websocket-demo.git
	cd bootique-jetty-websocket-demo
	mvn package

## Run the Demo

Now you can check the options available in your app:

    java -jar target/bootique-jetty-websocket-demo-1.0-SNAPSHOT.jar --help

      -c yaml_location, --config=yaml_location
           Specifies YAML config location, which can be a file path or a URL.

      -h, --help
           Prints this message.

      -H, --help-config
           Prints information about application modules and their configuration
           options.

      -s, --server
           Starts Jetty server. 
           
    java -jar target/bootique-jersey-websoket-demo-1.0-SNAPSHOT.jar -s -c bootique.yml 
      
To test the simple part of example you will need a websocket client:

    * Connect to webscket ws://127.0.1.1:8080/ws/simple
    ** In Console you will see: "Socket Connected: {session info}"
    * Send some message
    ** In Console you will see: Received TEXT message: message
    * Disconnect 
    ** In Console you will see: Socket Closed: CloseReason[{status code}]
    
To test the streaming example part you will need just go to url:
    
    http://127.0.1.1:8080/content
    
Application will initiate connection and start streaming generated data which will imitate change prise of shares of the companies. 
