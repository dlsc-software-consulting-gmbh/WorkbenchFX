require "bibtex"
require "bibtex/filters"
require "latex/decode/base"
require "latex/decode/maths"
require "latex/decode/accents"
require "latex/decode/diacritics"
require "latex/decode/punctuation"
require "latex/decode/symbols"
require "latex/decode/greek"

module AsciidoctorBibliography
  module Databases
    module BibTeX
      EXTENSIONS = %w[.bib .bibtex .biblatex].freeze

      def self.load(filename)
        # TODO: detect BibLaTeX code w/ other extensions
        warn <<~MESSAGE if File.extname(filename) == '.biblatex'
          WARNING: you are requiring a BibLaTeX database; only features compatible with BibTeX are guaranteed to work.
        MESSAGE
        ::BibTeX.open(filename, filter: [LatexFilter]).to_citeproc
      end

      # NOTE: the class below comes from asciidoctor-bibtex

      # This filter extends the original latex filter in bibtex-ruby to handle
      # unknown latex macros more gracefully. We could have used latex-decode
      # gem together with our custom replacement rules, but latex-decode eats up
      # all braces after it finishes all decoding. So we hack over the
      # LaTeX.decode function and insert our rules before `strip_braces`.
      class LatexFilter < ::BibTeX::Filter
        def apply(value) # rubocop:disable Metrics/MethodLength; keep this a list, though!
          text = value.to_s
          LaTeX::Decode::Base.normalize(text)
          LaTeX::Decode::Maths.decode!(text)
          LaTeX::Decode::Accents.decode!(text)
          LaTeX::Decode::Diacritics.decode!(text)
          LaTeX::Decode::Punctuation.decode!(text)
          LaTeX::Decode::Symbols.decode!(text)
          LaTeX::Decode::Greek.decode!(text)
          # TODO: could we be doing something smarter with some macros, e.g. \url?
          text.gsub!(/\\url\{(.+?)\}/, ' \\1 ')
          text.gsub!(/\\\w+(?=\s+\w)/, "")
          text.gsub!(/\\\w+(?:\[.+?\])?\s*\{(.+?)\}/, '\\1')
          LaTeX::Decode::Base.strip_braces(text)
          LaTeX.normalize_C(text)
        end
      end
    end
  end
end
