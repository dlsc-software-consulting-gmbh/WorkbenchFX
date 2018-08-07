require "asciidoctor"
require_relative "errors"
require "csl/styles"

module AsciidoctorBibliography
  class Options < Hash
    PREFIX = "bibliography-".freeze

    DEFAULTS = {
      "bibliography-database" => nil,
      "bibliography-locale" => "en-US",
      "bibliography-style" => "apa",
      "bibliography-hyperlinks" => "true",
      "bibliography-order" => "alphabetical", # TODO: deprecate
      "bibliography-tex-style" => "authoryear",
      "bibliography-sort" => nil,
      "bibliography-prepend-empty" => "true",
      "bibliography-passthrough" => "false",
    }.freeze

    def initialize
      merge DEFAULTS
    end

    def self.build(document, reader)
      header_attributes = get_header_attributes_hash reader
      header_attributes.select! { |key, _| DEFAULTS.keys.include? key }
      cli_attributes = document.attributes.select { |key, _| DEFAULTS.keys.include? key }
      new.merge!(header_attributes).merge!(cli_attributes)
    end

    def self.get_header_attributes_hash(reader)
      # We peek at the document attributes we need, without perturbing the parsing flow.
      # NOTE: we'll use this in a preprocessor and they haven't been parsed yet, there.
      tmp_document = ::Asciidoctor::Document.new
      tmp_reader = ::Asciidoctor::PreprocessorReader.new(tmp_document, reader.source_lines)

      ::Asciidoctor::Parser.
        parse(tmp_reader, tmp_document, header_only: true).
        attributes
    end

    def style
      # First we check whether an internal style exists to qualify its path.
      if self["bibliography-style"]
        filepath = File.join AsciidoctorBibliography.csl_styles_root,
                             self["bibliography-style"] + ".csl"
        self["bibliography-style"] = filepath if File.exist? filepath
      end
      # Then error throwing is delegated to CSL library.
      self["bibliography-style"] || DEFAULTS["bibliography-style"]
    end

    def locale
      value = self["bibliography-locale"] || DEFAULTS["bibliography-locale"]
      raise_invalid <<~MESSAGE unless CSL::Locale.list.include? value
        Option :bibliography-locale: has an invalid value (#{value}).
        Allowed values are #{CSL::Locale.list.inspect}.
      MESSAGE

      value
    end

    def hyperlinks?
      value = self["bibliography-hyperlinks"] || DEFAULTS["bibliography-hyperlinks"]
      raise_invalid <<~MESSAGE unless %w[true false].include? value
        Option :bibliography-hyperlinks: has an invalid value (#{value}).
        Allowed values are 'true' and 'false'.
      MESSAGE

      value == "true"
    end

    def database
      value = self["bibliography-database"] || DEFAULTS["bibliography-database"]
      raise Errors::Options::Missing, <<~MESSAGE if value.nil?
        Option :bibliography-database: is mandatory.
        A bibliographic database is required.
      MESSAGE

      value
    end

    def sort
      begin
        value = YAML.safe_load self["bibliography-sort"].to_s
      rescue Psych::SyntaxError => psych_error
        raise_invalid <<~MESSAGE
          Option :bibliography-sort: is not a valid YAML string: \"#{psych_error}\".
        MESSAGE
      end

      value = validate_parsed_sort_type! value
      value = validate_parsed_sort_contents! value unless value.nil?
      value
    end

    def tex_style
      value = self["bibliography-tex-style"] || DEFAULTS["bibliography-tex-style"]
      raise_invalid <<~MESSAGE unless %w[authoryear numeric].include? value
        Option :bibliography-tex-style: has an invalid value (#{value}).
        Allowed values are 'authoryear' (default) and 'numeric'.
      MESSAGE

      value
    end

    def passthrough?(context)
      # NOTE: allowed contexts are :citation and :reference
      value = self["bibliography-passthrough"] || DEFAULTS["bibliography-passthrough"]
      raise_invalid <<~MESSAGE unless %w[true citations references false].include? value
        Option :bibliography-passthrough: has an invalid value (#{value}).
        Allowed values are 'true', 'citations', 'references' and 'false'.
      MESSAGE

      evaluate_ext_boolean_value_vs_context value: value, context: context
    end

    def prepend_empty?(context)
      # NOTE: allowed contexts are :citation and :reference
      value = self["bibliography-prepend-empty"] || DEFAULTS["bibliography-prepend-empty"]
      raise_invalid <<~MESSAGE unless %w[true citations references false].include? value
        Option :bibliography-prepend-empty: has an invalid value (#{value}).
        Allowed values are 'true', 'citations', 'references' and 'false'.
      MESSAGE

      evaluate_ext_boolean_value_vs_context value: value, context: context
    end

    private

    def evaluate_ext_boolean_value_vs_context(value:, context:)
      return true if value.to_s == "true"
      return false if value.to_s == "false"
      return context.to_s == "citation" if value.to_s == "citations"
      return context.to_s == "reference" if value.to_s == "references"
    end

    def raise_invalid(message)
      raise Errors::Options::Invalid, message
    end

    def validate_parsed_sort_type!(value)
      return value if value.nil?
      return value if value.is_a?(Array) && value.all? { |v| v.is_a? Hash }
      return [value] if value.is_a? Hash
      raise_invalid <<~MESSAGE
        Option :bibliography-sort: has an invalid value (#{value}).
        Please refer to manual for more info.
      MESSAGE
    end

    def validate_parsed_sort_contents!(array)
      # TODO: should we restrict these? Double check the CSL spec.
      allowed_keys = %w[variable macro sort names-min names-use-first names-use-last]
      return array unless array.any? { |hash| (hash.keys - allowed_keys).any? }
      raise_invalid <<~MESSAGE
        Option :bibliography-sort: has a value containing invalid keys (#{array}).
        Allowed keys are #{allowed_keys.inspect}.
        Please refer to manual for more info.
      MESSAGE
    end
  end
end
