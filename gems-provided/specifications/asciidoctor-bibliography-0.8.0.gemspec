# -*- encoding: utf-8 -*-
# stub: asciidoctor-bibliography 0.8.0 ruby lib

Gem::Specification.new do |s|
  s.name = "asciidoctor-bibliography".freeze
  s.version = "0.8.0"

  s.required_rubygems_version = Gem::Requirement.new(">= 0".freeze) if s.respond_to? :required_rubygems_version=
  s.require_paths = ["lib".freeze]
  s.authors = ["Ribose Inc.".freeze]
  s.date = "2018-04-07"
  s.description = "asciidoctor-bibliography lets you handle citations and bibliography the \"asciidoctor-way\"!\n\nIts syntax is designed to be native-asciidoctor:\n* single cite `cite:[key]`;\n* contextual cite `cite[key, page=3]`;\n* multiple cites `cite:[key1]+[key2]`;\n* full cite `fullcite:[key]`; and\n* TeX-compatible macros including `citep:[key]`, `citet:[key]` and friends.\n\nCitation output styles are fully bridged to the CSL library, supporting formats such as IEEE, APA, Chicago, DIN and ISO 690.\n\nThe `bibliography::[]` command generates a full reference list that adheres to your configured citation style.\n".freeze
  s.email = ["open.source@ribose.com".freeze]
  s.homepage = "https://github.com/riboseinc/asciidoctor-bibliography".freeze
  s.licenses = ["MIT".freeze]
  s.required_ruby_version = Gem::Requirement.new(">= 2.3.0".freeze)
  s.rubygems_version = "2.6.14.1".freeze
  s.summary = "Citations and bibliography the \"asciidoctor-way\"".freeze

  s.installed_by_version = "2.6.14.1" if s.respond_to? :installed_by_version

  if s.respond_to? :specification_version then
    s.specification_version = 4

    if Gem::Version.new(Gem::VERSION) >= Gem::Version.new('1.2.0') then
      s.add_runtime_dependency(%q<asciidoctor>.freeze, ["~> 1.5.6"])
      s.add_runtime_dependency(%q<bibtex-ruby>.freeze, ["~> 4.4.4"])
      s.add_runtime_dependency(%q<citeproc-ruby>.freeze, ["~> 1.1.7"])
      s.add_runtime_dependency(%q<csl-styles>.freeze, ["~> 1.0.1"])
      s.add_runtime_dependency(%q<latex-decode>.freeze, ["~> 0.2.2"])
      s.add_runtime_dependency(%q<nokogiri>.freeze, ["~> 1.8.1"])
      s.add_development_dependency(%q<byebug>.freeze, ["~> 10.0.1"])
      s.add_development_dependency(%q<rake>.freeze, ["~> 12.3.0"])
      s.add_development_dependency(%q<rspec>.freeze, ["~> 3.7.0"])
      s.add_development_dependency(%q<rubocop>.freeze, ["~> 0.54.0"])
      s.add_development_dependency(%q<simplecov>.freeze, ["~> 0.16.1"])
      s.add_development_dependency(%q<yard>.freeze, ["~> 0.9.12"])
    else
      s.add_dependency(%q<asciidoctor>.freeze, ["~> 1.5.6"])
      s.add_dependency(%q<bibtex-ruby>.freeze, ["~> 4.4.4"])
      s.add_dependency(%q<citeproc-ruby>.freeze, ["~> 1.1.7"])
      s.add_dependency(%q<csl-styles>.freeze, ["~> 1.0.1"])
      s.add_dependency(%q<latex-decode>.freeze, ["~> 0.2.2"])
      s.add_dependency(%q<nokogiri>.freeze, ["~> 1.8.1"])
      s.add_dependency(%q<byebug>.freeze, ["~> 10.0.1"])
      s.add_dependency(%q<rake>.freeze, ["~> 12.3.0"])
      s.add_dependency(%q<rspec>.freeze, ["~> 3.7.0"])
      s.add_dependency(%q<rubocop>.freeze, ["~> 0.54.0"])
      s.add_dependency(%q<simplecov>.freeze, ["~> 0.16.1"])
      s.add_dependency(%q<yard>.freeze, ["~> 0.9.12"])
    end
  else
    s.add_dependency(%q<asciidoctor>.freeze, ["~> 1.5.6"])
    s.add_dependency(%q<bibtex-ruby>.freeze, ["~> 4.4.4"])
    s.add_dependency(%q<citeproc-ruby>.freeze, ["~> 1.1.7"])
    s.add_dependency(%q<csl-styles>.freeze, ["~> 1.0.1"])
    s.add_dependency(%q<latex-decode>.freeze, ["~> 0.2.2"])
    s.add_dependency(%q<nokogiri>.freeze, ["~> 1.8.1"])
    s.add_dependency(%q<byebug>.freeze, ["~> 10.0.1"])
    s.add_dependency(%q<rake>.freeze, ["~> 12.3.0"])
    s.add_dependency(%q<rspec>.freeze, ["~> 3.7.0"])
    s.add_dependency(%q<rubocop>.freeze, ["~> 0.54.0"])
    s.add_dependency(%q<simplecov>.freeze, ["~> 0.16.1"])
    s.add_dependency(%q<yard>.freeze, ["~> 0.9.12"])
  end
end
