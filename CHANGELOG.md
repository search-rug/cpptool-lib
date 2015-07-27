# Change Log
All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased][unreleased]
### Added
- Support for importing access specifiers:
    + Enumaration for access specification (i.e., public, protected, private).
    + Interface and implementation for class parents.
    + Accessor for access specifier of parent classes.
    + Accessor for access specifier of methods.
    + Accessor for access specifier of fields.
- Support for importing pure virtual methods:
    + Accessor **isPureVirtual** to Interface/Class **CxxFunction**.

### Changed
- For supporting access specifiers:
    + Accessor **parents** (in **CxxRecord**) to new parent interface.
- Interface/Class **ClassVar** to **Field**.

## 0.1.0 - 2015-07-24
### Added
- Project info files (README, LICENSE, CHANGELOG).
- Importing of declaration tree exported by cpptool.
- Example code for using the library.

[unreleased]: https://github.com/search-rug/cpptool-lib-core/compare/v0.1.0...HEAD