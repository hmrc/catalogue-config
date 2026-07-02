
# catalogue-config

The catalogue-config is a backend service for providing the configuration for building the menu that is used in the Catalogue Frontend. 
It serves the menu structure as JSON and also provides a search index for the quick search functionality in the Catalogue Frontend.

## Why is this service needed?
Representing the menu structure became necessary as there is a plan to split the Catalogue Frontend into multiple services. 
The menu structure is now served by this service and consumed by the library `catalogue-wrapper` 
which allows other frontend apps to embed the necessary menu structure from this shared configuration.

### License
This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").