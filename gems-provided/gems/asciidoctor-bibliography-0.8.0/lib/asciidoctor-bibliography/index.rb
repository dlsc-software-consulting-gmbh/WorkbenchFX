require "asciidoctor/attribute_list"
require_relative "formatter"

module AsciidoctorBibliography
  class Index
    REGEXP = /^(bibliography)::(\S+)?\[(|.*?[^\\])\]$/

    attr_reader :macro, :target, :attributes

    def initialize(macro, target, attributes)
      @macro = macro
      @target = target.to_s.empty? ? "default" : target
      @attributes = ::Asciidoctor::AttributeList.new(attributes).parse
    end

    def render(bibliographer)
      formatter = setup_formatter bibliographer

      lines = []
      formatter.bibliography.each_with_index do |reference, index|
        id = anchor_id "bibliography", target, formatter.data[index].id
        lines << wrap_up_reference(reference: reference, id: id, bibliographer: bibliographer)
      end

      # Intersperse the lines with empty ones to render as paragraphs.
      lines.join("\n\n").lines.map(&:strip)
    end

    private

    def wrap_up_reference(reference:, id:, bibliographer:)
      text = reference.dup
      text.prepend "anchor:#{id}[]" if bibliographer.options.hyperlinks?
      text = ["+++", reference, "+++"].join if bibliographer.options.passthrough?(:reference)
      text.prepend "{empty}" if bibliographer.options.prepend_empty?(:reference)
      text
    end

    def setup_formatter(bibliographer)
      formatter = Formatter.new(bibliographer.options.style, locale: bibliographer.options.locale)

      formatter.replace_bibliography_sort bibliographer.options.sort unless bibliographer.options.sort.nil?

      filtered_db = prepare_filtered_db bibliographer
      formatter.import filtered_db
      formatter.force_sort!(mode: :bibliography)

      formatter
    end

    def prepare_filtered_db(bibliographer)
      bibliographer.occurring_keys[target].
        map { |id| bibliographer.database.find_entry_by_id(id) }.
        map { |entry| prepare_entry_metadata bibliographer, entry }
    end

    def prepare_entry_metadata(bibliographer, entry)
      entry.
        merge('citation-number': bibliographer.appearance_index_of(target, entry["id"])).
        merge('citation-label': entry["id"]) # TODO: smart label generators
    end

    def anchor_id(*fragments)
      fragments.compact.join("-")
    end
  end
end
