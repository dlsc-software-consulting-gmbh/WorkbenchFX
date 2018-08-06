# coding: utf-8

require_relative "../../citation_helper"

describe "citet* macro with numeric style" do
  let(:options) { { "bibliography-tex-style" => "numeric", "bibliography-database" => "database.bib" } }

  it "formats a single citation" do
    expect(formatted_citation("citet*:[Erdos65]", options: options)).
      to eq "Erdős, Heyting, and Brouwer [1]"
  end

  it "formats a grouped citation" do
    expect(formatted_citation("citet*:[Erdos65]+[Einstein35]", options: options)).
      to eq "Erdős, Heyting, and Brouwer [1], Einstein, Podolsky, and Rosen [2]"
  end

  it "formats a single citation with a prefix" do
    expect(formatted_citation("citet*:[Erdos65, prefix=see]", options: options)).
      to eq "Erdős, Heyting, and Brouwer [see 1]"
  end

  it "formats a single citation with a suffix" do
    expect(formatted_citation("citet*:[Erdos65, suffix=new edition]", options: options)).
      to eq "Erdős, Heyting, and Brouwer [1, new edition]"
  end

  it "formats a single citation with both a prefix and a suffix" do
    expect(formatted_citation("citet*:[Erdos65, prefix=see, suffix=new edition]", options: options)).
      to eq "Erdős, Heyting, and Brouwer [see 1, new edition]"
  end

  it "formats a single citation with a standard locator" do
    expect(formatted_citation("citet*:[Erdos65, page=41-43]", options: options)).
      to eq "Erdős, Heyting, and Brouwer [1, pp. 41-43]"
  end

  it "formats a single citation with a custom locator" do
    expect(formatted_citation("citet*:[Erdos65, locator=somewhere]", options: options)).
      to eq "Erdős, Heyting, and Brouwer [1, somewhere]"
  end
end
