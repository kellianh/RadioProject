package shapecomparison;

import utilities.Vector2;

/**
 * Created by miste on 4/28/2016.
 */
public class ShapePoint
{
    public Vector2 Point;
    public int StrokeID;


    public ShapePoint(Vector2 point, int strokeID) {
        this.Point = point;
        this.StrokeID = strokeID;
    }

    public ShapePoint(float x, float y, int strokeID) {
        this.Point = new Vector2(x, y);
        this.StrokeID = strokeID;
    }

    public String ToString() {
    return this.StrokeID + "; " + "(" + this.Point.x + ", " + this.Point.y + ")";
}
}
