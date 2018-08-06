module AsciidoctorBibliography
  class Bibliographer
    attr_accessor :citations
    attr_accessor :indices
    attr_accessor :database
    attr_reader :occurring_keys
    attr_accessor :options

    def initialize
      @options = {}
      @citations = []
      @indices = []
      @database = nil
      @occurring_keys = {}
    end

    def add_citation(citation)
      citations << citation
      citation.citation_items.group_by(&:target).each do |target, citation_items|
        @occurring_keys[target] ||= []
        @occurring_keys[target].concat(citation_items.map(&:key)).uniq!
      end
    end

    def appearance_index_of(target, id)
      @occurring_keys[target].index(id) + 1
    end
  end
end
