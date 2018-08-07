require "asciidoctor/attribute_list"

module AsciidoctorBibliography
  class CitationItem
    LOCATORS = CiteProc::CitationItem.labels.map(&:to_s).push("locator").freeze

    attr_accessor :key, :target, :positional_attributes, :named_attributes

    def initialize
      yield self if block_given?
    end

    def prefix
      named_attributes["prefix"]
    end

    def suffix
      named_attributes["suffix"]
    end

    def text
      named_attributes["text"]
    end

    def locators
      named_attributes.select { |key, _| LOCATORS.include? key }
    end

    def locator
      locators.first
    end

    def parse_attribute_list(string)
      parsed_attributes = ::Asciidoctor::AttributeList.new(string).parse
      self.named_attributes = parsed_attributes.reject { |key, _| key.is_a? Integer }
      self.positional_attributes = parsed_attributes.select { |key, _| key.is_a? Integer }.values
      self.key = positional_attributes.shift
    end
  end
end
