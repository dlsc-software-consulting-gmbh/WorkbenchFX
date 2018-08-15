require "asciidoctor"

describe AsciidoctorBibliography::Options do
  describe "#database" do
    it "has no default" do
      expect { described_class.new.database }.
        to raise_exception AsciidoctorBibliography::Errors::Options::Missing
    end

    it "returns the provided database name" do
      expect(described_class.new.merge("bibliography-database" => "foobar").database).to eq("foobar")
    end
  end

  describe "#prepend_empty?" do
    it "defaults to true" do
      expect(described_class.new.prepend_empty?(:citation)).to be true
      expect(described_class.new.prepend_empty?(:reference)).to be true
    end

    it "returns true when provided option is true" do
      expect(described_class.new.merge("bibliography-prepend-empty" => "true").
               prepend_empty?(:citation)).to be true
      expect(described_class.new.merge("bibliography-prepend-empty" => "true").
               prepend_empty?(:reference)).to be true
    end

    it "returns true for citations when provided option is citations" do
      expect(described_class.new.merge("bibliography-prepend-empty" => "citations").
               prepend_empty?(:citation)).to be true
      expect(described_class.new.merge("bibliography-prepend-empty" => "citations").
               prepend_empty?(:reference)).to be false
    end

    it "returns true for references when provided option is references" do
      expect(described_class.new.merge("bibliography-prepend-empty" => "references").
               prepend_empty?(:citation)).to be false
      expect(described_class.new.merge("bibliography-prepend-empty" => "references").
               prepend_empty?(:reference)).to be true
    end

    it "returns false when provided option is false" do
      expect(described_class.new.merge("bibliography-prepend-empty" => "false").
               prepend_empty?(:citation)).to be false
      expect(described_class.new.merge("bibliography-prepend-empty" => "false").
               prepend_empty?(:reference)).to be false
    end

    it "raises an error when provided option is invalid" do
      expect do
        described_class.new.merge("bibliography-prepend-empty" => "foo").
          prepend_empty?(:citation)
      end.
        to raise_exception AsciidoctorBibliography::Errors::Options::Invalid
      expect do
        described_class.new.merge("bibliography-prepend-empty" => "foo").
          prepend_empty?(:reference)
      end.
        to raise_exception AsciidoctorBibliography::Errors::Options::Invalid
    end
  end

  describe "#hyperlinks?" do
    it "defaults to true" do
      expect(described_class.new.hyperlinks?).to be true
    end

    it "returns true when provided option is true" do
      expect(described_class.new.merge("bibliography-hyperlinks" => "false").hyperlinks?).to be false
    end

    it "returns true when provided option is true" do
      expect(described_class.new.merge("bibliography-hyperlinks" => "true").hyperlinks?).to be true
    end

    it "raises an error when provided option is invalid" do
      expect { described_class.new.merge("bibliography-hyperlinks" => "foo").hyperlinks? }.
        to raise_exception AsciidoctorBibliography::Errors::Options::Invalid
    end
  end

  describe "#locale" do
    it "defaults to en-US" do
      expect(described_class.new.locale).to eq "en-US"
    end

    it "returns the provided option when set" do
      expect(described_class.new.merge("bibliography-locale" => "it-IT").locale).to eq "it-IT"
    end

    it "raises an error when provided option is invalid" do
      expect { described_class.new.merge("bibliography-locale" => "foo").locale }.
        to raise_exception AsciidoctorBibliography::Errors::Options::Invalid
    end
  end

  describe "#tex_style" do
    it "defaults to en-US" do
      expect(described_class.new.tex_style).to eq "authoryear"
    end

    it "returns the provided option when set" do
      expect(described_class.new.merge("bibliography-tex-style" => "numeric").tex_style).to eq "numeric"
    end

    it "raises an error when provided option is invalid" do
      expect { described_class.new.merge("bibliography-tex-style" => "foo").tex_style }.
        to raise_exception AsciidoctorBibliography::Errors::Options::Invalid
    end
  end

  describe "#style" do
    it "defaults to apa" do
      expect(described_class.new.style).to eq "apa"
    end

    it "returns the provided style name" do
      expect(described_class.new.merge("bibliography-style" => "foobar").style).to eq("foobar")
    end
  end

  describe "#sort" do
    it "defaults to nil" do
      expect(described_class.new.sort).to be nil
    end

    it "parses and returns an empty array" do
      expect(described_class.new.merge("bibliography-sort" => "[]").sort).
        to eq([])
    end

    it "parses and returns a naked hash" do
      expect(described_class.new.merge("bibliography-sort" => "macro: author").sort).
        to eq([{ "macro" => "author" }])
    end

    it "parses and returns a hash" do
      expect(described_class.new.merge("bibliography-sort" => "{macro: author, sort: descending}").sort).
        to eq([{ "macro" => "author", "sort" => "descending" }])
    end

    it "parses and returns multiple hashes" do
      expect(described_class.new.merge("bibliography-sort" => "[{macro: author}, {variable: issued}]").sort).
        to eq([{ "macro" => "author" }, { "variable" => "issued" }])
    end

    it "raises an error when provided option is invalid (type)" do
      expect { described_class.new.merge("bibliography-sort" => "foo").sort }.
        to raise_exception AsciidoctorBibliography::Errors::Options::Invalid
    end

    it "raises an error when provided option is invalid (key)" do
      expect { described_class.new.merge("bibliography-sort" => "[{something: wrong}]").sort }.
        to raise_exception AsciidoctorBibliography::Errors::Options::Invalid
    end

    it "raises an error when provided option is invalid (syntax)" do
      expect { described_class.new.merge("bibliography-sort" => "foo: bar:").sort }.
        to raise_exception AsciidoctorBibliography::Errors::Options::Invalid
    end
  end

  describe ".build" do
    let(:document) do
      ::Asciidoctor::Document.new.tap do |doc|
        # NOTE: these attributes would come from CLI
        doc.attributes.merge! "bibliography-database" => "high_priority"
      end
    end

    let(:reader) do
      ::Asciidoctor::PreprocessorReader.new(document, <<~SOURCE.lines)
        = This is the document title
        :bibliography-database: foo
        :bibliography-locale: bar
        :bibliography-style: baz
        :bibliography-hyperlinks: quz
        :bibliography-order: zod
        :bibliography-tex-style: lep
        :bibliography-sort: kan
        :bibliography-bogus: pow
      SOURCE
    end

    subject { described_class.build document, reader }

    it "extracts all bibliography options ignoring others and includes CLI attributes" do
      expect(subject).to eq("bibliography-database" => "high_priority",
                            "bibliography-locale" => "bar",
                            "bibliography-style" => "baz",
                            "bibliography-hyperlinks" => "quz",
                            "bibliography-order" => "zod",
                            "bibliography-tex-style" => "lep",
                            "bibliography-sort" => "kan")
    end

    it "acts non-destructively on reader" do
      expect { subject }.to_not(change { reader.lines })
      expect { subject }.to_not(change { reader.cursor.lineno })
    end
  end
end
