package shapecomparison;

import utilities.Vector2;

/**
 * Created by miste on 4/28/2016.
 */
public class Shape
{
    /// <summary>
    /// Name of the multi stroke. It acts like an ID for this multi stroke,
    /// so you should give your multi strokes unique names.
    /// </summary>
    public String Name;

    /// <summary>
    /// Points that form this multi stroke. 
    /// </summary>
    public ShapePoint[] Points;

    /// <summary>
    /// This gesture will be resampled to have this much of points. 
    /// Best between 32 and 256
    /// </summary>
    int NUMBER_OF_POINTS = 32;


    public Shape(ShapePoint[] points) {
        this.Name = "";
        this.Points = points;
        this.Scale();
        this.TranslateToCenter();
        this.Resample();
    }

    public Shape(ShapePoint[] points, String name) {
        this.Name = name;
        this.Points = points;
        this.Scale();
        this.TranslateToCenter();
        this.Resample();
    }


    public ShapeResult Recognize(ShapeLibrary shapeLibrary) {

        ShapeResult result = new ShapeResult();
        result.Score = Float.MAX_VALUE;
        result.Name = "";

        for (Shape shape : shapeLibrary.library)
        {
            float distance = GreedyCloudMatch(this.Points, shape.Points);

            if (distance < result.Score) {
                result.Score = distance;
                result.Name = shape.Name;
            }
        }
        return result;
    }

    /// <summary>
    /// Scale the multi stroke so that it can fit into predefined bounding box 
    /// </summary>
    public void Scale() {

        float minx = Float.MAX_VALUE, miny = Float.MAX_VALUE, maxx = Float.MIN_VALUE, maxy = Float.MIN_VALUE;
        for (int i = 0; i < this.Points.length; i++) {
            if (minx > this.Points[i].Point.x) minx = this.Points[i].Point.x;
            if (miny > this.Points[i].Point.y) miny = this.Points[i].Point.y;
            if (maxx < this.Points[i].Point.x) maxx = this.Points[i].Point.x;
            if (maxy < this.Points[i].Point.y) maxy = this.Points[i].Point.y;
        }

        ShapePoint[] scaledPoints = new ShapePoint[this.Points.length];
        float scale = Math.max(maxx - minx, maxy - miny);

        for (int i = 0; i < this.Points.length; i++) {
            scaledPoints[i] = new ShapePoint((this.Points[i].Point.x - minx) / scale, (this.Points[i].Point.y - miny) / scale, this.Points[i].StrokeID);
        }

        this.Points = scaledPoints;
    }

    /// <summary>
    /// Move the multi stroke to the center
    /// </summary>
    /// <param name="point">Points to move</param>
    /// <returns>List of moved points</returns>
    public void TranslateToCenter() {

        Vector2 p = this.GetCenter();
        ShapePoint[] translatedPoints = new ShapePoint[this.Points.length];

        for (int i = 0; i < this.Points.length; i++) {
            translatedPoints[i] = new ShapePoint(this.Points[i].Point.x - p.x, this.Points[i].Point.y - p.y, this.Points[i].StrokeID);
        }

        this.Points = translatedPoints;
    }


