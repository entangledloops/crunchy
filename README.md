# Crunchy

A simplified Java implementation of the Linux crunch tool.

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
