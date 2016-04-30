package utilities;

/**
 * Created by miste on 4/28/2016.
 */
public class Vector2
{
    public float x;
    public float y;

    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    private float Magnitude()
    {
        //The length of the vector is square root of (x*x+y*y).
        return (float) Math.sqrt((this.x*this.x) + (this.y*this.y));
    }

    public static float Distance(Vector2 a, Vector2 b)
    {
        return Subtract(a,b).Magnitude();
    }

    public static Vector2 Subtract(Vector2 a, Vector2 b)
    {
        float newX = a.x - b.x;
        float newY = a.y - b.y;
        return new Vector2(newX, newY);
    }

    public static Vector2 Add(Vector2 a, Vector2 b)
    {
        float newX = a.x + b.x;
        float newY = a.y + b.y;
        return new Vector2(newX, newY);
    }

    public static Vector2 Zero()
    {
        return new Vector2(0,0);
    }
}
