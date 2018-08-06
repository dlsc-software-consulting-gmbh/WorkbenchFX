# coding: utf-8

require_relative "../../citation_helper"

describe "cite macro with rfc-v2 style" do
  let(:options) { { "bibliography-style" => "rfc-v2", "bibliography-database" => "database.rfc.xml", "bibliography-passthrough" => "true", "bibliography-prepend-empty" => "false" } }

  it "formats a single citation" do
    expect(formatted_citation("cite:[RFC2119]", options: options)).
      to eq '+++<xref target="RFC2119"/>+++'
  end

  it "formats a single citation with locator" do
    expect(formatted_citation("cite:[RFC2119, section=1.2.3]", options: options)).
      to eq '+++<xref target="RFC2119" section="1.2.3"/>+++'
  end

  it "formats a single bibliography entry" do
    expect(formatted_bibliography("cite:[RFC2119]", options: options)).
      to eq '+++<reference anchor="RFC2119" target="https://www.rfc-editor.org/info/rfc2119"><front><title>Key words for use in RFCs to Indicate Requirement Levels</title><author initials="S." surname="Bradner" fullname="S. Bradner"><organization/></author><date year="1997" month="March"/><abstract><t>In many standards track documents several words are used to signify the requirements in the specification. These words are often capitalized. This document defines these words as they should be interpreted in IETF documents. This document specifies an Internet Best Current Practices for the Internet Community, and requests discussion and suggestions for improvements.</t></abstract></front><seriesInfo name="BCP" value="14"/><seriesInfo name="RFC" value="2119"/><seriesInfo name="DOI" value="10.17487/RFC2119"/></reference>+++'
  end
end
