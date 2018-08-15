# coding: utf-8

require "asciidoctor-bibliography"
require_relative "../../../citation_helper"


describe CiteProc::Ruby::Formats::Adoc do
  before { processor.import(BibTeX.open("spec/fixtures/database.bib").to_citeproc) }

  context "using apa citations" do
    let(:processor) { CiteProc::Processor.new(style: "apa", format: "adoc") }
    let(:year) { processor.engine.style.citation.children["layout"].children[0].children[1] }
    let(:rendered_citation) { processor.render(:citation, id: "Gettier63") }

    describe ".apply_font_style" do
      it "makes journal italic" do
        year["font-style"] = "italic"
        expect(rendered_citation).to eq("(Gettier, _1963_)")
      end
    end

    describe ".apply_font_weight" do
      it "makes journal bold" do
        year["font-weight"] = "bold"
        expect(rendered_citation).to eq("(Gettier, *1963*)")
      end
    end

    describe ".apply_vertical_align" do
      it "makes journal supscript " do
        year["vertical-align"] = "sup"
        expect(rendered_citation).to eq("(Gettier, ^1963^)")
      end

      it "makes journal subscript " do
        year["vertical-align"] = "sub"
        expect(rendered_citation).to eq("(Gettier, ~1963~)")
      end
    end
  end

  context "using ieee bibliography" do
    let(:processor) { CiteProc::Processor.new(style: "ieee", format: "adoc") }
    let(:rendered_bibliography) { processor.render(:bibliography, id: "Gettier63").first }

    describe ".apply_suffix" do
      before { processor.import(BibTeX.open("spec/fixtures/database.bib").to_citeproc) }
      it "adds space between first and second field" do
        processor.items["Gettier63"].merge("citation-number": "0")
        expect(rendered_bibliography).to match(/^\[0\] E. L. Gettier/)
      end
    end
  end
end
