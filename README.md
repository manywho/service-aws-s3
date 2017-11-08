Boomi Flow S3 Service
=====================

[![Build Status](https://travis-ci.org/manywho/service-s3.svg)](https://travis-ci.org/manywho/service-s3)

This service allows you to work with files in your Boomi Flow app, backed by an S3 compatible storage provider.

## Running

### Heroku

The service is compatible with Heroku, and can be deployed by clicking the button below, which also handles any 
required configuration.

[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/manywho/service-s3)

### Locally

The service is a JAX-RS application, that by default runs on port 8080 (if you use the packaged JAR). To configure and 
build the service, you will need to have Apache Ant, Maven 3 and JDK 8+ installed.

#### Building

Once any configuration is complete, you can build the runnable shaded JAR:

```bash
$ mvn clean package
```

##### Defaults

Running the following command will start the service listening on `0.0.0.0:8080/api/s3/1`:

```bash
$ java -jar target/service-s3.jar
```

##### Custom Port

You can specify a custom port to run the service on by passing the `server.port` property when running the JAR. The
following command will start the service listening on port 9090 (`0.0.0.0:9090/api/s3/1`):

```bash
$ java -Dserver.port=9090 -jar target/service-s3.jar
```

## Contributing

Contributions are welcome to the project - whether they are feature requests, improvements or bug fixes! Refer to 
[CONTRIBUTING.md](CONTRIBUTING.md) for our contribution requirements.

## License

This service is released under the [MIT License](http://opensource.org/licenses/mit-license.php).