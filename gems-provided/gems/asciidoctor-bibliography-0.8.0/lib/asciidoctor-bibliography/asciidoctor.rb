require "asciidoctor/extensions"

require_relative "asciidoctor/bibliographer_preprocessor"
require_relative "asciidoctor/document_ext"
require_relative "bibliographer"

Asciidoctor::Document.include AsciidoctorBibliography::Asciidoctor::DocumentExt

Asciidoctor::Extensions.register do
  preprocessor AsciidoctorBibliography::Asciidoctor::BibliographerPreprocessor
end
