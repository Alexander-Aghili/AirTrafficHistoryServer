# Air Traffic History

## Description
I am collecting data from the Opensky API and storing it in a MongoDB database. 
This data is then to be used for a variety of purposes.
The main purpose as of right now is to generate timelapses of Air Traffic quickly. 
This can show visually how certain flows of traffic operate to those unfamiliar with Air Traffic Control.
It can also show how controllers handle certain situations like weather, emergencies, or dense traffic over an extended period of time.

## Documentation
Documentation for the code can be found is each respective folder. 
Several classes and functions include future ideas and possible implemenations that I haven't gotten around to doing yet.
Those are marked with @Future above the Method header or class declaration.

## To Do
Basic TODO list:
 - Finish documentation and comments for each method and class
 - Write unit tests
 - Start client side

## REST API
This project includes a RESTful API with JAX-RS utilizing Apache Tomcat.
A bit dated, I know, but it is all I know right now and I wanted to build this project quickly.
In the future, a NodeJS or another framework/language can be used to build the API.

## How to Contribute
If you like to contribute, you can contact me directly using my contact info below or simply make a pull request
Also read contributing.md if it is written

## Credit
Alexander Aghili: Author and Main Developer
alexander.w.aghili@gmail.com

## License
Copyright 2022 Alexander Aghili

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


