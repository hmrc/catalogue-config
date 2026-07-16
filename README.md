
# catalogue-config

The catalogue-config is a backend service for providing the configuration for building the menu that is used in the Catalogue Frontend. 
It serves the menu structure as JSON and also provides a search index for the quick search functionality in the Catalogue Frontend.

The menu endpoint is authenticated through Internal Auth and evaluated per request using the caller's credentials. The Users top-level link is always returned for authenticated users, while Users dropdown entries are returned only for the authorised Internal Auth actions. Because this response is user-specific, it must not be globally cached.

## Why is this service needed?
Representing the menu structure became necessary as there is a plan to split the Catalogue Frontend into multiple services. 
The menu structure is now served by this service and consumed by the library `catalogue-wrapper` 
which allows other frontend apps to embed the necessary menu structure from this shared configuration.

### License
This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").