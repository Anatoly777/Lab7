package com.lab7.client.builders.lineBuilders;

import com.lab7.client.builders.Builder;
import com.lab7.common.io.InputManager;
import com.lab7.common.io.OutputManager;

public abstract class LineBuilder<T extends Object> extends Builder<T> {
    InputManager inputManager;
    OutputManager outputManager;

    public LineBuilder(InputManager inputManager, OutputManager outputManager) {
        this.inputManager = inputManager;
        this.outputManager = outputManager;
    }


}
