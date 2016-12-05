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
    
  how to use:
  in xml:
     @string/quick
  in code:
   String s = getResources().getString(R.string.quick);
   
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
    
### The Directory Name
Our string resources in our stub project are in the res/values/strings.xml file.
Since this directory name (values) has no suffixes, the string resources in that
directory will be valid for any sort of situation, including any locale for the device.
We will need additional directories, with distinct strings.xml files, to support other
languages.

  - zh_cn: 简体中文
  - zh_hk: 繁体中文(中国香港)   
  - zh_tw: 繁体中文(中国台湾地区)
  - en-hk: 英语(香港)
  - en_us: 英语(美国)
  - en_gb: 英语(英国)
  - en_ww: 英语(全球)
  - ja_jp: 日语(日本)
  - ko_kr: 韩文(韩国)
