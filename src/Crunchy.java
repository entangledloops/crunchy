import java.util.concurrent.atomic.AtomicInteger;

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
        this.alphabet = alphabet.toCharArray();
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
    private static void o(String s) { System.out.println(s); }
    private static void e(String s) { System.err.println(s); }
    private static void e(Throwable t) { t.printStackTrace(); }

    // d() isn't static b/c debug mode depends on the state of this.debugMode
    private void d(String s) { if (debugMode) System.out.println(s); }

    /**
     * driver
     */
    public void crunch()
    {
        try
        {
            long crunching = alphabet.length; for (int i = 1; i < max; ++i) crunching *= alphabet.length;
            long crunched = 0; // start at 1 to account for initial output

            // no work was requested, bail
            if (crunching < alphabet.length) System.exit(0);

            d("crunching: " + crunching);

            // loop for each requested length of output, min -> max
            for (int len = min; len <= max; ++len)
            {
                // create reusable output holder initialized to first letter of alphabet and len = max
                final StringBuilder output = new StringBuilder(new String(new char[len]).replace('\0', alphabet[0]));

                // output and record initial state
                ++crunched;
                o( output.toString() );

                // calculate all variations
                do
                {
                    int cur = max;
                    int curIndex = 0;

                    // locate current char
                    while (--cur >= 0 && ((curIndex = indexOf( output.charAt(cur) ))+1) >= alphabet.length) ;

                    if (cur >= 0)
                    {
                        ++crunched;
                        output.setCharAt(cur, alphabet[curIndex+1]);
                        o(output.toString());
                        for (int i = max-1; i > cur; --i) output.setCharAt(i, alphabet[0]);
                    }

                } while (crunched < crunching);

                d("crunched: " + crunched + " / " + crunching);
            }
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
            e("You must provide at least max length to crunch.");
            System.exit(1);
        }

        // parse args
        final int min = Integer.parseInt( args[0] );
        final int max = args.length < 2 ? min : Integer.parseInt( args[1] );

        // prepare to crunch
        final String alphabet = args.length < 3 ? alphaLower : args[2].replaceAll("(.)\\1", "$1");
        final boolean debugMode = args.length < 4 ? false : Boolean.parseBoolean(args[3]);

        // crunch
        new Crunchy(min, max, alphabet, debugMode).crunch();
    }
}
