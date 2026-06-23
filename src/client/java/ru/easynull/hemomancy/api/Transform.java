package ru.easynull.hemomancy.api;

import org.joml.Quaternionf;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Consumer;

public record Transform(PoseStack ms) {
    public static Transform create(PoseStack ps, Consumer<Transform> transform) {
        Transform tr = new Transform(ps);
        transform.accept(tr);
        return tr;
    }

    public void start() {
        ms.pushPose();
    }

    public void stop() {
        ms.popPose();
    }

    public void autoPose(Runnable action) {
        start();
        action.run();
        stop();
    }

    public void rotate(float pX, float pY, float pZ, Quaternionf angel) {
        move(pX, pY, pZ);
        ms.mulPose(angel);
        move(-pX, -pY, -pZ);
    }

    public void rotate(float pX, float pY, Quaternionf angel) {
        rotate(pX, pY, 0, angel);
    }

    public void scale(float pX, float pY, float pZ, float sX, float sY, float sZ) {
        move(pX, pY, pZ);
        ms.scale(sX, sY, sZ);
        move(-pX, -pY, -pZ);
    }

    public void scale(float pX, float pY, float sX, float sY) {
        scale(pX, pY, 0, sX, sY, 0);
    }

    public void move(float pX, float pY, float pZ) {
        ms.translate(pX, pY, pZ);
    }

    public void move(float pX, float pY) {
        move(pX, pY, 0);
    }
}
