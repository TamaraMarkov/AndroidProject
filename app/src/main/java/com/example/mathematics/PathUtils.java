package com.example.mathematics;

import android.graphics.Path;
import android.graphics.RectF;

public class PathUtils {
    public static Path getSolidArcPath(Path solidArcPath, RectF outerCircleBounds, float startAngle, float sweepAngle) {
        solidArcPath.reset();

        // Move to start point
        float startX = outerCircleBounds.centerX();
        float startY = outerCircleBounds.centerY();
        solidArcPath.moveTo(startX, startY);


        // Line from inner to outer arc
        solidArcPath.lineTo(
                getPointX(
                        outerCircleBounds.centerX(),
                        outerCircleBounds.width() / 2f,
                        startAngle + sweepAngle),
                getPointY(
                        outerCircleBounds.centerY(),
                        outerCircleBounds.height() / 2f,
                        startAngle + sweepAngle));

        // Add outer arc
        solidArcPath.addArc(outerCircleBounds, startAngle, sweepAngle);

        // Close (drawing last line and connecting arcs)
        solidArcPath.lineTo(startX, startY);

        return solidArcPath;
    }

    public static float getPointX(float centerX, float distance, float degrees) {
        return (float) (centerX
                + distance * Math.sin(-degrees * Math.PI / 180 + Math.PI / 2));
    }

    public static float getPointY(float centerY, float distance, float degrees) {
        return (float) (centerY
                + distance * Math.cos(-degrees * Math.PI / 180 + Math.PI / 2));
    }
}
