<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				version="1.0">
	<xsl:output method="text" encoding="UTF-8" />
	<xsl:variable name="delimiter">,</xsl:variable>
	<xsl:template match="creditCheck">
        <xsl:value-of select="@name"/>
        <xsl:value-of select="$delimiter"/>
        <xsl:value-of select="@ssn"/>
		<xsl:value-of select="$delimiter"/>
        <xsl:value-of select="@address"/>
		<xsl:value-of select="$delimiter"/>
        <xsl:value-of select="@employerName"/>
		<xsl:value-of select="$delimiter"/>
        <xsl:value-of select="@salary"/>
		<xsl:value-of select="$delimiter"/>
        <xsl:value-of select="@loanAmount"/>
		<xsl:value-of select="$delimiter"/>
        <xsl:value-of select="@loanDuration"/>
		<xsl:value-of select="$delimiter"/>
        <xsl:value-of select="@email"/>
		<xsl:value-of select="$delimiter"/>
        <xsl:value-of select="@creditScore"/>
    </xsl:template>
</xsl:stylesheet>