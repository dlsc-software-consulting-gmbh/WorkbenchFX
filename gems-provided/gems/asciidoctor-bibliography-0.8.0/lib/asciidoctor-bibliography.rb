begin
  require "byebug"
rescue LoadError
end

require_relative "asciidoctor-bibliography/asciidoctor"

module AsciidoctorBibliography
  def self.root
    File.dirname __dir__
  end

  def self.csl_styles_root
    File.join AsciidoctorBibliography.root,
              "lib/csl/styles"
  end
end
