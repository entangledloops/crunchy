/**
 * Created by snd on 10/3/16.
 */
public class Crunchy
{
    ///////////////////////////////////////////////////////////////////////////
    // shared vars

    private static final String alphaLower = "abcdefghijklmnopqrstuvwxyz";
    private static final String alphaUpper = alphaLower.toUpperCase();
    private static final String alphaLowerCommon = "etaoinshrdlcumwfgypbvkjxqz";
    private static final String alphaUpperCommon = alphaLowerCommon.toUpperCase();
    private static final String digits = "0123456789";
    private static final String defaultAlphabet = alphaLower;

    ///////////////////////////////////////////////////////////////////////////
    // member vars

    private final int min; ///< the minimum output length
    private final int max; ///< the maximum output length
    private final char[] alphabet; ///< the alphabet to use
    private final boolean debugMode; ///< debug output

    /**
     * Prepares an object to crunch all possible variants of the provided alphabet to stdout.
     *
     * @param min min length to generate
     * @param max max length to generate
     * @param alphabet alphabet to iterate through
     * @param debugMode debug output to help trace issues
     */
    public Crunchy(int min, int max, String alphabet, boolean debugMode)
    {
        this.min = min;
        this.max = max;
        this.alphabet = alphabet.toCharArray(); /// @TODO allow symbols to be strings for variable size alphabets, such as dictionary combinations
        this.debugMode = debugMode;

        d(
            "\nmin: " + min +
            "\nmax: " + max +
            "\nalphabet: " + alphabet
        );
    }

    ///////////////////////////////////////////////////////////////////////////
    // logging methods

    // simple static wrappers
    private static void o(String s) { System.out.print(s + "\n"); }
    private static void e(String s) { System.err.print(s + "\n"); }
    private static void e(Throwable t) { t.printStackTrace(); }

    // d() isn't static b/c debug mode depends on the state of this.debugMode
    private void d(String s) { if (debugMode) System.out.print(s + "\n"); }

    /**
     * driver
     */
    public void crunch()
    {
        try
        {
            long crunchingTotal = 0;
            long crunchedTotal = 0;

            // loop for each requested length of output, min -> max
            for (int len = min; len <= max; ++len)
            {
                // calculate total expected output
                long crunching = alphabet.length; for (int i = 1; i < len; ++i) crunching *= alphabet.length;
                long crunched = 1; // start at 1 to account for initial output

                // create reusable output holder initialized to first letter of alphabet and len = max
                final StringBuilder output = new StringBuilder(new String(new char[len]).replace('\0', alphabet[0]));

                // output initial state
                o( output.toString() );

                // calculate all variations
                int cur = len;
                do
                {
                    // will hold index of currently-updating symbol position
                    int curIndex = 0;

                    // locate current char
                    while (--cur >= 0 && ((curIndex = indexOf( output.charAt(cur) ))+1) >= alphabet.length) ;

                    // ensure valid char could be located
                    if (cur >= 0)
                    {
                        output.setCharAt(cur, alphabet[curIndex+1]); // increment max symbol
                        for (int i = len-1; i > cur; --i) output.setCharAt(i, alphabet[0]); // reset symbols after cur pos
                        cur = len; // reset position to start after a change at max pos.

                        ++crunched;
                        o( output.toString() );
                    }

                } while (crunched < crunching);

                if (debugMode)
                {
                    crunchingTotal += crunching;
                    crunchedTotal += crunched;
                }
            }

            // basic stats report
            d(crunchedTotal + " / " + crunchingTotal + " variations crunched");
        }
        catch (Throwable t)
        {
            e(t);
        }
    }

    private int indexOf(char c)
    {
        for (int i = 0; i < alphabet.length; ++i) if (c == alphabet[i]) return i;
        return -1;
    }

    /**
     * @param args commandline args
     */
    public static void main(String[] args)
    {
        if (args.length < 1)
        {
            e("Crunch [minLen] maxLen [alphabet=" + defaultAlphabet + "] [debugMode=false]");
            System.exit(1);
        }

        try
        {
            // parse args
            final int min = Integer.parseInt(args[0]);
            final int max = args.length < 2 ? min : Integer.parseInt(args[1]);
            final String alphabet = args.length < 3 ? defaultAlphabet : args[2].replace("alphabet=", "").replaceAll("(.)\\1", "$1");
            final boolean debugMode = args.length < 4 ? false : Boolean.parseBoolean(args[3].replace("debugMode=", ""));

            // crunch
            new Crunchy(min, max, alphabet, debugMode).crunch();
        }
        catch (Throwable t)
        {
            e(t);
            System.exit(2);
        }
    }
}
