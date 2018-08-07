# coding: utf-8

require_relative "../../citation_helper"

describe "citeyear macro with authoryear style" do
  let(:options) { { "bibliography-tex-style" => "authoryear", "bibliography-database" => "database.bib" } }

  it "formats a single citation" do
    expect(formatted_citation("citeyear:[Erdos65]", options: options)).
      to eq "1965"
  end

  it "formats a grouped citation" do
    expect(formatted_citation("citeyear:[Erdos65]+[Einstein35]", options: options)).
      to eq "1965; 1935"
  end

  it "formats a single citation with a prefix" do
    expect(formatted_citation("citeyear:[Erdos65, prefix=see]", options: options)).
      to eq "see 1965"
  end

  it "formats a single citation with a suffix" do
    expect(formatted_citation("citeyear:[Erdos65, suffix=new edition]", options: options)).
      to eq "1965, new edition"
  end

  it "formats a single citation with both a prefix and a suffix" do
    expect(formatted_citation("citeyear:[Erdos65, prefix=see, suffix=new edition]", options: options)).
      to eq "see 1965, new edition"
  end

  it "formats a single citation with a standard locator" do
    expect(formatted_citation("citeyear:[Erdos65, page=41-43]", options: options)).
      to eq "1965, pp. 41-43"
  end

  it "formats a single citation with a custom locator" do
    expect(formatted_citation("citeyear:[Erdos65, locator=somewhere]", options: options)).
      to eq "1965, somewhere"
  end
end
