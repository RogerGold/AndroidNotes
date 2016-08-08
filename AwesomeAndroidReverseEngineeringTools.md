# 常用的Android逆向工具

### 1. [SMALI/BAKSMALI](https://github.com/JesusFreke/smali)

smali/baksmali is an assembler/disassembler for the dex format used by dalvik, Android’s Java VM implementation. The syntax is loosely based on Jasmin’s/dedexer’s syntax, and supports the full functionality of the dex format (annotations, debug info, line info, etc.)

### 2. [ANDBUG](https://github.com/swdunlop/AndBug)

AndBug is a debugger targeting the Android platform’s Dalvik virtual machine intended for reverse engineers and developers. It uses the same interfaces as Android’s Eclipse debugging plugin, the Java Debug Wire Protocol (JDWP) and Dalvik Debug Monitor (DDM) to permit users to hook Dalvik methods, examine process state, and even perform changes.

### 3. [ANDROGUARD](https://github.com/androguard/androguard)

Androguard is a full python tool to play with Android files.

- DEX, ODEX
- APK
- Android’s binary xml
- Android resources
- Disassemble DEX/ODEX bytecodes
- Decompiler for DEX/ODEX files

### 4. [APKTOOL](https://ibotpeaches.github.io/Apktool/)

A tool for reverse engineering 3rd party, closed, binary Android apps. It can decode resources to nearly original form and rebuild them after making some modifications; it makes possible to debug smali code step by step. Also it makes working with an app easier because of project-like file structure and automation of some repetitive tasks like building apk, etc.

Features:

- Disassembling resources to nearly original form (including resources.arsc, classes.dex, 9.png. and XMLs)
- Rebuilding decoded resources back to binary APK/JAR
- Organizing and handling APKs that depend on framework resources
- Smali Debugging (Removed in 2.1.0 in favor of IdeaSmali)
- Helping with repetitive tasks

### 5. [ANDROID FRAMEWORK FOR EXPLOITATION](https://github.com/appknox/AFE)

Android Framework for Exploitation is a framework for exploiting android based devices and applications.

### 6. [BYPASS SIGNATURE AND PERMISSION CHECKS FOR IPCS](https://github.com/iSECPartners/Android-KillPermAndSigChecks)

This tool leverages Cydia Substrate to bypass signature and permission checks for IPCs.

### 7. [ANDROID OPENDEBUG](https://github.com/iSECPartners/Android-OpenDebug)

This tool leverages Cydia Substrate to make all applications running on the device debuggable; once installed any application will let a debugger attach to them.

### 8. [DARE](http://siis.cse.psu.edu/dare/index.html)

Dare is a project which aims at enabling Android application analysis. The Dare tool retargets Android applications in .dex or .apk format to traditional .class files. These .class files can then be processed by existing Java tools, including decompilers. Thus, Android applications can be analyzed using a vast range of techniques developed for traditional Java applications.

### 9. [DEX2JAR](https://github.com/pxb1988/dex2jar)

Tools to work with android .dex and java .class files.

### 10. [ENJARIFY](https://github.com/google/enjarify)

Enjarify is a tool for translating Dalvik bytecode to equivalent Java bytecode. This allows Java analysis tools to analyze Android applications.

### 11. [DEDEXER](http://dedexer.sourceforge.net/)

Dedexer is a disassembler tool for DEX files. DEX is a format introduced by the creators of the Android platform. The format and the associated opcode set is in distant relationship with the Java class file format and Java bytecodes. Dedexer is able to read the DEX format and turn into an “assembly-like format”. This format was largely influenced by the Jasmin syntax but contains Dalvik opcodes. For this reason, Jasmin is not able to compile the generated files.

### 12. [FINO](https://github.com/sysdream/fino)

An Android Dynamic Analysis Tool.

### 13. [JAD](http://varaneckas.com/jad/)

Jad is a Java decompiler.

### 14. [JD-GUI](https://github.com/java-decompiler/jd-gui)

JD-GUI is a standalone graphical utility that displays Java source codes of “.class” files. You can browse the reconstructed source code with the JD-GUI for instant access to methods and fields.

### 15. [BYTECODE VIEWER](https://github.com/Konloch/bytecode-viewer)

Bytecode Viewer is an Advanced Lightweight Java Bytecode Viewer, GUI Java Decompiler, GUI Bytecode Editor, GUI Smali, GUI Baksmali, GUI APK Editor, GUI Dex Editor, GUI APK Decompiler, GUI DEX Decompiler, GUI Procyon Java Decompiler, GUI Krakatau, GUI CFR Java Decompiler, GUI FernFlower Java Decompiler, GUI DEX2Jar, GUI Jar2DEX, GUI Jar-Jar, Hex Viewer, Code Searcher, Debugger and more.

It’s written completely in Java, and it’s open sourced. It’s currently being maintained and developed by Konloch.

There is also a plugin system that will allow you to interact with the loaded classfiles, for example you can write a String deobfuscator, a malicious code searcher, or something else you can think of.

You can either use one of the pre-written plugins, or write your own. It supports groovy scripting. Once a plugin is activated, it will execute the plugin with a ClassNode ArrayList of every single class loaded in BCV, this allows the user to handle it completely using ASM.