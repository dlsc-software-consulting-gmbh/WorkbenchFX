# coding: utf-8

lib = File.expand_path("../lib", __FILE__)
$LOAD_PATH.unshift(lib) unless $LOAD_PATH.include?(lib)
require "asciidoctor-bibliography/version"

Gem::Specification.new do |spec| # rubocop:disable Metrics/BlockLength
  spec.name          = "asciidoctor-bibliography"
  spec.version       = AsciidoctorBibliography::VERSION
  spec.authors       = ["Ribose Inc."]
  spec.email         = ["open.source@ribose.com"]

  spec.summary       = 'Citations and bibliography the "asciidoctor-way"'
  spec.description   = <<~SPEC
    asciidoctor-bibliography lets you handle citations and bibliography the "asciidoctor-way"!

    Its syntax is designed to be native-asciidoctor:
    * single cite `cite:[key]`;
    * contextual cite `cite[key, page=3]`;
    * multiple cites `cite:[key1]+[key2]`;
    * full cite `fullcite:[key]`; and
    * TeX-compatible macros including `citep:[key]`, `citet:[key]` and friends.

    Citation output styles are fully bridged to the CSL library, supporting formats such as IEEE, APA, Chicago, DIN and ISO 690.

    The `bibliography::[]` command generates a full reference list that adheres to your configured citation style.
  SPEC

  spec.homepage      = "https://github.com/riboseinc/asciidoctor-bibliography"
  spec.license       = "MIT"

  spec.require_paths = ["lib"]
  spec.files         = `git ls-files`.split("\n")
  spec.test_files    = `git ls-files -- {spec}/*`.split("\n")
  spec.required_ruby_version = Gem::Requirement.new(">= 2.3.0")

  spec.add_dependency "asciidoctor", "~> 1.5.6"
  spec.add_dependency "bibtex-ruby", "~> 4.4.4"
  spec.add_dependency "citeproc-ruby", "~> 1.1.7"
  spec.add_dependency "csl-styles", "~> 1.0.1"
  spec.add_dependency "latex-decode", "~> 0.2.2"
  spec.add_dependency "nokogiri", "~> 1.8.1"

  spec.add_development_dependency "byebug", "~> 10.0.1"
  spec.add_development_dependency "rake", "~> 12.3.0"
  spec.add_development_dependency "rspec", "~> 3.7.0"
  spec.add_development_dependency "rubocop", "~> 0.54.0"
  spec.add_development_dependency "simplecov", "~> 0.16.1"
  spec.add_development_dependency "yard", "~> 0.9.12"
end
