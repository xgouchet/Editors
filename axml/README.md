
# About AXML

AXML is a library designed to parse binary Android XML files (ie : XML files compressed by the Android AAPT tool).
This library was first designed to be used inside the Axel app, available on [Google Play](https://play.google.com/store/apps/details?id=fr.xgouchet.xmleditor).

## Usage

The parser uses any InputStream and can be used with a SAX style listener, or can return a DOM representation of the XML document.

The following code requires an implementation of the AXMLParser.Listener interface (which is quite similar to a SaxListener interface) :

```java
File file = ...
AXMLParser.Listener listener = ...
new AXMLParser().parse(new FileInputStream(file), listener);
```

The following code will return the root of a DOM tree :

```java
File file = ...
Document doc = new AXMLParser().parseDOM(new FileInputStream(file));
```

