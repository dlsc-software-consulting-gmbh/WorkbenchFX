# coding: utf-8

require_relative "citation_helper"

describe "cite macro with apa style" do
  it "formats a complex citation" do
    expect(formatted_citation("cite:[Erdos65, prefix=see]+[Einstein35, page=41-43]",
                              options: { "bibliography-style" => "apa",
                                         "bibliography-database" => "database.bib",
                                         "bibliography-hyperlinks" => "true" })).
      to eq "(xref:bibliography-default-Einstein35[Einstein, Podolsky, & Rosen, 1935, pp. 41-43]; " +
      "xref:bibliography-default-Erdos65[seeErdős, Heyting, & Brouwer, 1965])"
  end
end

describe "cite macro with arbitrary interpolated text" do
  it "formats a complex citation" do
    expect(formatted_citation("cite:[Erdos65, text=foo {cite} bar]",
                              options: { "bibliography-style" => "apa",
                                         "bibliography-database" => "database.bib",
                                         "bibliography-hyperlinks" => "true" })).
      to eq "(xref:bibliography-default-Erdos65[foo Erdős, Heyting, & Brouwer, 1965 bar])"
  end
end

describe "fullcite macro with apa style" do
  it "formats a complex citation" do
    expect(formatted_citation("fullcite:[Erdos65]",
                              options: { "bibliography-style" => "apa",
                                         "bibliography-database" => "database.bib" })).
      to eq "Erdős, P., Heyting, A., & Brouwer, L. E. (1965). Some very hard sums. _Difficult Maths Today_, 30."
  end
end
