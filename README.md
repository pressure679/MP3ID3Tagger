# MP3ID3Tagger
Some code to tag mp3 files with artist, album and title using directories' and file's name.
You directory and file setup should be one of the following, depending if the track is a non-album track or not (for Windows HOME is C:\Users\<username>, and on Ubuntu and other linux-based operating system, /home/<username>):
The file can be run from any directory, it looks up the Music directory by looking up the HOME environment variable.

<HOME>/Music/<Artist>/<Album>/<Title>
<HOME>/Music/<Artist>/<Title>

The file is compiled by using the Java JDK 7 or later.

Dependency:
The code depends on https://github.com/mpatric/mp3agic, and when you have compiled it as specified on that page you put it in your CLASSPATH (optionally CLASSPATH/ext), in Windows 64-bit it is C:\Program Files\Java\<JDK version>\lib (optionally \ext).
In Ubuntu or another linux-based operating system the CLASSPATH should be as following: /usr/lib/jvm/<JDK version>/lib (optionally /ext).

How to compile and run:
- Compile
  javac sample.java

- Run
  java -cp . sample
