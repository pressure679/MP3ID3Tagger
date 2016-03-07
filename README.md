# MP3ID3Tagger

# Java version
- MP3 files stutter when tagged with this version, probably cause it detects id3v2 version and only set tags in one of the id3v2 versions. Golang version detects id3v# and set the tags to that version automatically.
Some code to tag mp3 files with artist, album and title using directories' and file's name.
You directory and file setup should be one of the following, depending if the track is a non-album track or not (for Windows HOME is C:\Users\<username>, and on Ubuntu and other linux-based operating system, /home/<username>):
The file can be run from any directory, it looks up the Music directory by looking up the HOME environment variable.

Also, the Mp3File.class in the mp3agic-0.8.5-SNAPSHOT.jar have been replaced with the one from my directory to save mp3 files in a custom directory instead of the same directory as the sample.class' directory (You can see some code at line 420 have been commented out).
You should compile my Mp3File.java using this command:

javac Mp3File.java

When you have the mp3agic-0.8.5-SNAPSHOT.jar file for mp3agic, open it with a file compression program like WinRar Windows or Archive Manager for Ubuntu and put the Mp3File.class in the mp3agic-0.8.5-SNAPSHOT.jar's com/mpatric/mp3agic/Mp3File directory.


The file is compiled using the Java JDK 7 or later.

Dependency:
The code depends on https://github.com/mpatric/mp3agic, and when you have compiled it as specified on that page you put it in your CLASSPATH (optionally CLASSPATH/ext), in Windows 64-bit it is C:\Program Files\Java\<JDK version>\lib (optionally \ext).
In Ubuntu or another linux-based operating system the CLASSPATH should be as following: /usr/lib/jvm/<JDK version>/lib (optionally /ext).

How to compile and run:
- Compile
  javac sample.java

- Run
  java -cp . sample

# Golang version
Depends on https://github.com/wtolson/go-taglib - See installation in that repository.
Also, in this version the directory tree has to be like this:
- Windows
  C:\Users\<User>\Music\<Genre>\<Artist>\<Song/Album>\<Song>
  or for non-album tracks
  C:\Users\<User>\Music\<Genre>\<Artist>\<Song>
- Ubuntu
  /home/<User>\Music\<Genre>\<Artist>\<Song/Album>\<Song>
  or for non-album tracks
  /home/<User>\Music\<Genre>\<Artist>\<Song>
- Compile
  go build sample.go

- Run
  ./sample
