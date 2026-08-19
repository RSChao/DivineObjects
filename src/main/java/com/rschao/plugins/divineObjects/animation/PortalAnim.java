package com.rschao.plugins.divineObjects.animation;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class PortalAnim {

    /**
     * Displays a particle animation consisting of a filled circle with inward spirals.
     * The circle is oriented perpendicular to the given direction vector.
     *
     * @param direction The direction vector that determines the viewing angle (circle appears 2D when viewed along this direction)
     * @param center The center location of the animation
     * @param color1 The primary color for the circle particles
     * @param color2 The secondary color for the spiral particles
     */
    public static void anim(Vector direction, Location center, int radius, Particle.DustOptions color1, Particle.DustOptions color2) {
        Vector normal = direction.clone().normalize();
        
        // Create two perpendicular vectors to the normal (X and Y axes in the plane perpendicular to direction)
        Vector xAxis = getPerpendicularVector(normal);
        Vector yAxis = normal.clone().crossProduct(xAxis).normalize();

        double spiralRadius = 0.3; // Distance of spirals from the circle
        int circlePoints = 120; // Points around the circle
        int spiralLayers = 5; // Number of spiral arms
        int spiralPoints = 30; // Points per spiral arm
        
        // Draw the filled circle
        drawFilledCircle(center, xAxis, yAxis, radius, circlePoints, color1);
        
        // Draw inward spirals
        drawInwardSpirals(center, xAxis, yAxis, normal, radius, spiralRadius, spiralLayers, spiralPoints, color2);
    }
    
    /**
     * Draws a filled circle by layering rings from center to edge.
     */
    private static void drawFilledCircle(Location center, Vector xAxis, Vector yAxis, double radius, int points, Particle.DustOptions color) {
        int layers = (int) (radius * 4);
        for (int layer = 0; layer <= layers; layer += 1) {
            double layerRadius = (layer / (double) layers) * radius;
            drawCircleRing(center, xAxis, yAxis, layerRadius, points, color);
        }
    }
    
    /**
     * Draws a single ring at a given radius.
     */
    private static void drawCircleRing(Location center, Vector xAxis, Vector yAxis, double radius, int points, Particle.DustOptions color) {
        for (int i = 0; i < points; i++) {
            double angle = (i / (double) points) * 2 * Math.PI;
            double x = Math.cos(angle) * radius;
            double y = Math.sin(angle) * radius;
            
            Vector offset = xAxis.clone().multiply(x).add(yAxis.clone().multiply(y));
            Location particleLocation = center.clone().add(offset);
            
            center.getWorld().spawnParticle(Particle.DUST, particleLocation, 1, 0, 0, 0, 0, color);
        }
    }
    
    /**
     * Draws inward spirals pointing toward the circle center.
     */
    private static void drawInwardSpirals(Location center, Vector xAxis, Vector yAxis, Vector normal, 
                                          double circleRadius, double spiralDistance, int spiralLayers, 
                                          int spiralPoints, Particle.DustOptions color) {
        for (int layer = 0; layer < spiralLayers; layer++) {
            double layerAngle = (layer / (double) spiralLayers) * 2 * Math.PI;
            
            // Calculate starting position on the circle
            double startX = Math.cos(layerAngle) * circleRadius;
            double startY = Math.sin(layerAngle) * circleRadius;
            
            Vector circlePos = xAxis.clone().multiply(startX).add(yAxis.clone().multiply(startY));
            
            // Spiral extends 0.3 blocks in front of the circle
            Vector spiralStart = circlePos.clone().subtract(normal.clone().multiply(spiralDistance));
            
            for (int i = 0; i < spiralPoints; i++) {
                double t = i / (double) (spiralPoints - 1);
                
                // Interpolate from spiral start toward the circle center
                double spiralCurveAngle = t * 2 * Math.PI;
                double spiralCurveRadius = (1 - t) * 0.5; // Radius decreases as it spirals inward
                
                // Calculate the curved position
                double curveX = Math.cos(spiralCurveAngle) * spiralCurveRadius;
                double curveY = Math.sin(spiralCurveAngle) * spiralCurveRadius;
                
                Vector curveOffset = xAxis.clone().multiply(curveX).add(yAxis.clone().multiply(curveY));
                
                // Interpolate toward center
                Vector position = spiralStart.clone()
                        .add(normal.clone().multiply(-spiralDistance * t))
                        .add(circlePos.clone().multiply(t).subtract(circlePos))
                        .add(curveOffset);
                
                Location particleLocation = center.clone().add(position);
                center.getWorld().spawnParticle(Particle.DUST, particleLocation, 1, 0, 0, 0, 0, color);
            }
        }
    }
    
    /**
     * Finds a vector perpendicular to the given vector.
     */
    private static Vector getPerpendicularVector(Vector v) {
        if (Math.abs(v.getX()) < 0.9) {
            return v.clone().crossProduct(new Vector(1, 0, 0)).normalize();
        } else {
            return v.clone().crossProduct(new Vector(0, 1, 0)).normalize();
        }
    }
}
