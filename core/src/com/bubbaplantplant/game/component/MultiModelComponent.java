package com.bubbaplantplant.game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class MultiModelComponent implements Component {

    private final Map<Integer, ModelInstance> library = new HashMap<>();
    private int selectedModel;

    public MultiModelComponent(int selectedModelId, ModelInstance selectedModel) {
        this.selectedModel = selectedModelId;
        library.put(selectedModelId, selectedModel);
    }

    public MultiModelComponent add(int modelId, ModelInstance modelInstance) {
        library.put(modelId, modelInstance);
        return this;
    }

    public void selectModel(int modelId) {
        if (!library.containsKey(modelId)) {
            throw new NoSuchElementException("No model with id " + modelId);
        }
        selectedModel = modelId;
    }

    public ModelInstance getSelectedModel() {
        return library.get(selectedModel);
    }
}
