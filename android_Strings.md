# String Theory
Keeping your labels and other bits of text outside the main source code of your
application is generally considered to be a very good idea. In particular, it helps with
internationalization (I18N) and localization (L10N). Even if you are not going to
translate your strings to other languages, it is easier to make corrections if all the
strings are in one spot instead of scattered throughout your source code.

### Plain Strings
Generally speaking, all you need to do is have an XML file in the res/values
directory (typically named res/values/strings.xml), with a resources root
element, and one child string element for each string you wish to encode as a
resource. The string element takes a name attribute, which is the unique name for
this string, and a single text element containing the text of the string:

    <resources>
    <string name="quick">The quick brown fox...</string>
    <string name="laughs">He who laughs last...</string>
    </resources>
    
### Styled Text
Many things in Android can display rich text, where the text has been formatted
using some lightweight HTML markup: <b>, <i>, and <u>. Your string resources
support this, simply by using the HTML tags as you would in a Web page:

    <resources>
    <string name="b">This has <b>bold</b> in it.</string>
    <string name="i">Whereas this has <i>italics</i>!</string>
    </resources>
### CDATA. CDATA Run. Run, DATA, Run.
Since a strings resource XML file is an XML file, if your message contains <, >, or &
characters (other than the formatting tags listed above), you will need to use a CDATA
section:
    <string name="report_body">
    <![CDATA[
    <html>
    <body>
    <h1>TPS Report for: {{reportDate}}</h1>
    <p>Here are the contents of the TPS report:</p>
    <p>{{message}}</p>
    <p>If you have any questions regarding this report, please
    do <b>not</b> ask Mark Murphy.</p>
    </body>
    </html>
    ]]>
    </string>
