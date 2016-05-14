package radiosignals;

/**
 * MorseDecoder
 *
 * This is a free software, you can do whatever you want
 * with this code. Credentials are appreciated.
 *
 * Files whitout this header are under specified license
 * and/or limits.
 *
 * This software can translate any morse code audio signal (.wav file)
 * into a human-readable text
 *
 * Donation (Litecoin): LYmQC9AMcvZq8dxQNkUBxngF6B2S2gafyX
 * Donation (Bitcoin):  1GEf7b2FjfCVSyoUdovePhKXe5yncqNZs7
 *
 * @author  Matteo Benetti, mathew.benetti@gmail.com
 * @version 2013-12-23
 */
public class WavFileException extends Exception
{
    private static final long serialVersionUID = 8106597985536609621L;

    public WavFileException()
    {
        super();
    }

    public WavFileException(String message)
    {
        super(message);
    }

    public WavFileException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public WavFileException(Throwable cause)
    {
        super(cause);
    }
}
