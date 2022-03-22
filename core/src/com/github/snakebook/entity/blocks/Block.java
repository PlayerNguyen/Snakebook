package com.github.snakebook.entity.blocks;

import com.github.snakebook.entity.Entity;

public interface Block extends Entity {

    float getLastDistanceFromCamera();

    void setLastDistanceFromCamera(float f);
}
