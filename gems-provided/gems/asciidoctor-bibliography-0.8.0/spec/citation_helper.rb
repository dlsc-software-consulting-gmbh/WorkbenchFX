# coding: utf-8

require "asciidoctor-bibliography"

def init_bibliographer(options: {})
  bibliographer = AsciidoctorBibliography::Bibliographer.new

  opts = options.dup

  db_path = File.join __dir__, "fixtures", opts.delete("bibliography-database")
  bibliographer.database = AsciidoctorBibliography::Database.new.append db_path

  bibliographer.options =
    AsciidoctorBibliography::Options.new.
    merge(
      "bibliography-hyperlinks" => "false",
      "bibliography-prepend-empty" => "false",
    ).merge(opts)

  bibliographer
end

def formatted_citation(macro, options: {})
  bibliographer = init_bibliographer options: options

  macro.gsub(AsciidoctorBibliography::Citation::REGEXP) do
    citation = AsciidoctorBibliography::Citation.new(*Regexp.last_match.captures)
    bibliographer.add_citation(citation)
    citation.render bibliographer
  end
end

def formatted_bibliography(macro, options: {})
  bibliographer = init_bibliographer options: options
  macro.gsub(AsciidoctorBibliography::Citation::REGEXP) do
    citation = AsciidoctorBibliography::Citation.new(*Regexp.last_match.captures)
    bibliographer.add_citation(citation)
    # citation.render bibliographer
    index = AsciidoctorBibliography::Index.new("bibliography", "", "")
    index.render(bibliographer).join
  end
end
