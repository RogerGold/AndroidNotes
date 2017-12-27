#MLTK学习笔记

## NLTK是什么？
[NLTK](http://www.nltk.org/index.html) is a leading platform for building Python programs to work with human language data.
It provides easy-to-use interfaces to over 50 corpora and lexical resources such as WordNet, 
along with a suite of text processing libraries for classification, tokenization, stemming,
tagging, parsing, and semantic reasoning, wrappers for industrial-strength NLP libraries, 
and an active discussion forum.

NLTK全称是Natural Language Toolkit，一个自然语言处理工具包，提供了易于使用的接口，超过50的语料库和词汇资源，如WordNet，连同一套文本处理库的分类、标记等功能。

## NLTK历史
NLTK 创建于2001 年，最初是宾州大学计算机与信息科学系计算语言学课程的一部分。从那以后，在数十名贡献者的帮助下不断发展壮大。如今，它已被几十所大学的课程所采纳，
并作为许多研究项目的基础。

## Ubuntu环境下安装
如果没有安装python和pip，安装pip：

    $ sudo apt-get install python-pip python-dev build-essential
    $ sudo pip install --upgrade pip
    $ sudo pip install --upgrade virtualenv

安装NLTK和numpy：

    Install NLTK: run sudo pip install -U nltk
    Install Numpy (optional): run sudo pip install -U numpy
    
为了画出一些示例中的图形，还需要安装Matplotlib 包,这是一个用于数据可视化的2D绘图库：

    Install Matplotlib: run sudo pip install Matplotlib.org
    
安装一些测试的数据，运行脚本，然后选择book，下载：

   >>> import nltk
   >>> nltk.download()
   
检测一下，在输出欢迎信息之后，将会加载下载的book的文本
    #加载一些我们刚才下载的book：
   >>> from nltk.book import *
   
其他平台参考[install](http://www.nltk.org/install.html)   


