# Crunchy

A simplified, iterative Java implementation of the Linux crunch tool.

Standard crunch is well-written, however it's also

 - implemented recursively,
 - requires a particular alphabet input format,
 - and written entirely in an (arguably) hard-to-read single, sprawling, vanilla C sourcefile.

While crunch is highly optimized, this iterative Java implementation competes against it quite admirably.

This Java version

 - adds about 1.5s to launch due to JVM overhead (but otherwise they scale comparably with only milliseconds in timing differences at numerous inputs tested),
 - does not print any statistics at launch nor does it have the 3 second delay prior to go,
 - and does not currently implement most of the various options available w/standard crunch.

My thoughts in writing this were that such a simple and fast implementation can be easily modified to suite your needs where crunch may have failed you.
I hope somebody finds it useful.

## Usage

```

# compile crunchy
javac Crunchy.java

# execute on default alphabet from 3 5 chars
java Crunchy 3 5 > crunchy.txt

```

See source for latest args. 
At this writing: `Crunchy [minLen] maxLen [alphabet] [debugMode=false]`.

If you provide only maxLen, then minLen == maxLen.
