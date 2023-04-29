package main.java.anivanchen.tracer.utils;

import main.java.anivanchen.tracer.shapes.Solid;

public class RayHit {
    private Ray ray;
    private Solid hitSolid;
    private Vector3d hitPose, normal;

    public RayHit(Ray ray, Solid hitSolid, Vector3d hitPose) {
        this.ray = ray;
        this.hitSolid = hitSolid;
        this.hitPose = hitPose;
        this.normal = hitSolid.getNormalAt(hitPose);
    }

    public Ray getRay() {
        return ray;
    }

    public Solid getHitSolid() {
        return hitSolid;
    }

    public Vector3d getHitPose() {
        return hitPose;
    }

    public Vector3d getNormal() {
        return normal;
    }
}
