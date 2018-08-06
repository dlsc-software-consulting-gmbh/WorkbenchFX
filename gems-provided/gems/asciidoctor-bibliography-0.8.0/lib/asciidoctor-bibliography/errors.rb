module AsciidoctorBibliography
  module Errors
    class Error < StandardError; end

    module Options
      class Missing < Error; end
      class Invalid < Error; end
    end

    module Database
      class UnsupportedFormat < Error; end
      class FileNotFound < Error; end
      class IdNotFound < Error; end
      class ConflictingIds < Error; end
    end
  end
end
