Pdf Service
===========

This service allows you to generate and manipulate PDFs.

## Running

### Configuration

This service require an account with amazon s3.

### Commands


##### Defaults

Running the following command will start the service listening on `0.0.0.0:8080/api/s3/1`:

```bash
$ java -jar target/demo-1.0-SNAPSHOT.jar
```

##### Custom Port

You can specify a custom port to run the service on by passing the `server.port` property when running the JAR. The
following command will start the service listening on port 9090 (`0.0.0.0:9090/api/s3/1`):

```bash
$ java -Dserver.port=9090 -jar target/demo-1.0-SNAPSHOT.jar
```


## Contributing

Contribution are welcome to the project - whether they are feature requests, improvements or bug fixes! Refer to 
[CONTRIBUTING.md](CONTRIBUTING.md) for our contribution requirements.

## License

This service is released under the [MIT License](http://opensource.org/licenses/mit-license.php).
