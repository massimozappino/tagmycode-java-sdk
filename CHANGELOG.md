# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [1.3.2] - (2022-04-28)
### Changed
- moved from slf4j to log4j
- updated dependencies

## [1.3.1] - (2020-07-26)
### Fixed
- force logout if refresh token is not valid

## [1.3.0] - (2020-07-19)
### Added
- added [Lombok](https://projectlombok.org/) library

### Fixed
- fixed java.lang.NoClassDefFoundError: javax/xml/bind/DatatypeConverter

### Changed
- changed java version from 1.6 to 1.8
- changed logger (slf4j instead of log4j)

## [1.2.1] - (2018-01-06)
### Added
- throws TagMyCodeUnauthorizedException if refresh token is not valid

## [1.2.0] - (2017-11-05)
### Changed
- renamed LanguagesCollection and SnippetsCollection
- rewritten synchronization snippets logic

### Added
- LanguagesCollection can be searched by code
- LanguagesCollection can be searched by file name extension
- managed schema version with db drop and create
- added "dirty" and "deleted" fields on snippet model
- added SnippetStorage to manage SQL snippets operations
- added log for API request
- added DateParser utilities to print datetime with different locales

## [1.1.1] - 2017-04-22
### Changed
- TagMyCodeException shows message of the parameter Exception

## [1.1.0] - 2017-03-25
### Added
- models are ready to be used by OrmLite and H2 Database
- added CHANGELOG.md file
- added SaveFilePath class
- added Property model

### Changed
- TagMyCode object wraps client methods

## [1.0.0] - 2016-06-06
### Added
- added methods to manage TagMyCode API

[1.3.2]: https://github.com/massimozappino/tagmycode-java-sdk/compare/v1.3.1...v1.3.2
[1.3.1]: https://github.com/massimozappino/tagmycode-java-sdk/compare/v1.3.0...v1.3.1
[1.3.0]: https://github.com/massimozappino/tagmycode-java-sdk/compare/v1.2.1...v1.3.0
[1.2.1]: https://github.com/massimozappino/tagmycode-java-sdk/compare/v1.2.0...v1.2.1
[1.2.0]: https://github.com/massimozappino/tagmycode-java-sdk/compare/v1.1.1...v1.2.0
[1.1.1]: https://github.com/massimozappino/tagmycode-java-sdk/compare/v1.1.0...v1.1.1
[1.1.0]: https://github.com/massimozappino/tagmycode-java-sdk/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/massimozappino/tagmycode-java-sdk/compare/v0.1.2...v1.0.0
