# coding: utf-8

require "nokogiri"

module AsciidoctorBibliography
  module Databases
    module RFC
      EXTENSIONS = %w[.rfc .xml].freeze

      MONTHS = %w{january february march april may june july
                  august september october november december}.freeze

      def self.load(filename)
        ::Nokogiri::XML(File.open(filename)).
          xpath("//reference").
          map { |reference_tag| tag_to_citeproc reference_tag }
      end

      def self.tag_to_citeproc(reference_tag)
        {
          "id" => reference_tag.attr("anchor"),
          "author" => get_author_list(reference_tag),
          "title" => reference_tag.xpath("//title").first&.text&.strip,
          "issued" => {
            "date-parts" => get_date_parts(reference_tag),
          },
          # NOTE: we keep the original XML to re-render it when needed
          "note" => reference_tag.to_xml,
        }
      end

      def self.get_date_parts(reference_tag)
        date_tag = reference_tag.xpath("//date").first
        year = date_tag&.attr("year")
        month = date_tag&.attr("month")
        day = date_tag&.attr("day")
        month = MONTHS.index(month.downcase) + 1 unless month.nil?
        [year, month, day].take_while { |date_part| !date_part.nil? }.map(&:to_s)
      end

      def self.get_author_list(reference_tag)
        author_tags = reference_tag.xpath("//author")
        author_tags.map do |author_tag|
          {
            "family" => author_tag&.attr("surname"),
            "given" => author_tag&.attr("initials"),
          }
        end
      end
    end
  end
end