    /// <summary>
    /// Resample the point list so that the list has NUMBER_OF_POINTS number of points
    /// and points are equidistant to each other.
    /// 
    /// First calculate the length of the path. Divided it by (numberOfPoints - 1)
    /// to find the increment. Step through the path, and if the distance covered is
    /// equal to or greater than the increment add a new point to the list by lineer
    /// interpolation.
    /// </summary>
    public void Resample() {

        ShapePoint[] resampledPoints = new ShapePoint[NUMBER_OF_POINTS];
        resampledPoints[0] = new ShapePoint(this.Points[0].Point, this.Points[0].StrokeID);
        int n = 1;

        float increment = GetPathlength() / (NUMBER_OF_POINTS - 1);
        float distanceCovered = 0;

        for (int i = 1; i < this.Points.length; i++) {

            if (this.Points[i].StrokeID == this.Points[i - 1].StrokeID) {
                float distance = Vector2.Distance(this.Points[i - 1].Point, this.Points[i].Point);

                if (distanceCovered + distance >= increment) {

                    ShapePoint firstPoint = this.Points[i - 1];

                    while (distanceCovered + distance >= increment) {

                        float t = Math.min(Math.max((increment - distanceCovered) / distance, 0.0f), 1.0f);

                        if (Float.isNaN(t)) t = 0.5f;

                        resampledPoints[n++] = new ShapePoint(
                                (1.0f - t) * firstPoint.Point.x + t * this.Points[i].Point.x,
                                (1.0f - t) * firstPoint.Point.y + t * this.Points[i].Point.y,
                                this.Points[i].StrokeID
                        );

                        distance = distanceCovered + distance - increment;
                        distanceCovered = 0;
                        firstPoint = resampledPoints[n - 1];
                    }

                    distanceCovered = distance;

                } else distanceCovered += distance;
            }
        }

        if (n == NUMBER_OF_POINTS - 1) {
            resampledPoints[n++] = new ShapePoint(
                    this.Points[this.Points.length - 1].Point.x,
                    this.Points[this.Points.length - 1].Point.y,
                    this.Points[this.Points.length - 1].StrokeID
            );
        }

        this.Points = resampledPoints;
    }


    /// <summary>
    /// Calculate the center of the points
    /// </summary>
    /// <param name="points">List of points</param>
    /// <returns></returns>
    public Vector2 GetCenter() {

        Vector2 total = Vector2.Zero();

        for (int i = 0; i < this.Points.length; i++) {
            //total += this.Points[i].Point;
            total = Vector2.Add(total, this.Points[i].Point);
        }
        return new Vector2(total.x / this.Points.length, total.y / this.Points.length);
    }


    /// <summary>
    /// Calculate total path length: sum of distance between each points
    /// </summary>
    /// <param name="points">List of points</param>
    /// <returns></returns>
    public float GetPathlength() {

        float length = 0;

        for (int i = 1; i < this.Points.length; i++) {
            if (this.Points[i].StrokeID == this.Points[i - 1].StrokeID) {
                length += Vector2.Distance(this.Points[i - 1].Point, this.Points[i].Point);
            }
        }

        return length;
    }


    private static float GreedyCloudMatch(ShapePoint[] points1, ShapePoint[] points2) {
        float e = 0.5f;
        int step = (int) Math.floor(Math.pow(points1.length, 1.0f - e));
        float minDistance = Float.MAX_VALUE;

        for (int i = 0; i < points1.length; i += step) {
            float distance1 = CloudDistance(points1, points2, i);
            float distance2 = CloudDistance(points2, points1, i);
            minDistance = Math.min(minDistance, Math.min(distance1, distance2));
        }
        return minDistance;
    }


    private static float CloudDistance(ShapePoint[] points1, ShapePoint[] points2, int startIndex) {
        boolean[] matched = new boolean[points1.length];
        //Array.Clear(matched, 0, points1.length); Should default to false regardless

        float sum = 0;
        int i = startIndex;

        do {
            int index = -1;
            float minDistance = Float.MAX_VALUE;

            for (int j = 0; j < points1.length; j++) {
                if (!matched[j]) {
                    float distance = Vector2.Distance(points1[i].Point, points2[j].Point);
                    if (distance < minDistance) {
                        minDistance = distance;
                        index = j;
                    }
                }
            }

            matched[index] = true;
            float weight = 1.0f - ((i - startIndex + points1.length) % points1.length) / (1.0f * points1.length);
            sum += weight * minDistance;
            i = (i + 1) % points1.length;

        } while (i != startIndex);

        return sum;
    }


    public String ToString() {
    String result = this.Name;

    for (int i = 0; i < this.Points.length; i++) {
        result += "\n" + this.Points[i];
    }

    return result;
    }
}

