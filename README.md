# literable-clj

A REST application layer written in Clojure and Compojure for Literable. Uses CouchDB for persistence.

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

Create a .lein-env file in the project directory. Example:

    { :database-url "https://user:pass@user.cloudant.com/database" }


[1]: https://github.com/technomancy/leiningen


## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2014 Devin Humbert
