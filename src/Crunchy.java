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
            // loop for each requested length
            for (int max = this.min; max <= this.max; ++max)
            {
                // create reusable output holder initialized to first letter of alphabet and len = max
                final StringBuilder output = new StringBuilder(new String(new char[max]).replace('\0', alphabet[0]));

                d("initialOutput: " + output);

                // for each symbol in alphabet, perform crunch routine
                for (int maxSymbol = 0; maxSymbol < alphabet.length; ++maxSymbol)
                {
                    int outerOffset = max;
                    while (--outerOffset >= 0)
                    {
                        // reset incomplete search portion to initial values
                        for (int i = outerOffset; i < max; ++i) output.setCharAt(i, alphabet[maxSymbol]);

                        // iterate over alphabet once per position of output
                        for (int innerOffset = outerOffset+1; innerOffset < max; ++innerOffset)
                        {
                            // iterate over all symbols of alphabet
                            for (int symbol = 1; symbol < alphabet.length; ++symbol)
                            {
                                output.setCharAt(innerOffset, alphabet[symbol]);
                                o( output.toString() );
                            }
                        }
                    }
                }

                d("finalOutput: " + output);
            }
        }
        catch (Throwable t)
        {
            e(t);
        }
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
        final String alphabet = args.length < 3 ? alphaLower : args[2].replaceAll("(.)\0", "\0");
        final boolean debugMode = args.length < 4 ? false : Boolean.parseBoolean(args[3]);

        // crunch
        new Crunchy(min, max, alphabet, debugMode).crunch();
    }
}
