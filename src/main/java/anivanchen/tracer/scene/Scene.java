package main.java.anivanchen.tracer.scene;

import java.util.concurrent.CopyOnWriteArrayList;

import main.java.anivanchen.tracer.shapes.Solid;
import main.java.anivanchen.tracer.utils.Ray;
import main.java.anivanchen.tracer.utils.RayHit;
import main.java.anivanchen.tracer.utils.Vector3d;

public class Scene {
    private Camera camera;
    private Light light;
    private CopyOnWriteArrayList<Solid> solids;

    public Scene() {
        this.camera = new Camera();
        this.light = new Light(new Vector3d(0f, 1f, 2f), 1f);
        this.solids = new CopyOnWriteArrayList<Solid>();
    }

    public void addSolid(Solid solid) {
        this.solids.add(solid);
    }

    public void clearSolids() {
        this.solids.clear();
    }

    public RayHit raycast(Ray ray) {
        RayHit closest = null;
        for (Solid solid : solids) {
            if (solid == null)
                continue;
            Vector3d intersection = solid.calculateIntersection(ray);
            if (intersection != null && (closest == null || Vector3d.distance(closest.getHitPose(),
                    ray.getOrigin()) > Vector3d.distance(intersection, ray.getOrigin()))) {
                closest = new RayHit(ray, solid, intersection);
            }
        }
        return closest;
    }

    public Camera getCamera() {
        return camera;
    }

    public Light getLight() {
        return light;
    }
}
