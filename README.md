# fastr-examples

Experimenting with Clojure-FastR interop.

## Intro

This repo offers some examples of using GraalVM's [FastR](https://github.com/oracle/fastr) from Clojure.

The idea is to have a proof of concept.

These days we are working on a general R-interop library, that is planned to target different R runtimes: GNU-R, Renjin, FastR. The experiments here are a proof of concept to supoprt that work.

## Usage

Make sure you have a working version of GraalVM with FastR -- details [here](https://github.com/oracle/fastr#getting-started).

Run this Clojure project where your default JVM is set to GraalVM. [TODO: Link to instructions].

Open a REPL with `lein repl` (or through your favourite editor), and play with the [examples](./examples).

## Recommended reading

* An [intro post](https://gigasquidsoftware.com/blog/2017/10/22/embedded-interop-between-clojure-r-and-python-with-graalvm/) to using polyglot GraalVM from Clojure by Carin Meier.
* A general Nextjournal [notebook](https://nextjournal.com/mk/graal-vm) about FastR setup by Martin Kavalar.
* A Nextjournal [notebook](https://nextjournal.com/wolfv/how-fast-is-r-with-fastr-pythran) with FastR benchmarks by Wolf Vollprecht.
* A small Nextjournal [notebook](https://nextjournal.com/daslu/clojure-fastr-interop) with a reproducible setup with examples similar to those in this project.

## License

Copyright © 2019 Scicloj

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
