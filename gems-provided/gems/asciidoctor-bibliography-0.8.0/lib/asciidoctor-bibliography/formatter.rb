require "citeproc"
require "csl/styles"
require "yaml"

require_relative "../citeproc/ruby/formats/adoc"

module AsciidoctorBibliography
  class Formatter < ::CiteProc::Processor
    def initialize(style, locale: "en-US")
      super style: style, format: :adoc, locale: locale
    end

    def replace_bibliography_sort(array)
      new_keys = array.map(&::CSL::Style::Sort::Key.method(:new))
      new_sort = ::CSL::Style::Sort.new.add_children(*new_keys)

      bibliography = engine.style.find_child("bibliography")
      bibliography.find_child("sort")&.unlink

      bibliography.add_child new_sort
    end

    def force_sort!(mode:)
      # Valid modes are :citation and :bibliography
      engine.sort! data, engine.style.send(mode).sort_keys if engine.style.send(mode).sort?
    end
  end
end
