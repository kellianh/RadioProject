package ShapeComparison;

/**
 * Created by miste on 4/28/2016.
 */
public class ShapeResult
{
    /// <summary>
    /// Name of the gesture.
    /// </summary>
    public String Name;

    /// <summary>
    /// Score of the gesture, i.e: how similar to the actual gesture
    /// from the library.
    /// </summary>
    public float Score;

    public ShapeResult () {
        this.Name = "No match";
        this.Score = 0;
    }


    public ShapeResult (String name, float score) {
        this.Name = name;
        this.Score = score;
    }

    public String ToString()
    {
        return this.Name + "; " + this.Score;
    }
}
