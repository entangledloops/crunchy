# Crunchy

A simplified, iterative Java implementation of the Linux [crunch](https://sourceforge.net/projects/crunch-wordlist/files/crunch-wordlist/) tool.

Standard crunch is well-written, however it's also:

 - implemented recursively
 - requires a particular alphabet input format
 - written entirely in an (arguably) hard-to-read single, sprawling, vanilla C sourcefile

While crunch is highly optimized, this iterative Java implementation competes against it quite admirably.

This Java version:

 - scales linearly due to JVM overhead (generally adds seconds to timed tests)
 - neither prints any statistics nor has a 3 second delay prior to launch
 - does not currently implement most of the various options available w/standard crunch

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
