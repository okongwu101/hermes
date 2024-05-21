# Changes

This log documents significant changes for each release.

## [1.4.1400] - 2024-05-21 

* Add support for ECL v2.2, including the operators top and bottom

## [1.4.1388] - 2024-05-19 

* Avoid deprecated methods in Lucene 9.10 series (e.g. use CollectorManager and not Collector) - preparing for Lucene 10 release
* Upgrade dependencies 
* Add more comprehensive tests for arbitrarily large descriptions (see #66) to prove Hermes will have no issues if maximum description length increases 

## [1.4.1368] - 2024-03-20

* Upgrade dependencies - Clojure 1.11.2 and miscellaneous libraries
* Improve GitHub actions to run tests after every release automatically

## [1.4.1362] - 2024-03-11

* Fix #64 such that integrity always maintained during updates-in-place and re-indexing 
* Upgrade to Apache Lucene 9.10.0

## [1.4.1351] - 2024-01-27

* Upgrade to lmdbjava 0.9.0 - includes embedded lmdb binaries for multiple architectures and operating systems
* Upgrade to Apache Lucene 9.9.1

## [1.4.1346] - 2023-11-29

* Fix #61 - attribute values defined by a subexpression that returns no results should not result in invalid expression error
* Bump to pedestal v0.6.2 - fixes CVE-2023-36478
* Fix resolution of relationship modifier in the graph API
* Improve documentation and functional specifications

## [1.4.1320] - 2023-10-12

* Eclipse Public License v2.0
* Include license in build products
* Add support for automatically installing the new UK 'monolith' edition

## [1.4.1312] - 2023-10-10

* Upgrade to Apache Lucene 9.8
* Automatically generate CITATION.cff file for GitHub during build
* Generated artifacts such as library jar now include improved metadata (pom.xml)

## [1.4.1296] - 2023-09-20
 
* #60 When not explicitly set, derive database default fallback locale on the basis 
of installed reference sets rather than the system default locale

## [1.4.1292] - 2023-09-19

* #59 Improve error reporting if installed reference sets do not support the requested, or default fallback locale

## [1.4.1278] - 2023-09-15

* #58 In case of an aborted `compact`, delete intermediary files prior to another attempt
* #58 Close store after copying datafiles and before moving to new location during `compact`

## [1.4.1265] - 2023-09-09

* Always use normalized search and normalized index (Fixes #56)

This version breaks compatibility with previously indexed databases.

## [1.3.1262] - 2023-09-07

* Use 'Accept-Language' header in HTTP server to select preferred terms
* Better logging during indexing
* Fail fast rather than trying to continue when there is a critical error during
import (e.g. running out of disk space)
* Add term folding / normalization to improve search in content with diacritics (Fixes #55)

This version updates the index to add a search field for normalized (folded) text, particularly
helpful in international deployments in which release centres do not include synonyms
with and without non-semantically meaningful diacritics. This does not break 
compatibility with older indexes, but search using the folded index will obviously
not work with indexes created by prior versions; as such, this release will 
show a warning to say that index is missing. This can be safely ignored if you do
not need search against a folded index.

## [1.3.1232] - 2023-07-14

* Change to search index structure to better handle runtime, dynamic results  
  based on requested locale. This version is therefore incompatible with 
  databases created by previous versions. Hermes always permitted getting a 
  preferred term given localisation preferences, but the search index, for 
  convenience, cached a preferred term at the time of index creation. Instead, 
  v1.3 series caches all preferred terms so that search can return 
  locale-specific results without requiring further round-trips.
* Remove --locale as an option during index creation given now unnecessary
* Add options `:accept-language` `:language-refset-ids` to core search API

## [1.2.1244] - 2023-07-07

* Add language preferences to `synonyms` in core (Clojure), graph (Pathom) and Java APIs 
* For graph API, expect service to be under key `:com.eldrix/hermes` in environment 

## [1.2.1218] - 2023-06-26

* Fix build of library jar

## [1.2.1214] - 2023-06-25

* Unmerge Java source files and resume using hermes-api as an internal dependency
* Upgrade dependencies, include Lucene 9.7.0 and Pedestal 0.6.0, with performance benefits, particularly on modern Java.

## [1.2.1208] - 2023-06-03

* Explicitly define Java 11 as the compilation target for Java source code files, as otherwise whatever is installed on build server is used.

## [1.2.1204] - 2023-05-29

* Add `expand-ecl*` to core API for human-readable results optimising fetch of preferred synonym for each result 
(and add `expandEclPreferred` for Java API clients)
* Merge Java API wrapper ('hermes-api') into the main source repository
* Add build steps to compile Java source code as part of 'prep-lib' and build of jar/uberjar artefacts thus
deprecating code in https://github.com/wardle/hermes-api. That repository will now be archived.


## [1.2.1190] - 2023-05-18

* Add `search-concept-ids` to facilitate pre-populating search results in pick lists
* When streaming concepts from store, abort if channel is closed rather than continuing to send to a closed channel
* Better reporting of errors when installing directly from MLDS

## [1.2.1172] - 2023-05-02

* Make `mrcm-domains` public in library API
* Add `/v1/snomed/mrcm-domains` to HTTP API
* More uniform error reporting when parsing SNOMED ECL
* More comprehensive unit testing for SNOMED ECL
* Update instructions for use under different architectures and operating systems

## [1.2.1150] - 2023-04-23

* HTTP 404 for concept/properties if concept not found, rather than HTTP 200 response with empty data
* Upgrade to latest version of [trud](https://github.com/wardle/trud)

## [1.2.1142] - 2023-04-18

* Make `properties` public in top-level library API
* Add `match-locale` to top-level library API for when consumers need to parse
the same language preferences for a number of separate calls
* Add `/v1/snomed/concepts/:concept-id/properties` endpoint to HTTP API with 
configuration options for pretty printing for human users, and `expand` for machine users
* Improve handling of invalid parameters in HTTP API 
* Improve documentation, including links to live examples 

## [1.2.1115] - 2023-04-15

* Return HTTP 200 and empty collection instead of HTTP 404 when search gives no results
* Add trace logging for all HTTP requests although default configuration logs only at the INFO level. This means this can be configured at deployment.
* Add concrete values to 'status' and 'statistics' reports

## [1.2.1108] - 2023-04-13

* Avoid HTTP 500 errors for bad client input in HTTP server. 
* More consistent responses for HTTP server (result vs. empty result vs. not found).
* Automated testing of HTTP server responses

## [1.2.1102] - 2023-04-12

* Show progress reporting when downloading from the UK's 'TRUD'
* Avoid unnecessary network calls for distribution metadata
* Fix import of concrete values when updating in place
* Add `valid-ecl?` and `isValidEcl()` to top-level Clojure and Java APIs respectively.

## [1.2.1088] - 2023-04-07

* Add support for the MLDS (Member Licensing and Distribution Service) for automated
downloads and install of a number of distributions across the World.

## [1.2.1080] - 2023-04-06

* Add support for the MRCM (Machine Readable Concept Model)
* Use MRCM to return concept properties according to cardinality rules

## [1.2.1060] - 2023-04-04

* Add support for concrete values for store, indexing and search 
* Improve server error handling and reporting
* Improve cardinality queries in ECL parsing/expansion
* Improve wildcard attribute search

## [1.2.1040] - 2023-04-03

* Improve cardinality constraints during ECL processing

## [1.2.1032] - 2023-04-01

* Optimise handling wildcard value for attribute in an ECL refinement; the prior implementation had no special 
handling for this, and so expanded the value to all concepts, impacting performance.
* Fix handling of cardinality in expressions with a minimum of '0' (e.g. [0..1] or [0..0]). 

## [1.2.1026] - 2023-03-29

* Significant but backwards-compatible change in top-level API with improved, and shortened function names (e.g. `concept` rather than `get-concept` for Clojure-based library clients)
* Miscellaneous code style and performance improvements 
* Internal (private) API changes
* Improve tests
* More deterministic derivation of 'replace by' in graph API.
* Remove unnecessary dependencies when building uberjar, improving start-up performance

## [1.1.1000] - 2023-03-23

* Update to version 1.1 series given drop in support for Lucene 8.x series and therefore Java 1.8
* Upgrade dependencies (e.g. netty-buffer 4.1.90)
* Add ability to stream search results asynchronously
* Miscellaneous performance improvements
* Miscellaneous code style improvements

## [1.0.960] - 2023-02-15

* Drop support for Lucene 8.x series and therefore Java 1.8 (latter out of support from 31 March 2022, and Lucene 9.x requires at least Java 11). 
* Improve Java API
* Add ability to stream all concepts to top level API
* Use new Lucene 9.5 'storedFields' in favour of deprecated document field access
* Add new benchmark test suite
* Optimisations

## [1.0.946] - 2023-02-06

* Add thin wrapper API for use by Java and other JVM languages when used as a library
* Upgrade internal dependencies (Lucene 9.5, lmdbjava 0.8.3)

## [1.0.938] - 2023-01-31

* Add support to give multiple commands in one invocation via command-line interface 
* Improve speed of import through tweaking memory map synchronisation 
* Improve speed of indexing through more efficient calculation of transitive closure tables for relationships


## [1.0.914] - 2023-01-23

* Improve speed and efficiency of import. Use a single thread for LMDB writes.
* Improve error handling (e.g. for automated install of distributions when a national distribution site is offline) and messages (e.g. when a database is not found on filesystem).

## [1.0.895] - 2023-01-15

* Significant update to command-line operation, while maintaining backwards-compatibility
* Status reports now record count data in a nested map with a choice of formatting in the CLI. 

## [1.0.875] - 2023-01-07

* Fix and speed-up `get-refset-members'
* More complete unit testing
* Add support for module dependency reference set items
* Flag any module dependency problems on indexing and serve
* Report size of search indices in status report
* Use language preferences for fully specified name


## [1.0.822] - 2022-12-24

* Enable native support for Apple Silicon, rather than needing to use an x86-64 JDK. (To add native aarm64 support, you will need to manually build lmdb binary - see README).
* Log detailed system information on server startup, including architecture.

## [1.0.815] - 2022-12-22

* Upgrade to netty-buffer 4.1.86
* Improve namespace stratification by factoring out common Lucene functions shared by the two indices (descriptions and refset members).
* Move ECL and CG implementations into `com.eldrix.hermes.impl` as they contain no public-facing API.
* Explicitly use an opaque handle for core API to represent encapsulated runtime state. The prior public but undocumented `Service` is now deprecated in favour of a private `Svc` with `hermes/open` returning `Closeable`.   

## [1.0.804] - 2022-11-27

* Upgrade to Lucene 9.4.2 (and Lucene 8.11.2 when Java 8 compatibility required)
* Improve namespace layering structure by ensuring graph API only uses top-level APIs

## [1.0.792] - 2022-11-14

* Add `get-description` and `get-relationship` to top-level API
* Add polymorphic `get-component` via graph API
* Add additional reference set item resolution via graph API
* Address miscellaneous code linting (removal unused imports and requires, avoid shadowing vars, improved comments / docstrings) 

## [1.0.772] - 2022-11-08

* Tweak indexing so that reference set items are indexed in a separate pass as
an interim step during import, for refset reification, and then re-indexed in 
the formal 'index' step.
* Update documentation to recommend compaction should be run *after* indexing, now
that the core datastore is updated during indexing.

## [1.0.770] - 2022-11-07

* Fix [#42](https://github.com/wardle/hermes/issues/42) in which relationship indices could be incorrect if
more than one relationship in a distribution relates to the same source-target-type tuple.
* Add `intersect-ecl` to core library to intersect a set of concept identifiers with an ECL expression.

## [1.0.764] - 2022-10-23

* Add recognised installed locales into status report
* Harmonise parameter style for HTTP REST server endpoints

## [1.0.754] - 2022-10-22

* Upgrade dependencies (including netty-buffer,logback-classic and tools.cli)
* Add automated release to GitHub for uberjar, to supplement automation to clojars for library jar

## [1.0.734] - 2022-10-15

* Add support to automatically remove duplicates during search, when term and conceptId match.
* Add ability to explicitly include or exclude inactive concepts and descriptions from HTTP API 

## [1.0.712] - 2022-08-14

* Tweak logging during import
* Refine handling of distribution metadata
* Remove unused code
* Fix graph resolution of a concept's membership of reference sets (via properties /refsetIds and /refsetItems).
* Upgrade dependencies (Lucene 9.3, netty buffers 4.1.79)

## [0.12.684] - 2022-06-12

* Add ability to download a specific release by date, for UK users.

## [0.12.681] - 2022-06-08

* Improve logging of metadata when importing distribution(s)
* For uberjar, fully ahead-of-time (AOT) compile, elide some metadata and use direct-linking
* Reduce logging during build process

## [0.12.664] - 2022-06-02

* More deterministic build for uberjars
* Reduce build product sizes by reducing dependencies
* Simplify usage when used as a library by Java clients, with an externally defined Java API.
* Avoid ahead-of-time compilation when used as a library
* Improve logging for distribution import
* Upgrade dependencies (Lucene v9.2, trud v1.0.80, 

## [0.12.644] - 2022-05-17

* Improve download provider parameters so can optionally use explicit assignment e.g. api-key=../trud/api-key.txt

## [0.12.640] - 2022-05-10

* Switch to new lmdb storage backend, version 'lmdb/15'
* Speed improvements and optional compaction step to reduce database size

## [0.11.604] - 2022-05-06

* Bump file-based database to 0.11
* Improve refset extended attribute support
* Add new search index 'members.db' for search of reference set members
* Partial ECL v2.0 support including +HISTORY and member filters
* Add support for lucene v8 usage when required (e.g. for Java 8 compatibility)

## [0.10.533] - 2022-04-11

* New two-phase import permitting reification of refset items on import.
* Return refset items with extended attributes by default in graph and HTTP APIs.

## [0.10.519] - 2022-04-09 

* Improve speed and error handling in import
* Revise approach to extended fields in reference set items
* Bump to version 0.7 of the file-based database given changes in schema.
This means this version will refuse to read databases created in prior versions.
* Add runtime reification of refset items for those not reified during import. 

## [0.9.486] - 2022-04-03

* Add specifications for all supported SNOMED CT entities
* Add generative tests for parsing and unparsing SNOMED data files
* Add synth_test.clj to export and then re-import generated synthetic data
* Bump to Clojure 1.11.0
* Speed up import and indexing

## [0.9.458] - 2022-03-27

* Upgrade to Lucene 9.1
* Add and improve graph API resolvers
* Improve synthetic and live test suite
* Fix extended reference set import pattern matching

## [0.9.408] - 2022-03-21

* Add synthetic SNOMED data generation
* Much more complete automated testing using synthetic data
* Add greater instrumentation when in development or testing
* Add import of OWL reference set types 
* Fix accept-language parsing and fallback in http server

## [0.9.369] - 2022-02-17

* Bump to Lucene 9.0 - hermes now requires JDK 11 or newer.
* Switch to using clojure.data.json for JSON export.
* Bump other minor outdated dependencies (logback, trud).

## [0.9.362] - 2022-02-15

* Support for mapping concepts into a defined subset of concepts.
* Add `get-synonyms` convenience function
* Expose `get-all-children` at top-level API

## [0.8.341] - 2022-01-22

* Bump to Apache Lucene 8.11.0 - no API or index changes, but simply bug fixes
* Add --allowed-origins option to server to enable configuration of CORS, if required
* Add build via [tools.build](https://github.com/clojure/tools.build)
* Add deployment to clojars.

## [0.8.3] - 2021-11-04

* More complete UK language refset priority list
* Fail fast if there are any issues caught during processing (e.g. broken data such as invalid dates)
* Optimise import, parsing and storage flow. Better handling of errors during import.
* Fix import of simple reference sets, including when 'Simple' not in summary field of filename
* Tidy status output when importing data if no metadata found.
* Add support for simple reference set extensions with flexible fields based on 's' 'c' or 'i' in filename for local customisation

## [0.8.2] - 2021-10-31

* Status command now prints installed reference sets, and optionally counts of SNOMED data
* Add `-v` `--verbose` flag to increase verbosity for commands, although they're pretty verbose already
* Better error reporting during import 


## [0.8.1] - 2021-10-30

* Add explicit choice of locale at time of index creation for preferred concept cache. Hermes supports multiple languages but for convenience caches the preferred synonym for a given locale (or set of reference sets) in the search index.
* Fallback to en-US if chosen locale, or system default locale, does not have a known set of 'best' language reference sets

## [0.8.0] - 2021-10-24

* Add fuzzy and fallback-fuzzy search to graph API.
* Add `get-all-parents`, and `get-parent-relationships` to core API
* Permit configuration of with-historical to allow choice of association refset types
* Add ECL membership check
* Add reverse cross-map prefix search (useful for ICD-10 and Read)
* Optimisations

## [0.7.5] - 2021-09-13

* Fix handling of '<<' in expressions.
* Improve validation of HTTP parameters
* Backend paths-to-root and some-indexed for dimensionality reduction work

## [0.7.4] - 2021-07-27

* Make JSON default for HTTP server
* Add refsets HTTP endpoint 

## [0.7.3] - 2021-07-27

* Add reverse index specifically for association reference set items
* Add expand API endpoint to HTTP server, including historical if requested

## [0.7.2] - 2021-07-20

* Bind address specified at command line now actually used.

## [0.7.1] - 2021-06-04

* Add support for new SNOMED filename naming system used by UK since May 21
* Force UTF-8 encoding to ensure works on platforms with different default character encoding.

## [0.7.0] - 2021-05-31

* New graph API using declarative approach permitting clients to ask for exactly what they need
* Expose fuzzy search in REST API.
* Restructuring - separate library code from cli commands / server
* Add rudimentary GitHub Actions tests, but not yet using live db
* Add historical associations reference set support
* Update database version to v0.6 given new indexes.
* Better query re-writing for when single must-not clauses used alone. 

## [0.6.2] - 2021-04-20

* Permit prefix search to run with one character or more, rather than minimum of three, for better autocompletion functionality.

## [0.6.1] - 2021-04-19

* Upgrade dependencies
* Temporarily bind to 0.0.0.0 pending more complete server configuration options

## [0.6.0] - 2021-03-31

* Support for unlimited results when used as a library, and when processing expressions
* Harmonise web service key names to use camel case and not mix in kebab-case.
* Expose library API call to get installed reference sets
* Expose support for `Accept-Language` using BCP 47 language tags in REST server, with additional support for specific language reference set extension of form `en-gb-x-XXXX` where XXXX is preferred language refset identifier.  
* Release information included in log files

## [0.5.0] - 2021-03-09

* Minor fixes

## [0.4.0] - 2021-03-08

* Major change to backend store with new custom serialization resulting in enormous speed-up and size benefits.
* UK dictionary of medicines and devices (dm+d) custom code extracted to [another repository](https://github.com/wardle/dmd) 
* Add support for transitive synonyms

## [0.3.0] - 2021-01-27

* Add special custom extension for UK dictionary of medicines and devices (dm+d) to bring in non-SNOMED BSA data
* SNOMED ECL (expression constraint language) support
* SNOMED CG (compositional grammar) support  
* Major improvements to server and backend. Unified terminology service abstracting underlying implementations.

## [0.2.0] - 2020-11-18

* Add search and autocompletion using Apache Lucene

## [0.1.0] - 2020-11-12

* Basic SNOMED service (store/retrieval/inference)

