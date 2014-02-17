# sinon-docs

The http://sinonjs.org website, implemented as a generated static site using
[Stasis](https://github.com/magnars/stasis/) with frontend optimization using
[Optimus](https://github.com/magnars/optimus).

## Usage

[Pygments](http://pygments.org) themes are fetched as a
[git submodule](https://github.com/richleland/pygments-css):

```sh
git submodule update --init
```

To run the site, make sure you have Java 1.7 or later. Then install
[Leiningen](http://leiningen.org). Finally, run the site with:

```sh
lein ring server-headless
```

Or generate the static files for deployment with:

```sh
lein build-site
```

## Where are things?

The main layout is defined in [hiccup](https://github.com/weavejester/hiccup)
syntax in `src/sinon_docs/pages.clj`. The frontpage is a partial that lives in
`resources/partials/index.html`.

## Running tests

There are some tests. Run them with [midje](https://github.com/marick/Midje):

```sh
lein midje
```

If you're going to add or change something, you might want the tests to keep
running on every save:

```sh
lein midje :autotest
```

## License

Copyright © 2014 Christian Johansen

BSD 2 Clause license
