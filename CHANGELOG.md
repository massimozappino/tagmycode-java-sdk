# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## UNRELEASED
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
 
[1.1.1]: https://github.com/massimozappino/tagmycode-java-sdk/compare/v1.1.0...v1.1.1
[1.1.0]: https://github.com/massimozappino/tagmycode-java-sdk/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/massimozappino/tagmycode-java-sdk/compare/v0.1.2...v1.0.0
