# lein-mangoes

A build system for client side development.

## Usage

This plugin is not yet avalaible on clojars. Thus please clone the repo, and run `lein install` from the folder.

Put `[lein-mangoes "0.1.0"]` into the `:plugins` vector of your project.clj.

As this is a development plugin, it can be included in the dev profile of the project.
```clojure
    :profiles {:dev {:source-paths ["dev"]
               :plugins [[lein-mangoes "0.1.0"]]}}
```

A `:mangoes` key defines the different folders to watch and the action to take -
```clojure
    :mangoes [[:hiccup->html "app/hiccup-templates" "app/templates"]
              [:html->hiccup "app/tmp" "app/hiccup-templates"]]
```
The watch can then be started using:
```bash
    $ lein mangoes
```
## Sample Usage

1. Add the following key to project.clj to convert from hiccup to html -
```clojure
    :mangoes [[:hiccup->html "hiccup-templates" "templates"]]
```
2. Create the folders `hiccup-templates` `templates` in your project.

3. cd into the `hiccup-templates` folder.

4. Create a hiccup file : `echo "[:a [:b]]" >> a.clj`

5. This will produce a html file : `cat ../templates/a.html` >> `<a><b></b></a>`

## Configuration

The configuration of the plugin is specified by using a sequence of vectors. Each vector has three entries -

1. A keyword which represents the fn that will be run.
2. The directory to observe for changes.
3. The destination directory to output the results.

The input directories should already exist before `lein mangoes` is run. The plugin uses the `java.nio` api to watch for changes in folders. Thus if a folder doesnt exist, or is deleted, then an error is thrown by the api, and the plugin terminates all threads listening for changes. In this case `lein mangoes` will have to be re-run.

## Rationale

This plugin is a convenience utility to ease client side development in clojure technoligies. There are excellent clojure libraries, which enable writing html and CSS in clojure syntax. However these libraries have to be called from repl to generate the corresponding html and CSS code.

Clojure is a powerful and expressive language, and just as its abilities can be leveraged for javascript development (via clojurescript), so can it be for html and CSS (via hiccup and garden). This workflow works well where the developers are also working on CSS and HTML. Thus it makes sense to have one language for all the needs.

However what is missing is a seamless way to convert between hiccup and html, and vice versa. The lein-mangoes plugin provides this convenience. The idea is to be able to write html and CSS in clojure syntax, and have it compile to html and CSS instantaneously. This is similar to the SASS, LESS, HAML and Jade pre compilers.

## Functionality

It currently supports the following options -

1. `:html->hiccup` It observes a directory for any changes to files in html format, and then converts them to .clj files in hiccup format.
2. `:hiccup->html` It observes a directory for any changes to files in hiccup format, and then converts them to .html files in html format.

## Design

The different actions are implemented as multimethods. Adding a new compilation target is as easy as adding a new multimethod.

Each vector results in creation of a new future. All the futures are maintained in an atom, and the `(cancel-future)` is called on them in case of an error. All errors are caught and then printed on the screen.

There is a transformer fn, which reads a file from a given path, applies a fn on it, and writes it on a given path.

Each future creates a watch over a directory, and receives events on any changes. The watch is implemented using the [clj-nio2][] wrapper over java's nio classess.

## The Road Ahead

At present it allows converting back and forth between html and hiccup. The back and forth between garden and CSS is still left to be implemented.

One goal is (I do not know of the feasibility) to allow the usage of namespaced hiccup snippets from libraries, to create hiccup templates, which can then be compiled to html.

## Contributions

This plugin is a very young library, and your comments, suggestions, ideas, code reviews and pull requests will make a big difference !

## Credits

1. [clj-nio2][] for the wonderful wrapper. Special thanks to Jurgen Hoetzel for helping out with its usage.
2. [hickory][] for providing the functionality to parse html into hiccup.
3. [hiccup][] for coming up with the wonderful idea that is hiccup.
4. [pedestal][] for rekindling the fun in client side development.

[clj-nio2]: https://github.com/juergenhoetzel/clj-nio2
[hickory]: https://github.com/davidsantiago/hickory
[hiccup]: https://github.com/weavejester/hiccup
[pedestal]: http://pedestal.io

## License

Copyright © 2013

Distributed under the Eclipse Public License, the same as Clojure.
