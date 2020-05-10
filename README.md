# Gatling Kinesis Plugin #

This is a simple plugin to use with Gatling to push records into kinesis. The code is for the most part inspired by other plugins such as gatling-grpc and gatling-jdbc. It is useful not to load test Kinesis itself (that's Amazon's job), but if you are load testing your application which reads records from a stream, you can use this plugin to put records inside the stream with different load characteristics. It can also be handy if you are testing logic that scales the number of shards up/down automatically. 

Current version is extremely alpha.

## Contribution policy ##

Contributions via GitHub pull requests are gladly accepted from their original author. Along with
any pull requests, please state that the contribution is your original work and that you license
the work to the project under the project's open source license. Whether or not you state this
explicitly, by submitting any copyrighted material via pull request, email, or other means you
agree to license the material under the project's open source license and warrant that you have the
legal authority to do so.

## License ##

This code is open source software licensed under the
[MIT](https://opensource.org/licenses/MIT) license.
