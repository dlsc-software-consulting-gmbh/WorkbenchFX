# Extends Document to support additional method
module AsciidoctorBibliography
  module Asciidoctor
    module DocumentExt
      # All our document-level permanence passes through this accessor.
      def bibliographer
        @bibliographer ||= AsciidoctorBibliography::Bibliographer.new
      end
    end
  end
end
