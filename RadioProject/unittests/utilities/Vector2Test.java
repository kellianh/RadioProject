package utilities;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Brayden on 5/26/2016.
 */
public class Vector2Test {

    @Test
    public void testSubtract() throws Exception {
        Vector2 vec2 = new Vector2(5,8);
        Vector2 vec21 = new Vector2(3,8);

        Vector2 actual = Vector2.Subtract(vec2, vec21);
        Vector2 desired = new Vector2(2,0);

        assertEquals(desired.x, actual.x, .01);
        assertEquals(desired.y, actual.y, .01);
    }

    @Test
    public void testAdd() throws Exception {
        Vector2 vec2 = new Vector2(5,8);
        Vector2 vec21 = new Vector2(3,8);

        Vector2 actual = Vector2.Add(vec2, vec21);
        Vector2 desired = new Vector2(8,16);

        assertEquals(desired.x, actual.x, .01);
        assertEquals(desired.y, actual.y, .01);
    }

    @Test
    public void testZero() throws Exception {
        Vector2 actual = Vector2.Zero();

        Vector2 desired = new Vector2(0,0);

        assertEquals(desired.x, actual.x, .01);
        assertEquals(desired.y, actual.y, .01);
    }
}