describe AsciidoctorBibliography::CitationItem do
  describe ".new" do
    it "can be mutely initialized" do
      expect { described_class.new }.to_not raise_exception
    end

    it "can be initialized with a block operating on itself" do
      itself = nil
      expect(described_class.new { |ci| itself = ci }).to be(itself)
    end
  end

  describe "#parse_attribute_list" do
    subject { described_class.new }

    before do
      subject.parse_attribute_list "foo, lol=bar, baz, qux, zod=13"
    end

    it "treats the first positional attribute as the id" do
      expect(subject.key).to eq "foo"
    end

    it "extracts the positional attributes in order, except the first one" do
      expect(subject.positional_attributes).to eq ["baz", "qux"]
    end

    it "extracts all named attributes" do
      expect(subject.named_attributes).to eq("lol" => "bar", "zod" => "13")
    end
  end

  describe "#locators" do
    subject { described_class.new }

    it "returns no locator if none are present" do
      subject.parse_attribute_list "foo, lol=bar, baz, qux, zod=42"
      expect(subject.locator).to be_nil
    end

    it "recognizes all CSL locators" do
      locators = %w[book chapter column figure folio issue line note opus
                    page paragraph part section sub-verbo verse volume]
      locators_hash = locators.map { |l| [l, rand(10).to_s] }.to_h
      locators_string = locators_hash.to_a.map { |a| a.join "=" }.join(", ")

      subject.parse_attribute_list "foo, #{locators_string}"
      expect(subject.locators).to eq locators_hash
    end

    it "recognizes non standard locator" do
      subject.parse_attribute_list "foo, locator=' somewhere'"
      expect(subject.locators).to eq("locator" => " somewhere")
    end
  end

  describe "#locator" do
    subject { described_class.new }

    it "returns the first locator if existing" do
      subject.parse_attribute_list("foo, page=42, locator=bar, chapter=24")
      expect(subject.locator).to eq(%w[page 42])
    end

    it "returns nil if no loctor exist" do
      subject.parse_attribute_list("foo, bar, zod=quz")
      expect(subject.locator).to be_nil
    end
  end

  describe "#prefix" do
    subject { described_class.new }

    it "returns the prefix if it exist" do
      subject.parse_attribute_list("foo, prefix=bar")
      expect(subject.prefix).to eq("bar")
    end

    it "returns nil if no prefix exists" do
      subject.parse_attribute_list("foo, bar, zod=quz")
      expect(subject.prefix).to be_nil
    end
  end

  describe "#suffix" do
    subject { described_class.new }

    it "returns the suffix if it exist" do
      subject.parse_attribute_list("foo, suffix=bar")
      expect(subject.suffix).to eq("bar")
    end

    it "returns nil if no suffix exists" do
      subject.parse_attribute_list("foo, bar, zod=quz")
      expect(subject.suffix).to be_nil
    end
  end

  describe "#text" do
    subject { described_class.new }

    it "returns the text if it exist" do
      subject.parse_attribute_list("foo, text='prefix {ref} suffix'")
      expect(subject.text).to eq("prefix {ref} suffix")
    end

    it "returns nil if no text exists" do
      subject.parse_attribute_list("foo, bar, zod=quz")
      expect(subject.text).to be_nil
    end
  end
end
