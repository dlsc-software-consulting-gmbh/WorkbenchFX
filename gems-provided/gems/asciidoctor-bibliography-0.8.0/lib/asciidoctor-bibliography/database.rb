require_relative "databases/bibtex"
require_relative "databases/rfc"
require_relative "errors"

module AsciidoctorBibliography
  # This is an array of citeproc entries.
  class Database < Array
    def initialize(*filepaths)
      filepaths.each do |filepath|
        append filepath
      end
    end

    def append(filepath)
      concat Database.load(filepath)
      ensure_no_conflicts!
      self
    end

    def find_entry_by_id(id)
      result = detect { |entry| entry["id"] == id }
      if result.nil?
        message = "No entry with id '#{id}' was found in the bibliographic database."
        raise Errors::Database::IdNotFound, message
      end
      result
    end

    def self.load(filepath)
      raise Errors::Database::FileNotFound, filepath unless File.exist?(filepath)

      fileext = File.extname filepath
      case fileext
      when *Databases::BibTeX::EXTENSIONS
        Databases::BibTeX.load filepath
      when *Databases::RFC::EXTENSIONS
        Databases::RFC.load filepath
      else
        raise Errors::Database::UnsupportedFormat, fileext
      end
    end

    private

    def ensure_no_conflicts!
      ids = map { |entry| entry["id"] }
      conflicting_ids = ids.select { |id| ids.count(id) > 1 }.uniq.sort
      raise Errors::Database::ConflictingIds, <<~MESSAGE if conflicting_ids.any?
        Conflicting ids were found during database import: #{conflicting_ids}.
      MESSAGE
    end
  end
end
