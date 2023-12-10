<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/ULA7Z2_kurzusfelvetel">
        <html>
            <body>
                <div>
                    <h2><xsl:value-of select="hallgato/hnev" /> (<xsl:value-of select="hallgato/@id"/>) kurzusfelvétel - 2023/24. I. félév</h2>
                    <ul>
                        <li>születési év: <xsl:value-of select="hallgato/szulev"/></li>
                        <li>szak: <xsl:value-of select="hallgato/szak"/></li>
                        <li><xsl:value-of select="hallgato/szak/@evf"/>. évfolyam</li>
                    </ul>
                </div>
                <div>
                    <table border="3" bgcolor="#c8e3fa">
                        <tr bgcolor="#2b5087" style="color:#ffffff">
                            <th>Kurzus neve (ID)</th>
                            <th>jóváhagyás</th>
                            <th>kredit</th>
                            <th>hely</th>
                            <th>időpont</th>
                            <th>tanárok</th>
                        </tr>
                    <xsl:for-each select="kurzusok/kurzus">
                        <tr>
                            <td><xsl:value-of select="kurzusnev"/> (<xsl:value-of select="@id"/>)</td>
                            <td><xsl:value-of select="@jóváhagyás"/></td>
                            <td><xsl:value-of select="kredit"/></td>
                            <td><xsl:value-of select="hely"/></td>
                            <td><xsl:value-of select="idopont"/></td>
                            <td>
                                <xsl:for-each select="oktató">
                                    <xsl:value-of select="."/><br />
                                </xsl:for-each>
                                <xsl:for-each select="óraadó">
                                    <xsl:value-of select="."/><br />
                                </xsl:for-each>
                            </td>
                        </tr>
                    </xsl:for-each>
                    </table>
                </div>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>